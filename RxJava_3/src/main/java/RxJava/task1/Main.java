package RxJava.task1;



import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Main{
    static Random rand = new Random();

    private static final int TEMPERATURE_THRESHOLD = 25;
    private static final int CO2_THRESHOLD = 80;

    public static int getTemperature(){
        int temp = rand.nextInt(15) + 15;
        System.out.println("temperature: " + temp);
        return temp;
    }

    public static int getCO2(){
        int CO2 = rand.nextInt(70) + 30;
        System.out.println("CO2: " + CO2);
        return CO2;
    }

   static Observer signalization = new Observer<EventType>() {

        @Override
        public void onSubscribe(@NonNull Disposable d) {
            System.out.println("Subscribed on " + d);
        }

        @Override
        public void onNext(@NonNull EventType eventType) {
            if (eventType != EventType.OK){
                System.out.println(eventType.getText());
            }
        }

        @Override
        public void onError(@NonNull Throwable e) {
            System.out.println(e.getMessage());
        }

        @Override
        public void onComplete() {
            System.out.println("Completed");
        }
    };

    public static void main(String[] args) {
        Observable<Integer> tempSensor = Observable
                .interval(1, TimeUnit.SECONDS)
                .map(x -> getTemperature());

        Observable<Integer> CO2OSensor = Observable
                .interval(1, TimeUnit.SECONDS)
                .map(x -> getCO2());

        Observable<Boolean> tempObservable = tempSensor
                .map(x -> x > TEMPERATURE_THRESHOLD);

        Observable<Boolean> CO2Observable = CO2OSensor
                .map(x -> x > CO2_THRESHOLD);


        Observable<EventType> alarmObservable = tempObservable
                .withLatestFrom(CO2Observable, (temp, CO2) -> {
            if (temp && CO2) return EventType.ALARM;
            if (temp) return EventType.TEMPERATURE_EXCEEDED;
            if (CO2) return EventType.CO2_EXCEEDED;
            return EventType.OK;
        });

        alarmObservable.subscribe(signalization);

        try {
            Thread.sleep(40000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}