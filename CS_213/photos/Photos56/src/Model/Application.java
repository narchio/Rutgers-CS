package Model;

import javafx.scene.Scene;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The class Application will provide basic functionality and maintain the user list and current application variables
 * @author Nicolas Carchio
 * @author Adam Romano
 */
public class Application implements Serializable {
    /**
     * Serialization class version
     */
    private static final long serialVersionUID = 1L;
    /**
     * Current User
     */
    private static User currentUser;
    /**
     * Current scene
     */
    private static Scene currentScene;
    /**
     * List of users
     */
    private static List<User> users = new ArrayList<User>();
    /**
     * Get the list of users with access to the application
     */
    public static List<User> getUsers() {
        return users;
    }
    /**
     * Add a user to the list
     */
    public static void addUser(User user) {
        users.add(user);
    }
    /**
     * Remove a user from the list
     */
    public static void deleteUser(User user) {
        users.remove(user);
    }
    /**
     * Get Current User
     */
    public static User getCurrentUser() { return currentUser;}
    /**
     * Set Current User
     */
    public static void setCurrentUser(User user) { currentUser = user;}
    /**
     * Get the current scene
     */
    public static Scene getCurrentScene() {
        return currentScene;
    }
    /**
     * Set the current scene
     */
    public static void setCurrentScene(Scene scene) {
        currentScene = scene;
    }
    /**
     * Checks if a user is in the list
     */
    public static boolean inList(String username){
        for(User user: users){
            if(user.getUsername().equals(username)){
                return true;
            }
        }
        return false;
    }
    /**
     * Get the index of a user in the list
     */
    public static int getIndexInUserList(String username){
        for(int i = 0; i < users.size(); i++){
            if(users.get(i).getUsername().equals(username)){
                return i;
            }
        }
        return -1;
    }
    /**
     * Serialize users to users.dat
     */
    public static void writeUsers() {
        try {
            FileOutputStream fos = new FileOutputStream("Photos56/users/users.dat");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(users);
            oos.close();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Deserialize users from users.dat
     */
    public static void readUsers() {
        try {
            FileInputStream fis = new FileInputStream("Photos56/users/users.dat");
            ObjectInputStream ois = new ObjectInputStream(fis);
            users = (ArrayList<User>) ois.readObject();
            ois.close();
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
