package NIO.task2;
import java.io.*;
import org.apache.commons.io.*;

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
    private static final String FILE_FROM_PATH = "src/pr_2/NIO/task_2/file_from_copy.txt";

    private static final String FILE_TO_PATH = "src/pr_2/NIO/task_2/file_to_copy.txt";

    private static void generateFile(){
        File file = new File(FILE_FROM_PATH);
        Random random = new Random();
        try (FileOutputStream stream = new FileOutputStream(file)){
            for(int i=0; i< 100000000; i++){
                stream.write(random.nextInt());
            }
            System.out.println("File generated succesfully");
        } catch (IOException e) {
            System.out.println("Error occured during generating file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void copyWithFileIOStream(){
        File fileFrom = new File(FILE_FROM_PATH);
        File fileTo = new File(FILE_TO_PATH);
        try(FileInputStream fromStream = new FileInputStream(fileFrom);
            FileOutputStream toStream = new FileOutputStream(fileTo);

        ){
            int buffer = fromStream.read();
            while(buffer != -1){
                toStream.write(buffer);
                buffer = fromStream.read();
            }
            if(fileFrom.equals(fileTo)) {
                System.out.println("Copied with IOStream succesfully");
            }
        } catch (Exception e) {
            System.out.println("Error occured during coping with IOStream: " + e.getMessage());
        }
    }

    private static void copyWithFileChannel(){
        try(
        RandomAccessFile fromFile = new RandomAccessFile(FILE_FROM_PATH, "r");
        RandomAccessFile toFile = new RandomAccessFile(FILE_TO_PATH, "w");
        FileChannel fromChannel = fromFile.getChannel();
        FileChannel toChannel = toFile.getChannel();
        ) {
            toChannel.transferFrom(fromChannel, 0, fromChannel.size());
            if(fromFile.equals(toFile)) {
                System.out.println("Copied with FileChannel succesfully");
            }
        } catch (Exception e){
            System.out.println("Error occured during coping with File Channel: " + e.getMessage());
        }
    }

    public static void copyWithApacheCommonsIO(){
        File fileFrom = new File(FILE_FROM_PATH);
        File fileTo = new File(FILE_TO_PATH);
        try {
            FileUtils.copyFile(fileFrom, fileTo);
            if(fileFrom.equals(fileTo)) {
                System.out.println("Copied with Apache IO succesfully");
            }
            } catch (IOException e) {
            System.out.println("Error occured during coping with apache IO: " + e.getMessage());
        }

    }

    private static void copyWithFilesClass(){
        File fileFrom = new File(FILE_FROM_PATH);
        File fileTo = new File(FILE_TO_PATH);
        try {
            Files.copy(fileFrom.toPath(), fileTo.toPath());
        } catch (IOException e) {
            System.out.println("Error occured during coping with Files class: " + e.getMessage());
        }
        if(fileFrom.equals(fileTo)) {
            System.out.println("Copied with Files class succesfully");
        }
    }

    private static void measureCopyFunctionTime(Copy copy){
        LocalTime start = LocalTime.now();
        copy.copy();
        Long time = ChronoUnit.MILLIS.between(start, LocalTime.now());
        System.out.println("Time for copying: " + time);
    }
    public static void main(String[] args){
        generateFile();
        measureCopyFunctionTime(Main::copyWithFileIOStream);
        measureCopyFunctionTime(Main::copyWithFileChannel);
        measureCopyFunctionTime(Main::copyWithApacheCommonsIO);
        measureCopyFunctionTime(Main::copyWithFilesClass);
    }
}
