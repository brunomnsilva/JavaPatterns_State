package pt.pa.model;

import javax.media.CannotRealizeException;
import javax.media.NoPlayerException;
import javax.media.Player;
import javax.media.Manager;
import java.io.IOException;

public class MusicPlayer {

    private Player player;
    private Playlist playlist;

    public MusicPlayer(String folderPath) {
        playlist = new Playlist();
        try {
            playlist.readFromFolder(folderPath);
            player = Manager.createRealizedPlayer(playlist.getCurrent().getUnderlyingFile().toURI().toURL());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CannotRealizeException e) {
            e.printStackTrace();
        } catch (NoPlayerException e) {
            e.printStackTrace();
        }
    }

    public void play() {
        player.start();
    }

    public void stop() {
        player.stop();
    }
}
