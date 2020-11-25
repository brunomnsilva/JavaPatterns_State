1. Existing *concrete states* also need changes, 
besides implementing `PausedState`. The class `MusicPlayer` remains unchanged.

    ```java
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
    ```
    ```java
    public class PlayingState extends MusicPlayerState {
        public PlayingState(MusicPlayer player) {
            super(player);
        }
    
        @Override
        public void playPause() {
            player.stopPlayback();
            player.changeState(new PausedState(player));
        }
    
        @Override
        public void stop() {
            player.stopPlayback();
            player.disposePlayer();
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
                    player.getPlaylist().getCurrent().toString());
        }
    }
    ```
    
    ```java
    public class StoppedState extends MusicPlayerState {
    
        public StoppedState(MusicPlayer player) {
            super(player);
        }
    
        @Override
        public void playPause() {
            player.initPlayer(player.getPlaylist().getCurrent());
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
            Song next = player.getPlaylist().getNext();
            player.initPlayer(next);
        }
    
        @Override
        public void prev() {
            //load previous song. Keep state.
            Song prev = player.getPlaylist().getNext();
            player.initPlayer(prev);
        }
    
        @Override
        public String status() {
            return "Stopped. Deck contains " + player.getPlaylist().getCurrent().toString();
        }
    }
    ```
   
2. TODO. Fork project and provide a "pull request" with the solution in this file.

