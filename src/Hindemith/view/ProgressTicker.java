/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Hindemith.view;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author alyssa
 */
public class ProgressTicker {
    Stage stage = new Stage();
    ProgressBar pb = new ProgressBar();
    
    public  void show() {
      
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Hindemith");
        stage.setMinWidth(250);
         
        Label mylabel = new Label("Generating Counterpoint");
        
        VBox pane = new VBox(20);
        pane.getChildren().addAll(pb, mylabel);
        pane.setAlignment(Pos.CENTER);
        
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.showAndWait();
    }

    public  void displayProgress(double frac) {
        //stage.hide();
        pb.setProgress(frac);
        if (frac == 1.0) stage.close();
        else stage.showAndWait();
    }
    
    public void closeMe () {
        stage.close();
    }
    
    
}
