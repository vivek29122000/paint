package com.example.paint;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import java.util.ArrayList;
import java.util.Stack;

public class DrawView extends View {

    private Bitmap bitmap;
    private Canvas canvas;

    private int color = getResources().getColor(R.color.yellow);

    private Path path = new Path();
    private Path pointpath = new Path();
    public ViewGroup.LayoutParams params;
    Context c;
    Stack<Stroke> list = new Stack<>();

    int w, h;

    private float x, y;
    private Paint brush = new Paint();
    private Paint brush2 = new Paint();
    Paint tempbrush = new Paint();

    public DrawView(Context context) {
        super(context);
        this.c = context;
//        init();
    }

    public DrawView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.c = context;
//        init();
    }

    public DrawView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.c = context;
//        init();
    }

    public DrawView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.c = context;
//        init();
    }

    public void init(int w, int h) {
        brush.setDither(false);
        brush.setAntiAlias(true);
        brush.setColor(color);
        brush.setStyle(Paint.Style.STROKE);
        brush.setStrokeJoin(Paint.Join.ROUND);
        brush.setStrokeCap(Paint.Cap.ROUND);
        brush.setStrokeWidth(5f);

        tempbrush.setDither(false);
        tempbrush.setAntiAlias(true);
        tempbrush.setColor(color);
        tempbrush.setStyle(Paint.Style.STROKE);
        tempbrush.setStrokeJoin(Paint.Join.ROUND);
        tempbrush.setStrokeCap(Paint.Cap.ROUND);
        tempbrush.setStrokeWidth(5f);

        brush2.setDither(false);
        brush2.setAntiAlias(true);
        brush2.setColor(color);
        brush2.setStyle(Paint.Style.STROKE);
        brush2.setStrokeJoin(Paint.Join.ROUND);
        brush2.setStrokeCap(Paint.Cap.ROUND);
        brush2.setStrokeWidth(18f);

        this.w = w;
        this.h = h;

        bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);

//        params = new ViewGroup.LayoutParams(-1, -2);
    }

    public void setStrokeWidth(float w) {
        brush.setStrokeWidth(w);
    }

    public void setColor(int color) {
        this.color = color;
        brush.setColor(color);
        brush2.setColor(color);
    }

    public void undo() {
        if(list.isEmpty()) Toast.makeText(c, "Drawing area is empty", Toast.LENGTH_SHORT).show();
        else list.pop();
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        if(false)
        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                path = new Path();
//                Log.i("color", ""+this.color);
                x = event.getX();
                y = event.getY();
                path.moveTo(x, y);
                list.add(new Stroke(path, brush.getColor(), brush.getStrokeWidth()));
                invalidate();
                return true;
            case MotionEvent.ACTION_MOVE:
                x = event.getX();
                y = event.getY();
                path.quadTo(x, y, x, y);
                invalidate();
                return true;
            case MotionEvent.ACTION_UP:
//                list.add(new Stroke(path, brush.getColor(), brush.getStrokeWidth()));
//                for(Stroke s: list) {
//                    System.out.print(s.brush.getStrokeWidth() + ", ");
//                }
//                System.out.println();
                return false;
        }
//        invalidate();
        return false;
    }

    public void pointDraw(float[] array, float dist) {
        bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        canvas.drawColor(Color.BLACK);

        list = new Stack<>();
        path = null;
        pointpath = new Path();

//        canvas.drawPoints(array, brush2);
        for(int j=0; j<array.length>>1; j++) {
            for (int i = 1; i < array.length; i += 2) {
                array[i] += dist;
                pointpath.moveTo(array[i-1], array[i]);
                pointpath.lineTo(array[i-1]+1, array[i]+1);
//                pointpath.addCircle(array[i-1], array[i], 5f, Path.Direction.CCW);
            }
        }
        list.add(new Stroke(pointpath, color, 8f));
        invalidate();
//        draw(canvas);

    }

    public void clear() {

        canvas.drawColor(Color.BLACK);
        list = new Stack<>();
        path = null;
        invalidate();

    }

    @Override
    protected void onDraw(Canvas canva) {

        canva.save();

        canvas.drawColor(Color.BLACK);

        for(Stroke stroke: list) {
            tempbrush.setStrokeWidth(stroke.stroke);
            tempbrush.setColor(stroke.color);
            canvas.drawPath(stroke.path, tempbrush);
        }
//        if(path != null) canvas.drawPath(path, brush);
        canva.drawBitmap(bitmap, 0, 0, brush);

        canva.restore();
    }
}


class Stroke {
    float stroke;
    int color;
    public Path path;

    Stroke (Path path, int color, float stroke) {
        this.path = path;
        this.stroke = stroke;
        this.color = color;
    }

}