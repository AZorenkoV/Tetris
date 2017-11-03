package ua.raccoon;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

public class Figure {
    private ArrayList<Block> figure = new ArrayList<>();
    private Random random = new Random();
    private int[][] shape = new int[4][4];
    private int type, size, color, block_size, arc_radius;
    private int x, y;

    Figure(int[][][] shapes, int block_size, int arc_radius){
        this.type = random.nextInt(shapes.length);
        this.size = shapes[type][4][0];
        this.color = shapes[type][4][1];
        this.block_size = block_size;
        this.arc_radius = arc_radius;
        if (size == 4) y = -1;
        for (int i = 0; i < size; i++){
            System.arraycopy(shapes[type][i],0, shape[i],0, shapes[type][i].length);
        }
        createFromShape();
    }

    private void createFromShape(){
        for (int x = 0; x < size; x++)
            for (int y = 0; y < size; y++) {
                if(shape[y][x] == 1) figure.add(new Block(x + this.x, y + this.y, block_size, arc_radius));
            }
    }

    public void paint(Graphics g){
        for (Block block : figure) {
            block.paint(g, color);
        }
    }

    public boolean isTouchGround(int[][] mine){
        for (Block block : figure) {
            if (mine[block.getY()+1][block.getX()] > 0) return true;
        }
        return false;
    }

    public void leaveOnTheGround(int[][] mine){
        for(Block block : figure) {
            mine[block.getY()][block.getX()] = color;
        }
    }

    public boolean isTouchWall(int[][] mine, int keyCode, int field_width){
        for (Block block :
                figure) {
            if(keyCode == KeyEvent.VK_LEFT && (block.getX() == 0 || mine[block.getY()][block.getX() - 1] > 0)) return true;
            if(keyCode == KeyEvent.VK_RIGHT && (block.getX() == field_width -1 || mine[block.getY()][block.getX()+1] > 0)) return true;
        }
        return false;
    }

    public void drop(int[][] mine) {
        while (!isTouchGround(mine)) stepDown();
    }

    public boolean isWrongPosition(int[][] mine, int field_width){
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                if (shape[y][x] == 1) {
                    if(y + this.y < 0) return true;
                    if(x + this.x < 0 || x + this.x > field_width - 1) return true;
                    if(mine[y + this.y][x + this.x] > 0) return true;
                }
            }
        }
        return false;
    }

    public void rotate(int[][] mine, int field_width) {
        for (int i = 0; i < size/2; i++) {
            for (int j = 0; j < size - 1 - i; j++) {
                int tmp = shape[size - 1 - j][i];
                shape[size - 1 - j][i] = shape[size - 1 - i][size - 1 - j];
                shape[size - 1 - i][j] = shape[j][size - 1 - i];
                shape[j][size - 1 - i] = shape[i][j];
                shape[i][j] = tmp;
            }
        }
        if(!isWrongPosition(mine, field_width)){
            figure.clear();
            createFromShape();
        }
    }

    public void move(int[][] mine, int keyCode, int field_width) {
        if(!isTouchWall(mine, keyCode, field_width)){
            int dx = keyCode - 38;
            for (Block block :
                    figure) {
                block.setX(block.getX() + dx);
            }
            x += dx;
        }
    }

    public boolean isCrossGround(int[][] mine) {
        for (Block block : figure) {
            if (mine[block.getY()][block.getX()] > 0) return true;
        }
        return false;
    }

    public void stepDown() {
        for (Block block : figure) {
            block.setY(block.getY() + 1);
            y++;
        }
    }
}