/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
import java.io.File;

/**
 *
 * @author alyssa
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

public static Boolean get_out_to_midi_yoke () {
    return out_to_midi_yoke;
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
    }

public static File getFilePath() {
    if(filePath != null) return filePath;
    else {
        System.out.println("file path is null");
        return null;
        }
    }
}
