package com.deciede.tetris;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class FallBlocks {

    private Blocks blocks, next_b;
    private int color, next_c;
    private final Timer timer;
    private final BlockPanel panel;
    private BlockScreen blockScreen;
    private boolean fall = false;
    private int count = 0,speed = 10;

    public static List<Integer> COLORS = new ArrayList<>();
    static {
        COLORS.add(Color.GREEN);
        COLORS.add(Color.RED);
        COLORS.add(Color.BLUE);
        COLORS.add(Color.YELLOW);
    }

    private FallBlocks(BlockPanel panel){
        this.blocks = Blocks.getRandom();
        this.next_b = Blocks.getRandom();
        this.color = COLORS.get(new Random().nextInt(COLORS.size()));
        this.next_c = COLORS.get(new Random().nextInt(COLORS.size()));
        this.panel = panel;
        this.timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (fall){
                    if (count == speed){
                        if(panel.isTouched(blocks)){
                            stopFall();
                            panel.write(blocks,color);
                            if (panel.isGameOver()){
                                stopFall();
                            }else {
                                changeBlocks();
                                List<Integer> lines = panel.hasFullX();
                                if (!lines.isEmpty()){
                                    panel.clearLine(lines);
                                }
                            }
                        }else blocks.y += 1;
                        count = 0;
                    }else {
                        count += 1;
                    }
                }
            }
        },1000,100);
    }

    public static FallBlocks getInstance(BlockPanel panel){
        return new FallBlocks(panel);
    }

    public void startFall(){
        fall = true;
    }

    public void stopFall(){
        fall = false;
    }

    public void changeBlocks(){
        blocks = next_b;
        next_b = Blocks.getRandom();
        color = next_c;
        next_c = COLORS.get(new Random().nextInt(COLORS.size()));
        blockScreen.setBlocks(next_b);
        blockScreen.setColor(next_c);
    }

    public void setSpeed(int speed) {
        this.speed = speed;
        count = 0;
    }

    public Blocks getBlocks() {
        return blocks;
    }

    public int getColor() {
        return color;
    }

    public boolean isFall() {
        return fall;
    }

    public void setBlockScreen(BlockScreen blockScreen) {
        blockScreen.setBlocks(next_b);
        blockScreen.setColor(next_c);
        this.blockScreen = blockScreen;
    }

    public void setOutline(boolean outline){
        blockScreen.setOutline(outline);
    }

    public void reset(){
        blocks = Blocks.getRandom();
        next_b = Blocks.getRandom();
        color = COLORS.get(new Random().nextInt(COLORS.size()));
        next_c = COLORS.get(new Random().nextInt(COLORS.size()));
        blockScreen.setBlocks(next_b);
        blockScreen.setColor(next_c);
    }

}
