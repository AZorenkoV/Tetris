package ua.raccoon;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;


public class GameTetris {
    private final String TITLE_OF_PROGAM = "Tetris";
    private final int BLOCK_SIZE = 25;
    private final int ARC_RADIUS = 6;
    private final int FIELD_WIDTH = 10; // in block
    private final int FIELD_HEIGHT = 18; //in block
    private final int START_LOCATION = 180;
    private final int FIELD_DX = 7;
    private final int FIELD_DY = 16;
    private final int LEFT = 37;
    private final int UP = 38;
    private final int RIGHT = 39;
    private final int DOWN = 40;
    private final int SHOW_DELAY = 350; // delay for animation
    private final int[][][] SHAPES = {
            {{0,0,0,0}, {1,1,1,1}, {0,0,0,0}, {0,0,0,0}, {4, 0x00f0f0}}, //I
            {{0,0,0,0}, {0,1,1,0}, {0,1,1,0}, {0,0,0,0}, {4, 0xf0f000}}, //O
            {{1,0,0,0}, {1,1,1,0}, {0,0,0,0}, {0,0,0,0}, {3, 0x0000f0}}, //J
            {{0,0,1,0}, {1,1,1,0}, {0,0,0,0}, {0,0,0,0}, {3, 0xf0a000}}, //L
            {{0,1,1,0}, {1,1,0,0}, {0,0,0,0}, {0,0,0,0}, {3, 0x00f000}}, //S
            {{1,1,1,0}, {0,1,0,0}, {0,0,0,0}, {0,0,0,0}, {3, 0xa000f0}}, //T
            {{1,1,0,0}, {0,1,1,0}, {0,0,0,0}, {0,0,0,0}, {3, 0xf00000}}, //Z
    };
    private final int [] SCORES = {100, 300, 700, 1500};
    private int gameScores = 0;
    private int [][] mine = new int[FIELD_HEIGHT + 1][FIELD_WIDTH];
    private JFrame frame;
    private Canvas canvasPanel = new Canvas();
    private Random random = new Random();
    private Figure figure = new Figure();
    private boolean gameOver = false;
    private final int[][] GAME_OVER_MSG = {
            {0,1,1,0,0,0,1,1,0,0,0,1,0,1,0,0,0,1,1,0},
            {1,0,0,0,0,1,0,0,1,0,1,0,1,0,1,0,1,0,0,1},
            {1,0,1,1,0,1,1,1,1,0,1,0,1,0,1,0,1,1,1,1},
            {1,0,0,1,0,1,0,0,1,0,1,0,1,0,1,0,1,0,0,0},
            {0,1,1,0,0,1,0,0,1,0,1,0,1,0,1,0,0,1,1,0},
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {0,1,1,0,0,1,0,0,1,0,0,1,1,0,0,1,1,1,0,0},
            {1,0,0,1,0,1,0,0,1,0,1,0,0,1,0,1,0,0,1,0},
            {1,0,0,1,0,1,0,1,0,0,1,1,1,1,0,1,1,1,0,0},
            {1,0,0,1,0,1,1,0,0,0,1,0,0,0,0,1,0,0,1,0},
            {0,1,1,0,0,1,0,0,0,0,0,1,1,0,0,1,0,0,1,0}
    };

    public static void main(String[] args) {
        new GameTetris().go();
    }

    private void go(){
        frame = new JFrame(TITLE_OF_PROGAM);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(FIELD_WIDTH * BLOCK_SIZE + FIELD_DX, FIELD_HEIGHT * BLOCK_SIZE + FIELD_DY);
        frame.setLocation(START_LOCATION, START_LOCATION);
        frame.setResizable(false);
        canvasPanel.setBackground(Color.BLACK);
        frame.addKeyListener(new KeyAdapter() {
                                 @Override
                                 public void keyPressed(KeyEvent keyEvent) {
                                     if(!gameOver) {
                                         if(keyEvent.getKeyCode() == DOWN) figure.drop();
                                         if(keyEvent.getKeyCode() == UP) figure.rotate();
                                         if(keyEvent.getKeyCode() == LEFT || keyEvent.getKeyCode() == RIGHT)
                                             figure.move(keyEvent.getKeyCode());
                                     }
                                     canvasPanel.repaint();
                                 }
                             });
        frame.getContentPane().add(BorderLayout.CENTER, canvasPanel);
        frame.setVisible(true);

        Arrays.fill(mine[FIELD_HEIGHT], 1);

        //main loop of game
        while(!gameOver){
            try{
                Thread.sleep(SHOW_DELAY);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            canvasPanel.repaint();
            if(figure.isTouchGround()){
                figure.leaveOnTheGround();
                checkFilling();
                figure = new Figure();
                gameOver = figure.isCrossGround();
            } else {
                figure.stepDown();
            }
        }
    }

    private void checkFilling(){

    }

    private class Figure {

        public boolean isTouchGround(){
            return false;
        }

        public void leaveOnTheGround(){

        }

        public void drop() {

        }

        public void rotate() {

        }

        public void move(int keyCode) {

        }

        public boolean isCrossGround() {
            return false;
        }

        public void stepDown() {

        }
    }

    private class Block {
        private int x, y;

        public Block(int x, int y){
            setX(x);
            setY(y);
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

        public void paint(Graphics g, int color) {
            g.setColor(new Color(color));
            g.drawRoundRect(x*BLOCK_SIZE+1, y*BLOCK_SIZE+1, BLOCK_SIZE-2, BLOCK_SIZE-2, ARC_RADIUS, ARC_RADIUS);
        }
    }

    private class Canvas extends JTable {
        @Override
        public void paint(Graphics g){
            super.paint(g);
        }
    }
}