package ua.raccoon;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;


public class GameTetris {

    /*CONSTANT BLOCK*/

    private final String TITLE_OF_PROGAM = "Tetris";
    private final int BLOCK_SIZE = 25;
    private final int ARC_RADIUS = 6;
    private final int FIELD_WIDTH = 10; // in block
    private final int FIELD_HEIGHT = 18; // in block
    private final int START_LOCATION = 200;
    private final int FIELD_DX = 7;
    private final int FIELD_DY = 16;
    private final int SHOW_DELAY = 200; // delay for animation
    private final int[] SCORES = {100, 300, 700, 1500};
    private final int[][][] SHAPES = {
            {{0,0,0,0}, {1,1,1,1}, {0,0,0,0}, {0,0,0,0}, {4, 0x00f0f0}}, //I
            {{0,0,0,0}, {0,1,1,0}, {0,1,1,0}, {0,0,0,0}, {4, 0xf0f000}}, //O
            {{1,0,0,0}, {1,1,1,0}, {0,0,0,0}, {0,0,0,0}, {3, 0x0000f0}}, //J
            {{0,0,1,0}, {1,1,1,0}, {0,0,0,0}, {0,0,0,0}, {3, 0xf0a000}}, //L
            {{0,1,1,0}, {1,1,0,0}, {0,0,0,0}, {0,0,0,0}, {3, 0x00f000}}, //S
            {{1,1,1,0}, {0,1,0,0}, {0,0,0,0}, {0,0,0,0}, {3, 0xa000f0}}, //T
            {{1,1,0,0}, {0,1,1,0}, {0,0,0,0}, {0,0,0,0}, {3, 0xf00000}}, //Z
    };
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

    private int gameScore = 0;
    private int [][] mine = new int[FIELD_HEIGHT + 1][FIELD_WIDTH];
    private JFrame frame;
    private Canvas canvasPanel = new Canvas();
    private Figure figure = new Figure(SHAPES, BLOCK_SIZE, ARC_RADIUS);
    private boolean gameOver = false;

    public static void main(String[] args) {
        new GameTetris().go();
    }

    private void go(){
        frame = new JFrame(TITLE_OF_PROGAM);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(FIELD_WIDTH * BLOCK_SIZE + FIELD_DX*2, FIELD_HEIGHT * BLOCK_SIZE + FIELD_DY*2);
        frame.setLocation(START_LOCATION, START_LOCATION);
        frame.setResizable(false);
        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent keyEvent) {
                System.out.println("key press!!!");
                if(!gameOver) {
                    if(keyEvent.getKeyCode() == KeyEvent.VK_DOWN) figure.drop(mine);
                    if(keyEvent.getKeyCode() == KeyEvent.VK_UP) figure.rotate(mine, FIELD_WIDTH);
                    if(keyEvent.getKeyCode() == KeyEvent.VK_LEFT || keyEvent.getKeyCode() == KeyEvent.VK_RIGHT)
                        figure.move(mine, keyEvent.getKeyCode(), FIELD_WIDTH);
                }
                canvasPanel.repaint();
            }
        });
        canvasPanel.setBackground(Color.BLACK);
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
            if(figure.isTouchGround(mine)){
                figure.leaveOnTheGround(mine);
                checkFilling();
                figure = new Figure(SHAPES, BLOCK_SIZE, ARC_RADIUS);
                gameOver = figure.isCrossGround(mine);
            } else {
                figure.stepDown();
            }
        }
    }

    private void checkFilling(){
        int row = FIELD_HEIGHT - 1;
        int countFillRows = 0;
        while (row > 0) {
            int filled = 1;
            for (int col = 0; col < FIELD_WIDTH; col++)
                filled *= Integer.signum(mine[row][col]);
            if(filled > 0) {
                countFillRows++;
                for (int i = row; i > 0; i--) {
                    System.arraycopy(mine[i-1], 0, mine[i], 0, FIELD_WIDTH);
                }
            } else {
                row--;
            }
        }
        if(countFillRows > 0) {
            gameScore += SCORES[countFillRows -1];
            frame.setTitle(TITLE_OF_PROGAM + " : " + gameScore);
        }
    }

    private class Canvas extends JTable {
        @Override
        public void paint(Graphics g){
            super.paint(g);
            for(int x = 0; x < FIELD_WIDTH; x++)
                for (int y = 0; y < FIELD_HEIGHT; y++)
                    if(mine[y][x] > 0) {
                        g.setColor(new Color(mine[y][x]));
                        g.fill3DRect(x*BLOCK_SIZE+1, y*BLOCK_SIZE+1, BLOCK_SIZE-1, BLOCK_SIZE-1, true);
                    }
            if(gameOver) {
                g.setColor(Color.white);
                for (int y = 0; y < GAME_OVER_MSG.length; y++)
                    for (int x = 0; x < GAME_OVER_MSG[y].length; x++)
                        if(GAME_OVER_MSG[y][x] == 1) g.fill3DRect(x*11 + 18, y*11+160, 10,10,true);
            } else figure.paint(g);
        }
    }
}