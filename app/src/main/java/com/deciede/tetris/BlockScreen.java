package com.deciede.tetris;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class BlockScreen extends SurfaceView implements SurfaceHolder.Callback, Runnable{

    private SurfaceHolder mSurfaceHolder;
    //绘图的Canvas
    private Canvas mCanvas;
    //子线程标志位
    private boolean mIsDrawing;
    private int gap = 5, e, color;
    private Blocks blocks;
    public BlockScreen(Context context) {
        this(context, null);
    }

    public BlockScreen(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BlockScreen(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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
        e = (h-2*gap)/4;
        while (mIsDrawing){
            drawBlocks();
        }
    }

    private void drawBlocks() {
        try {
            //获得canvas对象
            mCanvas = mSurfaceHolder.lockCanvas();
            //绘制背景
            mCanvas.drawColor(Color.WHITE);
            drawCombination(blocks,color);
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
        mCanvas.drawRoundRect(gap+x*e,gap+y*e,gap+(x+1)*e,gap+(y+1)*e,15,15,paint);
    }

    private void drawCombination(Blocks blocks,int color){
        Paint paint = new Paint();
        paint.setColor(color);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        for (int i=0;i<blocks.getBlocks().length;i++){
            for (int j=0;j<blocks.getBlocks()[i].length;j++){
                if (blocks.getBlocks()[i][j] == 1) {
                    drawBlock(i, j, color);
                }
            }
        }
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setBlocks(Blocks blocks) {
        this.blocks = blocks;
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
