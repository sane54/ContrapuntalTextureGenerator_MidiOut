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

import java.util.concurrent.atomic.AtomicBoolean;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import Hindemith.RhythmModule.*;
import Hindemith.ModeModules.*;
import static java.lang.Math.abs;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Objects;
import java.util.logging.Logger; //the Java Logger
import java.util.Random;
import org.jfugue.*;




/**
 * This is the class that creates counterpoint from rhythm patterns and melodic
 * rules. It is always called in a thread so that it may be canceled by the 
 * user. 
 * @author Trick's Music Boxes
 */
public class Model_1 {
    public Worker worker;
    public AtomicBoolean shouldThrow = new AtomicBoolean(false);
        
    static ArrayList<MelodicVoice> unbuilt_voices = new ArrayList();
    static ArrayList<MelodicVoice> built_voices = new ArrayList();
    static ArrayList<MotionCount> motion_counts = new ArrayList();
    static ArrayList<PitchCount>pitch_counts = new ArrayList();
    static int same_consonant_threshold = 6;
    static Random roll = new Random();
    static int sample_size = 5;
    private static final Logger logger = Logger.getLogger("org.jfugue");
    static int trough = 0;
    static int trough_count = 0;
    static int peak = 0;
    static int peak_count = 0;
    static int same_consonant_count = 0;
    static int root_key = 0;
    static int key_transpose = 0;
    static MelodicVoice harmonic_prog = new MelodicVoice();
    static Boolean harmonic_prog_built = false;
    static MelodicNote this_key = new MelodicNote();
    private static boolean stopper = false;

