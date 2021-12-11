package cn.bigorange.wheel.utils;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.util.SparseIntArray;

import cn.bigorange.wheel.R;

/**
 * @author 汪士臣 phone:15955995694
 */
public class SoundPoolUtil {

    private SoundPool soundPool;
    private SparseIntArray soundData;

    public static final int[] WAVE_ARRAY = new int[]
            {
                    R.raw.touch_pop,
                    R.raw.wheel_complete,
                    R.raw.wheel_tick
            };

    private SoundPoolUtil() {
    }

    /**
     * 单例持有者
     */
    private static class InstanceHolder {
        private static final SoundPoolUtil INSTANCE = new SoundPoolUtil();
    }

    /**
     * 获取SoundPoolUtil实例 ,单例模式
     */
    public static SoundPoolUtil getInstance() {
        return InstanceHolder.INSTANCE;
    }

    public void initSoundPool(Context tempContext) {

        //全局 ApplicationContext
        Context applicationContext = tempContext.getApplicationContext();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            SoundPool.Builder builder = new SoundPool.Builder();
            builder.setMaxStreams(WAVE_ARRAY.length); //传入音频的数量
            AudioAttributes.Builder attrBuilder = new AudioAttributes.Builder(); //AudioAttributes是一个封装音频各种属性的类
            attrBuilder.setLegacyStreamType(AudioManager.STREAM_MUSIC);//设置音频流的合适属性
            builder.setAudioAttributes(attrBuilder.build());
            soundPool = builder.build();
        } else {   //第一个参数是可以支持的声音数量，第二个是声音类型，第三个是声音品质
            soundPool = new SoundPool(WAVE_ARRAY.length, AudioManager.STREAM_MUSIC, 0);
        }
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
//                Toast.makeText(applicationContext, "声音" + sampleId + "加载完毕！", Toast.LENGTH_SHORT).show();
            }
        });
        soundData = new SparseIntArray(WAVE_ARRAY.length);
        for (int index = 0; index < WAVE_ARRAY.length; index++) {
            int resId = WAVE_ARRAY[index];
            soundData.put(index, soundPool.load(applicationContext, resId, 1));
        }
    }


    public void playSound(int soundIndex) {
        if (soundPool != null && soundData != null && soundIndex != -1) {
            soundPool.play(soundData.get(soundIndex), 1.0f, 1.0f, 0, 0, 1.0f);
        }
    }

    public void pauseSound() {
        if (soundPool != null) {
            soundPool.autoPause();
        }
    }

    public void destory() {
        if (soundPool != null) { // 销毁的时候释放SoundPool资源
            soundPool.release();
            soundPool = null;
        }
        if (soundData != null) {
            soundData.clear();
            soundData = null;
        }
    }


}
