package com.brunomnsilva.model.player;

import javax.media.Time;

public class PausedState extends MusicPlayerState {

    public PausedState(MusicPlayer player) {
        super(player);
    }

    @Override
    public void playPause() {
        player.startPlayback();
        player.changeState(new PlayingState(player));
    }

    @Override
    public void stop() {
        player.stopPlayback();
        player.disposePlayer();
        player.changeState(new StoppedState(player));
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
                MusicPlayer.timeToHuman(current),
                MusicPlayer.timeToHuman(total),
                player.getPlaylist().getCurrent().toString());
    }
}
