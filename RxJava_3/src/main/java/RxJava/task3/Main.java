package RxJava.task3;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;

import java.util.Random;
import java.util.stream.Stream;

public class Main {
    static UserFriend[] userFriends = new UserFriend[100];

    static Observable<UserFriend> getFriends(int userId){
        return Observable.fromArray(userFriends).filter(x -> x.id == userId);
    }
    public static void main(String[] args) {
        Random rand = new Random();

        for(int i=0; i<userFriends.length; i++){
            userFriends[i] = new UserFriend(rand.nextInt(50), rand.nextInt(50));
        }

       Integer[] userIds = new Integer[10];
        for(int i=0; i< userIds.length; i++){
            userIds[i] = rand.nextInt(50);
        }
        Observable.fromArray(userIds).flatMap(x -> getFriends(x));

    }
}
