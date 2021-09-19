package com.example.gomoku;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import android.view.View;
import android.widget.TextView;
import android.widget.Button;
import android.util.Log;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView playerOneTitle, playerTwoTitle;
    private Button [][] buttons = new Button[3][3];
    private Button newGame;

    int [][] gameState = new int [3][3];

    private int move = 0;

    boolean activePlayer = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playerOneTitle = (TextView) findViewById(R.id.playerOneTitle);
        playerTwoTitle = (TextView) findViewById(R.id.playerTwoTitle);

        newGame = (Button) findViewById(R.id.newGame);

        newGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetGame();
            }
        });

        playerOneTitle.setText("PLAYER ONE");
        playerTwoTitle.setText("PLAYER TWO");

        resetGame();

    }

    private void resetGame() {
        playerOneTitle.setTextColor(Color.parseColor("black"));
        playerTwoTitle.setTextColor(Color.parseColor("black"));
        activePlayer = true;
        move = 0;
        for (int i = 0; i < buttons.length; i++){
            for (int j = 0; j < buttons[0].length; j++){
                gameState[i][j] = 2;
                final int posI = i;
                final int posJ = j;
                String buttonString = "button_" + i + j;
                int buttonID = getResources().getIdentifier(buttonString, "id", getPackageName());
                buttons[i][j] = (Button) findViewById(buttonID);
                buttons[i][j].setText("");
                buttons[i][j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if ((move == 9) || (gameState[posI][posJ] != 2)){
                            return;
                        }
                        gameState[posI][posJ] = (activePlayer) ? 1 : 0;
                        String playerXO = gameState[posI][posJ] == 0 ? "O" : "X";
                        ((Button)view).setText(playerXO);
                        move++;
                        if (checkWin(posI, posJ) == 1) {
                            move = 9;
                            if (activePlayer == true) {
                                playerOneTitle.setTextColor(Color.parseColor("#FFD700"));
                            } else {
                                playerTwoTitle.setTextColor(Color.parseColor("#FFD700"));
                            }
                        }else{
                            if (move == 9){
                                playerOneTitle.setTextColor(Color.parseColor("red"));
                                playerTwoTitle.setTextColor(Color.parseColor("red"));
                            }
                        }
                        activePlayer = !activePlayer;
                    }
                });
            }
        }
    }

    @Override
    public void onClick(View view) {
        ((Button)view).setText("");
    }

    public int across(int i, int j, int offset){
        return gameState[i][((j + offset) % (gameState[0].length))];
    }

    public int down(int i, int j, int offset){
        return gameState[((i + offset) % (gameState.length))][j];
    }

    public int diagonal_right(int i, int j, int offset){
        return gameState[(i + offset) % (gameState.length)][((j + offset) % (gameState[0].length))];
    }

    public int diagonal_left(int i, int j, int offset){
        int newj = (j - offset);
        if (newj < 0){
            newj = gameState[0].length - 1;
        }
        return gameState[(i + offset) % (gameState.length)][newj];
    }

    public int checkWin(int i, int j){
        int x = gameState[i][j];
        if (((diagonal_left(2,0,0) == x) && (diagonal_left(1,1,0) == x) && (diagonal_left(0,2,0) == x))
                || ((diagonal_right(0,0,0) == x) && (diagonal_right(1,1,0) == x) && (diagonal_left(2,2,0) == x))
                || ((down(i, j, 1) == x) && (down(i, j, 2) == x))
                || ((across(i, j, 1) == x) && (across(i, j, 2) == x))){
            return 1;
        }
        return 0;
    }
}

