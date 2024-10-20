package RxJava.task4;

import java.util.Random;

public class FileGenerator {
    static Random random = new Random();
    static String[] formats = {"XML", "JSON", "XLS"};
    public static File getFile(){
        try {
            Thread.sleep(100, 1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        String format = formats[random.nextInt(formats.length)];
        int size = random.nextInt(100);
        System.out.println("Generated " + format + " " + size + "MB");
        return new File(format, size);
    }
}
