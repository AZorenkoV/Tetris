package ua.raccoon;

import java.awt.*;

public class Block {
    private int x, y;
    private int blockSize, arcRadius;


    public Block(int x, int y, int blockSize, int arcRadius){
        setX(x);
        setY(y);
        setBlockSize(blockSize);
        setArcRadius(arcRadius);
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public void setArcRadius(int arcRadius) {
        this.arcRadius = arcRadius;
    }

    public void setBlockSize(int blockSize) {
        this.blockSize = blockSize;
    }

    public int getArcRadius() {
        return arcRadius;
    }

    public int getBlockSize() {
        return blockSize;
    }

    public void paint(Graphics g, int color) {
        g.setColor(new Color(color));
        g.drawRoundRect(x*blockSize+1, y*blockSize+1, blockSize-2, blockSize-2, arcRadius, arcRadius);
    }
}
