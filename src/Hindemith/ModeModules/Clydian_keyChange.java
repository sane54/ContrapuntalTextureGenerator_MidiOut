/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Hindemith.ModeModules;

import Hindemith.ModeModules.ModeModule;
import static java.lang.Math.abs;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 *
 * @author alyssa
 */
public class Clydian_keyChange implements ModeModule {
    Random roll = new Random();
    private final int tonic = 0;
    
    private static final Map<Integer, Double> step_probability_0;
	static {
		step_probability_0 = new HashMap<>();
		step_probability_0.put(0, 0.03);
                step_probability_0.put(2, 0.01);
                step_probability_0.put(-1, 0.1);
                step_probability_0.put(-3, 0.1);
                step_probability_0.put(4, 0.15);
                step_probability_0.put(-5, 0.15);
                step_probability_0.put(7, 0.1);
                step_probability_0.put(-8, 0.1);
                step_probability_0.put(9, 0.1);
                step_probability_0.put(12, 0.03);
                step_probability_0.put(-12, 0.03);

	};
    private static final Map<Integer, Double> step_probability_1;
	static {
            step_probability_1 = new HashMap<>();
            step_probability_1.put(-1, 1.00);
        };

private static final Map<Integer, Double> step_probability_2;
static {
		step_probability_2 = new HashMap<>();
                step_probability_2.put(2, 0.35);
                step_probability_2.put(-2, 0.35);
                step_probability_2.put(-3, 0.15);
                step_probability_2.put(5, 0.1);
                step_probability_2.put(7, 0.05);
};
    private static final Map<Integer, Double> step_probability_3;
	static {
            step_probability_3 = new HashMap<>();
            step_probability_3.put(-3, 0.50);
            step_probability_3.put(1, 0.50);
        };
private static final Map<Integer, Double> step_probability_4;
static {
	step_probability_4 = new HashMap<>();
                step_probability_4.put(0, 0.02);
		step_probability_4.put(2, 0.05);
                step_probability_4.put(-2, 0.01);
                step_probability_4.put(3, 0.2);
                step_probability_4.put(-4, 0.3);
                step_probability_4.put(5, 0.06);
                step_probability_4.put(-7, 0.01);
                step_probability_4.put(8, 0.1);
                step_probability_4.put(-9, 0.11);

};
private static final Map<Integer, Double> step_probability_5;
    static {
            step_probability_5 = new HashMap<>();
            step_probability_5.put(2, 0.50);
            step_probability_5.put(-1, 0.50);
    };
private static final Map<Integer, Double> step_probability_6;
static {
	step_probability_6 = new HashMap<>();
	step_probability_6.put(1, 0.5);
	step_probability_6.put(-2, 0.5);

};

private static final Map<Integer, Double> step_probability_7;
static {
	step_probability_7 = new HashMap<>();
                step_probability_7.put(0, 0.03);
                step_probability_7.put(-1, 0.05);
                step_probability_7.put(2, 0.1);
                step_probability_7.put(-3, 0.1);
                step_probability_7.put(5, 0.1);
                step_probability_7.put(-5, 0.1);
                step_probability_7.put(7, 0.1);
                step_probability_7.put(-7, 0.1);
                step_probability_7.put(-8, 0.1);
                step_probability_7.put(9, 0.1);
                step_probability_7.put(12, 0.03);
                step_probability_7.put(-12, 0.04);
};
    private static final Map<Integer, Double> step_probability_8;
	static {
            step_probability_8 = new HashMap<>();
            step_probability_8.put(-1, 1.00);
        };
private static final Map<Integer, Double> step_probability_9;
static {
	step_probability_9 = new HashMap<>();
	step_probability_9.put(-2, 0.25);
        step_probability_9.put(3, 0.2);
        step_probability_9.put(-3, 0.2);
        step_probability_9.put(2, 0.25);
	step_probability_9.put(5, 0.07);
	step_probability_9.put(-5, 0.03);
};
    private static final Map<Integer, Double> step_probability_10;
	static {
            step_probability_10 = new HashMap<>();
            step_probability_10.put(2, 0.50);
            step_probability_10.put(-3, 0.50);
        };
private static final Map<Integer, Double> step_probability_11;
static {
	step_probability_11 = new HashMap<>();
	step_probability_11.put(1, 0.25);
	step_probability_11.put(-2, 0.25);
	step_probability_11.put(3, 0.25);
	step_probability_11.put(-4, 0.25);
};

    
    
    
    
    
    
    
    
