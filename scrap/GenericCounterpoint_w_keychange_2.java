/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Hindemith;


import Hindemith.RhythmModule.*;
import Hindemith.ModeModules.*;
import static java.lang.Math.abs;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Objects;
import java.util.logging.Logger; //the Java Logger
import java.util.Random;
import org.jfugue.*;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 *
 * @author alyssa
 */
public class GenericCounterpoint_w_keychange_2 {

    /**
     * @param args the command line arguments
    
    
    */
    
    
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



public static void generateMusic() {
        int piece_length = InputParameters.piece_length;
        int tempo_bpm = InputParameters.tempo_bpm;
        ModeModule my_mode_module = InputParameters.my_mode_module;
        String [] voice_array = InputParameters.voice_array; 
        RhythmModule james = InputParameters.james;       
        int number_of_voices = voice_array.length;
        Random my_roll = new Random();
        
                
        Pattern [] rhythm_patterns = james.generate(piece_length, number_of_voices);
        //Hindemith.view.ProgressTicker.displayProgress(0.1);
        for (int i = 0; i < number_of_voices; i++) {
            MelodicVoice this_voice = new MelodicVoice();
            this_voice.setRange(voice_array[i]);
            AccentListener my_accent_listener = new AccentListener();
            this_voice.setNoteArray(my_accent_listener.listen(rhythm_patterns[i]));
            System.out.println("adding voice " + i + " to unbuiltvoices");
            unbuilt_voices.add(this_voice);
        }

        //Hindemith.view.ProgressTicker.displayProgress(0.25); 
        int ub_size =unbuilt_voices.size();
        for (int i = 0; i < number_of_voices; i++){
            //int voice_index = my_roll.nextInt(unbuilt_voices.size()); 
            int voice_index = i;
            System.out.println(" about to build voice pitches for "+ voice_index);
            MelodicVoice nextVoice = buildVoicePitches(unbuilt_voices.get(voice_index), number_of_voices, my_mode_module);
            ArrayList<MelodicNote> verify_array = nextVoice.getNoteArray();
            System.out.println("Return Me: ");
            for(MelodicNote verify: verify_array) { 
             if (verify.getRest()) System.out.println("rest " + verify.getDuration() + "  " );
             else System.out.println(verify.getPitch() + " " + verify.getDuration() + "   ");
            }
            if (i == 0) {
                harmonic_prog = nextVoice;
                harmonic_prog_built = true;
            }
            else built_voices.add(nextVoice);
            //unbuilt_voices.remove(voice_index);

            
        }
        //Hindemith.view.ProgressTicker.displayProgress(0.5);
        unbuilt_voices.clear();
        Pattern music_output = new Pattern();
        music_output.addElement(new Tempo (tempo_bpm));
        
        for (byte i = 0; i < built_voices.size(); i++){
        // create a jfugue musicstring from the built voice
            MelodicVoice final_voice = built_voices.get(i);
            ArrayList<MelodicNote> final_note_array = final_voice.getNoteArray();
            Voice jf_voice = new Voice(i);
            music_output.addElement(jf_voice);
            for (MelodicNote final_note : final_note_array) {
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
                    //System.out.println("setting jf note to rest");
                    jf_note.setRest(true);
                    jf_note.setAttackVelocity((byte)0);
                    jf_note.setDecayVelocity((byte)0);
                }
                
                music_output.addElement(jf_note);
            } // add the musicstring to a pattern

            //Hindemith.view.ProgressTicker.displayProgress(0.75);
        }
        
        //Hindemith.view.ProgressTicker.displayProgress(1.0);
        
