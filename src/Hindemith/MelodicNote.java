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
 * Similar class to JFugue note but lacks many of the note methods 
 * in order to have an object that tracks accent. Basically stores 
 * and provides access methods to properties of a note. 
 * @author Trick's Music Boxes
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
