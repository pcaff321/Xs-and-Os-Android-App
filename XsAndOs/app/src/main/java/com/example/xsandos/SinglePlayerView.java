package com.example.xsandos;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import android.view.View;
import android.widget.TextView;
import android.widget.Button;
import android.util.Log;
import java.util.Arrays;



public class SinglePlayerView extends AppCompatActivity implements View.OnClickListener {

    private TextView playerOneTitle, playerTwoTitle;
    private Button [][] buttons = new Button[3][3];
    private Button newGame;
    private Button modeBtn;

    int [][] gameState = new int [3][3];

    int mode = 0;

    boolean [][] modes = new boolean [3][2];
    String [] modeStrings = new String[3];

    private int move = 9;

    boolean activePlayer = true;
    boolean playerIsO = true;
    boolean computer = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_player_view);

        //player V player
        modeStrings[0] = "> PLAYER VS PLAYER <";
        modes[0][0] = true;
        modes[0][1] = false;

        // computer X vs player
        modeStrings[1] = "> COMPUTER(X) VS PLAYER(O) <";
        modes[1][0] = true;
        modes[1][1] = true;

        // player vs computer O
        modeStrings[2] = "> PLAYER(X) VS COMPUTER(O) <";
        modes[2][0] = false;
        modes[2][1] = true;


        playerOneTitle = (TextView) findViewById(R.id.playerOneTitle);
        playerTwoTitle = (TextView) findViewById(R.id.playerTwoTitle);

        newGame = (Button) findViewById(R.id.newGame);
        modeBtn = (Button) findViewById(R.id.modeButton);

        newGame.setText("START GAME");

        newGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetGame();
            }
        });

        modeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mode = (mode + 1) % (modes.length);
                modeBtn.setText(modeStrings[mode]);
            }
        });

        playerOneTitle.setText("PLAYER ONE");
        playerTwoTitle.setText("PLAYER TWO");

    }

    private void resetGame() {
        playerOneTitle.setTextColor(Color.parseColor("black"));
        playerTwoTitle.setTextColor(Color.parseColor("black"));
        activePlayer = true;
        playerIsO = modes[mode][0];
        computer = modes[mode][1];
        newGame.setText("NEW GAME");
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
                        playMove(view, posI, posJ);
                        if (computer) {
                            computerMove(9);
                        }
                    }
                });
            }
        }
        if (playerIsO && computer) {
            computerMove(9);
        }
    }

    public void playMove(View view, int posI, int posJ){
        if ((move == 9) || (gameState[posI][posJ] != 2)){
            return;
        }
        gameState[posI][posJ] = (activePlayer) ? 1 : 0;
        String playerXO = gameState[posI][posJ] == 0 ? "O" : "X";
        ((Button)view).setText(playerXO);
        move++;
        if (checkWin(gameState, posI, posJ) == 1) {
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

    @Override
    public void onClick(View view) {
        ((Button)view).setText("");
    }

    public int across(int i, int j, int offset, int[][] state){
        return state[i][((j + offset) % (state[0].length))];
    }

    public int down(int i, int j, int offset, int[][] state){
        return state[((i + offset) % (state.length))][j];
    }

    public int diagonal_right(int i, int j, int offset, int[][] state){
        return state[(i + offset) % (state.length)][((j + offset) % (state[0].length))];
    }

    public int diagonal_left(int i, int j, int offset, int[][] state){
        int newj = (j - offset);
        if (newj < 0){
            newj = state[0].length - 1;
        }
        return state[(i + offset) % (state.length)][newj];
    }

    public int checkWin(int[][] state, int i, int j){
        int x = state[i][j];
        if (((diagonal_left(2,0,0, state) == x) && (diagonal_left(1,1,0, state) == x) && (diagonal_left(0,2,0, state) == x))
                || ((diagonal_right(0,0,0, state) == x) && (diagonal_right(1,1,0, state) == x) && (diagonal_left(2,2,0, state) == x))
                || ((down(i, j, 1, state) == x) && (down(i, j, 2, state) == x))
                || ((across(i, j, 1, state) == x) && (across(i, j, 2, state) == x))){
            return 1;
        }
        return 0;
    }

    public int minimax(int depth, int[][] state, boolean isMax, int posI, int posJ){
        int bestScore;
        int theMove;
        boolean draw = true;
        if (isMax) {
            theMove = (playerIsO) ? 1 : 0;
            if (checkWin(state, posI, posJ) == 1){
                return (-10 + depth);
            }
            bestScore = -1000;
            for (int i = 0; i < state.length; i++) {
                for (int j = 0; j < state[0].length; j++) {
                    if (state[i][j] == 2) {
                        draw = false;
                        state[i][j] = theMove;
                        int score = minimax(depth + 1, state, false, i, j);
                        state[i][j] = 2;
                        bestScore = Math.max(score, bestScore);
                    }
                }
            }
        } else{
            if (checkWin(state, posI, posJ) == 1){
                return (20 - depth);
            }
            theMove = (playerIsO) ? 0 : 1;
            bestScore = 1000;
            for (int i = 0; i < state.length; i++) {
                for (int j = 0; j < state[0].length; j++) {
                    if (state[i][j] == 2) {
                        state[i][j] = theMove;
                        draw = false;
                        int score = minimax(depth + 1, state, true, i, j);
                        state[i][j] = 2;
                        bestScore = Math.min(score, bestScore);
                    }
                }
            }
        }
        if (draw){
            return 0;
        }
        return bestScore;
    }

    public void computerMove(int depth){
        int [][] state = gameState;
        int [] bestMove = {0, 0};
        int bestScore = -1000;
        for (int i = 0; i < state.length; i++) {
            for (int j = 0; j < state[0].length; j++) {
                if (state[i][j] == 2) {
                    state[i][j] = (playerIsO) ? 1 : 0;
                    int score = minimax(0, state, false, i, j);
                    state[i][j] = 2;
                    if (score > bestScore){
                        bestScore = score;
                        bestMove = new int[] {i, j};
                    }
                }
            }
        }
        int posI = bestMove[0];
        int posJ = bestMove[1];
        playMove(buttons[posI][posJ], posI, posJ);
    }
}
