package com.fengfshao.hdfs;

import java.io.File;
import java.io.FileInputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.apache.commons.io.FileUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.BlockLocation;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.lib.input.LineRecordReader;
import org.apache.hadoop.mapreduce.lib.input.SplitLineReader;
import org.apache.hadoop.mapreduce.lib.input.UncompressedSplitLineReader;
import org.apache.hadoop.util.LineReader;

/**
 * 使用多个线程处理直接处理hdfs底层的block，并对比处理速度
 * 不方便无法直接读取block，且甚至可能无法正确解码（行的字节块不完整）
 * 需要借助mapreduce先获取到FileSplit
 *
 * https://data-flair.training/blogs/mapreduce-inputsplit-vs-block-hadoop/
 * https://stackoverflow.com/questions/14291170/how-does-hadoop-process-records-split-across-block-boundaries
 *
 * @author fengfshao
 */
public class FileBlockDemo {

    public static void main(String[] args) throws Exception {

        //localDebug(args);
        String inputPath = args[0];
        Path hadoopPath = new Path(inputPath);
        FileSystem hdfs = hadoopPath.getFileSystem(new Configuration());
        FileStatus fs = hdfs.getFileStatus(hadoopPath);
        Arrays.stream(hdfs.getFileBlockLocations(fs, 0, fs.getLen())).forEach(System.out::println);
        System.out.println("file size: " + FileUtils.byteCountToDisplaySize(hdfs.getFileStatus(hadoopPath).getLen()));

        sequenceRead(hdfs, hadoopPath);
        parallelRead(hdfs, fs);
    }

    public static void sequenceRead(FileSystem hdfs, Path path) throws Exception {
        System.out.println("begin sequence count. " + LocalDateTime.now());
        Iterator<String> lines = getLinesOfSplit(new FileSplit(path, 0, hdfs.getFileStatus(path).getLen(), null), hdfs);
        int i = 0;
        while (lines.hasNext()) {
            i += 1;
            lines.next();
        }
        System.out.println(i);
        //System.out.println(getLinesOfSplit(new FileSplit(path, 0, hdfs.getFileStatus(path).getLen(), null)));
        /*FSDataInputStream stream = hdfs.open(path);
        Scanner scanner = new Scanner(stream);
        int i = 0;
        while (scanner.hasNextLine()) {
            i += 1;
            scanner.nextLine();
        }
        stream.close();*/
        System.out.println(i + " " + LocalDateTime.now());
    }

    public static void parallelRead(FileSystem hdfs, FileStatus fs) throws Exception {
        System.out.println("begin parallel count. " + LocalDateTime.now());
        ExecutorService ex = Executors.newCachedThreadPool();
        // NLineInputFormat的api要遍历找到所有行，速度较慢，最好能找到直接从hdfs block尾找换行符的
        /*List<FileSplit> splits = NLineInputFormat.getSplitsForFile(fs,
                conf, 1048576); //速度比较慢
        System.out.println("total splits: " + splits.size() + " " + LocalDateTime.now());*/
        int maxLength = (int) Math.min(1024 * 1024 * 50, fs.getLen());
        List<FileSplit> splits = getSplitsByBlock(hdfs, fs, maxLength);
        System.out.println(splits);
        List<Future<Integer>> results = new ArrayList<>();
        splits.forEach(s -> {
            Future<Integer> f = ex.submit(new Callable<Integer>() {
                @Override
                public Integer call() throws Exception {
                    int i = 0;
                    Iterator<String> lines = getLinesOfSplit(s, hdfs);
                    while (lines.hasNext()) {
                        i += 1;
                        lines.next();
                    }
                    return i;
                }
            });
            results.add(f);
        });
        int totalLine = 0;
        for (Future<Integer> r : results) {
            totalLine += r.get();
        }
        System.out.println(totalLine + " " + LocalDateTime.now());
        ex.shutdownNow();
    }

