package com.example.paint;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    DrawView draw_view;
    int width, height;

    View selected_view;
    ImageButton ib1, ib2, ib3, ib4, cb1, cb2, cb3, cb4, cb5, cb6, cb7;
    EditText et1;
    CardView cv2;
    TextView tv1;

    boolean colorVisible = false;
    boolean brushistrue = true;
    int grid_count;
    float grid_dist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        draw_view = findViewById(R.id.drawView);
        ib1 = findViewById(R.id.ib1);
        ib2 = findViewById(R.id.ib2);
        ib3 = findViewById(R.id.ib3);
        ib4 = findViewById(R.id.ib4);
        et1 = findViewById(R.id.et1);

        cv2 = findViewById(R.id.cv2);
        tv1 = findViewById(R.id.tv1);

        cb1 = findViewById(R.id.cb1);
        cb2 = findViewById(R.id.cb2);
        cb3 = findViewById(R.id.cb3);
        cb4 = findViewById(R.id.cb4);
        cb5 = findViewById(R.id.cb5);
        cb6 = findViewById(R.id.cb6);
        cb7 = findViewById(R.id.cb7);

        cb1.setOnClickListener(this);
        cb2.setOnClickListener(this);
        cb3.setOnClickListener(this);
        cb4.setOnClickListener(this);
        cb5.setOnClickListener(this);
        cb6.setOnClickListener(this);
        cb7.setOnClickListener(this);


//        tv = findViewById(R.id.tv);
//        draw_view = new DrawView(this);
//        draw_view.setBackgroundColor(Color.WHITE);
//        setContentView(draw_view);



        DisplayMetrics metrics = getResources().getDisplayMetrics();

        int w = metrics.widthPixels;
        int h = metrics.heightPixels;

        this.getWindow().setStatusBarColor(getResources().getColor(R.color.yellow));

        ViewTreeObserver vto = draw_view.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                draw_view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                width = draw_view.getMeasuredWidth();
                height = draw_view.getMeasuredHeight();
                draw_view.init(width, height);
            }
        });

        ib1.setOnClickListener(vi -> {
            tv1.setText("Stroke Width");
            et1.setText("");
            et1.setHint("Enter stroke width");
            brushistrue = true;

            tv1.setVisibility(View.VISIBLE);
            et1.setVisibility(View.VISIBLE);
        });

        ib2.setOnClickListener(vi -> {
            draw_view.clear();
        });

        ib3.setOnClickListener(vieww -> {
            tv1.setText("point Count");
            brushistrue = false;
            et1.setText("");
            et1.setHint("Enter point count");

            tv1.setVisibility(View.VISIBLE);
            et1.setVisibility(View.VISIBLE);
        });

        ib4.setOnClickListener(v -> {
            draw_view.undo();
        });

    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if(keyCode == 66) {
            enter();
        }
        return false;
    }

    public  void enter() {
        String temp = et1.getText().toString();
        if(temp.length() <= 0) {
            et1.setError("Enter a number");
            return;
        }

        tv1.setVisibility(View.INVISIBLE);
        et1.setVisibility(View.INVISIBLE);

        if(!brushistrue) {
            grid_count = Integer.parseInt(temp);
            grid_dist = (float) (width) / (float) (grid_count + 1);

            if (grid_dist <= 0) {
                Toast.makeText(this, "more grids than screen can hold", Toast.LENGTH_SHORT).show();
            }
            float[] array = new float[grid_count << 1];
            array[0] = grid_dist;
            array[1] = grid_dist;
            for (int i = 2; i < grid_count << 1; i += 2) {
                array[i] = array[i - 2] + grid_dist;
                array[i + 1] = grid_dist;
            }
            draw_view.pointDraw(array, grid_dist);
            return;
        }

        draw_view.setStrokeWidth(Float.parseFloat(temp));
    }

    @Override
    public void onClick(View v) {

        int c = getResources().getColor(R.color.yellow);

        tv1.setVisibility(View.INVISIBLE);
        et1.setVisibility(View.INVISIBLE);

        switch(v.getId()) {
            case R.id.cb1:
                c = getResources().getColor(R.color.black);
                break;
            case R.id.cb2:
                c = getResources().getColor(R.color.cyan);
                break;
            case R.id.cb3:
                c = getResources().getColor(R.color.orange);
                break;
            case R.id.cb4:
                c = getResources().getColor(R.color.yellow);
                break;
            case R.id.cb5:
                c = getResources().getColor(R.color.white);
                break;
            case R.id.cb6:
                c = getResources().getColor(R.color.green);
                break;
            case R.id.cb7:
                c = getResources().getColor(R.color.magenta);
                break;
        }
        this.getWindow().setStatusBarColor(c);
        draw_view.setColor(c);
    }
}