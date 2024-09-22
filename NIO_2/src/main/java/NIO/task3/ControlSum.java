package NIO.task3;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class ControlSum {
    private static final String FILEPATH = "NIO_2\\src\\main\\resources\\crc16.txt";
    public static int getCRC16(String filepath){
        int crc = 0xFFFF;
        int polynomial = 0x8005;

        try(RandomAccessFile file = new RandomAccessFile(filepath, "rw");
            FileChannel channel = file.getChannel();
        ){
            ByteBuffer buffer = ByteBuffer.allocate(32);

            while(channel.read(buffer) != -1){
                buffer.flip();
                while(buffer.hasRemaining()){
                    byte b = buffer.get();
                    for(int j=0; j<8; j++){
                        boolean mostSignificantBufferBit = (b >> (7 - j) & 1) == 1;
                        boolean mostSignificantCRCBit = ((crc >> 15 & 1) == 1);
                        crc <<= 1;
                        if(mostSignificantBufferBit ^ mostSignificantCRCBit){
                            crc ^= polynomial;
                        }
                    }
                }
                buffer.clear();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return crc;
    }
    public static void main(String[] args) {
        System.out.println("0x" + Integer.toHexString(getCRC16(FILEPATH)).substring(3));
    }
}