    public static Iterator<String> getLinesOfSplit(FileSplit split, FileSystem hdfs) throws Exception {
        Configuration conf = hdfs.getConf();
        // maxLineLength不是必须的，只是能够增加效率，避免遇到脏数据时出错
        int maxLineLength = conf.getInt(LineRecordReader.MAX_LINE_LENGTH, 1024 * 1024 * 10);
        FSDataInputStream fileIn = hdfs.open(split.getPath());
        fileIn.seek(split.getStart());
        SplitLineReader reader = new UncompressedSplitLineReader(fileIn, hdfs.getConf(), null, split.getLength());

        return new Iterator<String>() {
            final Text nextLine = new Text();
            int bytesRead = reader.readLine(nextLine);

            @Override
            public boolean hasNext() {
                return bytesRead != 0;
            }

            @lombok.SneakyThrows
            @Override
            public String next() {
                String res = nextLine.toString();
                nextLine.clear();
                bytesRead = reader.readLine(nextLine, maxLineLength);
                return res;
            }
        };
    }

    public static List<FileSplit> getSplitsByBlock(FileSystem hdfs, FileStatus fs, int maxLineLength) throws Exception {
        // FileStatus必须要是文件
        List<FileSplit> res = new ArrayList<>();
        // 直接定位到最后i=len-maxLineLength，要么从[i,len)为完全新的一行，要么必然有换行符
        BlockLocation[] blocks = hdfs.getFileBlockLocations(fs, 0, fs.getLen());
        long lastEndOffset = 0;
        Path p = fs.getPath();
        FSDataInputStream fileIn = hdfs.open(p);
        for (BlockLocation block : blocks) {
            long tmpOffset = searchNewLine(fileIn, block.getOffset() + block.getLength() - maxLineLength,
                    maxLineLength);
            res.add(new FileSplit(p, lastEndOffset, tmpOffset - lastEndOffset, null));
            lastEndOffset = tmpOffset;
        }
        BlockLocation lastBlock = blocks[blocks.length - 1];
        long endOffset = lastBlock.getOffset() + lastBlock.getLength();
        if (lastEndOffset < endOffset) {
            res.add(new FileSplit(p, lastEndOffset, endOffset - lastEndOffset, null));
        }
        return res;
    }

    /**
     *
     * @param input hdfs文件
     * @param beginOffset 开始地址
     * @param maxLineLength 行最大字节数，非必须
     * @return 新行的起始位置endOffset或是文件末尾（length)，endOffset-1一般为换行符或是文件最后一个字符
     * 这样，对每个split，其offset地址为[beginOffset,endOffset)
     */
    private static long searchNewLine(FSDataInputStream input, long beginOffset, int maxLineLength) throws Exception {
        input.seek(beginOffset);
        // byteRead返回的字节数包括换行符在内，text的内容已去掉换行符
        int bytesRead = new LineReader(input).readLine(new Text(), maxLineLength);
        return bytesRead + beginOffset;
    }

    public static void localDebug(String[] args) throws Exception {
        int maxLineLength = 128;
        Text text = new Text();

        File file = new File("/Users/sakura1/stuff/codelib/shell/service.sh");
        FileInputStream fileIn = new FileInputStream(file);

        int startOffset = (int) (file.length() - maxLineLength);
        if (startOffset != fileIn.skip(startOffset)) {
            throw new RuntimeException("can't seek to  approximately last line.");
        }
        System.out.println(startOffset);
        LineReader reader = new LineReader(fileIn);
        int bytesRead = reader.readLine(text, maxLineLength);
        System.out.println(text);
        System.out.println(bytesRead);
        int a = bytesRead + startOffset;
        //System.out.println(Integer.toHexString(a/16*16));

        FileInputStream fileIn2 = new FileInputStream(file);
        fileIn2.skip(bytesRead + startOffset - 1);
        System.out.println(fileIn2.read());
        text.clear();

        int tmp;
        while ((tmp = reader.readLine(text, maxLineLength)) != 0) {
            System.out.println(text);
            text.clear();
        }
    }

}
