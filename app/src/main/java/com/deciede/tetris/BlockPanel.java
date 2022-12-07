package com.deciede.tetris;

import android.graphics.Color;
import android.os.Handler;
import android.os.Message;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BlockPanel {

    protected int[][] colors;
    protected int[][] panel;
    private int score = 0;
    private Handler activity;
    private int status = 1;

    public static int DOWN = 0;
    public static int RIGHT = 1;
    public static int LEFT = 2;

    public static int END = 0;
    public static int RUNNING = 1;
    public static int PAUSE = 2;

    public BlockPanel(){
        colors = getEmptyColors();
        panel = getEmptyMatrix();
    }

    public void write(Blocks blocks,int color){
        for (int i = 0;i < blocks.getBlocks().length;i++){
            for (int j = 0;j < blocks.getBlocks()[i].length;j++){
                if (blocks.getBlocks()[i][j] == 1){
                    panel[blocks.x+i][blocks.y+j] = blocks.getBlocks()[i][j];
                    colors[blocks.x+i][blocks.y+j] = color;
                }
            }
        }
    }

    public boolean isGameOver(){
        boolean res = false;
        for (int[] line:panel){
            if (line[4] == 1){
                res = true;
                break;
            }
        }

        if (res){
            status = END;
            Message msg = Message.obtain();
            msg.what = 1;
            activity.sendMessage(msg);
        }
        return res;
    }

    public boolean isTouched(Blocks blocks){
        if (touchBottom(blocks)){
            return true;
        }else {
            return touchBlock(blocks,DOWN);
        }
    }

    public boolean touchBottom(Blocks blocks){
        return blocks.y + blocks.getBlocks()[0].length > 27;
    }

    public boolean touchLeft(Blocks blocks){
        return blocks.x <= 0;
    }

    public boolean touchRight(Blocks blocks){
        return blocks.x+ blocks.getBlocks().length >= 12;
    }

    public boolean crossLeft(Blocks blocks){
        return blocks.x < 0;
    }

    public boolean crossRight(Blocks blocks){
        return blocks.x+ blocks.getBlocks().length > 12;
    }

    public boolean touchBlock(Blocks blocks,int direction){
        boolean res = false;
        for (int i = 0;i < blocks.getBlocks().length;i++){
            for (int j = 0;j < blocks.getBlocks()[i].length;j++){
                if (blocks.getBlocks()[i][j] == 1){
                    switch (direction){
                        case 0:
                            if (panel[blocks.x+i][blocks.y+j+1] == 1){
                                res = true;
                                break;
                            }
                            break;
                        case 1:
                            if (panel[blocks.x+i+1][blocks.y+j] == 1){
                                res = true;
                                break;
                            }
                            break;
                        case 2:
                            if (panel[blocks.x+i-1][blocks.y+j] == 1){
                                res = true;
                                break;
                            }
                            break;
                    }
                }
            }
        }
        return res;
    }

    public boolean inBlock(Blocks blocks){
        boolean res = false;
        int[][] b = blocks.getBlocks();
        for (int i = 0;i < b.length;i++){
            for (int j = 0;j < b[i].length;j++){
                if (b[i][j] == 1){
                    if (panel[i+blocks.x][j+blocks.y] == 1){
                        res = true;
                        break;
                    }
                }
            }
        }
        return res;
    }

    public List<Integer> hasFullX(){
        List<Integer> res = new ArrayList<>();
        int[] full = {1,1,1,1,1,1,1,1,1,1,1,1};
        int[][] after = Blocks.transpose(panel);
        for (int i = after.length-1;i > 3;i--){
            if (Arrays.equals(after[i],full)){
                res.add(i);
            }
        }
        return res;
    }

    public void clearLine(List<Integer> lines){
        int[][] p_after = Blocks.transpose(panel);
        int[][] c_after = Blocks.transpose(colors);
        for (Integer line:lines){
            line += lines.indexOf(line);
            for (;line > 2;line--){
                p_after[line] = p_after[line-1];
                c_after[line] = c_after[line-1];
            }
            score += 12;
        }
        panel = Blocks.transpose(p_after);
        colors = Blocks.transpose(c_after);
        Message msg = Message.obtain();
        msg.what = 0;
        msg.obj = String.valueOf(score);
        activity.sendMessage(msg);
    }

    private int[][] getEmptyMatrix(){
        int[][] matrix = new int[12][28];
        for (int i = 0;i < 12;i++){
            for (int j = 0;j < 28;j++){
                matrix[i][j] = 0;
            }
        }
        return matrix;
    }

    private int[][] getEmptyColors(){
        int[][] colors = new int[12][28];
        for (int i = 0;i < 12;i++){
            for (int j = 0;j < 28;j++){
                colors[i][j] = Color.WHITE;
            }
        }
        return colors;
    }

    public void reset(){
        colors = getEmptyColors();
        panel = getEmptyMatrix();
        status = RUNNING;
    }

    public void setActivity(Handler activity) {
        this.activity = activity;
    }

    public int getScore() {
        return score;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
