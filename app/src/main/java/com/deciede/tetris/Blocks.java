package com.deciede.tetris;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Blocks {

    private int[][] blocks;
    protected int x,y;

    protected static int[][] FOUR_O;
    protected static int[][] FOUR_L;
    protected static int[][] FOUR_I;
    protected static int[][] FOUR_T;
    protected static int[][] FOUR_Z;
    private static final List<int[][]> BLOCKS = new ArrayList<>();

    static {
        FOUR_O = new int[][]{{1,1},{1,1}};
        FOUR_L = new int[][]{{1,1,1},{0,0,1}};
        FOUR_I = new int[][]{{1,1,1,1}};
        FOUR_T = new int[][]{{1,0},{1,1},{1,0}};
        FOUR_Z = new int[][]{{0,1},{1,1},{1,0}};
        BLOCKS.add(FOUR_O);
        BLOCKS.add(FOUR_L);
        BLOCKS.add(FOUR_I);
        BLOCKS.add(FOUR_T);
        BLOCKS.add(FOUR_Z);
    }

    private Blocks(int[][] blocks){
        this.blocks = blocks;
    }

    public Blocks(int[][] blocks,int x,int y){
        this.blocks = blocks;
        this.x = x;
        this.y = y;
    }

    public static Blocks getRandom(){
        Random random = new Random();
        int[][] b = BLOCKS.get(random.nextInt(BLOCKS.size()));
        if (Arrays.deepEquals(b, FOUR_L) || Arrays.deepEquals(b, FOUR_Z)){
            mirror(b);
        }
        Blocks blocks = new Blocks(b);
        int t = random.nextInt(4);
        for (int i = 0;i<t;i++){
            blocks.rotate();
        }
        blocks.fixFirstPos();
        return blocks;
    }

    public void moveRight(){
        x += 1;
    }

    public void moveLeft(){
        x -= 1;
    }

    public void rotate(){
        int[][] after = transpose(blocks);
        this.blocks = mirror(after);
    }

    public static int[][] rotate(int[][] blocks){
        int[][] after = transpose(blocks);
        return mirror(after);
    }

    private static int[][] mirror(int[][] matrix){
        boolean even = matrix[0].length%2 == 0;
        int mid = matrix[0].length/2;
        for (int i = 0; i < matrix.length;i++){
            if (even){
                for (int j = mid;j < matrix[i].length;j++){
                    int t = matrix[i][j];
                    matrix[i][j] = matrix[i][j-(j-mid)-1];
                    matrix[i][j-(j-mid)-1] = t;
                }
            }else {
                for (int j = mid+1;j < matrix[i].length;j++){
                    int t = matrix[i][j];
                    matrix[i][j] = matrix[i][mid-(j-mid)];
                    matrix[i][mid-(j-mid)] = t;
                }
            }
        }
        return matrix;
    }


    public static int[][] transpose(int[][] matrix){
        int[][] after  = new int[matrix[0].length][matrix.length];
        for (int i = 0; i < matrix.length;i++){
            for (int j = 0;j < matrix[i].length;j++){
                after[j][i] = matrix[i][j];
            }
        }
        return after;
    }

    private void fixFirstPos(){
        x = 6-blocks.length/2;
        y = 4-blocks[0].length;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int[][] getBlocks() {
        return blocks;
    }
}
