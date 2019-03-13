package activitytest.example.com.tomatobell.Screen;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import activitytest.example.com.tomatobell.R;

public class setParam extends BaseActivity {

    private boolean islock=false;

    private Button normal;

    private Button lock;

    private Button start;

    private Intent intent;

    private EditText concentrateDuration;

    private EditText restDuration;

    private boolean isquit=true;

    @Override
    public void setTime(int concentrateTime, int restTime) {
        super.setTime( concentrateTime, restTime );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.set_param_activity);

        //获取普通模式按钮
        normal = (Button)findViewById(R.id.normalModel);
        normal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                islock=false;
            }
        });

        //获取锁死模式按钮
        lock = (Button)findViewById(R.id.lockModel);
        lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                islock=true;
            }
        });

        //获取专注时长
        concentrateDuration=(EditText)findViewById(R.id.concentrateDuration);

        //获取休息时长
        restDuration=(EditText)findViewById(R.id.restDuration);

        //获取开始按钮，以及处理逻辑
        start=findViewById(R.id.start);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //设置本次番茄钟专注时长和休息时长
                concentrateTime = toInt(concentrateDuration.getText().toString());
                restTime = toInt(restDuration.getText().toString());
                setTime(concentrateTime,restTime);

                if (concentrateTime!=0 && restTime!=0){
                    if (islock==true){
                        isquit=false;
                        ActivityCollector.finishAll();
                        intent=new Intent(setParam.this,LockBellscreen.class);
                        startActivity(intent);
                    }else {
                        isquit=false;
                        ActivityCollector.finishAll();
                        intent=new Intent(setParam.this,NormalBellScreen.class);
                        startActivity(intent);
                    }
                }else {
                    Toast.makeText(setParam.this,"专注时长或休息时长不能为空",Toast.LENGTH_SHORT).show();
                }


            }
        });
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isquit==true) {
            ActivityCollector.finishAll();
        }
    }
}
