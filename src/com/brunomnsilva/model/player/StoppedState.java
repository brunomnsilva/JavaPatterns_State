package com.brunomnsilva.model.player;

public class StoppedState extends MusicPlayerState {

    public StoppedState(MusicPlayer player) {
        super(player);
    }

    @Override
    public void play() {
        player.loadCurrentSong();
        player.startPlayback();
        player.changeState(new PlayingState(player));
    }

    @Override
    public void stop() {
        //do nothing. Keep state.
    }

    @Override
    public void next() {
        //load next song. Keep state.
        player.loadNextSong();
    }

    @Override
    public void prev() {
        //load previous song. Keep state.
        player.loadPreviousSong();
    }

    @Override
    public String status() {
        return "Stopped. Deck contains " + player.getCurrentLoadedSong();
    }
}
