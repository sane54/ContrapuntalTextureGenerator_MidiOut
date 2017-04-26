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



public class MessageBox {

    public static void show (String message, String title) {
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
        
        VBox pane = new VBox(30);
        pane.getChildren().addAll(lbl, btnOK);
        pane.setAlignment(Pos.CENTER);
        pane.setStyle("-fx-font: 11px \"Lucida Sans Unicode\"; -fx-padding: 10; -fx-background-color: beige;");
        
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.showAndWait();
        
        
    }


    
}
