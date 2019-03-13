package activitytest.example.com.tomatobell.Screen;

import android.content.Intent;
import android.os.Handler;
import android.os.SystemClock;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Timer;
import java.util.TimerTask;
import activitytest.example.com.tomatobell.R;
import android.media.MediaPlayer;
import android.widget.PopupMenu.OnMenuItemClickListener;


public class NormalBellScreen extends BaseActivity implements finishBell,View.OnClickListener,OnMenuItemClickListener {

    private TextView timerView;

    private long baseTimer;

    private int countDown=0;

    private int firstCopyCountDown=0;

    private int pauseCountDown=0;
    //工作钟
    private Timer workTimer;
    //工作钟任务
    private TimerTask timerTask;
    //isquit表示是否是退出
    private boolean isquit=true;
    private boolean isPause=false;
    private boolean isfirst=true;

    private Button pause;
    private Button finish;
    private Button beginMusic;
    private Button pauseMusic;
    private Button endMusic;
    private Button musicList;

    private MediaPlayer mediaPlayer = new MediaPlayer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.normal_bell_screen_activity);

        //保存完整最开始的倒计时
        firstCopyCountDown=concentrateTime;

        //获取倒计时组件
        timerView = (TextView) this.findViewById(R.id.timeView);
        final Handler startTimehandler = new Handler(){
            public void handleMessage(android.os.Message msg) {
                if (null != timerView) {
                    timerView.setText((String) msg.obj);
                }
            }
        };

        //获取当前界面时间
        NormalBellScreen.this.baseTimer = SystemClock.elapsedRealtime();
        //启动计时器
        NormalStartTimer(startTimehandler);
        //选择要播放的音乐
        mediaPlayer =MediaPlayer.create(this, R.raw.test);

        //获取暂停按钮，暂停计时
        pause = (Button)findViewById(R.id.pause);
        pause.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //做到轮换效果
                if (isPause==false){
                    isPause=true;
                }else {
                    isPause=false;
                }

                if (isPause==true) {
                    isfirst=false;
                    //保存当前时刻，并输出toast
                    pauseCountDown = countDown;
                    Toast.makeText( NormalBellScreen.this, pauseCountDown + "", Toast.LENGTH_SHORT ).show();
                    finishBell();
                }else {
                    //获取当前界面时间
                    NormalBellScreen.this.baseTimer = SystemClock.elapsedRealtime();
                    Toast.makeText( NormalBellScreen.this, "恢复倒计时", Toast.LENGTH_SHORT ).show();
                    NormalStartTimer(startTimehandler);
                }
            }
        } );

        //获取结束按钮
        finish = (Button) findViewById(R.id.finish);
        finish.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishBell();
                ActivityCollector.finishAll();
            }
        } );

        //播放音乐的按钮
        beginMusic = (Button)findViewById(R.id.beginMusic);
        pauseMusic = (Button)findViewById(R.id.pauseMusic);
        endMusic = (Button)findViewById(R.id.endMusic);

        beginMusic.setOnClickListener(this);
        pauseMusic.setOnClickListener(this);
        endMusic.setOnClickListener(this);

        musicList = (Button)findViewById(R.id.musicList);
        musicList.setOnClickListener(this);

        super.onCreate(savedInstanceState);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.beginMusic:
                if(!mediaPlayer.isPlaying()){
                    mediaPlayer.start();
                    mediaPlayer.setLooping(true);
                }
                break;
            case R.id.pauseMusic:
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                }
                break;
            case R.id.endMusic:
                mediaPlayer.reset();
                mediaPlayer =MediaPlayer.create(this, R.raw.test1);
                mediaPlayer.start();
                mediaPlayer.setLooping(true);
                break;
            case R.id.musicList:
                Toast.makeText(this, "没毛病1", Toast.LENGTH_SHORT).show();
                //创建弹出式菜单对象（最低版本11）
                PopupMenu popup = new PopupMenu(this, view);//第二个参数是绑定的那个view
                Toast.makeText(this, "没毛病2", Toast.LENGTH_SHORT).show();
                //获取菜单填充器
                MenuInflater inflater = popup.getMenuInflater();
                Toast.makeText(this, "没毛病3", Toast.LENGTH_SHORT).show();
                //填充菜单
                inflater.inflate(R.menu.main, popup.getMenu());
                Toast.makeText(this, "没毛病4", Toast.LENGTH_SHORT).show();
                //绑定菜单项的点击事件
                popup.setOnMenuItemClickListener(this);
                Toast.makeText(this, "没毛病5", Toast.LENGTH_SHORT).show();
                //显示(这一行代码不要忘记了)
                popup.show();
                Toast.makeText(this, "没毛病6", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    //弹出式菜单的单击事件处理


    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        // TODO Auto-generated method stub
        switch (menuItem.getItemId()) {
            case R.id.nomusic:
                Toast.makeText(this, "无音乐", Toast.LENGTH_SHORT).show();
                break;
            case R.id.music1:
                Toast.makeText(this, "音乐1", Toast.LENGTH_SHORT).show();
                break;
            case R.id.music2:
                Toast.makeText(this, "音乐2", Toast.LENGTH_SHORT).show();
                break;
            case R.id.music3:
                Toast.makeText(this, "音乐3", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isquit==true) {
            Toast.makeText( NormalBellScreen.this, "退出", Toast.LENGTH_SHORT ).show();
            //关闭计时器
            finishBell();
            ActivityCollector.finishAll();
        }
        //关闭音乐播放器
        if(mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }

    protected void NormalStartTimer(final Handler startTimehandler){
        //计时器
        workTimer = new Timer();

        //计时器任务
        timerTask = new TimerTask() {
            @Override
            public void run() {
                //暂停逻辑
                if (isfirst==true) {
                    //正常获取倒计时
                    //nowTime表示从当前开始计时多少秒
                    int nowTime = (int) ((SystemClock.elapsedRealtime() - NormalBellScreen.this.baseTimer) / 1000);
                    //换算为倒计时
                    countDown = concentrateTime - nowTime;

                } else {
                    //nowTime表示从当前开始计时多少秒
                    int nowTime = (int) ((SystemClock.elapsedRealtime() - NormalBellScreen.this.baseTimer) / 1000);
                    //换算为倒计时
                    countDown = pauseCountDown - nowTime;
                }

                //每个时刻判断是显示倒计时还是跳转到休息钟
                if (countDown>=0){
                    startTimehandler.sendMessage(getMessage(countDown));
                }else{
                    //不是退出，是切换到休息界面
                    isquit=false;
                    //关闭计时器
                    finishBell();
                    ActivityCollector.finishAll();
                    //跳转到休息界面
                    Intent intent = new Intent( NormalBellScreen.this, RestScreenActivity.class );
                    startActivity( intent );

                }
            }
        };

        //安排计时器
        workTimer.schedule(timerTask,0l,1000l);
    }

    @Override
    public void finishBell() {
        workTimer.cancel();
        timerTask.cancel();
    }

}