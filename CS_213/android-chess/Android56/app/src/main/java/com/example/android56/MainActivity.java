package com.example.android56;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

import Chess.ChessGame;
import Chess.Serialize;

public class MainActivity extends AppCompatActivity {

    private Button playButton;
    private Button instructionsButton;
    private Button playbackButton;
    private static ArrayList<ChessGame> savedGames;

    public static ArrayList<ChessGame> getSavedGames() {return savedGames; }
    public static void setSavedGames(ArrayList<ChessGame> games) {
        for(ChessGame cg: games) {
            savedGames.add(cg);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(isFinishing()) {
            Serialize.write(this);
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        if(isFinishing()) {
            Serialize.write(this);
        }

    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        permissionsGranted();

        savedGames = new ArrayList<ChessGame>();
        savedGames = Serialize.read(this);

        playButton = (Button) findViewById(R.id.playButton);
        instructionsButton = (Button) findViewById(R.id.instructionsButton);
        playbackButton = (Button) findViewById(R.id.playbackButton);

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), PlayActivity.class);
                startActivity(intent);
            }
        });

        instructionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), InstructionsActivity.class);
                startActivity(intent);
            }
        });

        playbackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), PlaybackActivity.class);
                startActivity(intent);
            }
        });
    }

    public boolean permissionsGranted() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                return true;
            }
            else{
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else{
            return true;
        }
    }
}
