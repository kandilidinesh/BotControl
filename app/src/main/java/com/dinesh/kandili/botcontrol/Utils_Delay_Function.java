package com.dinesh.kandili.botcontrol;

import android.os.Handler;
/**
 * Created by kandi on 24-02-2018.
 */

public class Utils_Delay_Function {

    // Delay mechanism

    public interface DelayCallback{
        void afterDelay();
    }

    public static void delay(int secs, final DelayCallback delayCallback){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                delayCallback.afterDelay();
            }
        }, secs * 1000); // afterDelay will be executed after (secs*1000) milliseconds.
    }
}