/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Hindemith;

/**
 *
 * @author alyssa
 */
public class MelodicNote {
        int pitch; 
        double duration, start_time, total_voice_duration_including_this_note;
	boolean accented, rest;

	public void setPitch( int input_pitch) {
	pitch= input_pitch;
	}
        
        public void setDuration( double input_duration) {
	duration = input_duration;
	}

	public void setStartTime ( double input_start_time) {
	start_time = input_start_time;
	}
			
	public void setTotalVoiceDuration(double input_total_duration) {
	total_voice_duration_including_this_note = input_total_duration;
	}

	public void setAccent(boolean input_accented) {
	accented = input_accented;
	}
        
        public void setRest(boolean input_accented) {
	rest = input_accented;
	}
        
	public double getDuration() {
	return duration;
	}

	public double getStartTime() {
	return start_time;
	}
			
	public double getPreviousDuration() {
	return total_voice_duration_including_this_note;
	}

	public boolean getAccent() {
	return accented;
	}
        
        public boolean getRest() {
	return rest;
	}
        
        public Integer getPitch() {
	return pitch;
	}
}
