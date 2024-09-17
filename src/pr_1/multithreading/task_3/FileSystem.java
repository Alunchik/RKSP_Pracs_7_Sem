package pr_1.multithreading.task_3;

import java.util.ArrayDeque;
import java.util.Queue;

public class FileSystem {

    private static final int QUEUE_MAX_SIZE = 5;

    class FileConsumer extends Thread{
        FileHandler handler = new FileHandler();

        Queue<File> queue;


        public FileConsumer(Queue queue) {
            this.queue = queue;
        }

        private void consume() throws InterruptedException {
            synchronized (queue) {
                while (queue.size() == 0) {
                    System.out.println("The Queue is empty. Consumer thread needs to wait.");
                    queue.wait();
                }
                handler.handle(queue.poll());
                queue.notifyAll();
            }
        }
        @Override
        public void start(){
            System.out.println("start");
            for(int i=0; i<100; i++){
                try {
                    consume();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    class FileProducer extends Thread{
        FileGenerator fileGenerator = new FileGenerator();

        Queue<File> queue;


        public FileProducer(Queue queue) {
            this.queue = queue;
        }

        private void produce() throws InterruptedException {
            synchronized (queue) {
                while (queue.size() == QUEUE_MAX_SIZE) {
                    System.out.println("The Queue is Full. Producer thread needs to wait.");
                    queue.wait();
                }
                queue.add(fileGenerator.getFile());
                System.out.println(queue.size());
                queue.notifyAll();
            }
        }

        @Override
        public void start(){
            for(int i=0; i<100; i++){
                try {
                    produce();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public void execute(){
        Queue<File> queue = new ArrayDeque();
        FileConsumer fc = new FileConsumer(queue);
        FileProducer fp = new FileProducer(queue);
        fp.start();
        fc.start();
    }
}
