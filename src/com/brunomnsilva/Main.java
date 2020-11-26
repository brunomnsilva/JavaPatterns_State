package com.brunomnsilva;

import com.brunomnsilva.model.player.MusicPlayer;

import java.util.Scanner;

/**
 * @author brunomnsilva
 */
public class Main {

    public static void main(String[] args) {
        MusicPlayer player = new MusicPlayer("songs");

        Thread t = new Thread(new CLI(player));
        t.run();
    }

    public static class CLI implements Runnable {

        /** player to control */
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
                System.out.println("------- CURRENT STATUS -------");
                System.out.println(player.status());
                System.out.println("------------------------------");
                System.out.println("Available commands: PLAY, STOP, NEXT, PREV, STATUS, QUIT");
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
            if(player.isPlaying()) player.stop();
            System.exit(0);
        }
    }
}
