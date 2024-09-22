package multithreading.task3;

/*
Реализовать следующую многопоточную систему.
Файл имеет следующие характеристики:
    Тип файла (например XML, JSON, XLS)
    Размер файла — целочисленное значение от 10 до 100.
Генератор файлов -- генерирует файлы с задержкой от 100 до 1000 мс.
Очередь — получает файлы из генератора. Вместимость очереди — 5 файлов.
Обработчик файлов — получает файл из очереди. Каждый обработчик имеет параметр
 — тип файла, который он может обработать. Время обработки файла: «Размер файла*7мс»
Система должна удовлетворять следующими условиям:
    Должна быть обеспечена потокобезопасность.
    Работа генератора не должна зависеть от работы обработчиков, и наоборот.
    Если нет задач, то потоки не должны быть активны.
    Если нет задач, то потоки не должны блокировать другие потоки.
    Должна быть сохранена целостность данных.
 */

import java.util.ArrayDeque;
import java.util.Queue;

public class Main {

    private static final int QUEUE_MAX_SIZE = 5;

    public static void main(String[] args) {
            Queue<File> queue = new ArrayDeque();

            FileSystem fs= new FileSystem(queue, QUEUE_MAX_SIZE);
            Thread consumer = new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < 100; i++) {
                        try {
                            fs.consume();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            });

        Thread producer = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 100; i++) {
                    try {
                        fs.produce();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        producer.start();
        consumer.start();
        }
    }