        //save and play the pattern
        System.out.println(music_output.getMusicString());
        Player my_player = new Player();
        my_player.play(music_output);
        Date today = new Date(System.currentTimeMillis());
        //Date format: "yyyy-MM-dd_HH:mm:ss"
        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM-dd-yyyy-HH-mm");
        String dateString = DATE_FORMAT.format(today);
    if (InputParameters.filePath != null) {
        try {
        my_player.saveMidi(music_output, InputParameters.filePath);
        }
        catch (IOException ex) { 
        }
    }    
    else {
        
            try {
            my_player.saveMidi(music_output, new File(tempo_bpm + "-" + dateString + ".mid"));
            }
            catch (IOException ex2) {
                
            }
        }
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
    
    }
    
    public static MelodicVoice buildVoicePitches (MelodicVoice alter_me, int number_of_voices, ModeModule my_mode_module){
    MelodicVoice return_me = new MelodicVoice();
    ArrayList<LinkedList> built_voice_queues = new ArrayList();
    int previous_cp_pitch = -13;
    int previous_melodic_interval = 0;
    trough = 200;
    trough_count = 0;
    peak = 0;
    peak_count = 0;
    same_consonant_count = 0;
    int voice_pitch_count = 0;
    LinkedList <MelodicNote> chord_prog_stack = new LinkedList<>();
    
    System.out.println("voiceRange min " + alter_me.getRangeMin() + "   voicerange max " + alter_me.getRangeMax());
    Integer pitch_center = my_mode_module.getPitchCenter(alter_me.getRangeMin(), alter_me.getRangeMax());
    System.out.println("pitchcenter = " + pitch_center);
    
    if (harmonic_prog_built) {
        ArrayList <MelodicNote> chord_prog_temp = harmonic_prog.getNoteArray();
        for (MelodicNote b_voice_note : chord_prog_temp){
            chord_prog_stack.add(b_voice_note);
        }  
    }
   
    
    
    if (!built_voices.isEmpty())
    for (MelodicVoice built_voice : built_voices) {
         LinkedList <MelodicNote> cf_stack = new LinkedList<>();
         ArrayList <MelodicNote> temp = built_voice.getNoteArray();
         for (MelodicNote b_voice_note : temp){
         cf_stack.add(b_voice_note);
         }
         built_voice_queues.add(cf_stack);
         System.out.println("created stack of melodic notes for each previously built voice ");
     }
     else {

         System.out.println("built voices Empty - start first melody");
     }
    
    ArrayList <MelodicNote> current_cf = new ArrayList();
    MelodicNote[] holdover_cf = new MelodicNote[ built_voice_queues.size()];
    Integer[] previous_cf_pitch = new Integer[built_voice_queues.size()];
    for(int h = 0; h<built_voice_queues.size(); h++)
         previous_cf_pitch[h] = 1111;
    ArrayList <MelodicNote> pending_rests = new ArrayList();
    Integer current_cp_index = -13;
    ArrayList<Integer> pitch_candidate_values = new ArrayList();
    ArrayList<PitchCandidate> pitch_candidates = new ArrayList();
    
    MelodicNote this_cf = null;
    Integer melodic_prev_cp = 0;

    
    for (int i = 0; i < alter_me.getVoiceLength(); i++){ //for each melodic note in the CP voice
        System.out.println("assigning pitch to note " + i +" of " + alter_me.getVoiceLength());
       
        MelodicNote CP_note = alter_me.getMelodicNote(i);
        if (!CP_note.getRest()) { 
            if (current_cp_index >=0) {
                if(previous_cp_pitch == 9999) previous_cp_pitch = melodic_prev_cp; 
                System.out.println("starting melody check on current cp"+ current_cp_index +" of " + alter_me.getVoiceLength());
                Boolean got_accent = alter_me.getMelodicNote(current_cp_index).getAccent();
                System.out.println("the current cp note's accent value is " + got_accent);
                

//MELODICALLY EVALUATE Pitch Candiates for Previous Note - Choose CP WinnerS - Previous CP = CP Winner
                pitch_candidates = melodicCheck(pitch_candidates, my_mode_module, alter_me, 
                             pitch_center, voice_pitch_count, previous_cp_pitch,  previous_melodic_interval, got_accent);
                Integer cp_winner = pickWinner(pitch_candidates);
                 //re-assign variables and move on to next CP note
                pitch_candidates.clear();
                System.out.println("CP winner" + cp_winner + " to note " + current_cp_index);
                MelodicNote current_cp = alter_me.getMelodicNote(current_cp_index);
                current_cp.setPitch(cp_winner);
                return_me.addMelodicNote(current_cp);
                if(!pending_rests.isEmpty())
                    for (MelodicNote my_rest: pending_rests ){
                        return_me.addMelodicNote(my_rest);
                    }
                pending_rests.clear();
                //Calculating Peaks and Troughs and note counts and save variables
                if (previous_cp_pitch != -13) {
                    if (voice_pitch_count> 1){
                        if (previous_melodic_interval < 0 && cp_winner - previous_cp_pitch  > 0 ) {// will there be a change in direction from - to +  ie trough?
                            if (previous_cp_pitch == trough) {
                                trough_count++;
                                
                            }
                            else if (previous_cp_pitch != trough)  {
                              
                                trough = previous_cp_pitch;
                                trough_count = 1;
                                System.out.println("setting new trough = " + previous_cp_pitch);
                                   
                            }
                        }

                        if (previous_melodic_interval > 0 && cp_winner - previous_cp_pitch  < 0 ) {// will there be a change in direction from - to +  ie trough?
                            if (previous_cp_pitch == peak) peak_count++;
                            else{
                                if (previous_cp_pitch > peak) {
                                peak = previous_cp_pitch;
                                peak_count = 1;
                                System.out.println("setting new peak = " + previous_cp_pitch);    
                                }

                            }
                        }    
                    }

                    previous_melodic_interval = cp_winner - previous_cp_pitch;
                    System.out.println("previous melodic interval = " + previous_melodic_interval);
                    
                    boolean add_pitch = true;
                    for(int pc = 0; pc < pitch_counts.size(); pc++) {
                        if (pitch_counts.get(pc).getPitch() == previous_cp_pitch%12) {
                            pitch_counts.get(pc).incrementCount();
                            add_pitch = false;
                        }        
                    }
                    if (add_pitch == true){
                    PitchCount my_pitch_count = new PitchCount(previous_cp_pitch %12);
                    pitch_counts.add(my_pitch_count);               
                    }

                    
                    boolean add_motn = true;
                    for(int mc = 0; mc< motion_counts.size(); mc++){
                        if (motion_counts.get(mc).getPreviousPitch() == previous_cp_pitch %12 && motion_counts.get(mc).getSucceedingPitch() == cp_winner %12) {
                            motion_counts.get(mc).incrementCount();
                            add_motn = false;
                        }   
                    }
                    if(add_motn ==true) {
                        MotionCount my_motionCount = new MotionCount(previous_cp_pitch %12, cp_winner %12);
                        motion_counts.add(my_motionCount);
                    }
                }


                previous_cp_pitch = cp_winner;
                melodic_prev_cp = cp_winner;
                voice_pitch_count++;    
            }

//get pitch_candidate pitches for this note from mode_module

            if (harmonic_prog_built) {
                do {

                        MelodicNote prog_stack_note = (MelodicNote)chord_prog_stack.pop();
                        if (prog_stack_note.getPitch() != 0) this_key.setPitch(prog_stack_note.getPitch());
                        this_key.setTotalVoiceDuration(prog_stack_note.getPreviousDuration());
                        System.out.println("this key = " + this_key.getPitch());
                        System.out.println("this key prevduration = " + this_key.getPreviousDuration());
                        if (this_key.getPreviousDuration() >= CP_note.getStartTime()) {
                            key_transpose = this_key.getPitch()%12;
                        }
                } while (this_key.getPreviousDuration()< CP_note.getPreviousDuration());   
            }
            
            if(voice_pitch_count == 0) { // If there is no previous pitch ie this is the first note 
                pitch_candidate_values = my_mode_module.getFirstNotePitchCandidates(alter_me.getRangeMin(), alter_me.getRangeMax(), key_transpose) ;
                System.out.println("using first note pitch candidates");
            }
            else {
                System.out.println("getting pitch candidates from my modemodule");
                pitch_candidate_values = my_mode_module.getPitchCandidates(previous_cp_pitch, key_transpose);
                if (pitch_candidate_values.isEmpty()) System.out.println("EMPTY ARRAY!!!!");
            }
            System.out.println("voice_pitch_count" + voice_pitch_count);
            System.out.print("pitch candidates: ");
         
            //build pitch_candidate object array with info from rhythm patterns and mode module
            for (Integer pitch_candidate_value : pitch_candidate_values) {
                System.out.print(pitch_candidate_value + " ");
                PitchCandidate myPC = new PitchCandidate();
                myPC.setPitch(pitch_candidate_value);
                pitch_candidates.add(myPC);
            }
            System.out.println();
            
            //Reset Current CP index
            current_cp_index = i;
        }
//If note is a Rest  add it to the voice       
        else {
            System.out.println(" is rest ");
            if (current_cp_index == -13) {
                return_me.addMelodicNote(CP_note);
                System.out.println("adding rest to beginning of voice");
                continue;
            }
            else {
                pending_rests.add(CP_note);
               System.out.println("adding rest to pending rest array");
            }
        }
/*
Regardless if note or rest run HARMONIC CHECKS on the current cp
If a rest, then we are still deciding the counterpoint for the 
immediately previous place in the melody. 

If this is the very first voice, then there are no prebuilt voices 
so skip this section        
*/        
        if (!built_voice_queues.isEmpty()){        
            for (int b = 0; b < built_voice_queues.size(); b++){
                    do {
                        boolean CF_root = false;
                        if (b == 0) CF_root = true;
                        boolean skip_me = false;
                        if (!built_voice_queues.get(b).isEmpty()){
                            if (holdover_cf[b] != null) this_cf = holdover_cf[b];
                            else this_cf = (MelodicNote)built_voice_queues.get(b).pop();//pop from builtvoicequeues[b];
                            System.out.println(" pitch = " + this_cf.getPitch());
                            System.out.println(" rest = " + this_cf.getRest());
                            System.out.println(" duration up to  = " + this_cf.getPreviousDuration());
                        }

                        else {
                            System.out.println("cf voice is empty");
                            break;
                        }
                        if (this_cf.getRest()) {
                            if (this_cf.getDuration() > .5 || 
                                    previous_cf_pitch[b] == -1 ||
                                     CP_note.getRest()) skip_me = true;
                        }
                        
                            if (!skip_me){
                                    //HARMONICALLY EVALUATE cp candidates against this_cf
                                    if (previous_cf_pitch[b]!=1111)
                                        pitch_candidates = harmonicChecks(pitch_candidates, this_cf, CF_root, previous_cf_pitch[b],
                                                            previous_cp_pitch, CP_note, voice_pitch_count);
                                    else pitch_candidates = harmonicChecksSuperBasic(pitch_candidates, this_cf, CF_root, alter_me.getMelodicNote(current_cp_index));
                                   previous_cp_pitch =9999; // 9999 is assigned in case CP is held over into next CF note
                            }                               //in which case the while loop repeats. When you break out
                                                            //of while loop previous_cp_pitch will be checked and re-assigned above
                            else {
                                    System.out.println("current cf index" + b + "is null");
                                    break;
                            }
                            if (this_cf.getPreviousDuration() > CP_note.getPreviousDuration())
                                    holdover_cf[b] =  this_cf;
                            else holdover_cf[b] = null;
                            
                            if (!skip_me) previous_cf_pitch[b] = this_cf.getPitch();
                    } while (this_cf.getPreviousDuration()< CP_note.getPreviousDuration());
            }
        }
/*
If this is the last melodic placeholder, run some closing procedures        
 
*/	
        if (i == alter_me.getVoiceLength()-1) {
            //MELODICALLY EVALUATE Current CP
            System.out.println("Last note of voice");
            Boolean last_accent = true;
            if (!CP_note.getRest()) {
                pitch_candidates = melodicCheck(pitch_candidates, my_mode_module, alter_me, 
                             pitch_center, voice_pitch_count, previous_cp_pitch,  previous_melodic_interval, last_accent);
                Integer cp_winner = pickWinner(pitch_candidates);
                System.out.println("CP winner" + cp_winner);
                pitch_candidates.clear();
                CP_note.setPitch(cp_winner);
                return_me.addMelodicNote(CP_note); 
            }
            else {
                Boolean got_accent = alter_me.getMelodicNote(current_cp_index).getAccent();
                System.out.println("this note's accent value is " + got_accent);
                //MELODICALLY EVALUATE Current CP - Choose CP WinnerS - Previous CP = CP Winner
                pitch_candidates = melodicCheck(pitch_candidates, my_mode_module, alter_me, 
                             pitch_center, voice_pitch_count, previous_cp_pitch,  previous_melodic_interval, got_accent);
                Integer cp_winner = pickWinner(pitch_candidates);
                pitch_candidates.clear();
                System.out.println("CP winner" + cp_winner + " to note " + current_cp_index);
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
    
    public static ArrayList<PitchCandidate>  melodicCheck(ArrayList<PitchCandidate> pitch_candidates, ModeModule my_mode_module, MelodicVoice alter_me, 
                             Integer pitch_center, int voice_pitch_count, Integer previous_cp_pitch, Integer previous_melodic_interval, Boolean is_accent) {
        Boolean large_dissonance_bad = InputParameters.large_dissonance_bad;
        Integer [] consonances = InputParameters.consonances;
        Integer [] perfect_consonances = InputParameters.perfect_consonances;
        Integer [] root_consonances = InputParameters.root_consonances;
        //Melodic Checks
        //Evaluate each pitch candidate
        for (PitchCandidate myPC : pitch_candidates){
            int cand_pitch = myPC.getPitch();
            int melody_motion_to_cand = 0;
            
            System.out.println("melodicCheck evaluating pitch candidate " + cand_pitch);
            
            //Check if Dissonant with Root
            boolean root_interval_consonant = false;
            int root_interval = abs(cand_pitch - root_key)%12;
            for (Integer consonance : root_consonances) {
                if (root_interval == consonance) root_interval_consonant = true;

            }
            if(root_interval_consonant) {
                System.out.println(cand_pitch + " root interval consonant");
            }
            else {
                if (is_accent ) {
                    myPC.decrementRank(Decrements.dissonant_with_root);
                    System.out.println(cand_pitch + " dissonant accent with root");
                }
                else System.out.println("dissonant with root but note accent = " + is_accent );
            } 
            
            
            //randomly decrement non-tonics
            if (cand_pitch%12 != my_mode_module.getTonic() && roll.nextInt(2) == 1){
                myPC.decrementRank(Decrements.is_not_tonic);
                System.out.println(cand_pitch + " is not tonic");
            }
            
            //decrement illegal notes
            if(cand_pitch <0 || cand_pitch > 127) {
                myPC.decrementRank(Decrements.illegal_note);
                System.out.println(cand_pitch + " is illegal note");                
            }

            //decrement motion outside of voice range                
            if (cand_pitch < alter_me.getRangeMin() || cand_pitch > alter_me.getRangeMax()) {
                myPC.decrementRank(Decrements.outside_range);
                 System.out.println(cand_pitch + " outside range " + alter_me.getRangeMin() + "-" + alter_me.getRangeMax());                
            }

            //decrement too far from pitch center
            if (abs(cand_pitch - pitch_center) > 16) {
                 myPC.decrementRank(Decrements.remote_from_pitchcenter);
                 System.out.println(cand_pitch + " too far from pitch center" + pitch_center);               
            } 

            if (voice_pitch_count > 0) {
                melody_motion_to_cand = cand_pitch - previous_cp_pitch;
            
            
                //The candidate has already followed the preceding note too often. (method created)                
                //look for previous_cp_pitch in PitchCount
                //if it's there get_count 
                // if the count is greater than samplesize
                //check if previous_cp_pitch and pitch_candidate in MOtion Counts
                //if so get count - then divide motion count by pitch count
                    // get the percentage from mode module
                //if actual count is greater than mode module percentage decrement
                for (PitchCount my_pitch_count: pitch_counts) {
                    if(my_pitch_count.getPitch() == previous_cp_pitch%12)
                        if(my_pitch_count.getCount() > sample_size)
                            for (MotionCount my_motion_count: motion_counts){
                               //logger.log(Level.INFO, "Entering Motion Counts");
                                //System.out.println("pitch_count for " + previous_cp_pitch %12 + " = " + my_pitch_count.getCount());
                                //System.out.println("motion count " + my_motion_count.getCount());
                                if (my_motion_count.getPreviousPitch()== previous_cp_pitch %12 && my_motion_count.getSucceedingPitch() == cand_pitch %12) {
                                    double actual = (double)my_motion_count.getCount()/(double)my_pitch_count.getCount();
                                    System.out.println("actual = " + actual);
                                    double thresh = 0.20;
                                    if (my_mode_module.getMelodicMotionProbability(cand_pitch, previous_cp_pitch, key_transpose)!= null){
                                      thresh = my_mode_module.getMelodicMotionProbability(cand_pitch, previous_cp_pitch, key_transpose);
                                      System.out.println("motion probability of " + previous_cp_pitch +" to " + cand_pitch + " = " + thresh  );
                                    }
                                    else System.out.println("motion probability of " + previous_cp_pitch +" to " + cand_pitch + " is NULL");
                                        
                                    if (actual >= thresh) {
                                        myPC.decrementRank(Decrements.melodic_motion_quota_exceed);
                                        System.out.println(cand_pitch + " is approached too often from " + previous_cp_pitch);
                                    }
                                }
                            }
                }
            }
            if (voice_pitch_count > 1){
                //Peak/Trough check
                // a melodic phrase should have no more than two peaks and two troughs
                // a peak is defined as a change in melodic direction 
                // so when a candidate pitch wants to go in the opposite direction of 
                // the previous melodic interval we want to increment the peak or trough count accordingly
                // and determine whether we have more than two peaks or more than two troughs
                // note that the melody can always go higher or lower than the previous peak or trough

                if (previous_melodic_interval < 0 && melody_motion_to_cand > 0 ) {// will there be a change in direction from - to +  ie trough?
                        if (previous_cp_pitch == trough) trough_count++; //will this trough = previous trough? then increment
                }        
                if (previous_melodic_interval > 0 && melody_motion_to_cand <0){ // will there be a trough?
                        if (previous_cp_pitch == peak) peak_count++; //will this trough = previous trough? then increment
                }
                if (peak_count > 1 || trough_count > 1) {
                        peak_count--; //remember to decrement these counts since we won't actually use this pitch
                        trough_count--;
                        myPC.decrementRank(Decrements.peak_trough_quota_exceed);
                        System.out.println(cand_pitch + " duplicates previous peak or trough");
                }
                //Motion after Leaps checks
                //First check if the melody does not go in opposite direction of leap
                // then check if there are two successive leaps in the same direction
                if (previous_melodic_interval > 4 && melody_motion_to_cand > 0){
                    myPC.decrementRank(Decrements.bad_motion_after_leap);
                    System.out.println(melody_motion_to_cand + " to "+ cand_pitch + " is bad motion after leap");
                    if (melody_motion_to_cand > 4) {
                        myPC.decrementRank(Decrements.successive_leaps);
                        System.out.println(cand_pitch + " is successive leap");
                    }
                        
                }    
                if (previous_melodic_interval < -4 && melody_motion_to_cand < 0){
                    myPC.decrementRank(Decrements.bad_motion_after_leap);
                    System.out.println(melody_motion_to_cand + " to "+cand_pitch + " is bad motion after leap");
                    if (melody_motion_to_cand < -4) {
                        myPC.decrementRank(Decrements.successive_leaps);  
                        System.out.println(cand_pitch + " is successive leap");
                    }

                }
                

            }           
            // end melody checks
        } //next pitch candidate
        return pitch_candidates; 
    }
    
    public static ArrayList<PitchCandidate> harmonicChecks(ArrayList<PitchCandidate> pitch_candidates, MelodicNote CF_note, Boolean CFnoteRoot, Integer previous_cf_pitch,
                                                        Integer previous_cp_pitch, MelodicNote CP_note, int voice_pitch_count ){
            Boolean large_dissonance_bad = InputParameters.large_dissonance_bad;
            Integer [] consonances = InputParameters.consonances;
            Integer [] perfect_consonances = InputParameters.perfect_consonances;
            Integer [] root_consonances = InputParameters.root_consonances;
            //begin Harmonic checks
            boolean cand_prev_cf_diss = true;
            //Will need to evaluate each CP pitch candidate against the counterpoint notes 
            //in each previously built voice

            //Evaluate Pitch Candidates Against these checke
            for (PitchCandidate myPC : pitch_candidates){
                Integer cand_pitch = myPC.getPitch();
                Integer cf_pitch = CF_note.getPitch();
                if (CF_note.getRest()) cf_pitch = previous_cf_pitch;
                Integer cand_prev_pitch = previous_cp_pitch;
                if(previous_cp_pitch == 9999) cand_prev_pitch = cand_pitch;// 9999 means the CP is held over to multiple cfs
                Integer melody_motion_to_cand = cand_pitch  - cand_prev_pitch;
                int this_interval = abs(myPC.getPitch() - CF_note.getPitch())%12;
                
                System.out.println("this interval  = " + this_interval);
                Integer melodic_motion_to_ = cf_pitch - previous_cf_pitch;
                Integer previous_interval = abs(cand_prev_pitch - previous_cf_pitch)%12;
                System.out.println("previous interval  = " + previous_interval);
                Double cp_start_time = CP_note.getStartTime();
                Double cf_start_time = CF_note.getStartTime();
                
                //compute interval whether consonant
                boolean this_interval_consonant = false;
                for (Integer consonance : consonances) {
                    if (this_interval == consonance){
                        this_interval_consonant = true;
                        System.out.println("this_interval == " +consonance + " means this_interval_consonant == " + this_interval_consonant);    
                        break;
                    }

                }
                
                
                                    
                if(this_interval_consonant) {
                    System.out.println("this interval consonant");
                    if (this_interval ==0) {
                    myPC.decrementRank(Decrements.octave);
                    System.out.println("octave");
                    }
                }
                else {
                        if (this_interval == 1 && (abs(myPC.getPitch() - CF_note.getPitch())<14 || large_dissonance_bad)){
                        myPC.decrementRank(Decrements.minor_9th);
                        System.out.println("minor 9th");    
                        }
                        if (CP_note.getAccent() && (abs(myPC.getPitch() - CF_note.getPitch())<36 || large_dissonance_bad)) {
                        //if (CP_note.getAccent() && CP_note.getStartTime()<= CF_note.getStartTime() ) {
                        myPC.decrementRank(Decrements.accented_dissonance);
                        System.out.println("dissonant accent");
                    }
                }                
                
                
                
                //compute previous_interval consonant
                boolean previous_interval_consonant = false;
                for (Integer consonance : consonances) {
                    if (previous_interval == consonance) previous_interval_consonant = true;
                }
                if(previous_interval_consonant) System.out.println("previous interval consonant");

                

                if (cp_start_time > cf_start_time){
                    System.out.println("CP starts after CF");   
                    if (melody_motion_to_cand == 0){
                        myPC.decrementRank(Decrements.seq_of_same_cons);
                        System.out.println("sequence of same consonsance");
                    }    
                    if (previous_interval_consonant){
                        if (!this_interval_consonant && abs(melody_motion_to_cand) >2) {
                            myPC.decrementRank(Decrements.bad_diss_approach_from_cons);
                            System.out.println("bad approach to dissonance from consonance");
                        }    
                    }
                    else if (this_interval_consonant){
                        if (abs(melody_motion_to_cand) >2){
                            myPC.decrementRank(Decrements.bad_cons_approach_from_diss);
                            System.out.println("bad approach to consonance from dissonance");
                        }
                        
                    }    
                    else {
                        if (abs(melody_motion_to_cand) > 4){ //New_Interval is dissonant
                            myPC.decrementRank(Decrements.bad_diss_approach_from_diss);
                            System.out.println("bad approach to dissonance from dissonance");
                        }
                        
                    }
                }
                
                else if (cp_start_time < cf_start_time) {
                    System.out.println("CP starts before CF"); 
                    if (previous_interval_consonant){
                        if (previous_interval == this_interval) {
                        myPC.decrementRank(Decrements.seq_same_type_cons);
                        System.out.println("sequence of same typ of consonance");
                        
                        }
                    }             
                    else {//ie Previous_Interval is dissonant
                        System.out.println("previous interval is dissonant");
                        if (this_interval_consonant) 
                            if (abs(melodic_motion_to_) > 2) {
                                  myPC.decrementRank(Decrements.bad_cons_approach_from_diss);
                                  System.out.println("bad approach to consonance from dissonance");
                        }
                        else //New_Interval is dissonant
                                 if (abs(melodic_motion_to_) > 4){
                                  myPC.decrementRank(Decrements.bad_diss_approach_from_diss);
                                  System.out.println(" bad approach to dissonance from dissonance");    
                                 }
                                  
                    }
                }
                else  {
                    System.out.println("CP and CF start at the same time");         
                    if (previous_interval_consonant){
                        System.out.println("previous interval consonant");
                        if (this_interval_consonant){
                            for (Integer consonance : consonances) {
                                if ((cand_pitch - previous_cf_pitch)%12 == consonance) {
                                cand_prev_cf_diss = false;    
                                }
                            }

                            if (cand_prev_cf_diss == true) {
                             myPC.decrementRank(Decrements.diss_cp_previous_cf);
                             System.out.println("counterpoint is dissonant with previous cantus firmus");
                            }
                                   

                            if (previous_interval == this_interval) {
                                same_consonant_count++;
                                if (same_consonant_count > same_consonant_threshold) {
                                 myPC.decrementRank(Decrements.seq_of_same_cons);
                                 System.out.println("sequence of too many consonant interval");
                                }
                                   
                            }
                            //Too many of same type of interval
                            for (Integer perfect_consonance : perfect_consonances) {
                                if (this_interval == perfect_consonance) {
                                    if (this_interval == previous_interval ){
                                        myPC.decrementRank(Decrements.seq_same_type_cons);
                                        if (melody_motion_to_cand > 0)
                                            if (melodic_motion_to_ >0 ) {
                                             myPC.decrementRank(Decrements.parallel_perf_consonance);
                                             System.out.println("parallel perfect consonance");
                                            }
                                               
                                        if (melody_motion_to_cand < 0)
                                            if (melodic_motion_to_ <0 ) {
                                            myPC.decrementRank(Decrements.parallel_perf_consonance);
                                            System.out.println("parallel perfect consonance");
                                            }
                                                    
                                    }
                                } 
                                else {
                                    if (melody_motion_to_cand > 0)
                                        if (melodic_motion_to_ >0 ) {
                                        myPC.decrementRank(Decrements.direct_motion_perf_cons);
                                        System.out.println("direct motion into perfect consonance");
                                        }
                                            
                                    if (melody_motion_to_cand < 0)
                                        if (melodic_motion_to_ <0 ) {
                                        myPC.decrementRank(Decrements.direct_motion_perf_cons);
                                        System.out.println("direct motion into perfect consonance");
                                        }
                                            
                                }
                            }
                            //If dissonance between CP1 and CF2 is this resolved?
                        }
                        else  {//New_Interval is dissonant
                            myPC.decrementRank(Decrements.motion_into_diss_both_voices_change);
                            System.out.println("both voices move into dissonance");
                        }
                    }
                    else  {//ie Previous_Interval is dissonant
                        System.out.println("previous interval is dissonant");
                        if (this_interval_consonant){

                            for (Integer consonance : consonances) {
                                if ((cand_pitch - previous_cf_pitch)%12 == consonance) {
                                cand_prev_cf_diss = false;    
                                }
                            }

                            if (cand_prev_cf_diss == true) {
                            myPC.decrementRank(Decrements.diss_cp_previous_cf);
                            System.out.println("counterpoint dissonant with previous cf");
                            }
                                   
                            if (melody_motion_to_cand > 0)
                                if (melodic_motion_to_ >0 ) {
                                    myPC.decrementRank(Decrements.direct_motion_perf_cons);
                                    System.out.println("direct motion into perfect consonance");
                                    }
                                            
                            if (melody_motion_to_cand < 0)
                                if (melodic_motion_to_ <0 ) {
                                    myPC.decrementRank(Decrements.direct_motion_perf_cons);
                                    System.out.println("direct motion into perfect consonance");
                                    }
                                            
                        }
                        else { //this interval is dissonant
                            myPC.decrementRank(Decrements.motion_into_diss_both_voices_change);
                            myPC.decrementRank(Decrements.seq_of_diss);
                            System.out.println("two dissonances in a row approached from both voices");
                            if(this_interval == previous_interval ){
                                myPC.decrementRank(Decrements.seq_same_type_diss);
                                System.out.println("sequence of same type of dissonance");
                                    if (melody_motion_to_cand > 0)
                                        if (melodic_motion_to_ >0 ) {
                                        myPC.decrementRank(Decrements.direct_motion_into_diss);
                                        System.out.println("direct motion into dissonance");
                                        }
                                            
                                    if (melody_motion_to_cand < 0)
                                        if (melodic_motion_to_ <0 ) {
                                        myPC.decrementRank(Decrements.direct_motion_into_diss);
                                        System.out.println("direct motion into dissonance");
                                        }
                                            
                            }
                        }
                    }
                }

            }
        return pitch_candidates;
    }
    
    public static Integer pickWinner(ArrayList<PitchCandidate> pitch_candidates){
        //pick highest ranking pitch candidate -> return_me[i]
        ArrayList<PitchCandidate> pitch_winners = new ArrayList();
        for (PitchCandidate myPC : pitch_candidates){
            System.out.println( "pitch candidate pitch: "+ myPC.getPitch() + " and rank: " + myPC.getRank() );
            if (pitch_winners.isEmpty()) {
                pitch_winners.add(myPC);
                System.out.println("pitch_winners is empty. adding " + myPC.getPitch() + " with rank" + myPC.getRank());
            }
            else if (myPC.getRank() > pitch_winners.get(0).getRank()) {
                     pitch_winners.clear();
                     pitch_winners.add(myPC);
                     System.out.println("after emptying pitch_winners adding " + myPC.getPitch() +" with rank "+ myPC.getRank());
                }
            else if (Objects.equals(myPC.getRank(), pitch_winners.get(0).getRank())) {
                pitch_winners.add(myPC);
                System.out.println("adding " + myPC.getPitch() + " to pitch_winners with rank " + myPC.getRank());
            }
            
        }
        int cp_winner = pitch_winners.get(0).getPitch();
         
        if (pitch_winners.size() >1) cp_winner = pitch_winners.get(roll.nextInt(pitch_winners.size())).getPitch();
        pitch_winners.clear();
        return cp_winner;
    }
    public static ArrayList<PitchCandidate> harmonicChecksSuperBasic(ArrayList<PitchCandidate> pitch_candidates, MelodicNote CF_note, Boolean CFnoteRoot,
                                                        MelodicNote CP_note){
            Boolean large_dissonance_bad = InputParameters.large_dissonance_bad;
        Integer [] consonances = InputParameters.consonances;
        Integer [] perfect_consonances = InputParameters.perfect_consonances;
        Integer [] root_consonances = InputParameters.root_consonances;
            //begin Harmonic checks
            //Will need to evaluate each CP pitch candidate against the counterpoint notes 
            //in each previously built voice

            //Evaluate Pitch Candidates Against these checke
            for (PitchCandidate myPC : pitch_candidates){
                int this_interval = abs(myPC.getPitch() - CF_note.getPitch())%12;
                System.out.println("this interval  = " + this_interval);
                
                //compute interval whether consonant
                boolean this_interval_consonant = false;
                boolean root_interval_consonant = false;
                for (Integer consonance : consonances) {
                    if (this_interval == consonance) this_interval_consonant = true;
                }
                if(this_interval_consonant) {
                    System.out.println("this interval consonant");
                }
                else {
                    if (CP_note.getAccent()) {
                        //if (CP_note.getAccent() && CP_note.getStartTime() <= CF_note.getStartTime()) {
                        myPC.decrementRank(Decrements.accented_dissonance);
                        System.out.println("dissonant accent2");
                    }
                }
            }
        return pitch_candidates;
    }
       
}


