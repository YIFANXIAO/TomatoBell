package activitytest.example.com.tomatobell;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import activitytest.example.com.tomatobell.Screen.setParam;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button tomatobell=(Button) findViewById(R.id.tomatobell);
        tomatobell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,setParam.class);
                startActivity(intent);
            }
        });
    }
}
