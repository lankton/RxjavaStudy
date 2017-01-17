package com.github.lankton.rxjava1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    TextView tv;
    Button btn_emit;
    Button btn_emit_throttle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = (TextView) findViewById(R.id.tv);
        btn_emit = (Button) findViewById(R.id.emit);
        btn_emit_throttle = (Button) findViewById(R.id.emit_throttle);
        subsribeEmit(btn_emit);
        subscribeEmitThrottle(btn_emit_throttle);

        findViewById(R.id.clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.startActivity(new Intent(MainActivity.this, MainActivity.class));
                MainActivity.this.finish();
            }
        });
    }

    void subsribeEmit(final View v) {
        Observable observable = Observable.create(new Observable.OnSubscribe<Integer>() {
            int a = 1;
            @Override
            public void call(final Subscriber<? super Integer> subscriber) {
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!subscriber.isUnsubscribed()) {
                            subscriber.onNext(a++);
                        }
                    }
                });
            }
        });
        observable.observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        tv.append(integer.toString() + "\n");
                    }

                });
    }

    void subscribeEmitThrottle(final View v) {
        Observable observable = Observable.create(new Observable.OnSubscribe<Integer>() {
            int a = 1;
            @Override
            public void call(final Subscriber<? super Integer> subscriber) {
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!subscriber.isUnsubscribed()) {
                            subscriber.onNext(a++);
                        }
                    }
                });
            }
        });
        observable.observeOn(AndroidSchedulers.mainThread())
                .throttleFirst(300, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        tv.append(integer.toString() + "\n");
                    }

                });
    }
}
