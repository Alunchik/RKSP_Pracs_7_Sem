package multithreading.task3;

public class FileHandler {
    public void handle(File file){
        try {
            Thread.sleep(file.size * 7);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println(file.format + " " + file.size + " MB handled");
    }
}
