package com.pao.laboratory05.playlist;

import java.util.Arrays;

public class Playlist {
    private String name;
    private Song[] songs;

    public Playlist(String name) {
        this.name = name;
        this.songs = new Song[0]; // Inițializare cu array gol
    }

    public String getName() {
        return name;
    }

    public void addSong(Song song) {
        // Resize array (creăm unul mai mare cu 1)
        Song[] tmp = new Song[songs.length + 1];
        System.arraycopy(songs, 0, tmp, 0, songs.length);
        tmp[tmp.length - 1] = song;
        songs = tmp;
    }

    public void printSortedByTitle() {
        if (songs.length == 0) return;
        Song[] copy = songs.clone();
        Arrays.sort(copy); // Folosește compareTo din record-ul Song
        for (Song s : copy) {
            System.out.println(s);
        }
    }

    public void printSortedByDuration() {
        if (songs.length == 0) return;
        Song[] copy = songs.clone();
        Arrays.sort(copy, new SongDurationComparator()); // Folosește comparatorul extern
        for (Song s : copy) {
            System.out.println(s);
        }
    }

    public int getTotalDuration() {
        int total = 0;
        for (Song s : songs) {
            total += s.durationSeconds();
        }
        return total;
    }
}