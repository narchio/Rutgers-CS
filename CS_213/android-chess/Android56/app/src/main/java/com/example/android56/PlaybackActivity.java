package com.example.android56;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Comparator;

import Chess.ChessGame;
import Chess.Serialize;

public class PlaybackActivity extends AppCompatActivity {

    private ListView listView;
    private TextView sortByGameTitle, sortByDate;

    public void sortByGameTitle() {
        MainActivity.getSavedGames().sort(Comparator.comparing(ChessGame::getTitle, String.CASE_INSENSITIVE_ORDER));
        listView.setAdapter(new ArrayAdapter<ChessGame>(this, R.layout.game, MainActivity.getSavedGames()));
    }

    public void sortByDate() {
        MainActivity.getSavedGames().sort(Comparator.comparing(ChessGame::getDate).reversed());
        listView.setAdapter(new ArrayAdapter<ChessGame>(this, R.layout.game, MainActivity.getSavedGames()));
    }

    @Override
    public void onPause() {
        super.onPause();
        Serialize.write(this);
    }
    @Override
    public void onStop() {
        super.onStop();
        Serialize.write(this);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playback);
        listView = (ListView) findViewById(R.id.gamesList);
        sortByGameTitle = (TextView) findViewById(R.id.sortByGameTitle);
        sortByDate = (TextView) findViewById(R.id.sortByDate);

        listView.setAdapter(new ArrayAdapter<ChessGame>(this, R.layout.game, MainActivity.getSavedGames()));

        sortByGameTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sortByGameTitle();
            }
        });

        sortByDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sortByDate();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(view.getContext(), PlaythroughActivity.class);
                intent.putExtra("SelectedGame", (ChessGame) listView.getAdapter().getItem(position));
                startActivity(intent);
            }
        });

    }
}
