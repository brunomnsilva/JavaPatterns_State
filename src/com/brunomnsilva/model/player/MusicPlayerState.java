package com.brunomnsilva.model.player;

public abstract class MusicPlayerState {

    protected MusicPlayer player;

    public MusicPlayerState(MusicPlayer player) {
        this.player = player;
    }

    public abstract void play();
    public abstract void stop();
    public abstract void next();
    public abstract void prev();
    public abstract String status();

}
