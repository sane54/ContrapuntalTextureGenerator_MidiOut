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

import java.util.ArrayList;


/**
 * A melodic voice is an array of melodic notes with some additional properties. 
 * A voice has an upper and lower bound, in order to simulate the classic vocal 
 * ranges. Also the pitches of the notes in the voice must be clustered around 
 * a pitch center and not deviate too much from the center. 
 * @author Trick's Music Boxes
 */
public class MelodicVoice {
    private ArrayList<MelodicNote> note_arraylist = new ArrayList();
    enum VoiceRange {bass, tenor, alto, soprano, ultra};
    private VoiceRange range_id;
    private int range_min;
    private int range_max;
    private int pitch_center;

    /**
     * Takes a string designating a voice range as input and sets the upper and 
     * lower bounds accordingly.
     * @param input_range_id bass, tenor, alto, soprano, ultra
     */
    public void setRange (String input_range_id) {
        
        if ("bass".equals(input_range_id)) range_id = VoiceRange.bass;
        if ("tenor".equals(input_range_id)) range_id = VoiceRange.tenor;
        if ("alto".equals(input_range_id)) range_id = VoiceRange.alto;
        if ("soprano".equals(input_range_id)) range_id = VoiceRange.soprano;
        if ("ultra".equals(input_range_id)) range_id = VoiceRange.ultra;
        
        
        if (range_id == VoiceRange.bass) {
            range_min = 24;
            range_max = 48;
        }
        else if (range_id == VoiceRange.tenor) {
            range_min = 48;
            range_max = 72;
        }
        else if (range_id == VoiceRange.alto) {
            range_min = 54;
            range_max = 78;
        }
        else if (range_id == VoiceRange.soprano) {
            range_min = 60;
            range_max = 84;
        }
        else if (range_id == VoiceRange.ultra) {
            range_min = 84;
            range_max = 108;
        }
    }

    public void setPitchCenter (int input_pitch_center) {
        pitch_center = input_pitch_center;
    }
    
    public void addMelodicNote (MelodicNote input_melodic_note){
        note_arraylist.add(input_melodic_note);
    }
    
    public MelodicNote getMelodicNote (int index){
       return  note_arraylist.get(index);
    }
    
    public int getVoiceLength() {
        return note_arraylist.size();
    }
    
    public ArrayList getNoteArray(){
        return note_arraylist;
    }
    
    public void setNoteArray(ArrayList<MelodicNote> input_note_array){
        note_arraylist  = input_note_array;
    }
    
    public int getRangeMin(){
        return range_min;
    }
    
    public int getRangeMax(){
        return range_max;
    }
    public VoiceRange getRangeId(){
        return range_id;
    }
    
    public int getPitchCenter(){
        return pitch_center;
    }
    
}

