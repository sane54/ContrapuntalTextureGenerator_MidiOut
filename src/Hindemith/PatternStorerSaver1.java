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

import static Hindemith.InputParameters.tempo_bpm;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.jfugue.*;
import Hindemith.view.MessageBox;



/**
 * This class stores and provides access to a JFugue pattern which is the 
 * output of the Model1 class, the class that actually composes the music. 
 * It is accessed by view classes who will get the pattern in order to play the
 * music composed by Model1. It also contains  a method for saving the pattern. 
 * @author Trick's Music Boxes
 */
public class PatternStorerSaver1 {
    static Player pps_player = new Player();
    static Pattern music_output;
    
    public static void add_pattern (Pattern my_pattern) {
        if (music_output == null) music_output = my_pattern;
        else music_output.add(my_pattern);
    }
    
    public static Pattern get_pattern() {
        return music_output;
    }
    
    public static void clear_pattern() {
        music_output = null;
    }
    

    
    /**
     * File location is specified in InputParameters class. Leverages the 
     * JFugue Player class to save the pattern to a file. Default name is a 
     * simple date string.
     */
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
                    MessageBox.show("couldn't save pattern in file location", "fail");
                }
            }    
            else {
                try {
                    pps_player.saveMidi(music_output, new File(tempo_bpm + "-" + dateString + ".mid"));
                }
                catch (IOException ex2) {
                MessageBox.show("couldn't save pattern in file location", "fail");
                }
            } 
            InputParameters.setFilePath(null);
    }
   
    public static void resetPlayer() {
        pps_player.close();
    }
}
