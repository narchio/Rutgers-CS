package Chess;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.android56.MainActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class Serialize implements Serializable {

    public static void write(Context context) {
        try {
            File file = new File(context.getFilesDir() + "/games.ser");
            if(file.exists() == false) {
                file.createNewFile();
            }
            FileOutputStream fos = context.openFileOutput(file.getName(), Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(MainActivity.getSavedGames());
            oos.close();
            fos.close();
            //Log.println(Log.ASSERT, "MESSAGE", "LIST SERIALIZED, SIZE="+MainActivity.getSavedGames().size());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<ChessGame> read(Context context) {
        ArrayList<ChessGame> games = new ArrayList<ChessGame>();
        try {
            File file = new File(context.getFilesDir() + "/games.ser");
            if(file.exists() == false) {
                file.createNewFile();
            }
            FileInputStream fis = context.openFileInput(file.getName());
            ObjectInputStream ois = new ObjectInputStream(fis);
            games = (ArrayList<ChessGame>) ois.readObject();
            MainActivity.setSavedGames(games);
            ois.close();
            fis.close();
            //Log.println(Log.ASSERT, "MESSAGE", "LIST DESERIALIZED, SIZE="+MainActivity.getSavedGames().size()+", TITLE = "+MainActivity.getSavedGames().get(0).getTitle());
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return games;
    }
}
