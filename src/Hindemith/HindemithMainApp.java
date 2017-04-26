/*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
* 
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
* 
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
 * The Hindemith main class which contains the main method 
 * which in turn simply launches the FXML GUI 
 * @author alyssa
 */
public class HindemithMainApp extends Application {
    
    //static Hindemith.view.ProgressTicker myprogress = new Hindemith.view.ProgressTicker();
    
    /**
     * Starts the GUI
     * @param stage
     * @throws Exception 
     */
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
    /**
     * The main method which simply calls start
     * @param args 
     */   
    public static void main(String[] args) {
        launch(args);
    }
       
}
