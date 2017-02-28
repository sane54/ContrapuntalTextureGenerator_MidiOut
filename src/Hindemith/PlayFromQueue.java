/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Hindemith;

import static Hindemith.InputParameters.tempo_bpm;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import javax.sound.midi.Sequence;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import org.jfugue.DeviceThatWillReceiveMidi;
import javax.sound.midi.MidiUnavailableException;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import org.jfugue.*;


/**
 *
 * @author Owner
 */
public class PlayFromQueue {
    static Player my_player = new Player();
    static LinkedList <Pattern> pattern_queue = new LinkedList();
    
    public static void add_pattern_to_queue (Pattern my_pattern) {
        pattern_queue.add(my_pattern);
    }
    
    public static void play_pattern_from_queue(){
        LinkedList <Pattern> player_queue = pattern_queue;
        Pattern music_output = player_queue.pop();
        if (InputParameters.get_out_to_midi_yoke())  {
            MidiOut.setDevice();
            Sequence music_sequence = my_player.getSequence(music_output);
            MidiOut.device.sendSequence(music_sequence);
        }
            else my_player.play(music_output);
    }

    public static void save_pattern_from_queue() {
        
    }
    public static void save_pattern(Pattern my_pattern) {
             //save the pattern
            Date today = new Date(System.currentTimeMillis());
            SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM-dd-yyyy-HH-mm");
            String dateString = DATE_FORMAT.format(today);
            if(InputParameters.filePath != null) {
                try {
                my_player.saveMidi(my_pattern, InputParameters.filePath);
                }
                catch (IOException ex) { 
                }
            }    
            else {
                try {
                    my_player.saveMidi(my_pattern, new File(tempo_bpm + "-" + dateString + ".mid"));
                }
                catch (IOException ex2) {
                
                }
            } 
    }
      
}
