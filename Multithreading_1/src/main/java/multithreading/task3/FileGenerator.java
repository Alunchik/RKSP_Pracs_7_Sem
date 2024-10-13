package multithreading.task3;

import java.util.Random;

public class FileGenerator {
    Random random = new Random();
    String[] formats = {"XML", "JSON", "XLS"};
    public File getFile(){
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