    @Override
    public ArrayList<Integer> getFirstNotePitchCandidates(int input_range_min, int input_range_max, int key_transpose ) {
    System.out.println("starting get first Note Pitch Candidates with key transpose" + key_transpose);
    Integer[] pitch_classes = {key_transpose + 0, key_transpose + 4,  key_transpose + 7 };
    ArrayList<Integer> pitch_candidates = new ArrayList();
        System.out.println("starting transposition");
        for (Integer pitch_candidate : pitch_classes) {
            while (pitch_candidate < input_range_min + 8 || pitch_candidate > input_range_max) {
                pitch_candidate +=  12;
            }
            pitch_candidates.add(pitch_candidate);
        }
        System.out.println("finish transposition");
        return pitch_candidates; 
    }
  
    @Override
    public Integer getPitchCenter (int input_range_min, int input_range_max ){
      Integer[] pitch_classes = {0,  4,  7 };
      Integer pitch_candidate = pitch_classes[roll.nextInt(pitch_classes.length)];
      while (pitch_candidate < input_range_min + 8 && pitch_candidate < input_range_max) {
         pitch_candidate +=  12;
      }
      return pitch_candidate;
    }
    
    @Override
    public ArrayList<Integer> getPitchCandidates(int input_previous_pitch, int key_transpose){
        
        ArrayList<Integer> pitch_step_candidates = new ArrayList();
        ArrayList<Integer> pitch_candidates = new ArrayList();
        Integer previous_pitch_class = (input_previous_pitch % 12) - key_transpose;
        if (previous_pitch_class < 0) previous_pitch_class += 12;
        System.out.println("getting pitch candidates with key transpose " + key_transpose + " and note approached from " + previous_pitch_class);
        switch (previous_pitch_class) {
            case ( 0 ): 
                pitch_step_candidates.add(0);
                pitch_step_candidates.add(2);
                pitch_step_candidates.add(-1);
                pitch_step_candidates.add(-3);
                pitch_step_candidates.add(4);
                pitch_step_candidates.add(-5);
                pitch_step_candidates.add(7);
                pitch_step_candidates.add(-8);
                pitch_step_candidates.add(9);
                pitch_step_candidates.add(12);
                pitch_step_candidates.add(-12);
                break;
            case ( 1 ):
                pitch_step_candidates.add(-1);
                break;
            case ( 2 ): 
                pitch_step_candidates.add(-2);
                pitch_step_candidates.add(2);
                pitch_step_candidates.add(-3);
                pitch_step_candidates.add(5);
                pitch_step_candidates.add(7);
                break;
            case ( 3 ):
                pitch_step_candidates.add(1);
                pitch_step_candidates.add(-3);
                break;
            case ( 4 ): 
                // 0 1 -2 3 -4 12
                pitch_step_candidates.add(0);
                pitch_step_candidates.add(2);
                pitch_step_candidates.add(-2);
                pitch_step_candidates.add(3);
                pitch_step_candidates.add(-4);
                pitch_step_candidates.add(5);
                pitch_step_candidates.add(-7);
                pitch_step_candidates.add(8);
                pitch_step_candidates.add(-9);
                break; 
            case ( 5 ):
                pitch_step_candidates.add(-1);
                pitch_step_candidates.add(2);
                break;
            case ( 6 ):
                pitch_step_candidates.add(1);
                pitch_step_candidates.add(-2);
                break;
            case ( 7 ): 
                // 0 2 -2 3 -3 5 -5 7 12
                pitch_step_candidates.add(0);
                pitch_step_candidates.add(2);
                pitch_step_candidates.add(-1);
                pitch_step_candidates.add(-3);
                pitch_step_candidates.add(5);
                pitch_step_candidates.add(-5);
                pitch_step_candidates.add(7);
                pitch_step_candidates.add(12);
                pitch_step_candidates.add(-12);
                pitch_step_candidates.add(9);
                pitch_step_candidates.add(-7);
                pitch_step_candidates.add(-8);
                break;
            case ( 8 ):
                pitch_step_candidates.add(-1);
                break;
            case ( 9 ): 
                // 0 1 2 -2 -4 5 -5 -7 12
                pitch_step_candidates.add(2);
                pitch_step_candidates.add(3);
                pitch_step_candidates.add(-2);
                pitch_step_candidates.add(-3);
                pitch_step_candidates.add(5);
                pitch_step_candidates.add(-5);
                break;
            case ( 10 ):
                pitch_step_candidates.add(2);
                pitch_step_candidates.add(-3);
                break;
            case (11) : 
                // -1 -3
                pitch_step_candidates.add(1);
                pitch_step_candidates.add(-2);
                pitch_step_candidates.add(3);
                pitch_step_candidates.add(-4);
                break;
        }
        
        for (Integer pitch_step_candidate : pitch_step_candidates) {
            pitch_candidates.add(pitch_step_candidate + input_previous_pitch);
        }
        
        return pitch_candidates;
    }
    
