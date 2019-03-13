package activitytest.example.com.tomatobell.Screen;

import android.os.Handler;
import android.os.SystemClock;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import activitytest.example.com.tomatobell.R;


public class RestScreenActivity extends BaseActivity {

    private TextView restTimerView;

    private long baseTimer;

    private int restCountDown=0;

    private Timer restTimer;

    private TimerTask timerTask;

    private boolean isquit=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_rest_screen);

        RestScreenActivity.this.baseTimer = SystemClock.elapsedRealtime();
        restTimerView = (TextView) this.findViewById(R.id.restTimeView);
        final Handler startTimehandler = new Handler(){
            public void handleMessage(android.os.Message msg) {
                if (null != restTimerView) {
                    restTimerView.setText((String) msg.obj);
                }
            }
        };

        restTimer = new Timer();

        timerTask = new TimerTask() {
            @Override
            public void run() {
                //nowTime表示从当前开始计时多少秒
                int nowTime = (int)((SystemClock.elapsedRealtime() - RestScreenActivity.this.baseTimer) / 1000);
                //10是10秒
                restCountDown=restTime-nowTime;

                if (restCountDown>=0){
                    startTimehandler.sendMessage(getMessage(restCountDown));
                }else{
                    isquit=false;
                    //关闭计时器
                    restTimer.cancel();
                    timerTask.cancel();
                    ActivityCollector.finishAll();
                }
            }
        };

        restTimer.schedule(timerTask,0l,1000l);


        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isquit==true) {
            Toast.makeText( RestScreenActivity.this, "退出休息", Toast.LENGTH_SHORT ).show();
            //关闭计时器
            restTimer.cancel();
            timerTask.cancel();
            ActivityCollector.finishAll();
        }
    }
}
