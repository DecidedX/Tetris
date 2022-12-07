package com.deciede.tetris;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.provider.Settings;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

@SuppressLint("ViewConstructor")
public class GameView extends SurfaceView implements SurfaceHolder.Callback, Runnable{

    private SurfaceHolder mSurfaceHolder;
    //绘图的Canvas
    private Canvas mCanvas;
    //子线程标志位
    private boolean mIsDrawing;
    private int start_x,start_y,h_w,h_h,e;
    private final BlockPanel panel;
    private final Paint mPaint;
    private final FallBlocks fallBlocks;
    public GameView(Context context) {
        this(context, null);
    }

    public GameView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(5);
        panel = new BlockPanel();
        fallBlocks = FallBlocks.getInstance(panel);
        initView();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mIsDrawing = true;
        new Thread(this).start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mIsDrawing = false;
    }

    @Override
    public void run() {
        int h = getMeasuredHeight();
        int w = getMeasuredWidth();
        e = h/24;
        h_h = h/2;
        h_w = w/2;
        start_x = h_w-6*e;
        start_y = h_h-12*e;
        int i = 0;
        while (mIsDrawing){
//            boolean draw = i == 4;
//            i = draw?0:i+1;
//            if (draw){
//                blocks[x][y] = 1;
//                colors[x][y] = Color.argb(255,14,171,72);
//            }
//            drawBlocks();
//            x += 1;
//            if (x == 12) {
//                x = 0;
//                y += 1;
//            }
//            if (y == 24) {
//                mIsDrawing = false;
//            }
            drawBlocks();
        }
    }

    public void setActivityHandle(Handler handler){
        panel.setActivity(handler);
    }

    public void setBlockScreen(BlockScreen blockScreen) {
        fallBlocks.setBlockScreen(blockScreen);
    }

    public int getStatus(){
        return panel.getStatus();
    }

    public void play(){
        if (!fallBlocks.isFall()){
            fallBlocks.startFall();
            panel.setStatus(BlockPanel.RUNNING);
        }
    }

    public void pause(){
        if (fallBlocks.isFall()){
            fallBlocks.stopFall();
            panel.setStatus(BlockPanel.PAUSE);
        }
    }

    public void reset(){
        panel.reset();
        fallBlocks.reset();
    }

    public void moveRight(){
        Blocks blocks = fallBlocks.getBlocks();
        if (!panel.touchRight(blocks)){
            if (!panel.touchBlock(blocks,BlockPanel.RIGHT)){
                blocks.moveRight();
            }
        }
    }

    public void moveLeft(){
        Blocks blocks = fallBlocks.getBlocks();
        if (!panel.touchLeft(blocks)){
            if (!panel.touchBlock(blocks,BlockPanel.LEFT)){
                blocks.moveLeft();
            }
        }
    }

    public void rotate(){
        Blocks blocks = fallBlocks.getBlocks();
        Blocks after = new Blocks(Blocks.rotate(blocks.getBlocks()),blocks.x,blocks.y);
        if (!(panel.crossRight(after) || panel.crossLeft(after) || panel.inBlock(after))){
            fallBlocks.getBlocks().rotate();
        }
    }

    public void fast(){
        fallBlocks.setSpeed(0);
    }

    public void normal(){
        fallBlocks.setSpeed(10);
    }

    private void drawBlocks() {
        try {
            //获得canvas对象
            mCanvas = mSurfaceHolder.lockCanvas();
            //绘制背景
            mCanvas.drawColor(Color.WHITE);
            mCanvas.drawRoundRect(start_x,start_y,h_w+6*e,h_h+12*e,20,20,mPaint);
            if (panel.getStatus() == 1){
                for (int i = 0;i < 12;i++){
                    for (int j = 4;j < 28;j++){
                        if (panel.panel[i][j] == 1){
                            drawBlock(i,j-4,panel.colors[i][j]);
                        }
                    }
                }
                drawCombination(fallBlocks.getBlocks(),fallBlocks.getColor());
                if (!fallBlocks.isFall()){
                    fallBlocks.startFall();
                }
            }
        }catch (Exception e){

        }finally {
            if (mCanvas != null){
                //释放canvas对象并提交画布
                mSurfaceHolder.unlockCanvasAndPost(mCanvas);
            }
        }
    }

    private void drawBlock(int x,int y,int color){
        Paint paint = new Paint();
        paint.setColor(color);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        mCanvas.drawRoundRect(start_x+x*e,start_y+y*e,start_x+(x+1)*e,start_y+(y+1)*e,20,20,paint);
    }

    private void drawCombination(Blocks blocks,int color){
        Paint paint = new Paint();
        paint.setColor(color);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        for (int i=0;i<blocks.getBlocks().length;i++){
            for (int j=0;j<blocks.getBlocks()[i].length;j++){
                if (blocks.y+j > 3 && blocks.getBlocks()[i][j] == 1) {
                    drawBlock(blocks.x + i, blocks.y + j-4, color);
                }
            }
        }
    }


    /**
     * 初始化View
     */
    private void initView(){
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);
        setFocusable(true);
        setKeepScreenOn(true);
        setFocusableInTouchMode(true);
    }

}
