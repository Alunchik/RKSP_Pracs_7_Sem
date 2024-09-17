package pr_1.multithreading.task_1;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/*
Дан массив из 10000 элементов. Необходимо написать несколько реализа- ций некоторой функции F в зависимости от варианта. Функция должна быть ре- ализована следующими способами:
    Последовательно
    С использованием многопоточности (Thread, Future, и т. д.)
    С использованием ForkJoin.
После каждой операции с элементом массива (сравнение, сложение)до- бавить задержку в 1 мс при помощи Thread.sleep(1);
Провести сравнительный анализ затрат по времени и памяти при за- пускекаждого из вариантов реализации.
Варианты функций (выбор варианта осуществляется по формуле «Номер всписке группы % 3»)
    Поиск суммы элементов массива.
    +++ Поиск максимального элемента в массиве. +++
    Поиск минимального элемента в массиве.
 */

class FindMax {
    Integer max = Integer.MIN_VALUE;

    class FindMaxThread extends Thread {
        int startIndex;
        int endIndex;

        int[] arr;

        public FindMaxThread(int startIndex, int endIndex, int[] arr) {
            this.startIndex = startIndex;
            this.endIndex = endIndex;
            this.arr = arr;
        }

        @Override
        public void start() {
            synchronized (Main.class) {
                for (int i = startIndex; i < endIndex; i++) {
                    max = getMax(max, arr[i]);
                }
            }
        }
    }

    int[] getArray() {
        Random random = new Random();
        int[] arr = new int[10000];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = random.nextInt(1, 100);
        }
        return arr;
    }

    int getMax(int a, int b) {
        if (a < b) {
            return b;
        }
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return a;
    }

    int findMaxSequentially() {
        int[] arr = getArray();
        int max = arr[0];
        for (int i = 1; i < arr.length; i++) {
            max = getMax(max, arr[i]);
        }
        return max;
    }

    int findMaxParallel() {
        int[] arr = getArray();
        int startIndex = 0;
        int endIndex = 9;
        int step = 10;
        //делим массим на кусочки, по каждому из которых пройдется один поток
        //Потоки делают запись в общий ресурс - переменную max
        for (int i = endIndex + step; i < arr.length; i += step) {
            if (i > arr.length) i = arr.length;
            startIndex = endIndex;
            endIndex = i;
            new FindMaxThread(startIndex, endIndex, arr).start();
        }
        return max;
    }


    public int findMaxForkJoin() {
        int[] array = getArray();
        FindMaxRecursiveTask findMaxRecursiveTask = new FindMaxRecursiveTask(array, 0, array.length - 1);
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        return forkJoinPool.invoke(findMaxRecursiveTask);
    }

    class FindMaxRecursiveTask extends RecursiveTask<Integer> {
        private int[] array;

        private int startIndex;
        private int endIndex;

        public FindMaxRecursiveTask(int[] array, int startIndex, int endIndex) {
            this.array = array;
            this.startIndex = startIndex;
            this.endIndex = endIndex;
        }

        @Override
        protected Integer compute() {
            if (startIndex - endIndex == -1) {
                return getMax(array[startIndex], array[endIndex]);
            } else if (startIndex == endIndex) {
                return array[startIndex];
            }
            int middle = startIndex + ((endIndex - startIndex) / 2);
            FindMaxRecursiveTask first = new FindMaxRecursiveTask(array, startIndex, middle);
            FindMaxRecursiveTask second = new FindMaxRecursiveTask(array, middle, endIndex);
            first.fork();
            second.fork();
            return getMax(first.join(), second.join());
        }
    }
}
public class Main {

    public static void main(String[] args) {
        FindMax findMax = new FindMax();
        LocalDateTime start = LocalDateTime.now();
        System.out.println(findMax.findMaxSequentially());
        LocalDateTime end = LocalDateTime.now();
        Long delta = ChronoUnit.MILLIS.between(start, end);
        System.out.println("Finding max sequentially: "
                + delta + " millis");

        start = LocalDateTime.now();
        System.out.println(findMax.findMaxParallel());
        end = LocalDateTime.now();
        delta = ChronoUnit.MILLIS.between(start, end);
        System.out.println("Finding max parallel: "
                + delta + " millis");

        start = LocalDateTime.now();
        System.out.println(findMax.findMaxForkJoin());
        end = LocalDateTime.now();
        delta = ChronoUnit.MILLIS.between(start, end);
        System.out.println("Finding max with forkJoin: "
                + delta + " millis");
    }
}