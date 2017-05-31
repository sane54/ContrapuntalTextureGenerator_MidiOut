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
 * Starts a separate thread to play a pattern or queue of patterns stored in 
 * PatternStorerSaver1, then shows a screen of controls for that thread. If the 
 * program is sending to a MidiPort, the thread can't be restarted after starting
 * it. 
 * @author Trick's Music Boxes
 */

import Hindemith.InputParameters;
import Hindemith.MidiOut;
import Hindemith.PatternQueueStorerSaver;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import org.jfugue.DeviceThatWillReceiveMidi;
import org.jfugue.Pattern;
import org.jfugue.Player;




public class PlayerBox {
    private final Stage jwstage = new Stage();
    static Player pps_player;
    static Pattern pattern_to_play;
    static Sequencer mysequencer;
    static Integer queue_index;
    
    public PlayerBox() {
        nowshow ("Now Playing", "Pattern Player", jwstage);   
     }
    /**
     * Launches the player thread and builds a stage with controls for that 
     * thread. 
     * @param message
     * @param title
     * @param stage 
     */
    private void nowshow (String message, String title, Stage stage) {
        boolean usedefault = true;
        if (!Hindemith.InputParameters.get_q_mode()) pattern_to_play  = Hindemith.PatternStorerSaver1.get_pattern();
        if (InputParameters.get_out_to_midi_yoke())  {
            MidiOut.setDevice();
            if (MidiOut.device != null) {
                jplayer myjplayer = new jplayer();
                try {
                    Sequencer myseq = myjplayer.getSequencerConnectedToDevice(MidiOut.device);
                   pps_player = new Player(myseq);
                   usedefault = false;
                } catch (MidiUnavailableException ex) {
                }
            }
            else {
                InputParameters.set_out_to_midi_yoke(false);
                MessageBox.show( "No Midi Output", "Using default midi sounds");
                usedefault = true;
            }
           
            
        }
        if (usedefault) pps_player = new Player();
            
        LauncherThread starter_thread = new LauncherThread();
        Thread PlayerStarterThread = new Thread((Runnable) starter_thread.worker);
        PlayerStarterThread.start();
        

        
        
       
        
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
    /**
     * The launcher for the player thread. 
     */
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
                        if (!Hindemith.InputParameters.get_q_mode()) pps_player.play(pattern_to_play);
                        else {
                            queue_index = 0;
                            while (queue_index < PatternQueueStorerSaver.get_queue_size()) {
                                pps_player.play(Hindemith.PatternQueueStorerSaver.get_queue_pattern(queue_index));
                                queue_index++;
                            }

                        }
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
    /**
     * Solely used to connect the Java Midi sequencer to a port rather than a 
     * synthesizer. 
     */
    public class jplayer extends Player {
        public Sequencer getSequencerConnectedToDevice(DeviceThatWillReceiveMidi device) throws MidiUnavailableException{
            Sequencer sequencer = MidiSystem.getSequencer(false); // Get Sequencer which is not connected to new Synthesizer.
            sequencer.open();
            sequencer.getTransmitter().setReceiver(device.getReceiver()); // Connect the Synthesizer to our synthesizer instance.
            return sequencer;
        }
    }
        
    
}
