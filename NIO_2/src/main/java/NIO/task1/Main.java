package NIO.task1;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class Main {


    public static void main(String[] args) throws IOException {
        String path = "NIO_2\\src\\main\\resources\\file1.txt";
        RandomAccessFile file = new RandomAccessFile(path, "rw");

        FileChannel fileChannel = file.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        int bytesRead = fileChannel.read(byteBuffer);

        while(bytesRead > 0){
            byteBuffer.flip();
            while(byteBuffer.hasRemaining()){
                System.out.print((char) byteBuffer.get());
            }
            byteBuffer.clear();
            bytesRead = fileChannel.read(byteBuffer);
        }
        fileChannel.close();
    }
}
