package com.deciede.tetris;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.MotionEvent;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GameView gameView = findViewById(R.id.gameView);
        BlockScreen screen = findViewById(R.id.blockScreen);
        ImageButton rotate = findViewById(R.id.rotate),
                fast = findViewById(R.id.fast),
                right = findViewById(R.id.right),
                left = findViewById(R.id.left),
                pause = findViewById(R.id.pause);
        TextView score = findViewById(R.id.score);
        TextView status = findViewById(R.id.status);
        TextView tip = findViewById(R.id.tip);

        Handler handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 0:
                        score.setText((String) msg.obj);
                        break;
                    case 1:
                        status.setText("游戏结束");
                        tip.setText("点击旋转重新开始");
                        break;
                }
            }
        };

        gameView.setBlockScreen(screen);
        gameView.setActivityHandle(handler);

        rotate.setOnClickListener(view -> {
            switch (gameView.getStatus()){
                case 0:
                    status.setText("");
                    tip.setText("");
                    gameView.reset();
                    break;
                case 1:
                    gameView.rotate();
                    break;
            }
        });

        fast.setOnTouchListener((view, motionEvent) -> {
            switch (motionEvent.getAction()){
                case MotionEvent.ACTION_DOWN:
                    gameView.fast();
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    gameView.normal();
                    break;
            }
            return false;
        });

        right.setOnClickListener(view -> {
            gameView.moveRight();
        });

        left.setOnClickListener(view -> {
            gameView.moveLeft();
        });

        pause.setOnClickListener(view -> {
            switch (gameView.getStatus()){
                case 1:
                    gameView.pause();
                    status.setText("游戏暂停");
                    pause.setImageResource(android.R.drawable.ic_media_play);
                    break;
                case 2:
                    gameView.play();
                    status.setText("");
                    pause.setImageResource(android.R.drawable.ic_media_pause);
                    break;
            }
        });

    }
}