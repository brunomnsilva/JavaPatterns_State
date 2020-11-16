package pt.pa.model.player;

import pt.pa.model.Playlist;
import pt.pa.model.Song;

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

    /* INNER STATES */
    private class StoppedState extends MusicPlayerState {

        public StoppedState(MusicPlayer player) {
            super(player);
        }

        @Override
        public void playPause() {
            initPlayer(playlist.getCurrent());
            startPlayback();
            changeState(new PlayingState(MusicPlayer.this));
        }

        @Override
        public void stop() {
            //do nothing. Keep state.
        }

        @Override
        public void next() {
            //load next song. Keep state.
            Song next = playlist.getNext();
            initPlayer(next);
        }

        @Override
        public void prev() {
            //load previous song. Keep state.
            Song prev = playlist.getNext();
            initPlayer(prev);
        }

        @Override
        public String status() {
            return "Stopped. Deck contains " + playlist.getCurrent().toString();
        }
    }

    private class PlayingState extends MusicPlayerState {

        public PlayingState(MusicPlayer player) {
            super(player);
        }

        @Override
        public void playPause() {
            stopPlayback();
            changeState(new PausedState(MusicPlayer.this));
        }

        @Override
        public void stop() {
            stopPlayback();
            disposePlayer();
            changeState(new StoppedState(MusicPlayer.this));
        }

        @Override
        public void next() {
            skip10seconds();
        }

        @Override
        public void prev() {
            rewind10seconds();
        }

        @Override
        public String status() {
            Time current = player.getMediaTime();
            Time total = player.getDuration();
            return String.format("Playing (%s / %s) %s",
                    timeToHuman(current),
                    timeToHuman(total),
                    playlist.getCurrent().toString());
        }
    }

    private class PausedState extends MusicPlayerState {

        public PausedState(MusicPlayer player) {
            super(player);
        }

        @Override
        public void playPause() {
            startPlayback();
            changeState(new PlayingState(MusicPlayer.this));
        }

        @Override
        public void stop() {
            stopPlayback();
            disposePlayer();
            changeState(new StoppedState(MusicPlayer.this));
        }

        @Override
        public void next() {
            //do nothing
        }

        @Override
        public void prev() {
            //do nothing
        }

        @Override
        public String status() {
            Time current = player.getMediaTime();
            Time total = player.getDuration();
            return String.format("Paused (%s / %s) %s",
                    timeToHuman(current),
                    timeToHuman(total),
                    playlist.getCurrent().toString());
        }
    }

    /* Internal utilities */
    public static String timeToHuman(Time t) {
        System.out.println("SECONDS = " + t.getSeconds());
        int totalSeconds = (int)t.getSeconds();
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        return String.format("%d:%02d", minutes, seconds);
    }

}