    public  Model_1() {
        worker = new Task<String>() {
     /**
     * The call method here provides the flow of the music composition
     * It runs the user-specified rhythm module to get a collection of 
     * patterns, each of which consists solely of note durations. It then runs
     * AccentListener to determine the accent of each note based on rhythmic 
     * placement. Now we have a collection of note durations with accents. Now, 
     * each pattern will be sent to BuildVoicePitches which assigns pitches 
     * based on melodic rules but also on harmonic rules and the pitch content of
     * the previously built voices. Now we have a collection of patterns of 
     * pitches plus durations. These however must now be converted into JFugue 
     * notes so that we can leverage JFugue's capabilities for playing and saving
     * these note patterns as MIDI. When this is done, the patterns are merged
     * into one large JFugue pattern, each pattern now a voice in the 
     * larger pattern. This pattern is stored in the PatternStorerSaver1 class
     * as a static variable. 
     */
        @Override
        protected String call() throws Exception {
            resetParams();
            int piece_length = InputParameters.piece_length;
            int tempo_bpm = InputParameters.getTempo();
            //DEBUG
            //System.out.println("tempo =  " + tempo_bpm);
            ModeModule my_mode_module = InputParameters.my_mode_module;
            String [] voice_array = InputParameters.voice_array; 
            RhythmModule james = InputParameters.james;       
            int number_of_voices = voice_array.length;
            Random my_roll = new Random();
            double completedWorkSteps = 0;
            double totalWorkSteps = number_of_voices + 4;
            
            updateProgress(completedWorkSteps, totalWorkSteps);
            updateMessage("Starting...now building rhythm patterns");
            
            //get rhythm patterns
            Pattern [] rhythm_patterns = james.generate(piece_length, number_of_voices);
            
            if (isCancelled()) {
                resetParams();
                return "cancelled";
            }
            
            //build blank Melodic Voice for each rhythm pattern
            for (int i = 0; i < number_of_voices; i++) {
                MelodicVoice this_voice = new MelodicVoice();
                this_voice.setRange(voice_array[i]);
                AccentListener my_accent_listener = new AccentListener();
                this_voice.setNoteArray(my_accent_listener.listen(rhythm_patterns[i]));
                //DEBUG
                //System.out.println("adding voice " + i + " to unbuiltvoices");
                unbuilt_voices.add(this_voice);
            }
            if (isCancelled()) {
                resetParams();
                return "cancelled";
            }
            completedWorkSteps++;
            updateProgress(completedWorkSteps, totalWorkSteps);
            updateMessage("Completed rhythm patterns ... now adding pitches");
            

            int ub_size =unbuilt_voices.size();
            for (int i = 0; i < number_of_voices; i++){ 
                //build contrapuntal voice
                
                if (isCancelled()) {
                    resetParams();
                    return "cancelled";
                }
                
                //run buildVoicePitches method on next unbuilt voice
                int voice_index = i;
                //DEBUG
                //System.out.println(" about to build voice pitches for "+ voice_index);
                MelodicVoice nextVoice = buildVoicePitches(unbuilt_voices.get(voice_index), number_of_voices, my_mode_module);
                
                if (isCancelled()) {
                    resetParams();
                    return "cancelled";
                }
                
                //Printing voice strings to Standard Out
                ArrayList<MelodicNote> verify_array = nextVoice.getNoteArray();
                //DEBUGS
                //System.out.println("Return Me: ");
                //for(MelodicNote verify: verify_array) { 
                //  if (verify.getRest()) System.out.println("rest " + verify.getDuration() + "  " );
                //    else System.out.println(verify.getPitch() + " " + verify.getDuration() + "   ");
                //  }
                
                if (i == 0) {
                    harmonic_prog = nextVoice;
                    harmonic_prog_built = true;
                }
                else built_voices.add(nextVoice);
                
                if (isCancelled()) {
                    resetParams();
                    return "cancelled";
                }
                
                completedWorkSteps++;
                updateProgress(completedWorkSteps, totalWorkSteps);
                if (i ==0) updateMessage("Finished building harmonic progression....now building voice 1");
                else updateMessage("Finished contrapuntal voice " + i + "... now working on voice " + (i+1));
                
            }// end build contrapuntal voice
            
            if (isCancelled()) {
                resetParams();
                return "cancelled";
            }
            completedWorkSteps++;
            updateProgress(completedWorkSteps, totalWorkSteps);
            updateMessage("Finished building voices... now creating final midi sequence");
            

            unbuilt_voices.clear();
            Pattern music_output = new Pattern();
            music_output.addElement(new Tempo (tempo_bpm));
        
            for (byte i = 0; i < built_voices.size(); i++){
                // create a jfugue musicstring from the built voice
                MelodicVoice final_voice = built_voices.get(i); //get melodic voice from built_voices
                ArrayList<MelodicNote> final_note_array = final_voice.getNoteArray(); // get Melodic note array from the Melodic voice
                Voice jf_voice = new Voice(i); 
                music_output.addElement(jf_voice);
                for (MelodicNote final_note : final_note_array) {
                    //create jfugue note from MelodicNote
                    int jf_int = 0;
                    Note jf_note = new Note();
                    jf_note.setDecimalDuration(final_note.getDuration());
                    if (final_note.getPitch() != null && final_note.getRest() == false ) {
                        jf_int = final_note.getPitch();
                        byte jf_note_byte = (byte)jf_int;
                        jf_note.setValue(jf_note_byte);
                        if (!final_note.getAccent()) jf_note.setAttackVelocity((byte)40);
                    }
                    else {
                        //DEBUG
                        //System.out.println("setting jf note to rest");
                        jf_note.setRest(true);
                        jf_note.setAttackVelocity((byte)0);
                        jf_note.setDecayVelocity((byte)0);
                    }
                    //add the jfugue note to jfugue voice
                    music_output.addElement(jf_note);
                } // end create a jfugue musicstring from the built voice loop

                if (isCancelled()) {
                    resetParams();
                    return "cancelled";
                }
                music_output.add(" Rh"); //so midi doesn't hang
            }// end create a jfugue musicstring from the built voice loop
            
            if (isCancelled()) {
                resetParams();
                return "cancelled";
            }
            completedWorkSteps++;
            updateProgress(completedWorkSteps, totalWorkSteps);
            updateMessage("MIDI sequence built.... ");
            
            //save and play the pattern as a MIDI file
            //DEBUG
            //System.out.println(music_output.getMusicString());
            if (Hindemith.InputParameters.get_q_mode()) PatternQueueStorerSaver.add_pattern_to_queue(music_output);
            else PatternStorerSaver1.add_pattern(music_output);
            //all done
            completedWorkSteps++;
            updateProgress(completedWorkSteps, totalWorkSteps);
            resetParams();
            return "done";
        }
    };
        
    }
    /**
     * Assigns pitches to a melodic voice without pitches. Contains the main
     * logic of abstract counterpoint algorithm. The algorithm proceeds note by 
     * note. Candidates for each note's  pitch are returned from the mode module. 
     * are evaluated harmonically and melodically against pitches in notes 
     * which occur simultaneously in previously built melodic voices, and 
     * against pitches already assigned in this melodic voice. Each candidate is
     * decremented when it violates a contrapuntal practice in terms of harmony 
     * or of melody. The candidate with the least decremented value is chosen
     * for the note. The algorithm then moves to the next note in the melodic 
     * voice.
     * 
     * @param alter_me a melodic voice containing note durations without pitch
     * @param number_of_voices 
     * @param my_mode_module the mode module specified by the user
     * @return a melodic voice with pitches assigned to the note durations
     */
    public static MelodicVoice buildVoicePitches (MelodicVoice alter_me, int number_of_voices, ModeModule my_mode_module){
        MelodicVoice return_me = new MelodicVoice();
        ArrayList<LinkedList> built_voice_queues = new ArrayList();
        int previous_cp_pitch = -13;//null value for comparisons
        int previous_melodic_interval = 0;
        trough = 200;
        trough_count = 0;
        peak = 0;
        peak_count = 0;
        same_consonant_count = 0;
        int voice_pitch_count = 0;
        LinkedList <MelodicNote> chord_prog_stack = new LinkedList<>();
        
        //DEBUG
        //System.out.println("voiceRange min " + alter_me.getRangeMin() + "   voicerange max " + alter_me.getRangeMax());
        Integer pitch_center = my_mode_module.getPitchCenter(alter_me.getRangeMin(), alter_me.getRangeMax());
        //DEBUG
        //System.out.println("pitchcenter = " + pitch_center);
		//Build Chord Progression
        if (harmonic_prog_built) {
            ArrayList <MelodicNote> chord_prog_temp = harmonic_prog.getNoteArray();
            for (MelodicNote b_voice_note : chord_prog_temp){
                if (stopper) {
                    //System.out.println("Stopper = " + stopper);
                    return null;
                }
                chord_prog_stack.add(b_voice_note);
            }  
        }
   
        if (stopper) {
            return null;
        }
		//Create stack of previously built voices        
        if (!built_voices.isEmpty())
        for (MelodicVoice built_voice : built_voices) {
            if (stopper) {
                return null;
            }
            LinkedList <MelodicNote> cf_stack = new LinkedList<>();
            ArrayList <MelodicNote> temp = built_voice.getNoteArray();
            for (MelodicNote b_voice_note : temp){
                cf_stack.add(b_voice_note);
            }
            built_voice_queues.add(cf_stack);
            //DEBUG
            //System.out.println("created stack of melodic notes for each previously built voice ");
        }
        else {
            //DEBUG
            //System.out.println("built voices Empty - start first melody");
        }
        if (stopper) {
            return null;
        }
        
        ArrayList <MelodicNote> current_cf = new ArrayList();
        MelodicNote[] holdover_cf = new MelodicNote[ built_voice_queues.size()];
        Integer[] previous_cf_pitch = new Integer[built_voice_queues.size()];
        for(int h = 0; h<built_voice_queues.size(); h++)
             previous_cf_pitch[h] = 1111;
        ArrayList <MelodicNote> pending_rests = new ArrayList();
        Integer current_cp_index = -13; //-13 is our null value for comparisons
        ArrayList<Integer> pitch_candidate_values = new ArrayList();
        ArrayList<PitchCandidate> pitch_candidates = new ArrayList();

        MelodicNote this_cf = null;
        Integer melodic_prev_cp = 0;

		//For each note in CP voice
        for (int i = 0; i < alter_me.getVoiceLength(); i++){ //for each melodic note in the CP voice
            if (stopper) return null;
            MelodicNote CP_note = alter_me.getMelodicNote(i);
			//If it isn't a rest
            if (!CP_note.getRest()) {
            //if our new note isn't a rest, this signals we now have terminated the duration of 
            //previous note and have finished evaluating it harmonically against all chord
            //changes and notes in previously built voices. We now can evaluate its
            //pitch candidates melodically and pick a winner. 
                if (current_cp_index >=0) { //ie if current_cp_index isn't -13 the null value, ie for the very first note skip this section 
                    if(previous_cp_pitch == 9999) previous_cp_pitch = melodic_prev_cp; 
                    //DEBUG
                    //System.out.println("starting melody check on current cp"+ current_cp_index +" of " + alter_me.getVoiceLength());
                    //Getting accent value of previous note 
                    //Here current_cp_index means index of previous note
                    Boolean got_accent = alter_me.getMelodicNote(current_cp_index).getAccent();
                    //DEBUG
                    //System.out.println("the current cp note's accent value is " + got_accent);


                    //MELODICALLY EVALUATE Pitch Candiates for Previous Note - Choose CP WinnerS - Previous CP = CP Winner
                    pitch_candidates = melodicCheck(pitch_candidates, my_mode_module, alter_me, 
                                 pitch_center, voice_pitch_count, previous_cp_pitch,  previous_melodic_interval, got_accent, harmonic_prog_built);
                    if (stopper) return null;
                    Integer cp_winner = pickWinner(pitch_candidates);
					//re-set variables and put pitch-assigned note in new array
                    pitch_candidates.clear();
                    //DEBUG
                    //System.out.println("CP winner" + cp_winner + " to note " + current_cp_index);
                    MelodicNote current_cp = alter_me.getMelodicNote(current_cp_index);
                    current_cp.setPitch(cp_winner);
                    return_me.addMelodicNote(current_cp);
                    if(!pending_rests.isEmpty())
                        for (MelodicNote my_rest: pending_rests ){
                            return_me.addMelodicNote(my_rest);
                        }
                    pending_rests.clear();
					//If there is a previous CP Pitch
                    if (previous_cp_pitch != -13) { //s andie its not null
						//Calculating Peaks and Trough note counts                        
                        if (voice_pitch_count> 1){ // lets make sure we have a peak/trough
                            if (previous_melodic_interval < 0 && cp_winner - previous_cp_pitch  > 0 ) {// will there be a change in direction from - to +  ie trough?
                                if (previous_cp_pitch == trough) {
                                    trough_count++;

                                }
                                else if (previous_cp_pitch < trough)  { //used to be != trough ... why??

                                    trough = previous_cp_pitch;
                                    trough_count = 1;
                                    //DEBUG
                                    //System.out.println("setting new trough = " + previous_cp_pitch);

                                }
                            }

                            if (previous_melodic_interval > 0 && cp_winner - previous_cp_pitch  < 0 ) {// will there be a change in direction from - to +  ie trough?
                                if (previous_cp_pitch == peak) peak_count++;
                                else{
                                    if (previous_cp_pitch > peak) {
                                    peak = previous_cp_pitch;
                                    peak_count = 1;
                                    //DEBUG
                                    //System.out.println("setting new peak = " + previous_cp_pitch);    
                                    }

                                }
                            }    
                        }
						// Calculate new previous_melodic interval
                        previous_melodic_interval = cp_winner - previous_cp_pitch;
                        //DEBUG
                        //System.out.println("previous melodic interval = " + previous_melodic_interval);
						// Calculate pitch counts and motion counts based on previous CP Pitch and new CP winner
                        boolean add_pitch = true;
                        for(int pc = 0; pc < pitch_counts.size(); pc++) {
                            if (pitch_counts.get(pc).getPitch() == previous_cp_pitch%12) {
                                pitch_counts.get(pc).incrementCount();
                                add_pitch = false;
                                break;
                            }        
                        }
                        if (add_pitch == true){
                        PitchCount my_pitch_count = new PitchCount(previous_cp_pitch %12);
                        my_pitch_count.incrementCount();
                        pitch_counts.add(my_pitch_count);
                        }


                        boolean add_motn = true;
                        for(int mc = 0; mc< motion_counts.size(); mc++){
                            //DEBUG
                            //System.out.println("in 'motion counts ' found motion count " + motion_counts.get(mc).getPreviousPitch() + " / " + motion_counts.get(mc).getSucceedingPitch());
                            if (motion_counts.get(mc).getPreviousPitch() == previous_cp_pitch %12 && motion_counts.get(mc).getSucceedingPitch() == cp_winner %12) {
                                //DEBUG
                                //System.out.println("Motion count from " + previous_cp_pitch %12 + " to " + previous_cp_pitch %12 + " FOUND");
                                motion_counts.get(mc).incrementCount();
                                add_motn = false;
                            }   
                        }
                        if(add_motn == true) {
                            //DEBUG
                            //System.out.println("Motion count from " + previous_cp_pitch %12 + " to " + previous_cp_pitch %12 + " not found. Adding");
                            MotionCount my_motionCount = new MotionCount(previous_cp_pitch %12, cp_winner %12);
                            motion_counts.add(my_motionCount);
                        }
                    }

				//Reassign Variables
                    previous_cp_pitch = cp_winner;
                    melodic_prev_cp = cp_winner;
                    voice_pitch_count++;    
                }
				
				//Now Start Pitch Assignment of the actual new note
				//DEBUG
                //System.out.println("************************************************************************");
                //System.out.println("assigning pitch to note in position " + i +" of " + alter_me.getVoiceLength() + " with start time " + CP_note.getStartTime() + " and end time " + CP_note.getPreviousDuration());
                
				if (stopper) return null;
				
				//Compute Key Transpose
                if (harmonic_prog_built) {
                      Double prog_start_time;
                      do {
                        MelodicNote prog_stack_note = (MelodicNote)chord_prog_stack.pop();
                        prog_start_time = prog_stack_note.getStartTime();
                        if (!prog_stack_note.getRest()) {
                            this_key.setPitch(prog_stack_note.getPitch());
                        }
                        //System.out.println("key transpose: " + this_key.getPitch()%12);
                      } while (prog_start_time< CP_note.getStartTime());
                      key_transpose = this_key.getPitch()%12;
                }
				
				// Get Pitch Candidates for the CP note given the voice range and key transpose
                if (stopper) return null;
                //Debug
                //System.out.println("getting pitch candidates from my modemodule based on key " + key_transpose);
                if(voice_pitch_count == 0) { // If there is no previous pitch ie this is the first note 
                    pitch_candidate_values = my_mode_module.getFirstNotePitchCandidates(alter_me.getRangeMin(), alter_me.getRangeMax(), key_transpose);
                    //DEBUG
                    //System.out.println("using first note pitch candidates");
                }
                else {
                    pitch_candidate_values = my_mode_module.getPitchCandidates(previous_cp_pitch, key_transpose);
                    //DEBUG
                    //if (pitch_candidate_values.isEmpty()) System.out.println("EMPTY ARRAY!!!!");
                }
                //DEBUG
                //System.out.println("voice_pitch_count" + voice_pitch_count);
                //System.out.print("pitch candidates: ");
                if (stopper) return null;
                for (Integer pitch_candidate_value : pitch_candidate_values) {
                    if (stopper) return null;
                    //DEBUG
                    //System.out.print(pitch_candidate_value + " ");
                    PitchCandidate myPC = new PitchCandidate();
                    myPC.setPitch(pitch_candidate_value);
                    pitch_candidates.add(myPC);
                }
				//Reset Current CP index
                current_cp_index = i;
            }
			//If note is a Rest ie you've just skipped most of the above....      
            else {
                //DEBUG
                //("note in position " + i +" is rest ");
                if (current_cp_index == -13) {
                    return_me.addMelodicNote(CP_note);
                    //DEBUG
                    //System.out.println("at beginning of voice");
                    //System.out.println("*****************************************************");
                    continue;
                }
                else {
                    pending_rests.add(CP_note);
                    //DEBUG
                    //System.out.println("adding rest to pending rests for note in position " + current_cp_index);
                }
            }
			
            if (stopper) return null;
			
			/*
				Regardless if note or rest run HARMONIC CHECKS on the current cp
				If a rest, then we are still deciding the counterpoint for the 
				immediately previous place in the melody. 

				If this is the very first voice, then there are no prebuilt voices 
				so skip this section        
			*/        
            if (!built_voice_queues.isEmpty()){        
                for (int b = 0; b < built_voice_queues.size(); b++){
                    //System.out.println("Starting harmonic analysis of pitch candidates against contemoraneous pitches in voice "+ b);
                    if (stopper) return null;    
                    do {
                        if (stopper) return null;
                        boolean CF_root = false;
                        if (b == 0) CF_root = true;
                        boolean skip_me = false;
                        if (!built_voice_queues.get(b).isEmpty()){
                            //System.out.println("Getting simulataneous pitches in voice " + b);
                                if (holdover_cf[b] != null) {
                                    this_cf = holdover_cf[b];
                                    //System.out.println("cf pitch " + this_cf.getPitch() + " with start time " + this_cf.getStartTime() + " and end time " + this_cf.getPreviousDuration() +" is held over into this CP note");
                                }
                                else {
                                    this_cf = (MelodicNote)built_voice_queues.get(b).pop();
                                    //System.out.println("cf pitch " + this_cf.getPitch() + " with start time " + this_cf.getStartTime() + " and end time " + this_cf.getPreviousDuration() +" is popped from voice " + b );
                                }
                                //DEBUGS
                                //System.out.println("CF pitch = " + this_cf.getPitch());
                                //System.out.println(" rest = " + this_cf.getRest());
                                //System.out.println(" duration up to  = " + this_cf.getPreviousDuration());
                        }

                        else {
                                //DEBUG
                                //System.out.println("cf voice " + b + " is empty");
                                break;
                        }
                        if (this_cf.getRest()) {
                                //System.out.println("This cf is a rest of " + this_cf.getDuration());
                                if (this_cf.getDuration() > .5 || //if the cf rest is longer than a half note the power of the preceding note doesn't carry
                                        previous_cf_pitch[b] == 1111 || //if there is no previous cf pitch ie we are at beginning of voice
                                         CP_note.getRest()) skip_me = true; // if the CP note and CF note both are rests
                        }

                        if (!skip_me){
                                        //HARMONICALLY EVALUATE cp candidates against this_cf
                                        //DEBUG
                                        //System.out.println("previous cf pitch = "+previous_cf_pitch[b]);
                                        if (previous_cf_pitch[b]!=1111)
                                            pitch_candidates = harmonicChecks(pitch_candidates, this_cf, CF_root, previous_cf_pitch[b],
                                                                previous_cp_pitch, CP_note, voice_pitch_count);
                                        else pitch_candidates = harmonicChecksSuperBasic(pitch_candidates, this_cf, CF_root, alter_me.getMelodicNote(current_cp_index));
                                       previous_cp_pitch =9999; // 9999 is assigned in case CP is held over into next CF note
                        }                               //in which case the while loop repeats. When you break out
                                                                //of while loop previous_cp_pitch will be checked and re-assigned above
                        else {
                                        //DEBUG
                                        //System.out.println("current cf index" + b + "is null");
                                        //System.out.println("skip me was true skip harmonic checks ");
                                        //break;
                        }
                        if (this_cf.getPreviousDuration() > CP_note.getPreviousDuration())  //if cf note extends into next CP note
                                        holdover_cf[b] =  this_cf; //reassign holdover note in cf voice, you'll break out of while loop after this
                        else holdover_cf[b] = null; //note it doesn't matter if holdover is rest or pitched. 
                        if (!this_cf.getRest()) previous_cf_pitch[b] = this_cf.getPitch();//if the cf isn't a rest its the new previous aka "current" cf pitch
                    } while (this_cf.getPreviousDuration()< CP_note.getPreviousDuration()); //loop while CP note extends into next CF in voice
                }
            }

			//If this is the last note, you now have to evaluate it melodically       
            if (stopper) return null;
            if (i == alter_me.getVoiceLength()-1) {
                //DEBUG
                //System.out.println("Last note of voice");
                if(previous_cp_pitch == 9999) previous_cp_pitch = melodic_prev_cp;
                pitch_candidates = melodicCheck(pitch_candidates, my_mode_module, alter_me, 
                    pitch_center, voice_pitch_count, previous_cp_pitch,  previous_melodic_interval, true, harmonic_prog_built);
                Integer cp_winner = pickWinner(pitch_candidates);
                //DEBUG
                //System.out.println("Last note's pc winner" + cp_winner + " to note " + current_cp_index + " out of " + alter_me.getVoiceLength());
                pitch_candidates.clear();    
				if (!CP_note.getRest()) {	
                    CP_note.setPitch(cp_winner);
                    return_me.addMelodicNote(CP_note); 
                }
				else {
                    MelodicNote current_cp = alter_me.getMelodicNote(current_cp_index);
                    current_cp.setPitch(cp_winner);
                    return_me.addMelodicNote(current_cp);
                    if(!pending_rests.isEmpty())
                        for (MelodicNote my_rest: pending_rests ){
                            return_me.addMelodicNote(my_rest);
                        }
                    pending_rests.clear(); 
                }    
            }
        }//loop through next CP note 
		return return_me;

    } //end method buildVoicePitches
    
