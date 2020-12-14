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
   
2. Neste caso todas as classes sofreram alterações dado que foram adicionados: um novo estado, e novos métodos, refletindo o comportamento da MusicPlayer para o novo estado.
    ```java
    public abstract class MusicPlayerState {

        protected MusicPlayer player;

        public MusicPlayerState(MusicPlayer player) {
            this.player = player;
        }

        public abstract void play();
        public abstract void stop();
        public abstract void next();
        public abstract void prev();
        public abstract void turnOn();
        public abstract void turnOff();
        public abstract String status();

    }
    ```
    
    ```java
    public class MusicPlayer {

        private Player javaMediaPlayer;
        private Playlist playlist;

        private MusicPlayerState state;

        public MusicPlayer(String folderPath) {
            playlist = new Playlist();
            try {
                playlist.readFromFolder(folderPath);
                state = new OffState(this); //ATENÇÃO: A class começa agora como "Turned Off" e não "Stopped".
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /*
        Public interface of Music Player. Delegate commands to current state
         */

        public void play() {
            state.play();
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

        public void turnOn() { state.turnOn(); }

        public void turnOff() { state.turnOff(); }

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
    ```

    ```java
    public class OffState extends MusicPlayerState {
        public OffState(MusicPlayer player) {
            super(player);
        }

        @Override
        public void play() {

        }

        @Override
        public void stop() {

        }

        @Override
        public void next() {

        }

        @Override
        public void prev() {

        }

        @Override
        public void turnOn() {
            player.initPlayer(player.getPlaylist().getCurrent());
            player.changeState(new StoppedState(player));
        }

        @Override
        public void turnOff() {

        }

        @Override
        public String status() {
            return "Turned Off";
        }
    }
    ```
    
    ```java
    public class PlayingState extends MusicPlayerState {
        public PlayingState(MusicPlayer player) {
            super(player);
        }

        @Override
        public void play() {
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
        public void turnOn() {

        }

        @Override
        public void turnOff() {
            player.disposePlayer();
            player.changeState(new OffState(player));
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
    public class PausedState extends MusicPlayerState {
        public PausedState(MusicPlayer player) {
            super(player);
        }

        @Override
        public void play() {
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
            player.skip10seconds();
        }

        @Override
        public void prev() {
            player.rewind10seconds();
        }

        @Override
        public void turnOn() {

        }

        @Override
        public void turnOff() {
            player.disposePlayer();
            player.changeState(new OffState(player));
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
        public void play() {
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
        public void turnOn() {

        }

        @Override
        public void turnOff() {
            player.disposePlayer();
            player.changeState(new OffState(player));
        }

        @Override
        public String status() {
            return "Stopped. Deck contains " + player.getPlaylist().getCurrent().toString();
        }
    }
    ```
    
    ```java
    public static class CLI implements Runnable {

        /**
         * player to control
         */
        private MusicPlayer player;

        public CLI(MusicPlayer player) {
            this.player = player;
        }

        @Override
        public void run() {
            boolean isRunning = true;
            Scanner keyboard = new Scanner(System.in);
            String command;
            while (isRunning) {
                System.out.println("------- CURRENT STATUS -------");
                System.out.println(player.status());
                System.out.println("------------------------------");
                System.out.println("Available commands: TURN ON, TURN OFF, PLAY, STOP, NEXT, PREV, STATUS, QUIT");
                System.out.print("Prompt> ");
                command = keyboard.nextLine().toLowerCase();

                switch (command) {
                    case "play":
                        player.play();
                        break;
                    case "stop":
                        player.stop();
                        break;
                    case "next":
                        player.next();
                        break;
                    case "prev":
                        player.prev();
                        break;
                    case "turn on":
                        player.turnOn();
                        break;
                    case "turn off":
                        player.turnOff();
                        break;
                    case "status":
                        System.out.println(player.status());
                        break;
                    case "quit":
                        isRunning = false;
                        break;
                    default:
                        System.out.println("[Invalid command]");
                }
            }
            System.out.println("[Terminated]");
            if (player.isPlaying()) player.stop();
            System.exit(0);
        }
    }
    ```
