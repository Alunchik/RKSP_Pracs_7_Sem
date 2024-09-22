package NIO.task4;

import java.io.IOException;
import java.nio.file.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections4.CollectionUtils;

public class Main {
    private static final String DIRECTORY_PATH = "NIO_2\\src\\main\\resources\\task4";

    private static  Map<String, List<String>> files;

    private static void addFile(String path){
        Path file = Paths.get(path);
        try {
            files.put(path, Files.readAllLines(file));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void printChanges(String path){
        Path file = Paths.get(path);
        try{
            for(String elem: CollectionUtils.subtract(files.get(path), Files.readAllLines(file))){
                System.out.println(elem);
            }
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        files = new HashMap<>();



        try (WatchService watchService = FileSystems.getDefault().newWatchService();
        ){
            WatchKey key = Paths.get(DIRECTORY_PATH)
                    .register(watchService, StandardWatchEventKinds.ENTRY_CREATE,
                            StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY);
            key = watchService.take();
            while(key != null){
                for(WatchEvent<?> event: key.pollEvents()){
                    if(event.kind() == StandardWatchEventKinds.ENTRY_CREATE){
                        System.out.println("Created: " + event.context());
                        addFile(DIRECTORY_PATH + "\\" + event.context());
                    }
                    else if(event.kind() == StandardWatchEventKinds.ENTRY_MODIFY){
                        System.out.println("Modified: " + event.context());
                       printChanges(DIRECTORY_PATH + "\\" + event.context());
                       addFile(DIRECTORY_PATH + "\\" + event.context());
                    } else if(event.kind() == StandardWatchEventKinds.ENTRY_DELETE){
                        System.out.println("Deleted: " + event.context());
                    }
                    /*
                    Третий пункт невыполним, так как при получении стандартных событий, в т.ч. удаления, в контексте мы получаем
                    только путь до файла, по пути до уже удаленного файла посчитать его контрольную сумму и узнать размер мы не можем
                     */
                }
                key.reset();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
