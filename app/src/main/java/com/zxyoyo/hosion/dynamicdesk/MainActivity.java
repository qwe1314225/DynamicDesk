package com.zxyoyo.hosion.dynamicdesk;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

public class MainActivity extends AppCompatActivity {
    private CheckBox cbSound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cbSound = (CheckBox) findViewById(R.id.cb_sound);
        //监听是否开启声音
        cbSound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    //开启声音
                    MyWallpaper.openSound(MainActivity.this);
                }else {
                    //关闭声音
                    MyWallpaper.closeSound(MainActivity.this);
                }
            }
        });
    }

    //按钮的点击事件
    public void setDynamicDesk(View view){
        MyWallpaper.setDynamicWallPaper(this);
    }
}
