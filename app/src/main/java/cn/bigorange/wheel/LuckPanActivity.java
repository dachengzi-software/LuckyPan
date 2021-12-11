package cn.bigorange.wheel;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import cn.bigorange.wheel.utils.SoundPoolUtil;

import java.util.Random;

public class LuckPanActivity extends AppCompatActivity implements View.OnClickListener, LuckPanView.RealTimePositionListener, LuckPanView.AnimationEndListener {

    private LuckPanView mIdLuckyPan;
    private ImageView mIdStartBtn;
    private TextView mTvPosition;
    private String[] arr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_luck_pan);

        SoundPoolUtil.getInstance().initSoundPool(this);


        mIdLuckyPan = (LuckPanView) findViewById(R.id.id_luckyPan);
        mIdStartBtn = (ImageView) findViewById(R.id.id_start_btn);
        mTvPosition = (TextView) findViewById(R.id.tv_position);

        mIdStartBtn.setOnClickListener(this);
        mIdLuckyPan.setRealTimePositionListener(this);
        mIdLuckyPan.setAnimationEndListener(this);
//        arr = new String[]{"海底捞0", "日本料理1"};
        arr = new String[]{"海底捞0", "日本料理1", "火锅底料2", "饺子3", "泰国菜4", "麦当劳5", "满汉全席6", "四川火锅7", "老乡鸡8"};
        mIdLuckyPan.setmStrs(arr);
    }

    @Override
    public void onClick(View v) {
        int position = getRandomNumber(arr.length, 0);
        mIdLuckyPan.rotate(position);
    }

    public int getRandomNumber(int max, int min) {
        return new Random().nextInt(max - min + 1) + min;
    }

    private int lastPosition = -1;

    @Override
    public void getPosition(final int position, final String name) {
        if (lastPosition != position) {
            lastPosition = position;
            SoundPoolUtil.getInstance().playSound(2);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mTvPosition.setText(name);
//                    mTvPosition.setText(mIdLuckyPan.getmStrs()[position]);
                }
            });
        }
    }

    @Override
    public void endAnimation() {
        SoundPoolUtil.getInstance().playSound(1);
        mTvPosition.setText(mIdLuckyPan.getmStrs()[lastPosition]);
        lastPosition = -1;
    }
}