package pt.pa;

import pt.pa.model.MusicPlayer;
import pt.pa.model.Playlist;
import pt.pa.model.Song;

import java.io.IOException;
import java.util.Scanner;

/**
 * @author brunomnsilva
 */
public class Main {

    public static void main(String[] args) {

        MusicPlayer player = new MusicPlayer("songs");
        //player.play();

        Thread t = new Thread(new CLI(player));
        t.run();
    }

    public static class CLI implements Runnable {

        private MusicPlayer player;

        public CLI(MusicPlayer player) {
            this.player = player;
        }

        @Override
        public void run() {
            boolean isRunning = true;
            Scanner keyboard = new Scanner(System.in);
            String command;
            while(isRunning) {
                System.out.println("Available commands: PLAY, STOP, QUIT");
                System.out.print("Prompt> ");
                command = keyboard.nextLine().toLowerCase();

                switch (command) {
                    case "play":
                        player.play();
                        break;
                    case "stop":
                        player.stop();
                        break;
                    case "quit":
                        isRunning = false;
                        break;
                    default:
                        System.out.println("[Invalid command]");
                }
            }
            System.out.println("[Terminated]");
        }
    }
}
