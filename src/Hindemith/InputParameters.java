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

import Hindemith.ModeModules.AtonalNoRepeat;
import Hindemith.ModeModules.Cblues1;
import Hindemith.ModeModules.AtonalRepeatPrefer;
import Hindemith.ModeModules.ChromaticTonic_keyChange;
import Hindemith.ModeModules.Clydian_keyChange;
import Hindemith.ModeModules.ModeModule;
import Hindemith.RhythmModule.DrumNBassRiffPatternGenerator1;
import Hindemith.RhythmModule.FunkRiffPatternGenerator;
import Hindemith.RhythmModule.VarNoteFunkRiffPatternGenerator;
import Hindemith.RhythmModule.VarTimeSigFunkPatternGenerator;
import Hindemith.RhythmModule.VarTimeSigSuperStraightPatternGenerator;
import Hindemith.RhythmModule.RhythmModule;
import Hindemith.RhythmModule.VarTimeSigFunkPatternGeneratorLastHold;
import Hindemith.RhythmModule.VarTimeSigFunkPatternGeneratorMotif;
import Hindemith.RhythmModule.VarTimeSigMultiBarFunkRiffPatternGenerator;
import Hindemith.RhythmModule.VarTimeSigStraightPatternGenerator;
import java.io.File;

/**
 * Stores and controls access to application input parameters.
 * Default values aren't really used as they are set to whatever is 
 * specified in the input GUI. 
 * @author Trick's Music Boxes
 */
public class InputParameters {
static Boolean out_to_midi_yoke = false;
static Boolean q_mode = false;
static Integer [] consonances = {0, 3, 4, 7, 8, 9};
static Integer [] perfect_consonances = {0, 7};
static Integer [] root_consonances = {0, 3, 4, 7};
static int tempo_bpm = 120;
static int piece_length = 4;
static int root_key = 0;
static ModeModule my_mode_module = new Clydian_keyChange();
static String [] voice_array = {"bass", "soprano",  "ultra", "tenor"};  
static RhythmModule james = new VarNoteFunkRiffPatternGenerator();
static boolean large_dissonance_bad = false;
static File filePath = null;
static File queueDir = null;
static File fileDir = null;
static String queue_directory = null;


public static Boolean get_out_to_midi_yoke () {
    return out_to_midi_yoke;
}
 public static void setQueueDirectory(String direct) {
     queue_directory = direct;
 }
 
 public static String getQueueDirectory() {
     return queue_directory;
 }
 public static void setQueueDir(File direct) {
     queueDir = direct;
 }
 
 public static File getQueueDir() {
     return queueDir;
 }
public static void set_out_to_midi_yoke (Boolean out2yoke) {
    out_to_midi_yoke = out2yoke;
}

public static void set_q_mode (Boolean queue_mode) {
    q_mode = queue_mode;
}

public static Boolean get_q_mode() {
    return q_mode;
}

public static void setLargeDissonanceBad(boolean is_bad){
    large_dissonance_bad = is_bad;
    }
public static void setJames(String generator_string){
    if (generator_string.contains("Funk Rhythm Patterns 1") ) 
        james = new FunkRiffPatternGenerator();
    if (generator_string.contains("Funk Rhythm Patterns 2") ) 
        james = new VarNoteFunkRiffPatternGenerator();
    if (generator_string.contains("Variable Time Signature Unfunky") )
        james = new VarTimeSigSuperStraightPatternGenerator();
    if (generator_string.contains("Variable Time Signature Funky")) 
        james = new VarTimeSigFunkPatternGenerator();
    if (generator_string.contains("Drum and Bass Patterns 1")) 
        james = new DrumNBassRiffPatternGenerator1();
    if (generator_string.contains("Variable TS Funky Last Hold")) 
        james = new VarTimeSigFunkPatternGeneratorLastHold();
    if (generator_string.contains("Variable TS Funky Motive")) 
        james = new VarTimeSigFunkPatternGeneratorMotif();
    if (generator_string.contains("Variable TS Straight Time")) 
        james = new VarTimeSigStraightPatternGenerator();
    if (generator_string.contains("Variable TS Funk Riff Multibar")) 
        james = new VarTimeSigMultiBarFunkRiffPatternGenerator();
    }


public static String testJames() {
    return james.toString();
}

public static void setVoiceArray (String [] my_voice_array){
    voice_array = my_voice_array;
    }
public static ModeModule getModeModule() {
    return my_mode_module;
}
public static String testModeModule() {
    return my_mode_module.toString();
}

public static void setModeModule (String this_mode_module) {
    if (this_mode_module.contains("Lydian"))
        my_mode_module = new Clydian_keyChange();
    if (this_mode_module.contains("Chromatic Tonic")) {
        my_mode_module = new ChromaticTonic_keyChange();
    }
    if (this_mode_module.contains("Atonal (w/o Repeat Notes)")) {
        my_mode_module = new AtonalNoRepeat();
    }
        if (this_mode_module.contains("Atonal (repeat pitch prefer)")) {
        my_mode_module = new AtonalRepeatPrefer();
    }
        if (this_mode_module.contains("Blues")) {
        my_mode_module = new Cblues1();
    }
}
public static void setPieceLength(int my_piece_length) {
    piece_length = my_piece_length;    
}
public static int getLength() {
    return piece_length;
}
public static void setTempo(int my_tempo) {
    tempo_bpm = my_tempo;    
    }
public static int getTempo() {
    return tempo_bpm;
}
public static void setConsonances(Integer [] my_consonances) {
    consonances = my_consonances;
    }
public static void setPerfectConsonances(Integer [] my_perf_consonances) {
    perfect_consonances = my_perf_consonances;
    }
public static void setFilePath(File file) {
    filePath = file;
    if (file != null) fileDir = file.getParentFile();
    }

public static File getFilePath() {
    if(filePath != null) return filePath;
    else {
        //System.out.println("file path is null");
        return null;
        }
}

public static File getFileDir() {
    if(fileDir!= null) return fileDir;
    else {
        //System.out.println("file path is null");
        return null;
        }
}

}
