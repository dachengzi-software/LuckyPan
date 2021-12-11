package cn.bigorange.wheel;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;

import cn.bigorange.wheel.utils.AngleUtils;
import cn.bigorange.wheel.utils.ThreadUtil;

import java.math.BigDecimal;

public class LuckPanView extends View {

    private static final String TAG = "汪士臣LuckPanView";
    //盘块的奖项
    private String[] mStrs;

    //    private int currentPosition = -1;//当前指向的位置
    private ValueAnimator valueAnimator;
    private ObjectAnimator objectAnimator;
    private boolean isEnd = false;//是否结束

    public String[] getmStrs() {
        return mStrs;
    }

    public void setmStrs(String[] mStrs) {
        this.mStrs = mStrs;
        this.mItemCount = mStrs.length;
        this.startAngleArray = new float[mItemCount];
        this.sweepAngle = 360.0f / mItemCount;
    }

    private PorterDuffXfermode xfermode;
    private Paint zhezhaoPain = new Paint();

    private int mItemCount;
    private float[] startAngleArray;

    private final Bitmap mBgBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.bg2);

    //盘块的颜色
    private final int[] mColor = getResources().getIntArray(R.array.customizedColors);

    //绘制盘块的画笔
    private Paint mArcPaint;
    //绘制文本的画笔
    private Paint mTextPaint;
    private Paint backgroundPaint;
    private Paint mSeparatorPaint;
    private Path textBaseLinePath;//每块扇形上面文字以此线垂直居中


    private int _20dp = dp2px(8);

    private float mTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14, getResources().getDisplayMetrics());

    //整个盘块的范围
    private RectF mRange = new RectF();
    //半径
    private int mRadius;

    //转盘的中心位置
    private int mCenter;

    //角度
    private volatile float mStartAngle = 0.0f;

    private float sweepAngle;

    //是否点击了停止按钮
    private boolean isShouldEnd;

    public LuckPanView(Context context) {
        this(context, null, 0);
    }

    public LuckPanView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LuckPanView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setmStrs(new String[]{"单反相机0", "IPAD1", "恭喜发财2", "IPHONE3", "服装一套4", "恭喜发财5", "恭喜发财6", "恭喜发财7", "恭喜发财8", "恭喜发财9", "恭喜发财10", "恭喜发财11", "恭喜发财12"});
        init();
    }

    public int dp2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private void init() {
        //初始化绘制盘块的画笔
        mArcPaint = new Paint();
        mArcPaint.setAntiAlias(true);
        mArcPaint.setDither(true);

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setColor(0xffffffff);
        mTextPaint.setTextSize(mTextSize);

        backgroundPaint = new Paint();
        backgroundPaint.setAntiAlias(true);
        backgroundPaint.setColor(Color.WHITE);
        backgroundPaint.setShadowLayer(20f, 0, 0, 0xFFDDDDDD);

        mSeparatorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSeparatorPaint.setAntiAlias(true);
        mSeparatorPaint.setColor(Color.WHITE);
        mSeparatorPaint.setStyle(Paint.Style.STROKE);
        mSeparatorPaint.setStrokeWidth(2.0f);
        mSeparatorPaint.setStrokeJoin(Paint.Join.ROUND);
        mSeparatorPaint.setStrokeCap(Paint.Cap.ROUND);
        //每块扇形上面文字的居中线
        textBaseLinePath = new Path();

        xfermode = new PorterDuffXfermode(PorterDuff.Mode.XOR);//遮罩
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = Math.min(getMeasuredWidth(), getMeasuredHeight());

        //这里我们的padding直接以paddingLeft为准
        int mPadding = getPaddingLeft();
        //半径
        mRadius = (width - mPadding * 2) / 2;
        //中心点
        mCenter = width / 2;

        //初始化扇形盘块绘制的范围
        mRange = new RectF(mPadding, mPadding, mPadding + mRadius * 2, mPadding + mRadius * 2);

        setMeasuredDimension(width, width);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

//        canvas.rotate(mStartAngle, mCenter, mCenter);
        //绘制背景
        drawBg(canvas);
        drawPan(canvas);

//        LogUtils.d(TAG, mStartAngle + "");
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    public void layout(int l, int t, int r, int b) {
        super.layout(l, t, r, b);
    }

    private void drawPan(Canvas canvas) {

        float temp = 0;
        for (int i = 0; i < mItemCount; i++) {
            startAngleArray[i] = temp;
            temp += sweepAngle;
        }
        //绘制盘块
        for (int i = 0, length = startAngleArray.length; i < length; i++) {
            mArcPaint.setColor(mColor[i % 8]);
            canvas.drawArc(mRange, startAngleArray[i], sweepAngle, true, mArcPaint);//绘制盘块
        }

        for (int i = 0, length = startAngleArray.length; i < length; i++) {
            drawText2(canvas, startAngleArray[i], mStrs[i]);
        }

        for (int i = 0, length = startAngleArray.length; i < length; i++) {
            drawSeperatorLine(canvas, startAngleArray[i]);
        }

//        ThreadUtil.getInstance().execute(new Runnable() {
//            @Override
//            public void run() {
//                float startTemp;
//                float nextStartTemp;
//                for (int i = 0, length = startAngleArray.length; i < length; i++) {
//                    startTemp = startAngleArray[i];
//                    if (i == length - 1) {
//                        nextStartTemp = startTemp + sweepAngle;
//                    } else {
//                        nextStartTemp = startAngleArray[i + 1];
//                    }
//                    if (realTimePositionListener != null) {
//                        if (AngleUtils.angleIsBetweenAngles(startTemp, nextStartTemp)) {
//                            realTimePositionListener.getPosition(i);
//                        }
//                    }
//                }
//
//            }
//        });

    }

    /**
     * 方法一通过 paint 的 xfermode 绘制遮罩
     */
    private void fun1(Canvas canvas) {
        zhezhaoPain.setDither(true);
        zhezhaoPain.setAntiAlias(true);
        zhezhaoPain.setStyle(Paint.Style.FILL);
        //先画一个圆角矩形,也就是透明区域
        //设置遮罩的颜色
        zhezhaoPain.setColor(Color.parseColor("#FFD34646"));
        //设置paint的 xfermode 为PorterDuff.Mode.SRC_OUT
        zhezhaoPain.setXfermode(xfermode);
        //画遮罩的矩形
        canvas.drawCircle(mCenter, mCenter, mRadius * 1.0f + _20dp, zhezhaoPain);
        //清空paint 的 xfermode
        zhezhaoPain.setXfermode(null);
    }

    private void drawSeperatorLine(Canvas canvas, float tmpAngle) {

        double radians = Math.toRadians(tmpAngle);

        float x = (float) (mCenter + mRadius * Math.cos(radians));
        float y = (float) (mCenter + mRadius * Math.sin(radians));

        canvas.drawLine(mCenter, mCenter, x, y, mSeparatorPaint);
    }

    /**
     * 绘制背景
     */
    private void drawBg(Canvas canvas) {

        canvas.drawCircle(mCenter, mCenter, mRadius * 1.0f + _20dp, backgroundPaint);

//        canvas.drawColor(0xffffffff);
//        canvas.drawBitmap(mBgBitmap, null, new Rect(mPadding / 2, mPadding / 2, getMeasuredWidth() - mPadding / 2, getMeasuredHeight() - mPadding / 2), null);
    }

    /**
     * 绘制每个盘块的文本
     */

//    private void drawText(Canvas canvas, float tmpAngle, float sweepAngle, String string) {
//        Path path = new Path();
//        path.addArc(mRange, tmpAngle, sweepAngle);
//
//        path.lineTo();
//
//        //利用水平偏移量让文字居中
//        float textWidth = mTextPaint.measureText(string);
//        int hOffset = (int) (mRadius * Math.PI / mItemCount / 2 - textWidth / 2);
//        int vOffset = mRadius / 2 / 6;//垂直偏移量
//        canvas.drawTextOnPath(string, path, hOffset, vOffset, mTextPaint);
//    }
    private void drawText2(Canvas canvas, float tmpAngle, String string) {

        double radians = Math.toRadians(tmpAngle + sweepAngle / 2);

        float x = (float) (mCenter + mRadius * Math.cos(radians));
        float y = (float) (mCenter + mRadius * Math.sin(radians));

        textBaseLinePath.reset();
        textBaseLinePath.moveTo(mCenter, mCenter);
        textBaseLinePath.lineTo(x, y);

        Rect bounds = new Rect();
        mTextPaint.getTextBounds(string, 0, string.length(), bounds);

        float hOffset = mRadius - bounds.width() - _20dp;//水平偏移量
        float vOffset = bounds.height() * 1.0f / 2 - bounds.bottom;

        canvas.drawTextOnPath(string, textBaseLinePath, hOffset, vOffset, mTextPaint);
    }


    @Override
    public void destroyDrawingCache() {
        super.destroyDrawingCache();
    }


    /**
     * 开始转动
     * 如果 pos = -1 则随机，如果指定某个值，则转到某个指定区域
     */
    public void startRotate(final int position) {
//        if (valueAnimator != null) {
//            valueAnimator.removeAllListeners();
//            valueAnimator.cancel();
//            valueAnimator = null;
//        }
        int rotateToPosition = 360 / mItemCount * (mItemCount - position);
        float toDegree = 360f * 5 + rotateToPosition;
        valueAnimator = ValueAnimator.ofFloat(0, toDegree);
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.setDuration(100000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float updateValue = (float) animation.getAnimatedValue();
                mStartAngle = updateValue % 360;
                ViewCompat.postInvalidateOnAnimation(LuckPanView.this);
                ThreadUtil.getInstance().execute(new Runnable() {
                    @Override
                    public void run() {
                        float eachAngle = 360.0f / mItemCount;
                        for (int i = mItemCount - 1; i >= 0; i--) {
                            if (AngleUtils.angleIsBetweenAngles(i * eachAngle + mStartAngle, (i + 1) * eachAngle + mStartAngle)) {
                                realTimePositionListener.getPosition(i, mStrs[i]);
                                break;
                            }
                        }
                    }
                });
            }
        });

