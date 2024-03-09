package com.example.mob;

import static android.opengl.ETC1.getHeight;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.EditText;
public class LinedEditText extends androidx.appcompat.widget.AppCompatEditText {

    private Rect mRect;
    private Paint mPaint;

    // we need this constructor for LayoutInflater
    public LinedEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        mRect = new Rect();
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        // Задаем цвет текста (например, красный)
        mPaint.setColor(getResources().getColor(R.color.blue));
        // Задаем размер текста (например, 20dp)
        setTextSize(25);
    }

//    @Override
//    protected void onDraw(Canvas canvas) {
//        //int count = getLineCount();
//
//        int height = getHeight();
//        int line_height = getLineHeight();
//
//        int count = height / line_height;
//
//        if (getLineCount() > count)
//            count = getLineCount();//for long text with scrolling
//
//        Rect r = mRect;
//        Paint paint = mPaint;
//        int baseline = getLineBounds(0, r);//first line
//
//        for (int i = 0; i < count; i++) {
//
//            canvas.drawLine(r.left, baseline + 1, r.right, baseline + 1, paint);
//            baseline += getLineHeight();//next line
//        }
//
//        super.onDraw(canvas);
//    }


    @Override
    protected void onDraw(Canvas canvas) {
        int height = getHeight();
        int lineHeight = getLineHeight();
        int count = height / lineHeight;

        Rect rect = mRect;
        Paint paint = mPaint;

        int baseline = getLineBounds(0, rect);

        for (int i = 0; i < count; i++) {
            canvas.drawLine(rect.left, baseline + 1, rect.right, baseline + 1, paint);
            baseline += lineHeight;
        }

        super.onDraw(canvas);
    }

}