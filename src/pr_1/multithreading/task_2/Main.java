package pr_1.multithreading.task_2;

/* Программа запрашивает у пользователя на
вход число. Программа имитирует обработку
запроса пользователя в виде задержки от 1
до 5 секунд выводит результат: число,
возведенное в квадрат. В момент выполнения
 запроса пользователь имеет возможность
 отправить новый запрос. Реализовать с
 использованием Future.

 */

import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.*;


public class Main {
    static Scanner sc = new Scanner(System.in);
    static ExecutorService executorService = Executors.newFixedThreadPool(10);

    static int getDelay(){
        Random random = new Random();
        return random.nextInt(1, 5) * 1000;
    }

    static int getSquare(int a){
        try {
            Thread.sleep(getDelay());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return a * a;
    }

    static void handleNumber(){
        int a = sc.nextInt();
        Callable callable = new Callable() {
            @Override
            public Object call() throws Exception {
                return getSquare(a);
            }
        };
        Future future = executorService.submit(callable);
        try {
            System.out.println(future.get());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        System.out.println("Enter numbers and i will square it.");
        while(true){
            handleNumber();
        }
    }
}
