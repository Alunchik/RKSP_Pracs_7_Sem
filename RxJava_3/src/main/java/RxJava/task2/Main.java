//2%3+1=2+1=3 - 3 вариант


package RxJava.task2;


import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.schedulers.Schedulers;

import java.util.Random;

public class Main {
    public static void main(String[] args) {
        Random rand = new Random();
        /*
        2.1.3	Преобразовать поток из случайного
        количества (от 0 до 1000) случайных чисел в
         поток, содержащий количество чисел.
         */
        System.out.println("task 1");
        int size = rand.nextInt(1000);
        System.out.println(size);
        Observable<Integer> observable = Observable.range(0, size)
                .map(x -> rand.nextInt(1000));
        System.out.println(observable.count().blockingGet());
                /*
                2.2.3.	Даны два потока по 1000 элементов.
                Каждый содержит случайные цифры. Сформировать поток, обрабатывающий оба
                потока параллельно. Например, при входных потоках
                (1, 2, 3) и (4, 5, 6) выходной поток — (1, 4, 2, 5, 3, 6).
                 */
        System.out.println("task 2");
        Observable<Integer> observable1 = Observable.range(0, 1000);
        Observable<Integer> observable2 = Observable.range(0, 1000);
        Observable<Integer> mergedObservable = Observable.merge(
                observable1.observeOn(Schedulers.newThread()),
                observable2.observeOn(Schedulers.newThread()));
        mergedObservable
                .blockingSubscribe(number -> System.out.print(number + " "));
        /*
        2.3.3. 2.3.3.	Дан поток из случайного количества случайных чисел.
         Сформировать поток, содержащий только последнее число.
         */
        System.out.println();
        System.out.println("task 3");

        observable = Observable.range(0, rand.nextInt(1000))
                .map(x -> {
                    int num = rand.nextInt(1000);
                    System.out.print(num + " ");
                    return num;
                }).doFinally(() -> System.out.println());
        System.out.println(observable.reduce((x, y) -> y).blockingGet());
    }
}
