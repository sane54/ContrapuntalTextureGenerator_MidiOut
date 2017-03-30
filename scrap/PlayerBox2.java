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



public class PlayerBox2 {
    
    private static Boolean proceed;
    private static boolean autostart = true;
   //public static Hindemith.PlayerThread model = new Hindemith.PlayerThread();
   // public static Thread generatorThread = new Thread((Runnable) model.worker);
    public static Boolean getProceed() {
        return proceed;          
    }
    
    public static void setProceed() {
        proceed = false;
    }
    
    public static void PlayerBox() {
        if (autostart) {
            show ("Now Playing", "Pattern Player");
            //generatorThread.start();
        }
    }
    
    public static void show (String message, String title) {
        proceed = true;
        final Stage stage = new Stage ();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(title);
        stage.setMinWidth(450);
        
        Label lbl = new Label();
        lbl.setText(message);
        
        Button btnPause = new Button();
        btnPause.setText("Pause");
        btnPause.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                PatternPlayerSaver.pause_player();
                
            }
        });
        
        Button btnPlay = new Button("Play");
        btnPlay.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                PatternPlayerSaver.resume_player();
            }
        });
        
        Button btnStop = new Button("Stop");
        btnStop.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                PatternPlayerSaver.stop_player();
                stage.close();
            }
        });
        
        VBox pane = new VBox(30);
        pane.getChildren().addAll(lbl, btnPause, btnPlay, btnStop);
        pane.setAlignment(Pos.CENTER);
        pane.setStyle("-fx-font: 11px \"Lucida Sans Unicode\"; -fx-padding: 10; -fx-background-color: beige;");
        
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        //stage.showAndWait();
        stage.show();
        
        
    }


    
}