    public Double getMelodicMotionProbability (Integer input_current_pitch_cand, Integer input_previous_pitch, Integer key_transpose) {
        input_previous_pitch = input_previous_pitch - key_transpose;
        if (input_previous_pitch < 0) input_previous_pitch += 12;
        Integer difference = input_current_pitch_cand - input_previous_pitch;
        Double motion_probability = 0.00;
        Integer p_class = input_previous_pitch % 12;
        System.out.println("getting probability for " + difference + "steps from " + p_class);
        switch ( p_class ){
            case (0):
                motion_probability = step_probability_0.get(difference);
                System.out.println(step_probability_0.get(difference));
                break;
            case (1):
                motion_probability = step_probability_1.get(difference);
                System.out.println(step_probability_1.get(difference));
                break;
            case (2):
                motion_probability = step_probability_2.get(difference);
                System.out.println(step_probability_2.get(difference));
                break;
            case (3):
                motion_probability = step_probability_3.get(difference);
                System.out.println(step_probability_3.get(difference));
                break;
            case (4):
                motion_probability = step_probability_4.get(difference);
                System.out.println(step_probability_4.get(difference));
                break;
            case (5):
                motion_probability = step_probability_5.get(difference);
                System.out.println(step_probability_5.get(difference));
                break;
            case (6):
                motion_probability = step_probability_6.get(difference);
                System.out.println(step_probability_6.get(difference));
                break;
            case (7):
                motion_probability = step_probability_7.get(difference);
                System.out.println(step_probability_7.get(difference));
                break;
            case (8):
                motion_probability = step_probability_8.get(difference);
                System.out.println(step_probability_8.get(difference));
                break;
            case (9):
                motion_probability = step_probability_9.get(difference);
                System.out.println(step_probability_9.get(difference));
                break;
            case (10):
                motion_probability = step_probability_10.get(difference);
                System.out.println(step_probability_10.get(difference));
                break;
            case (11):
                motion_probability = step_probability_11.get(difference);
                System.out.println(step_probability_11.get(difference));
                break;
        }
        return motion_probability;
    }
    
    public Integer getTonic() {
        return tonic;
    }
    
    public String mytoString() {
        return "C Lydian";
    }
}
