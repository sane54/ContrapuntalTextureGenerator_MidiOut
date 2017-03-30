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
import org.jfugue.*;



public class PatternStorerSaver1 {
    static Player pps_player = new Player();
    static Pattern music_output;
    
    public static void add_pattern (Pattern my_pattern) {
        music_output = my_pattern;
    }
    
    public static Pattern get_pattern() {
        return music_output;
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
