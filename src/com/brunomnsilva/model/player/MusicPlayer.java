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

    /**
     * Action for play command.
     */
    public void play() {
        state.play();
    }

    /**
     * Action for stop command.
     */
    public void stop() {
        state.stop();
    }

    /**
     * Action for next command.
     */
    public void next() {
        state.next();
    }

    /**
     * Action for prev command.
     */
    public void prev() {
        state.prev();
    }

    /**
     * Returns a string with the player status.
     * @return the player status
     */
    public String status() {
        return state.status();
    }

    /**
     * Checks if the player is playing a song.
     * @return true if playing; false, otherwise.
     */
    public boolean isPlaying() {
        return state instanceof PlayingState;
    }

    /**
     * Change state method.
     * @param s state to transition to.
     */
    public void changeState(MusicPlayerState s) {
        this.state = s;
    }

    /**
     * Returns the underlying loaded playlist.
     * @return the playlist.
     */
    private Playlist getPlaylist() {
        return playlist;
    }

    /**
     * Returns a string with the current loaded song in the player.
     * @return song information.
     */
    public String getCurrentLoadedSong() {
        return playlist.getCurrent().toString();
    }

    /**
     * Loads into the player the current song from the playlist.
     */
    public void loadCurrentSong() {
        initPlayer(getPlaylist().getCurrent());
    }

    /**
     * Loads into the player the next song from the playlist.
     */
    public void loadNextSong() {
        initPlayer(getPlaylist().getNext());
    }

    /**
     * Loads into the player the previous song from the playlist.
     */
    public void loadPreviousSong() {
        initPlayer(getPlaylist().getPrevious());
    }

    /**
     * Starts playback of the current loaded song.
     */
    public void startPlayback() {
        javaMediaPlayer.start();
    }

    /**
     * Pauses playback. Keeps the song in the player.
     */
    public void pausePlayback() {
        javaMediaPlayer.stop();
    }

    /**
     * Stops playback and unloads the current song.
     */
    public void stopPlayback() {
        javaMediaPlayer.stop();
        disposePlayer();
    }

    /**
     * Skips 10 seconds of playback.
     */
    public void skip10seconds() {
        Time mediaTime = javaMediaPlayer.getMediaTime();
        Time skipTime = new Time(mediaTime.getSeconds() + 10);
        javaMediaPlayer.setMediaTime(skipTime);
    }

    /**
     * Rewinds 10 seconds of playback.
     */
    public void rewind10seconds() {
        Time mediaTime = javaMediaPlayer.getMediaTime();
        Time skipTime = new Time(mediaTime.getSeconds() - 10);
        javaMediaPlayer.setMediaTime(skipTime);
    }

    /**
     * Returns play time of the current song.
     * @return song play time
     */
    public Time getMediaTime() {
        return javaMediaPlayer.getMediaTime();
    }

    /**
     * Returns duration of the current song.
     * @return song duration
     */
    public Time getDuration() {
        return javaMediaPlayer.getDuration();
    }

    private void initPlayer(Song song) {
        try {
            javaMediaPlayer = Manager.createRealizedPlayer(playlist.getCurrent().getUnderlyingFile().toURI().toURL());
        } catch (IOException | CannotRealizeException | NoPlayerException e) {
            e.printStackTrace();
        }
    }

    private void disposePlayer() {
        javaMediaPlayer.stop();
        javaMediaPlayer.close();
        javaMediaPlayer = null;
    }

    /* Internal utilities */
    public static String timeToHuman(Time t) {
        int totalSeconds = (int)t.getSeconds();
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        return String.format("%d:%02d", minutes, seconds);
    }

}
