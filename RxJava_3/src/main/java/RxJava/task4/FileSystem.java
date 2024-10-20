package RxJava.task4;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

import java.util.concurrent.atomic.AtomicInteger;

public class FileSystem{

    void printSize(int size){
        System.out.println("QueueSize: " + size);
    }

    AtomicInteger queueSize = new AtomicInteger(0);
    int maxSize;

    public FileSystem(int maxSize) {
        this.maxSize = maxSize;
    }

    public void start(){
        Observable<File> fileStream = Observable.generate(
                emitter -> {
                    if(queueSize.get() < maxSize){
                        emitter.onNext(FileGenerator.getFile());
                        printSize(queueSize.incrementAndGet());
                    }
                }
        )
                .take(20).cast(File.class)
                .subscribeOn(Schedulers.io());
        fileStream.blockingSubscribe((x) ->{
            FileHandler.handle(x);
            printSize(queueSize.decrementAndGet());
        });
    }
}
