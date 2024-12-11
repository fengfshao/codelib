package com.shaoff.dig.io;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;


/**
 * Author: shaoff
 * Date: 2020/6/18 11:10
 * Package: io1.nio
 * Description:
 */
public class Versus {
    static void streamInput(InputStream in) throws Exception {
        byte[] buffer = new byte[256];
        int readed;
        while ((readed = in.read(buffer)) > 0) {
            String s = new String(buffer, 0, readed);
            System.out.print(s);
        }
        System.out.println("EOF");
    }

    static void testReadStream() throws Exception {
//        InputStream in=new FileInputStream("/Users/sakura/stuff/stuff-projects/corel/javal/src/main/java/io1/nio/Versus.java");
//        InputStream in = System.in;
        Socket socket = new Socket();
        socket.connect(new InetSocketAddress(8890));
        InputStream in = socket.getInputStream();
        streamInput(in);
    }

    static void channelInput(ReadableByteChannel input) throws Exception {
        ByteBuffer buffer = ByteBuffer.allocate(256);
        while (input.read(buffer) >= 0) {
            buffer.flip();
            String s = StandardCharsets.UTF_8.decode(buffer).toString();
            buffer.flip();
            System.out.print("get:" + s);
//            buffer.clear();
        }
    }

    static void testReadChannel() throws Exception {
//        FileInputStream fin = new FileInputStream("/Users/sakura/stuff/stuff-projects/corel/javal/src/main/java/io1/nio/Versus.java");
//        channelInput(fin.getChannel());
        SocketChannel ch = SocketChannel.open();
        ch.connect(new InetSocketAddress(8890));
        channelInput(ch);
    }

    public static void main(String[] args) throws Exception {
        testReadChannel();
    }

    public static void readFully(ReadableByteChannel channel, ByteBuffer dst) throws IOException {
        int expected = dst.remaining();
        while (dst.hasRemaining()) {
            if (channel.read(dst) < 0) {
                throw new EOFException(String.format("Not enough bytes in channel (expected %d).",
                        expected));
            }
        }
    }
}
