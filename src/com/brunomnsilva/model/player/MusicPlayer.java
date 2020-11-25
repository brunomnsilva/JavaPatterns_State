package com.brunomnsilva.model.player;

import com.brunomnsilva.model.Playlist;
import com.brunomnsilva.model.Song;

import javax.media.*;
import java.io.IOException;

public class MusicPlayer {

    private Player javaMediaPlayer;
    private Playlist playlist;

    private MusicPlayerState state;

    public MusicPlayer(String folderPath) {
        playlist = new Playlist();
        try {
            playlist.readFromFolder(folderPath);
            state = new StoppedState(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
    Public interface of Music Player. Delegate commands to current state
     */

    public void playPause() {
        state.playPause();
    }

    public void stop() {
        state.stop();
    }

    public void next() {
        state.next();
    }

    public void prev() {
        state.prev();
    }

    public String status() {
        return state.status();
    }

    public boolean isPlaying() {
        return state instanceof PlayingState;
    }

    /*
    Protected methods of player. Exposed to in-package classes
     */

    /**
     * Change state method.
     * @param s state to transition to
     */
    protected void changeState(MusicPlayerState s) {
        this.state = s;
    }

    protected Playlist getPlaylist() {
        return playlist;
    }

    protected void initPlayer(Song song) {
        try {
            javaMediaPlayer = Manager.createRealizedPlayer(playlist.getCurrent().getUnderlyingFile().toURI().toURL());
        } catch (IOException | CannotRealizeException | NoPlayerException e) {
            e.printStackTrace();
        }
    }

    protected void disposePlayer() {
        javaMediaPlayer.stop();
        javaMediaPlayer.close();
    }

    protected void startPlayback() {
        javaMediaPlayer.start();
    }

    protected void stopPlayback() {
        javaMediaPlayer.stop();
    }

    protected void skip10seconds() {
        Time mediaTime = javaMediaPlayer.getMediaTime();
        Time skipTime = new Time(mediaTime.getSeconds() + 10);
        javaMediaPlayer.setMediaTime(skipTime);
    }

    protected void rewind10seconds() {
        Time mediaTime = javaMediaPlayer.getMediaTime();
        Time skipTime = new Time(mediaTime.getSeconds() - 10);
        javaMediaPlayer.setMediaTime(skipTime);
    }

    protected Time getMediaTime() {
        return javaMediaPlayer.getMediaTime();
    }

    protected Time getDuration() {
        return javaMediaPlayer.getDuration();
    }

    /* Internal utilities */
    public static String timeToHuman(Time t) {
        int totalSeconds = (int)t.getSeconds();
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        return String.format("%d:%02d", minutes, seconds);
    }

}