    public static class PitchCandidate {
        Integer rank = 0;
        Integer pitch = 0;
        
        public Integer getRank (){
        return rank;
        }
        
        public void decrementRank(int decrement){
            rank -= decrement;
        }
        
        public void setPitch (Integer input_pitch){
            pitch = input_pitch;
        }
        
        public Integer getPitch (){
            return pitch;
        }
    }
    
    public static class MotionCount {
        
       private final int previous_pitch;
       private final int succeeding_pitch;
       private int count;

        
       public int getCount() {
       return count;
       }
       
       public int getPreviousPitch() {
       return previous_pitch;
       }
       
       public int getSucceedingPitch() {
       return succeeding_pitch;
       }
       
       public void incrementCount(){
           count++;
       }
               
       private MotionCount (int input_prev_pitch, int input_succ_pitch) {
           this.previous_pitch = input_prev_pitch;
           this.succeeding_pitch = input_succ_pitch;
           this.count = 1;
       }
    }
    
    public static class PitchCount {
        private final int the_pitch;
        private int count;
       
        private PitchCount(int input_the_pitch){
            this.the_pitch = input_the_pitch;
        }
        
        public void incrementCount(){
           count++;
        }
        
        public int getPitch() {
        return the_pitch;
        }
        
        public int getCount() {
        return count;
        }
    }
    /**
     * Given a set of possible candidates for a note's pitch along with key,
     * surrounding notes, and state of the melodic curve, determine decrements
     * for each candidate. 
     * @param pitch_candidates
     * @param my_mode_module
     * @param alter_me
     * @param pitch_center
     * @param voice_pitch_count
     * @param previous_cp_pitch
     * @param previous_melodic_interval
     * @param is_accent
     * @return 
     */
    public static ArrayList<PitchCandidate>  melodicCheck(ArrayList<PitchCandidate> pitch_candidates, ModeModule my_mode_module, MelodicVoice alter_me, 
        Integer pitch_center, int voice_pitch_count, Integer previous_cp_pitch, Integer previous_melodic_interval, Boolean is_accent, Boolean prog_built) {
        Boolean large_dissonance_bad = InputParameters.large_dissonance_bad;
        Integer [] consonances = InputParameters.consonances;
        Integer [] perfect_consonances = InputParameters.perfect_consonances;
        Integer [] root_consonances = InputParameters.root_consonances;
        
        for (PitchCandidate myPC : pitch_candidates){
            int cand_pitch = myPC.getPitch();
            int melody_motion_to_cand = 0;
            
            //DEBUG
            //("melodicCheck evaluating pitch candidate " + cand_pitch);
            
            //Check if Dissonant with Root - Does not apply for harmonic prog
            if (prog_built) {
                boolean root_interval_consonant = false;
                int root_interval = abs(cand_pitch%12 - key_transpose);
                for (Integer consonance : root_consonances) {
                    if (root_interval == consonance) root_interval_consonant = true;
                }
                if(root_interval_consonant) {
                    //DEBUG
                    //System.out.println(cand_pitch + " consonant with root " + key_transpose );
                }
                else {
                    if (is_accent ) {
                        myPC.decrementRank(Decrements.dissonant_with_root);
                        //DEBUG
                        //System.out.println(cand_pitch + " dissonant accent with root " + key_transpose);
                    }
                    else {
                        //DEBUG
                        //System.out.println("dissonant with root but note not accented" );
                    }
                }                 
            }

            //randomly decrement non-tonics
            if (cand_pitch%12 != my_mode_module.getTonic() && roll.nextInt(2) == 1){
                myPC.decrementRank(Decrements.is_not_tonic);
                //DEBUG
                //System.out.println(cand_pitch + " is not tonic");
            }
            
            //decrement illegal notes
            if(cand_pitch <0 || cand_pitch > 127) {
                myPC.decrementRank(Decrements.illegal_note);
                //DEBUG
                //System.out.println(cand_pitch + " is illegal note");                
            }

            //decrement motion outside of voice range                
            if (cand_pitch < alter_me.getRangeMin() || cand_pitch > alter_me.getRangeMax()) {
                myPC.decrementRank(Decrements.outside_range);
                //DEBUG
                //System.out.println(cand_pitch + " outside range " + alter_me.getRangeMin() + "-" + alter_me.getRangeMax());                
            }

            //decrement too far from pitch center
            if (abs(cand_pitch - pitch_center) > 16) {
                 myPC.decrementRank(Decrements.remote_from_pitchcenter);
                 //DEBUG
                 //System.out.println(cand_pitch + " too far from pitch center" + pitch_center);               
            } 

            if (voice_pitch_count > 0) {
                melody_motion_to_cand = cand_pitch - previous_cp_pitch;
            
            
                //Check if The candidate has already followed the preceding pitch too often.                
                //look for previous_cp_pitch in PitchCount
                //if it's there get how many times it's appeared in the melody
                // if the count is greater than samplesize threshold
                //check if there are previous_cp_pitch to pitch_candidate motions in MOtion Counts
                //if so get the motion count - then divide motion count by pitch count
                // get the percentage of motions from mode module
                //if actual count is greater than mode module percentage decrement
				
                double thresh = 0;
				Double threshornull = my_mode_module.getMelodicMotionProbability(cand_pitch, previous_cp_pitch, key_transpose);
				if (threshornull == null){
                    //DEBUG
                    //System.out.println("From mode module motion probability of " + previous_cp_pitch %12 +" to " + cand_pitch%12 + " is NULL");
                    myPC.decrementRank(Decrements.melodic_motion_quota_exceed);
                }
                else {
                    thresh = threshornull;
                    //DEBUG
                    //System.out.println("From mode module, motion probability of " + previous_cp_pitch%12 +" to " + cand_pitch%12 + " = " + thresh  );
                }
                for (PitchCount my_pitch_count: pitch_counts) {
                    if(my_pitch_count.getPitch() == previous_cp_pitch%12)
						//DEBUG
                        //System.out.println("found preceding cp pitch " + previous_cp_pitch%12 +" in pitch counts with count " + my_pitch_count.getCount());
                        if(my_pitch_count.getCount() > sample_size)     
                        for (MotionCount my_motion_count: motion_counts){
                            //DEBUG
                            // System.out.println("pitch_count for " + previous_cp_pitch %12 + " = " + my_pitch_count.getCount());
                            //System.out.println("motion count for " + my_motion_count.getPreviousPitch() + "/" + my_motion_count.getSucceedingPitch() + "="+ my_motion_count.getCount());
                            if (my_motion_count.getPreviousPitch()== previous_cp_pitch %12 && my_motion_count.getSucceedingPitch() == cand_pitch %12) {
                                double actual = (double)my_motion_count.getCount()/(double)my_pitch_count.getCount();
                                //DEBUG
                               // System.out.println("found " + my_motion_count.getCount() + " instances of motion from " + previous_cp_pitch %12 + " to " +cand_pitch %12 );
                                //System.out.println("frequency of motion from " + previous_cp_pitch %12 + " to " + cand_pitch%12 + " = " + actual);       
                                if (actual >= thresh) {
                                    myPC.decrementRank(Decrements.melodic_motion_quota_exceed);
                                    //DEBUG
                                    //System.out.println(cand_pitch %12 + " is approached too often from " + previous_cp_pitch %12);
                                }
                            }
                        }
                }
            }
            
            if (voice_pitch_count > 1){
                // Peak/Trough check
                // a melodic phrase should have no more than two peaks and two troughs
                // a peak is defined as a change in melodic direction 
                // so when a candidate pitch wants to go in the opposite direction of 
                // the previous melodic interval we want to increment the peak or trough count accordingly
                // and determine whether we have more than two peaks or more than two troughs
                // note that the melody can always go higher or lower than the previous peak or trough

                if (previous_melodic_interval < 0 && melody_motion_to_cand > 0 ) {// will there be a change in direction from - to +  ie trough?
                        if (previous_cp_pitch == trough && trough_count >=2) {
                            myPC.decrementRank(Decrements.peak_trough_quota_exceed);
                            //DEBUG
                            //System.out.println(previous_cp_pitch + " duplicates previous peak");
                        } //will this trough = previous trough? then increment
                }        
                if (previous_melodic_interval > 0 && melody_motion_to_cand <0){ // will there be a trough?
                        if (previous_cp_pitch == peak && peak_count >=2) {
                            myPC.decrementRank(Decrements.peak_trough_quota_exceed);
                            //DEBUG
                            //System.out.println(previous_cp_pitch + " duplicates previous trough");
                        } //will this trough = previous trough? then increment
                }
				
                //Motion after Leaps checks
                //First check if the melody does not go in opposite direction of leap
                // then check if there are two successive leaps in the same direction
                if (previous_melodic_interval > 4 && melody_motion_to_cand > 0){
                    myPC.decrementRank(Decrements.bad_motion_after_leap);
                    //DEBUG
                    //System.out.println(melody_motion_to_cand + " to "+ cand_pitch + " is bad motion after leap");
                    if (melody_motion_to_cand > 4) {
                        myPC.decrementRank(Decrements.successive_leaps);
                        //DEBUG
                        //System.out.println(cand_pitch + " is successive leap");
                    }
                        
                }    
                if (previous_melodic_interval < -4 && melody_motion_to_cand < 0){
                    myPC.decrementRank(Decrements.bad_motion_after_leap);
                    //DEBUG
                    //System.out.println(melody_motion_to_cand + " to "+cand_pitch + " is bad motion after leap");
                    if (melody_motion_to_cand < -4) {
                        myPC.decrementRank(Decrements.successive_leaps);  
                        //DEBUG
                        //System.out.println(cand_pitch + " is successive leap");
                    }

                }   
            }           
            // end melody checks
        } //next pitch candidate
        return pitch_candidates; 
    }
    /**
     * Similar to melodicChecks however in these method we are interested in 
     * harmonic properties of the pitch candidates relative to pitches in voices
     * that have already been built. 
     * @param pitch_candidates
     * @param CF_note
     * @param CFnoteRoot
     * @param previous_cf_pitch
     * @param previous_cp_pitch
     * @param CP_note
     * @param voice_pitch_count
     * @return 
     */
public static ArrayList<PitchCandidate> harmonicChecks(ArrayList<PitchCandidate> pitch_candidates, MelodicNote CF_note, Boolean CFnoteRoot, Integer previous_cf_pitch,
    Integer previous_cp_pitch, MelodicNote CP_note, int voice_pitch_count ){
        Boolean large_dissonance_bad = InputParameters.large_dissonance_bad;
        Integer [] consonances = InputParameters.consonances;
        Integer [] perfect_consonances = InputParameters.perfect_consonances;
        Integer [] root_consonances = InputParameters.root_consonances;
        boolean cand_prev_cf_diss = true;

        for (PitchCandidate myPC : pitch_candidates){
		
            Integer cand_pitch = myPC.getPitch();
            Integer cf_pitch = CF_note.getPitch();
            if (CF_note.getRest()) cf_pitch = previous_cf_pitch;
            Integer cand_prev_pitch = previous_cp_pitch;
            if(previous_cp_pitch == 9999) cand_prev_pitch = cand_pitch;// 9999 means the CP is held over to multiple cfs
            Integer melody_motion_to_cand = cand_pitch  - cand_prev_pitch;
            int this_interval = abs(myPC.getPitch() - CF_note.getPitch())%12;
            Integer melodic_motion_to_ = cf_pitch - previous_cf_pitch;
            Integer previous_interval = abs(cand_prev_pitch - previous_cf_pitch)%12;
            Double cp_start_time = CP_note.getStartTime();
            Double cf_start_time = CF_note.getStartTime();
			Boolean directm = false;
			Boolean this_interval_perf_cons = false;
            boolean this_interval_consonant = false;               
            
			//is this interval consonant
            for (Integer consonance : consonances) {
                if (this_interval == consonance){
                    this_interval_consonant = true; 
                    break;
                }

            }
			
			//decrement if an octave
            if(this_interval_consonant) {
				if (this_interval ==0) {
					myPC.decrementRank(Decrements.octave);
                }
            }
            else {
				//decrement if a minor 9th
                if (this_interval == 1 && (abs(myPC.getPitch() - CF_note.getPitch())<14 || large_dissonance_bad)){
					myPC.decrementRank(Decrements.minor_9th);
                }
				//decrement accented dissonance
                if (CP_note.getAccent() && (abs(myPC.getPitch() - CF_note.getPitch())<36 || large_dissonance_bad)) {
					myPC.decrementRank(Decrements.accented_dissonance);

                }
            }
            
			//check for pitch candidate dissonance against previous cantus firmus
            for (Integer consonance : consonances) {
                if ((cand_pitch - previous_cf_pitch)%12 == consonance) {
					cand_prev_cf_diss = false;    
                }
            }
            if (cand_prev_cf_diss == true) {
				myPC.decrementRank(Decrements.diss_cp_previous_cf);
            }
			
            //compute whether previous_interval consonant
            boolean previous_interval_consonant = false;
            for (Integer consonance : consonances) {
                if (previous_interval == consonance) previous_interval_consonant = true;
				break;
            }
			
			//check for same type of consonance
            if (previous_interval_consonant && (previous_interval == this_interval) ){
					myPC.decrementRank(Decrements.seq_same_type_cons);
            }
			
			//check for sequence of dissonances
            if(!previous_interval_consonant && !this_interval_consonant) {
				myPC.decrementRank(Decrements.seq_of_diss);
				if(this_interval == previous_interval ){
                    myPC.decrementRank(Decrements.seq_same_type_diss);
				}
			} 
	
			//check for too long a sequence of same interval
			if (previous_interval == this_interval) {
                same_consonant_count++;
                if (same_consonant_count > same_consonant_threshold) {
					myPC.decrementRank(Decrements.seq_of_same_cons);
                }
                              
            }
			else {
				same_consonant_count =0;
			}

			
			//if CF starts before CP 
            if (cp_start_time > cf_start_time){
				//the following  checks rely on knowing motion to pitch candidate from previous pitch
				//check for a bad approach to a dissonance from a consonance
				//ie CP pitch approached by greater than a step
                if (previous_interval_consonant){
                    if (!this_interval_consonant && abs(melody_motion_to_cand) >2) {
                        myPC.decrementRank(Decrements.bad_diss_approach_from_cons);
                    }    
                }
				//check for a bad approach to consonance from dissonance
                else if (this_interval_consonant){
                    if (abs(melody_motion_to_cand) >2){
                        myPC.decrementRank(Decrements.bad_cons_approach_from_diss);
                    }    
                }
				//check for bad approach to dissonance from dissonance
                else { //implies both this interval and previous are dissonant
                    if (abs(melody_motion_to_cand) > 4){
                        myPC.decrementRank(Decrements.bad_diss_approach_from_diss);
                    }
                    
                }
            }
            // If CP starts before CF
            else if (cp_start_time < cf_start_time) {
				// the following checks rely on knowing motion to CF pitch from previous CF pitch
				//check for bad motion into consonance from dissonance
                if (!previous_interval_consonant) {//ie Previous_Interval is dissonant
                    if (this_interval_consonant) {
                        if (abs(melodic_motion_to_) > 2) {
                              myPC.decrementRank(Decrements.bad_cons_approach_from_diss);
						}
                    }
					//check for bad motion into dissonance from dissonance
                    else {
                        if (abs(melodic_motion_to_) > 4){
							myPC.decrementRank(Decrements.bad_diss_approach_from_diss);   
                        }
                    }          
                }
            }
			// If CP and CF start at the same time
            else  {
                //Check for parallel perfect consonances
				if((melody_motion_to_cand > 0 && melodic_motion_to_ >0) || (melody_motion_to_cand < 0 && melodic_motion_to_ <0) )
					directm = true;
				if (this_interval_consonant) {
					if (previous_interval_consonant)	{
						if (this_interval == previous_interval ){
							myPC.decrementRank(Decrements.seq_same_type_cons);
							if (directm) {
								myPC.decrementRank(Decrements.parallel_perf_consonance);
							}      
						}
						else {
							//check for direct motion into a perfect consonance
							if (directm ) {
								myPC.decrementRank(Decrements.direct_motion_perf_cons);
							}
						}
					}
				}
				//check for motion into a dissonance
                else  { //this interval is dissonant
                    myPC.decrementRank(Decrements.motion_into_diss_both_voices_change);
					if (directm ) {
						myPC.decrementRank(Decrements.direct_motion_into_diss);
                    }
                }

            }
        }  
		return pitch_candidates;
    }
   
