/*
Authors: Nicolas Carchio and Adam Romano
(Each contributed to this file)
 */
package View;

public class Song {
    // fields
    private String name;
    private String artist;
    private String album;
    private String year;

    // constructor
    public Song(String name, String artist, String album, String year) {
        this.name = name;
        this.artist = artist;
        this.album = album;
        this.year = year;
    }
    /*
    * These methods will update the current field that we need in the class
    * (Will make our lives easier and the code more modular)
    * */
    // update the name of the current song
    public void updateName(String name) {
        this.name = name;
    }
    // update the artist of the current song
    public void updateArtist(String artist) {
        this.artist = artist;
    }
    // update the album of the current song
    public void updateAlbum(String album) {
        this.album = album;
    }
    // update the year of the current song
    public void updateYear(String year) {
        this.year = year;
    }

    /*
     * These methods will return the fields
     * */

    //fetch name of the song
    public String getName() { return this.name; }
    //fetch the artist of the song
    public String getArtist() { return this.artist; }
    //fetch the album of the song
    public String getAlbum() { return this.album; }
    //fetch the year of the song
    public String getYear() { return this.year; }

    // to string for testing purposes
//    @Override
//    public String toString() {
//        System.out.println("Song");
//        System.out.println("name: " + this.name);
//        System.out.println("artist: " + this.artist);
//        System.out.println("album: " + this.album);
//        System.out.println("year: " + this.year);
//        //return "name: " + this.name + " artist: " + this.artist + " album " + this.album + " year " + this.year;
//        return "";
//    }
    @Override
    public String toString() {
        return this.name + ", " + this.artist;
    }
}
