package com.zxyoyo.hosion.dynamicdesk;

import android.app.WallpaperManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;

import java.io.IOException;

/**
 * ---------------------------------------------
 * Created by small-star-star on 2017/10/11.
 * tel:zx9797997@outlook.com
 * 纸上得来终觉浅
 * ---------------------------------------------
 */

public class MyWallpaper extends WallpaperService {

    public static  final String MYWALLPAPER_ACTION="com.zxyoyo.hosion.dynamicdesk";
    public static  final String KEY_ACTION="key_action";
    public static  final int SOUND_OPEN=1;
    public static  final int SOUND_CLOSE=0;

    @Override
    public Engine onCreateEngine() {
        return new MyEngine();
    }

    public static void openSound(Context context) {
        Intent intent = new Intent(MYWALLPAPER_ACTION);
        intent.putExtra(KEY_ACTION, SOUND_OPEN);
        context.sendBroadcast(intent);
    }
    public static void closeSound(Context context) {
        Intent intent = new Intent(MYWALLPAPER_ACTION);
        intent.putExtra(KEY_ACTION, SOUND_CLOSE);
        context.sendBroadcast(intent);
    }

    public static void setDynamicWallPaper(Context context){
        final Intent intent = new Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
        intent.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
                new ComponentName(context,MyWallpaper.class));
        context.startActivity(intent);

    }



    class MyEngine extends Engine {

        private MediaPlayer mediaPlayer;

        private BroadcastReceiver broadcastReceiver;

        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);

            IntentFilter intentFilter = new IntentFilter(MYWALLPAPER_ACTION);
            registerReceiver(broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    int action = intent.getIntExtra(KEY_ACTION, -1);
                    switch (action) {
                        case SOUND_OPEN:
                            mediaPlayer.setVolume(1.0f, 1.0f);
                            break;
                        case SOUND_CLOSE:
                            mediaPlayer.setVolume(0, 0);
                            break;

                    }
                }
            }, intentFilter);


        }

        @Override
        public void onDestroy() {
            unregisterReceiver(broadcastReceiver);
            super.onDestroy();

        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            if (visible) {
                mediaPlayer.start();
            } else {
                mediaPlayer.pause();
            }
        }


        @Override
        public void onSurfaceCreated(SurfaceHolder holder) {
            super.onSurfaceCreated(holder);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setSurface(holder.getSurface());
            try{
                AssetManager aManager = getApplicationContext().getAssets();
                AssetFileDescriptor fileDescriptor = aManager.openFd("3sheng3shi.mp4");
                mediaPlayer.setDataSource(fileDescriptor.getFileDescriptor(), fileDescriptor.getStartOffset(), fileDescriptor.getLength());
                //循环播放我们的视频
                mediaPlayer.setLooping(true);
                //默认将音量设置成最小
                mediaPlayer.setVolume(0,0);
                mediaPlayer.prepare();
                mediaPlayer.start();
            }catch (IOException e){
                e.printStackTrace();
            }

        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            super.onSurfaceChanged(holder, format, width, height);
        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
            //释放，置空
            mediaPlayer.release();
            mediaPlayer = null;

        }
    }
}