//        valueAnimator.addListener(new AnimatorListenerAdapter() {
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                super.onAnimationEnd(animation);
//                isEnd = true;
//                ViewCompat.postInvalidateOnAnimation(LuckPanView.this);
//                if (animationEndListener != null) {
//                    animationEndListener.endAnimation();
//                }
//            }
//        });
        valueAnimator.start();
    }


    public static float mockFloatBetween2(Float begin, Float end) {
        BigDecimal bigDecimal = new BigDecimal(end - begin);
        BigDecimal point = BigDecimal.valueOf(Math.random());
        BigDecimal pointBetween = point.multiply(bigDecimal);
        BigDecimal result = pointBetween.add(new BigDecimal(begin)).setScale(2, BigDecimal.ROUND_FLOOR);
        return result.floatValue();
    }


    public void rotate(final int position) {
        if (objectAnimator != null) {
            objectAnimator.removeAllListeners();
            objectAnimator.cancel();
            objectAnimator = null;
        }

//        int rotateToPosition = 360 / mItemCount * (mItemCount - position);
//        float toDegree = 360f * 5 + rotateToPosition;


        float eachAngle = 360.0f / mItemCount;

        float deflectionAngle;
        float separatorAngle = 5.0f;
        if (eachAngle <= 2 * separatorAngle) {
            deflectionAngle = eachAngle / 2;
        } else if (eachAngle <= 4 * separatorAngle) {
            deflectionAngle = mockFloatBetween2(eachAngle / 4, eachAngle - eachAngle / 4);//随机偏转角度
        } else {
            deflectionAngle = mockFloatBetween2(separatorAngle, eachAngle - separatorAngle);//随机偏转角度
        }

        float toDegree = 360.0f * 5 + 270.0f + eachAngle * (mItemCount - position - 1) + deflectionAngle;

        objectAnimator = ObjectAnimator.ofFloat(LuckPanView.this, "rotation", 0f, toDegree);
        objectAnimator.setDuration(5000);
//        objectAnimator.setRepeatCount(ValueAnimator.INFINITE);
        objectAnimator.setInterpolator(new DecelerateInterpolator());
//        objectAnimator.setAutoCancel(true);
        objectAnimator.start();

        objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float updateValue = (float) animation.getAnimatedValue();
                mStartAngle = updateValue % 360;
                ViewCompat.postInvalidateOnAnimation(LuckPanView.this);
                ThreadUtil.getInstance().execute(new Runnable() {
                    @Override
                    public void run() {
                        float eachAngle = 360.0f / mItemCount;
                        for (int i = mItemCount - 1; i >= 0; i--) {
                            if (AngleUtils.angleIsBetweenAngles(i * eachAngle + mStartAngle, (i + 1) * eachAngle + mStartAngle)) {
                                realTimePositionListener.getPosition(i, mStrs[i]);
                                break;
                            }
                        }
                    }
                });


//                float updateValue = (float) animation.getAnimatedValue();
//                LogUtils.d(TAG, String.valueOf(updateValue));
//
//                final float mStartAngleTemp = updateValue % 360;
//
//                ThreadUtil.getInstance().execute(new Runnable() {
//                    @Override
//                    public void run() {
//                        float startTemp;
//                        float nextStartTemp;
//                        for (int i = 0, length = startAngleArray.length; i < length; i++) {
//                            startTemp = startAngleArray[i];
//                            nextStartTemp = startTemp + sweepAngle;
//                            if (realTimePositionListener != null) {
//                                if (AngleUtils.angleIsBetweenAngles2(startTemp, nextStartTemp, mStartAngleTemp)) {
//                                    realTimePositionListener.getPosition(i);
//                                }
//                            }
//                        }
//
//                    }
//                });
            }
        });

        objectAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Toast.makeText(getContext(), "结束了" + position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
    }

    public interface AnimationEndListener {
        void endAnimation();
    }

    private AnimationEndListener animationEndListener;

    public void setAnimationEndListener(AnimationEndListener animationEndListener) {
        this.animationEndListener = animationEndListener;
    }

    public interface RealTimePositionListener {
        void getPosition(int position, String name);

    }

    private RealTimePositionListener realTimePositionListener;

    public void setRealTimePositionListener(RealTimePositionListener realTimePositionListener) {
        this.realTimePositionListener = realTimePositionListener;
    }
}