    /**
     * Picks the pitch candidate that will be the pitch of the note. 
     * @param pitch_candidates
     * @return 
     */
    public static Integer pickWinner(ArrayList<PitchCandidate> pitch_candidates){
        //pick highest ranking pitch candidate -> return_me[i]
        //System.out.println("start Pick Winner");
        ArrayList<PitchCandidate> pitch_winners = new ArrayList();
        for (PitchCandidate myPC : pitch_candidates){
            //DEBUG
            //System.out.println( "pitch candidate pitch: "+ myPC.getPitch() + " and rank: " + myPC.getRank() );
            if (pitch_winners.isEmpty()) {
                pitch_winners.add(myPC);
                //DEBUG
                //System.out.println("pitch_winners is empty. adding " + myPC.getPitch() + " with rank" + myPC.getRank());
            }
            else if (myPC.getRank() > pitch_winners.get(0).getRank()) {
                     pitch_winners.clear();
                     pitch_winners.add(myPC);
                     //DEBUG
                     //System.out.println("after emptying pitch_winners adding " + myPC.getPitch() +" with rank "+ myPC.getRank());
                }
            else if (Objects.equals(myPC.getRank(), pitch_winners.get(0).getRank())) {
                pitch_winners.add(myPC);
                //DEBUG
                //System.out.println("adding " + myPC.getPitch() + " to pitch_winners with rank " + myPC.getRank());
            }
            
        }
        int cp_winner = pitch_winners.get(0).getPitch();
         
        if (pitch_winners.size() >1) cp_winner = pitch_winners.get(roll.nextInt(pitch_winners.size())).getPitch();
        pitch_winners.clear();
        return cp_winner;
    }
    /**
     * Another harmonicCheck method for first and last pitches in a voice.
     * This simply decrements if the interval is not consonant
     * @param pitch_candidates
     * @param CF_note
     * @param CFnoteRoot
     * @param CP_note
     * @return 
     */
    public static ArrayList<PitchCandidate> harmonicChecksSuperBasic(ArrayList<PitchCandidate> pitch_candidates, MelodicNote CF_note, Boolean CFnoteRoot,
        MelodicNote CP_note){
		
        Integer [] consonances = InputParameters.consonances;

            for (PitchCandidate myPC : pitch_candidates){
                int this_interval = abs(myPC.getPitch() - CF_note.getPitch())%12;
              
                //compute interval whether consonant
                boolean this_interval_consonant = false;
                for (Integer consonance : consonances) {
                    if (this_interval == consonance) this_interval_consonant = true;
                }
                if(!this_interval_consonant) {
                    if (CP_note.getAccent()) {
                        myPC.decrementRank(Decrements.accented_dissonance);
                    }

                }
            }
        return pitch_candidates;
    }
    /**
     * Helper for canceling while in BuildVoicePitches
     */
    public void setSTOP(){
        stopper = true;
    }

    public void resetParams() {
        built_voices.clear();
        unbuilt_voices.clear();
        motion_counts.clear();
        pitch_counts.clear();
        trough = 0;
        trough_count = 0;
        peak = 0;
        peak_count = 0;
        same_consonant_count = 0;
        key_transpose = 0;
        root_key = 0;
        harmonic_prog_built = false;
        stopper = false;

    }
}
