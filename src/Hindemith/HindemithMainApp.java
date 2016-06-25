/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Hindemith;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author alyssa
 */
public class HindemithMainApp extends Application {
    
    //static Hindemith.view.ProgressTicker myprogress = new Hindemith.view.ProgressTicker();
    
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        Parent root = FXMLLoader.load(getClass().getResource("view/HindemithSkin.fxml"));
        Hindemith.view.FXMLDocumentController my_controller = loader.getController();               
        root.setStyle("-fx-font: 10px \"Lucida Sans Unicode\"; -fx-padding: 10; -fx-background-color: beige;");
        Scene scene = new Scene(root);
        
        stage.setTitle("Trick's Music Boxes");
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent e) {
                Platform.exit();
                System.exit(0);
            }
        });
        stage.setScene(scene);
        stage.show();
}
       
    public static void main(String[] args) {
        launch(args);
    }
       
}
