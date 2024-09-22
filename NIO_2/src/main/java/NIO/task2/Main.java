package NIO.task2;
import java.io.*;
import org.apache.commons.io.*;

import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Random;

@FunctionalInterface
interface Copy{
    public void copy();
}
public class Main {
    private static final String FILE_FROM_PATH = "NIO_2\\src\\main\\resources\\file_from_copy.txt";

    private static final String FILE_TO_PATH = "NIO_2\\src\\main\\resources\\file_to_copy.txt";

    private static void generateFile(){
        Random random = new Random();
        try(RandomAccessFile file = new RandomAccessFile(FILE_FROM_PATH, "rw");
        FileChannel channel = file.getChannel();
        ){
            ByteBuffer byteBuffer = ByteBuffer.allocate(2); // 2 байта
            for(int i=0; i<1000; i++) {
                for(int j=0; j<1000000; j++){
                    byteBuffer.putShort((short)random.nextInt(32736));  // записываем по short значению весом 2 байта
                    // Unicode (от 0 до 65535)
                    channel.write(byteBuffer);
                }
            }
            System.out.println("File generated succesfully");
        } catch (Exception e) {
            System.out.println("Error occured during generating file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void copyWithFileIOStream(){
        File fileFrom = new File(FILE_FROM_PATH);
        File fileTo = new File(FILE_TO_PATH + "_IO.txt");
        try(FileInputStream fromStream = new FileInputStream(fileFrom);
            FileOutputStream toStream = new FileOutputStream(fileTo);

        ){
            int buffer = fromStream.read();
            while(buffer != -1){
                toStream.write(buffer);
                buffer = fromStream.read();
            }
                System.out.println("Copied with IOStream succesfully");
        } catch (Exception e) {
            System.out.println("Error occured during coping with IOStream: " + e.getMessage());
        }
    }

    private static void copyWithFileChannel(){
        try(
        RandomAccessFile fromFile = new RandomAccessFile(FILE_FROM_PATH, "rw");
        RandomAccessFile toFile = new RandomAccessFile(FILE_TO_PATH + "_channel.txt", "rw");
        FileChannel fromChannel = fromFile.getChannel();
        FileChannel toChannel = toFile.getChannel();
        ) {
            toChannel.transferFrom(fromChannel, 0, fromChannel.size());
                System.out.println("Copied with FileChannel succesfully");
        } catch (Exception e){
            System.out.println("Error occured during coping with File Channel: " + e.getMessage());
        }
    }

    public static void copyWithApacheCommonsIO(){
        File fileFrom = new File(FILE_FROM_PATH);
        File fileTo = new File(FILE_TO_PATH + "_ApacheIO.txt");
        try {
            FileUtils.copyFile(fileFrom, fileTo);
                System.out.println("Copied with Apache IO succesfully");
            } catch (IOException e) {
            System.out.println("Error occured during coping with apache IO: " + e.getMessage());
        }

    }

    private static void copyWithFilesClass(){
        File fileFrom = new File(FILE_FROM_PATH);
        File fileTo = new File(FILE_TO_PATH + "_files.txt");
        try {
            Files.copy(fileFrom.toPath(), fileTo.toPath());
        } catch (IOException e) {
            System.out.println("Error occured during coping with Files class: " + e.getMessage());
        }
            System.out.println("Copied with Files class succesfully");
    }

    private static void measureCopyFunctionTime(Copy copy){
        LocalTime start = LocalTime.now();
        copy.copy();
        Long time = ChronoUnit.MILLIS.between(start, LocalTime.now());
        System.out.println("Time for copying: " + time);
    }

    public static void main(String[] args){
        //generateFile();
        measureCopyFunctionTime(Main::copyWithFileIOStream);
        measureCopyFunctionTime(Main::copyWithFileChannel);
        measureCopyFunctionTime(Main::copyWithApacheCommonsIO);
        measureCopyFunctionTime(Main::copyWithFilesClass);
    }
}
