 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Hindemith;

import javafx.concurrent.Task;
import javafx.concurrent.Worker;

public class PlayerThread {
    public Worker worker;
    static boolean resume = false;
    static String playerstate;
    
    public  PlayerThread(String inputState) {
        playerstate = inputState;
        worker = new Task<String>() {
            @Override
            protected String call() throws Exception {
            try {
                if (isCancelled()) {
                System.out.println("Thread was cancelled");
                return "Cancelled";
                }
                
                if ("pause".equals(inputState)) PausePlayer();
                if ("stop".equals(inputState)) StopPlayer();
                if ("resume".equals(inputState)) {
                    PlayPlayer();
                    System.out.println("Player Thread method PlayPlayer has completed");
                } 
                Thread.sleep(1);
            } catch (InterruptedException interrupted) {
                System.out.println("I'm interrupted");
                if (isCancelled()) {
                    System.out.println("Thread was cancelled");
                    return "Cancelled";
                }
                else {
                    System.out.println("defacto cancel");
                    return "cancelled";
                }
            }
            System.out.println("about to return thread done");
            return "done";
            }
        };
    }
    
    public static void PausePlayer() {
        System.out.println("called pause player");
        PatternPlayerSaver.pause_player();
    }
    
    public static void StopPlayer() {
        System.out.println("stopping player");
        PatternPlayerSaver.stop_player();
        //PatternPlayerSaver.resetPlayer();
    }
    
    public static void PlayPlayer() {
        if (resume) PatternPlayerSaver.resume_player();
        else {
            resume = true;
            while (true) {
                if (PatternPlayerSaver.play_pattern()) break;
            }
            
            
        }
    }
}
