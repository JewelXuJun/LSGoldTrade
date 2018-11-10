package com.jme.common.ui.base;

/**
 * Created by gengda on 16/9/18.
 */

import android.os.CountDownTimer;
import android.widget.Button;

public class JMECountDownTimer extends CountDownTimer {
    private Button countdownButton;
    private String buttonText;

    public JMECountDownTimer(long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
    }

    /**
     * @param millisInFuture    总的时间（毫秒）
     * @param countDownInterval 间隔时间（毫秒）
     * @param button            倒计时的按钮
     * @param buttonText        倒计时按钮的初始文字
     */
    public JMECountDownTimer(long millisInFuture, long countDownInterval, Button button, String buttonText) {
        this(millisInFuture, countDownInterval);
        this.countdownButton = button;
        this.buttonText = buttonText;
    }

    @Override
    public void onFinish() {
        countdownButton.setEnabled(true);
        countdownButton.setText(buttonText);
    }

    @Override
    public void onTick(long millisUntilFinished) {
        countdownButton.setEnabled(false);
        countdownButton.setText("剩余" + millisUntilFinished / 1000 + "秒");
    }
}