# Solution

## 1 

We need to:

1. Create the new `PausedState` class, and;
2. Update the FSM transition for the `play()` action in the `PlayingState` class.

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
        return String.format("Paused (%s / %s) %s",
                MusicPlayer.timeToHuman(current),
                MusicPlayer.timeToHuman(total),
                player.getCurrentLoadedSong());
    }
}
```

```java
public class PlayingState extends MusicPlayerState {
    //...

    @Override
    public void play() {
        player.pausePlayback();
        player.changeState(new PausedState(player));
    }
}
```

## 2

Now we need to update the following:

1. Music player's actions with the additional `turnOn()` and `turnOff()` methods and include them in the player state abstract class.
2. Create the `OffState` class and make it the default player state;
3. (Re)implement the state's transitions, and;
4. Update the CLI with for the new actions.

`MusicPlayerState` class:

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
    public abstract void turnOff(); //new action
    public abstract void turnOn(); //new action
    public abstract String status();

}
```

`MusicPlayer` class:

```java
public class MusicPlayer {
    
    //...

    public void turnOn() {
        state.turnOn();
    }
    
    public void turnOff(){
        state.turnOff();
    }
    
}
```

`OffState` class:

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
    public void turnOff() {

    }

    @Override
    public void turnOn() {
        player.changeState(new StoppedState(player));
    }

    @Override
    public String status() {
        return "Off";
    }
}
```

`StoppedState` class:

```java
public class StoppedState extends MusicPlayerState {

    //...

    @Override
    public void turnOff() {
        player.changeState(new OffState(player));
    }

    @Override
    public void turnOn() {
        //no action
    }
}

```

`PlayingState` class:

```java
public class PlayingState extends MusicPlayerState {
    //...

    @Override
    public void turnOff() {
        player.stopPlayback();
        player.changeState(new OffState(player));
    }

    @Override
    public void turnOn() {
        //no action
    }
}
```

`PausedState` class:

```java
public class PausedState extends MusicPlayerState {


    //...

    @Override
    public void turnOff() {
        player.stopPlayback();
        player.changeState(new OffState(player));
    }

    @Override
    public void turnOn() {
        //no action
    }
    
}
```

`Main` class:

```java
public class Main {
    //...
    public static class CLI implements Runnable {
        
        //...
        @Override
        public void run() {
            boolean isRunning = true;
            Scanner keyboard = new Scanner(System.in);
            String command;
            while(isRunning) {
                System.out.println("------- CURRENT STATUS -------");
                System.out.println(player.status());
                System.out.println("------------------------------");
                System.out.println("Available commands: ON, OFF, PLAY, STOP, NEXT, PREV, STATUS, QUIT");
                System.out.print("Prompt> ");
                command = keyboard.nextLine().toLowerCase();

                switch (command) {
                    //...
                    case "on":
                        player.turnOn();
                        break;
                    case "off":
                        player.turnOff();
                        break;
                    
                }
            }
        }
        //...
    }
}
```