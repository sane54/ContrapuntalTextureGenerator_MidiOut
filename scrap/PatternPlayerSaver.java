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
import java.util.Date;
import javax.sound.midi.Sequence;
import org.jfugue.*;


/**
 *
 * SEPARATE OUT THE SAVER STORAGE FUNCTIONS INTO PATTERNSAVER AND THE PLAY
 * 
 * FUNCTIONS INTO PATTERNPLAYER
 */
public class PatternPlayerSaver {
    static Player pps_player = new Player();
    static Pattern music_output;
    
    public static void add_pattern (Pattern my_pattern) {
        music_output = my_pattern;
    }
    
    public static Pattern get_pattern() {
        return music_output;
    }
            
    
    public static boolean play_pattern(){
        if (InputParameters.get_out_to_midi_yoke())  {
            MidiOut.setDevice();
            Sequence music_sequence = pps_player.getSequence(music_output);
            MidiOut.device.sendSequence(music_sequence);
        }
            else pps_player.play(music_output);
        while (true) {
            if (pps_player.isFinished()) {
                System.out.println("pps_player has stopped");
                break;
            }
        }
        return true;
    }

    public static void pause_player() {
        pps_player.pause();
    }
    
    public static void resume_player() {
        pps_player.resume();
    }
    
    
    public static void stop_player() {
        pps_player.stop();
    }
    
    
    public static void save_pattern() {
             //save the pattern
            Date today = new Date(System.currentTimeMillis());
            SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM-dd-yyyy-HH-mm");
            String dateString = DATE_FORMAT.format(today);
            if(InputParameters.filePath != null) {
                try {
                pps_player.saveMidi(music_output, InputParameters.filePath);
                }
                catch (IOException ex) { 
                }
            }    
            else {
                try {
                    pps_player.saveMidi(music_output, new File(tempo_bpm + "-" + dateString + ".mid"));
                }
                catch (IOException ex2) {
                
                }
            } 
    }
   
    public static void resetPlayer() {
        pps_player.close();
    }
}
