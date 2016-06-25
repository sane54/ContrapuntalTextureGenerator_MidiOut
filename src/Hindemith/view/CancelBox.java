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
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;



public class CancelBox {
    
    private static Boolean proceed;
    
    public static Boolean getProceed() {
        return proceed;          
    }
    
    public static void setProceed() {
        proceed = false;
    }
    
    public static void show (String message, String title) {
        proceed = true;
        final Stage stage = new Stage ();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(title);
        stage.setMinWidth(450);
        
        Label lbl = new Label();
        lbl.setText(message);
        
        Button btnOK = new Button();
        btnOK.setText("OK");
        btnOK.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                stage.close();
            }
        });
        
        Button btnCancel = new Button("Cancel");
        btnCancel.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                setProceed();
                stage.close();
            }
        });
        
        VBox pane = new VBox(30);
        pane.getChildren().addAll(lbl, btnOK, btnCancel);
        pane.setAlignment(Pos.CENTER);
        pane.setStyle("-fx-font: 11px \"Lucida Sans Unicode\"; -fx-padding: 10; -fx-background-color: beige;");
        
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.showAndWait();
        
        
    }


    
}
