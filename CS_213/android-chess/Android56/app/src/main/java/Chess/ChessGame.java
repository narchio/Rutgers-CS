package Chess;

import com.example.android56.PlayActivity;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ChessGame implements Serializable {
    private static final long serialVersionUID = 1L;
    private String title;
    private Date date;
    private String winner;
    private ArrayList<String> moves;

    public ChessGame(String title, String winner) {
        this.title = title;
        this.date = Calendar.getInstance().getTime();
        this.moves = new ArrayList<String>();
        this.winner = winner;
        for(String string: PlayActivity.getExecutedMoves()){
            this.moves.add(string);
        }
    }

    public String getWinner() {return this.winner;}
    public String getTitle() {return this.title;}
    public Date getDate() {return this.date;}
    public ArrayList<String> getMoves() {return this.moves;}
    public String toString() {
        DateFormat dateFormat = DateFormat.getDateInstance();
        return this.title + '\n' + dateFormat.format(this.date);
    }

}
