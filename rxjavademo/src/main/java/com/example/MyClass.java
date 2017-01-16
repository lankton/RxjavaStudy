package com.example;

import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MyClass {
    public static void main(String[] args) throws InterruptedException {
        Flowable.just("hello world").subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                System.out.println(s);
            }
        });

        Flowable.fromCallable(() -> {
            Thread.sleep(1000);
            return "done";
        })
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.single())
                .subscribe((s) -> {
                    System.out.println(s);
                });


//        Flowable.range(1, 10)
//                .observeOn(Schedulers.computation())
//                .map(v -> v * v)
//                .blockingSubscribe(System.out::println);

        Flowable.range(1, 10)
                .flatMap(v ->
                    Flowable.just(v)
                            .subscribeOn(Schedulers.computation())
                            .map(s -> s * s)
                )
                .blockingSubscribe(System.out::println);

        Thread.sleep(2000);
    }
}
