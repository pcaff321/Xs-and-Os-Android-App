package com.example.xsandos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.HashMap;
import java.util.Map;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class JoinOrCreate extends AppCompatActivity {

    String gameID = "XXXXXX";

    private EditText codeBox;

    private Button createBtn;
    private Button joinBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_or_create);

        codeBox = (EditText) findViewById(R.id.codeBox);

        createBtn = (Button) findViewById(R.id.createBtn);
        joinBtn = (Button) findViewById(R.id.joinBtn);

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createGame();
            }
        });

        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                joinGame();
            }
        });
    }

    private void createGame(){
        String code = codeBox.getText().toString();
        if (code == "ENTER CODE HERE"){
            return;
        }
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference().child("games");
        Map<String, String> games = new HashMap<>();
        games.put(code, "game");

        ref.setValue(games);

        joinGame();
    }

    private void joinGame() {
        String code = codeBox.getText().toString();
        if (code == "ENTER CODE HERE"){
            return;
        }
        Intent intent = new Intent(JoinOrCreate.this, MultiplayerView.class);
        intent.putExtra("gameID", code);
        startActivity(intent);
    }
}