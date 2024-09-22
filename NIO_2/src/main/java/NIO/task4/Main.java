package NIO.task4;

import java.io.IOException;
import java.nio.file.*;

public class Main {
    private static final String DIRECTORY_PATH = "NIO_2\\src\\main\\resources\\task4";

    public static void main(String[] args) {
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
                    }
                    else if(event.kind() == StandardWatchEventKinds.ENTRY_MODIFY){
                        System.out.println("Modified: " + event.context());
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
