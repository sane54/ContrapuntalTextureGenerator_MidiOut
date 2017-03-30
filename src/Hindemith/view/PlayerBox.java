/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Hindemith.view;

/**
 *
 * @author alyssa
 */

import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;
import org.jfugue.Pattern;
import org.jfugue.Player;




public class PlayerBox {
    private final Stage jwstage = new Stage();
    static Player pps_player = new Player();
    static Pattern pattern_to_play;
    
    public PlayerBox() {
        nowshow ("Now Playing", "Pattern Player", jwstage);
    }
    
    private  void nowshow (String message, String title, Stage stage) {
        
        
        LauncherThread starter_thread = new LauncherThread();
        Thread PlayerStarterThread = new Thread((Runnable) starter_thread.worker);
        PlayerStarterThread.start();
        pattern_to_play  = Hindemith.PatternStorerSaver1.get_pattern();
        
       
        
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(title);
        stage.setMinWidth(450);
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                if (pps_player.isPlaying()) {
                    pps_player.stop();
                }
            }
        });  
        
        
        
        Label lbl = new Label();
        lbl.setText(message);
        
        Button btnPause = new Button();
        btnPause.setText("Pause");
        btnPause.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
            if (pps_player.isPlaying()) pps_player.pause();
            }
        });
        
        Button btnPlay = new Button("Play");
        btnPlay.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                if (!pps_player.isPlaying()) {
                    LauncherThread resume_thread = new LauncherThread();
                    Thread PlayerResumeThread = new Thread((Runnable) resume_thread.worker);
                    PlayerResumeThread.start();   
                }

            }
        });
        
        Button btnStop = new Button("Stop");
        btnStop.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
            if (pps_player.isPlaying())pps_player.stop();
            }
        });
        
        VBox pane = new VBox(30);
        pane.getChildren().addAll(lbl, btnPause, btnPlay, btnStop);
        pane.setAlignment(Pos.CENTER);
        pane.setStyle("-fx-font: 11px \"Lucida Sans Unicode\"; -fx-padding: 10; -fx-background-color: beige;");
        
        Scene jwscene = new Scene(pane);
        stage.setScene(jwscene);
        //DEBUG
        //System.out.println("about to show and wait stage");
        stage.showAndWait();

    }
    
    public class LauncherThread {
        public Worker worker;
        boolean resume = false;
        String playerstate;
    
        public  LauncherThread() {
            worker = new Task<String>() {
                @Override
                protected String call() throws Exception {
                    if (pps_player.isPaused()) {
                        pps_player.resume();
                    }
                    else {
                        pps_player.play(pattern_to_play);
                    }

                    while (true) {
                        if (!pps_player.isPlaying()) {
                            break;
                        }
                    }
                    return "done";
                }
            };
        }
    }
}
