package activitytest.example.com.tomatobell.Screen;

import android.content.Intent;
import android.os.Handler;
import android.os.SystemClock;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Timer;
import java.util.TimerTask;
import activitytest.example.com.tomatobell.R;

public class LockBellscreen extends BaseActivity {

    private TextView timerView;

    private long baseTimer;

    private int countDown=0;

    private Timer workTimer;

    private TimerTask timerTask;
    //isquit表示是否是退出
    private boolean isquit=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.lock_bell_screen_activity);

        //获取当前界面时间
        LockBellscreen.this.baseTimer = SystemClock.elapsedRealtime();
        timerView = (TextView) this.findViewById(R.id.timeView);
        final Handler startTimehandler = new Handler(){
            public void handleMessage(android.os.Message msg) {
                if (null != timerView) {
                    timerView.setText((String) msg.obj);
                }
            }
        };

        //计时器
        workTimer = new Timer();

        //计时器任务
        timerTask = new TimerTask() {
            @Override
            public void run() {
                //nowTime表示从当前开始计时多少秒
                int nowTime = (int)((SystemClock.elapsedRealtime() - LockBellscreen.this.baseTimer) / 1000);
                //换算为倒计时
                countDown=concentrateTime-nowTime;
                //倒计时结束处理逻辑：前显示时间，后换界面
                if (countDown>=0){
                    startTimehandler.sendMessage(getMessage(countDown));
                }else{
                    //不是退出，是切换到休息界面
                    isquit=false;
                    //关闭计时器
                    workTimer.cancel();
                    timerTask.cancel();
                    ActivityCollector.finishAll();
                    //跳转到休息界面
                    Intent intent = new Intent( LockBellscreen.this, RestScreenActivity.class );
                    startActivity( intent );

                }
            }
        };

        //安排计时器
        workTimer.schedule(timerTask,0l,1000l);

        super.onCreate(savedInstanceState);


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isquit==true) {
            Toast.makeText( LockBellscreen.this, "锁死退出", Toast.LENGTH_SHORT ).show();
            //关闭计时器
            workTimer.cancel();
            timerTask.cancel();
            ActivityCollector.finishAll();
        }
    }
}
