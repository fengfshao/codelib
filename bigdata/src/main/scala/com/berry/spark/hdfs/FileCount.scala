package com.berry.spark.hdfs

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, Path}

/**
 * 本地scala代码调试，统计目录下的文件数
 *
 * @author fengfshao
 * @since 2024/7/3
 *
 */
object Demo {

  def computeFileCount(path: Path, hdfs: FileSystem): Int = {
    val fileStatuses = hdfs.listStatus(path)
    fileStatuses.map { fileStatus =>
      if (fileStatus.isFile) {
        1
      } else if (fileStatus.isDirectory) {
        computeFileCount(fileStatus.getPath, hdfs)
      } else {
        0
      }
    }.sum
  }

  def main(args: Array[String]): Unit = {
    val conf = new Configuration()

    val outputPath = "filesCount.txt"
    val hdfs: FileSystem = FileSystem.getLocal(conf)
    // 注意：这里hdfs变量名可能有点误导，因为它只是指向本地文件系统
    val outputFilePath = new Path(outputPath)

    println(s"Output file path: $outputFilePath")
    if (hdfs.exists(outputFilePath)) {
      hdfs.delete(outputFilePath, true)
      println(s"Deleted existing file: $outputFilePath")
    }

    val fsDataOutputStream = hdfs.create(outputFilePath, true)
    try {
      val fileCount=computeFileCount(new Path("file:///Users/sakura1/projects/codelib/bigdata"),hdfs)
      println(s"Writing $fileCount to file...")
      fsDataOutputStream.writeBytes(fileCount.toString)
    } finally {
      fsDataOutputStream.close()
    }
    println("Done!!!")
  }
}