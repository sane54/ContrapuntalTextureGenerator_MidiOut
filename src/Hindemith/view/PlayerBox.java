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

import Hindemith.PatternPlayerSaver;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;




public class PlayerBox {
    private final Stage jwstage = new Stage();
    
    public PlayerBox() {
        nowshow ("Now Playing", "Pattern Player", jwstage);
    }
    
    private  void nowshow (String message, String title, Stage stage) {
        
        Hindemith.PlayerThread myPlayerStarter = new Hindemith.PlayerThread("resume");
        Thread PlayerStartThread = new Thread((Runnable) myPlayerStarter.worker);
        PlayerStartThread.setDaemon(false);
        PlayerStartThread.start();
        
        Hindemith.PlayerThread myPlayerPauser = new Hindemith.PlayerThread("pause");
        Thread PlayerPauseThread = new Thread((Runnable) myPlayerPauser.worker);
        
        Hindemith.PlayerThread myPlayerResumer = new Hindemith.PlayerThread("resume");
        Thread PlayerResumeThread = new Thread((Runnable) myPlayerResumer.worker);

        Hindemith.PlayerThread myPlayerStopper = new Hindemith.PlayerThread("stop");
        Thread PlayerStopThread = new Thread((Runnable) myPlayerStopper.worker);        
        
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(title);
        stage.setMinWidth(450);
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                System.out.println("Stage is closing");
            if (PlayerStartThread.isAlive()) {
                System.out.println("about to stop player " + PlayerStartThread.getName() );
                PatternPlayerSaver.stop_player();
                //System.out.println("Starter Worker is still alive Closing Player Starter Worker");
                //PlayerStopThread.start();
                //myPlayerStarter.worker.cancel();
                //PlayerStartThread.interrupt();
                //if (PlayerStartThread.isInterrupted()) System.out.println("Did interrupt thread");
                //if (PlayerStartThread.isAlive())System.out.println("Cancel didn't work - thread still alive");

            }
            else System.out.println("Start thread is dead");
            if (PlayerResumeThread.isAlive()) PlayerResumeThread.stop();
            else System.out.println("Resume thread is dead");
            if (PlayerPauseThread.isAlive()) PlayerPauseThread.stop();
            else System.out.println("Pause thread is dead");
            if (PlayerStopThread.isAlive()) PlayerStopThread.stop();
            else System.out.println("Stop thread is dead");
            if (PlayerStartThread.isAlive()) System.out.println("start thread is still alive....still");
            else System.out.println("start thread is dead");
            
            }
        });  
        
        
        
        Label lbl = new Label();
        lbl.setText(message);
        
        Button btnPause = new Button();
        btnPause.setText("Pause");
        btnPause.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
//            Hindemith.PlayerThread myPlayerPauser = new Hindemith.PlayerThread("pause");
//            Thread PlayerPauseThread = new Thread((Runnable) myPlayerPauser.worker);    
            PlayerPauseThread.start();
            myPlayerStarter.worker.cancel();
            }
        });
        
        Button btnPlay = new Button("Play");
        btnPlay.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
//            Hindemith.PlayerThread myPlayerResumer = new Hindemith.PlayerThread("resume");
//            Thread PlayerResumeThread = new Thread((Runnable) myPlayerResumer.worker);
            PlayerResumeThread.start();
            }
        });
        
        Button btnStop = new Button("Stop");
        btnStop.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
//            Hindemith.PlayerThread myPlayerStopper = new Hindemith.PlayerThread("stop");
//            Thread PlayerStopThread = new Thread((Runnable) myPlayerStopper.worker); 
            PlayerStopThread.start();
//            
//

                stage.close();
            }
        });
        
        VBox pane = new VBox(30);
        pane.getChildren().addAll(lbl, btnPause, btnPlay, btnStop);
        pane.setAlignment(Pos.CENTER);
        pane.setStyle("-fx-font: 11px \"Lucida Sans Unicode\"; -fx-padding: 10; -fx-background-color: beige;");
        
        Scene jwscene = new Scene(pane);
        stage.setScene(jwscene);
        System.out.println("about to show and wait stage");
        stage.showAndWait();

    }
    
    
}
