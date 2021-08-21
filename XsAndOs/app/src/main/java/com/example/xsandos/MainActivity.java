package com.example.xsandos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import android.view.View;
import android.widget.TextView;
import android.widget.Button;
import android.util.Log;
import android.content.Intent;



public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView playerOneTitle, playerTwoTitle;
    private Button [][] buttons = new Button[3][3];
    private Button single;
    private Button multi;

    int [][] gameState = new int [3][3];

    private int move = 0;

    boolean activePlayer = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        single = (Button) findViewById(R.id.singleBtn);
        multi = (Button) findViewById(R.id.multiBtn);

        single.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SinglePlayerView.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View view) {
        // todo
    }
}
