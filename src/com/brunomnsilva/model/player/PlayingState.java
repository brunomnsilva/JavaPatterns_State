package com.brunomnsilva.model.player;

import javax.media.Time;

public class PlayingState extends MusicPlayerState {
    public PlayingState(MusicPlayer player) {
        super(player);
    }

    @Override
    public void play() {
        //do nothing
    }

    @Override
    public void stop() {
        player.stopPlayback();
        player.changeState(new StoppedState(player));
    }

    @Override
    public void next() {
        player.skip10seconds();
    }

    @Override
    public void prev() {
        player.rewind10seconds();
    }

    @Override
    public String status() {
        Time current = player.getMediaTime();
        Time total = player.getDuration();
        return String.format("Playing (%s / %s) %s",
                MusicPlayer.timeToHuman(current),
                MusicPlayer.timeToHuman(total),
                player.getCurrentLoadedSong());
    }
}
