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

/**
 * Static class that:
 * Stores values for decrements. Decrements are set to defaults which aren't 
 * respected by the application, which resets them to the values indicated by 
 * the input controls. 
 * 
 * @author Trick's Music Boxes
 */
public class Decrements {
    static int peak_trough_quota_exceed = 1;
    static int melodic_motion_quota_exceed = 2;
    static int successive_leaps = 2;
    static int bad_motion_after_leap = 2;
    static int outside_range  = 19;
    static int remote_from_pitchcenter = 18;
    static int illegal_note  = 500;
    static int overlap = 1;
    static int accented_dissonance = 400;
    static int bad_diss_approach_from_cons = 1;
    static int bad_cons_approach_from_diss = 1;
    static int motion_into_diss_both_voices_change = 1;
    static int seq_of_diss = 1;
    static int seq_same_type_diss = 2;
    static int parallel_dissonance = 2;
    static int parallel_perf_consonance = 1000;
    static int diss_cp_previous_cf = 1;
    static int direct_motion_perf_cons = 1;
    static int seq_of_same_cons = 1;
    static int seq_same_type_cons = 2;
    static int bad_diss_approach_from_diss = 1;
    static int diss_prev_cp_next_cf_unresolved = 1;
    static int is_not_tonic = 0;
    static int direct_motion_into_diss = 1;
    static int dissonant_with_root = 1;
    static int minor_9th = 100;
    static int octave = 10;
    
 public static void setPeak_Trough_Quota_Exceed (int my_ptqe){
    peak_trough_quota_exceed = my_ptqe;
    }   

public static void setMelodic_Motion_Quota_Exceed  (int my_mmqe) {
    melodic_motion_quota_exceed  = my_mmqe;
    }
public static void setSuccessive_Leaps (int my_sl) {
    successive_leaps = my_sl;
    }
public static void setBad_Motion_After_Leap (int my_bmal) {
    bad_motion_after_leap = my_bmal;
    }
public static void setOutside_Range (int my_or) {
    outside_range = my_or;
    }
public static void setRemote_From_Pitchcenter (int my_rfpc) {
    remote_from_pitchcenter = my_rfpc;
    }
public static void setIllegal_Note (int my_iln) {
    illegal_note = my_iln;
}

public static void setOverlap (int my_ov) {
    overlap = my_ov;
    }
public static void setAccented_Dissonance (int my_ad) {
    accented_dissonance = my_ad;
    }
public static void setBad_Diss_Approach_From_Cons (int my_bdafc) {
    bad_diss_approach_from_cons = my_bdafc;
    }
public static void setBad_Cons_Approach_From_Diss (int my_bcafd) {
    bad_cons_approach_from_diss = my_bcafd;
    }
public static void setMotion_Into_Diss_Both_Voices_Change (int my_midbvc) {
    motion_into_diss_both_voices_change  = my_midbvc;
    }
public static void setSeq_Of_Diss (int my_sd) {
    seq_of_diss = my_sd;
    }
public static void setSeq_Same_Type_Diss (int my_sstd) {
    seq_same_type_diss = my_sstd;
    }
public static void setParallel_Dissonance (int my_pard) {
    parallel_dissonance = my_pard;
    }
public static void setParallel_Perf_Consonance (int my_parpc) {
    parallel_perf_consonance = my_parpc;
    }
public static void setDiss_Cp_Previous_Cf (int my_discpc) {
    diss_cp_previous_cf  = my_discpc;
    }
public static void setDirect_Motion_Perf_Cons (int my_dmpcon) {
    direct_motion_perf_cons = my_dmpcon;
    }
public static void setSeq_Of_Same_Cons (int my_sosc) {
    seq_of_same_cons = my_sosc;
    }
public static void setSeq_Same_Type_Cons (int my_sstcon) {
    seq_same_type_cons = my_sstcon;
    }
public static void setBad_Diss_Approach_From_Diss (int my_bdafdi) {
    bad_diss_approach_from_diss = my_bdafdi;
    }
public static void setDiss_Prev_Cp_Next_Cf_Unresolved (int my_dpcncu) {
    diss_prev_cp_next_cf_unresolved = my_dpcncu;
    }
public static void setIs_Not_Tonic (int my_inott) {
    is_not_tonic = my_inott;
    }
public static void setDirect_Motion_Into_Diss (int my_dmid) {
direct_motion_into_diss = my_dmid;
    }
public static void setDissonant_With_Root (int my_dwr) {
    dissonant_with_root = my_dwr;
    }
public static void setMinor_9th (int my_m9) {
    minor_9th = my_m9;
    }
public static void setOctave (int my_oct) {
    octave = my_oct;
    }
}