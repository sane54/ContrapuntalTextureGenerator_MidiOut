/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Hindemith.ModeModules;

import java.util.ArrayList;

/**
 *
 * @author alyssa
 */
public interface ModeModule {

    ArrayList<Integer> getFirstNotePitchCandidates(int rangeMin, int rangeMax, int key_transpose);
    ArrayList<Integer> getPitchCandidates(int input_previous_pitch, int key_transpose);
    Integer getPitchCenter(int rangeMin, int rangeMax);
    Double getMelodicMotionProbability (Integer input_current_pitch_cand, Integer input_previous_pitch, Integer key_transpose);
    Integer getTonic();
    String mytoString();
    
}
