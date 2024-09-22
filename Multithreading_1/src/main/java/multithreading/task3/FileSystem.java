package multithreading.task3;

import java.util.Queue;

public class FileSystem{
    FileHandler handler = new FileHandler();

    FileGenerator fileGenerator = new FileGenerator();

    Queue<File> queue;

    int maxSize;

    public FileSystem(Queue queue, int maxSize) {
        this.queue = queue;
        this.maxSize = maxSize;
    }

    public void consume() throws InterruptedException {
        synchronized (this) {
            while (queue.isEmpty()) {
                System.out.println("The Queue is empty. Consumer thread needs to wait.");
                    wait();
            }
            handler.handle(queue.poll());
            System.out.println("Queue size: " + queue.size());
            notifyAll();
        }
    }
    public void produce() throws InterruptedException {
        synchronized (this) {
            while (queue.size() == maxSize) {
                System.out.println("The Queue is Full. Producer thread needs to wait.");
                wait();
            }
            queue.add(fileGenerator.getFile());
            System.out.println("Queue size: " + queue.size());
            notifyAll();
        }
    }
}
