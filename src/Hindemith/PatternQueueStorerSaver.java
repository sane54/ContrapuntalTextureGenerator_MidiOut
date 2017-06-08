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
import java.util.ArrayList;



/**
 * This class stores and provides access to a JFugue pattern which is the 
 * output of the Model1 class, the class that actually composes the music. 
 * It is accessed by view classes who will get the pattern in order to play the
 * music composed by Model1. It also contains  a method for saving the pattern. 
 * @author Trick's Music Boxes
 */
public class PatternQueueStorerSaver {
    static Player pps_player = new Player();
    static ArrayList<Pattern> pattern_queue = new ArrayList();
    
    public static void add_pattern_to_queue (Pattern my_pattern) {
         pattern_queue.add(my_pattern);
    }
    
    public static Pattern get_queue_pattern(Integer index) {
        return pattern_queue.get(index);
    }
    
    public static void clear_queue() {
        pattern_queue.clear();
    }
    public static Integer get_queue_size() {
        return pattern_queue.size();
    }
    public static void save_queue() {
        for (int i = 0; i < pattern_queue.size(); i++) {
            save_pattern(pattern_queue.get(i), i);
        }
        clear_queue();
    }

    
    /**
     * File location is specified in InputParameters class. Leverages the 
     * JFugue Player class to save the pattern to a file. Default name is a 
     * simple date string.
     */
    public static void save_pattern(Pattern pattern, int i) {
             //save the pattern
            Date today = new Date(System.currentTimeMillis());
            SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM-dd-yyyy-HH-mm-ss");
            String dateString = DATE_FORMAT.format(today);
            if(InputParameters.getQueueDirectory() != null) {
//                String mydir = InputParameters.getQueueDirectory();
//                System.out.println(mydir);
                try {
                pps_player.saveMidi(pattern, new File(mydir + "\\" + tempo_bpm + "-" + dateString + "-" + i + ".mid"));
                }
                catch (IOException ex) {
                    MessageBox.show("couldn't save pattern in file location", "fail");
                }
            }    
//            else {
//                try {
//                    pps_player.saveMidi(pattern, new File(tempo_bpm + "-" + dateString + "-" + i + ".mid"));
//                }
//                catch (IOException ex2) {
//                MessageBox.show("couldn't save pattern in file location", "fail");
//                }
//            } 
//            InputParameters.setFilePath(null);
    }
   
    public static void resetPlayer() {
        pps_player.close();
    }
}
