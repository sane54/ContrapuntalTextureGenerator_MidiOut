/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Hindemith;

import org.jfugue.*;
import java.util.ArrayList;
/**
 *
 * @author alyssa
 */
public class AccentListener implements ParserListener{
 /*
    Marks accents in a jfugue music string produced by a Rhythm module
    Accents are evaluated slightly in hindsight or retrograde since a note 
    can only been analyzed for accented-ness once it has ended, ie when the 
    parser detects another note. Most notes are added to the output with their
    accented status in limbo. The exception is the first accented note of the pattern
    which is added to the output with its accented status turned on. 
    
    The output is an array of melodic notes which differ from Jfugue notes in that they
    have an accent value and also record the time elapsed up until that point. 
    
    */
    Double duration_tally = 0.00; //time as a division of a whole note up to this point
    //Double previous_duration = 0.00; Not used
    ArrayList<MelodicNote> melody_note_array = new ArrayList();
    MusicStringParser parser = new MusicStringParser();//uses the jfugue parser
    int ticker = 0; //Ticker measures ticks within an eighth note here 0 or 1
    int pending_note_index = -1; //Index of the note that is still "open"
    int pattern_index =0; //Index of this note in the pattern
    Double pending_duration = 0.00; //Duration total of the current open note
    Double prior_duration = 0.00;//Duration of the note prior to the pending
    boolean syncopate = false; //A flag to determine if a beat is held over
    int prior_index = 0; //Index in melodic_note_array of the prior to pending note
    
    public ArrayList<MelodicNote> listen(Pattern p) {
        parser.addParserListener(this);
        System.out.println("about to parse" + p.getMusicString());
        try {
            parser.parse(p);
        } catch (JFugueException e)
        {
            e.printStackTrace();
        }
        System.out.println("accentlistener returning array");
        printArray(melody_note_array);
        return melody_note_array;
    }

    public void voiceEvent(Voice voice){
		
	}

 
    public void tempoEvent(Tempo tempo) {
		
	}

    public void instrumentEvent(Instrument instrument){
	}

    
    public void layerEvent(Layer layer){
		
	}

    
    public void measureEvent(Measure measure){
	}
    
    
    public void timeEvent(Time time){
	}
    
    
    public void keySignatureEvent(KeySignature keySig){
	}
    
    
    public void controllerEvent(Controller controller){
	}
    
    
    public void channelPressureEvent(ChannelPressure channelPressure){
	}
    
    
    public void polyphonicPressureEvent(PolyphonicPressure polyphonicPressure){
	}
    
    
    public void pitchBendEvent(PitchBend pitchBend){
	}

    
    @Override
    public void noteEvent(Note note){
         System.out.println("about to run get accent on " + note.getMusicString() + "with duration " + note.getDecimalDuration()); 
         getAccent(note, note.getDecimalDuration());	
	}

    
    public void sequentialNoteEvent(Note note){
	}

    
    public void parallelNoteEvent(Note note){
	}
	
    public void getAccent(Note note, double duration ) {

        Boolean accented = false; //only used for first accent in pattern
        Boolean is_rest = false;
        double start_time = duration_tally;
        duration_tally = duration_tally + duration;
        ticker = (int)(duration_tally * 16)%2;
        System.out.println("ticker = " + ticker);
        
        if ((note.getMusicString().contains("C0") || note.getMusicString().contains("R"))) {
            is_rest = true;
            System.out.println(note.getMusicString() + "is a rest");
        }
        if( pending_note_index >= 0){             
            //If Note is not "R" (ie a rest)
            if (!is_rest) {
                //The note not being a rest closes out the previous pending note
                //Pending duration is now that note's effective duration
                //regardless of how short the sounding duration of the note is
                
                //We can now determine if this note should be accented
                //The first criteria is that it's longer than a certain threshold value
                //In the case of most funky music that is the eighth note
                if (pending_duration > 0.125){
                    melody_note_array.get(pending_note_index).setAccent(true);
                    System.out.println("greater or = than 3 16ths rule applied");
                }
                //If it isn't greater than the threshold value is it syncopated
                //To be syncopated it must be longer than the minimum unit
                //In the case of funk, that's a 16th
                //It also must be held over from the previous tick
                //If it isn't syncopated, it still might be accented if it is longer
                //than the previous note
                
                else {
                    if (pending_duration  > 0.0625) {
                        if (syncopate) {
                            melody_note_array.get(pending_note_index).setAccent(true);
                            syncopate = false;
                            System.out.println("syncopate rule applied");
                        }
                        if (pending_duration > prior_duration && prior_duration >= 0) {
                            // the second part of the condition after the "and" isn't needed
                            melody_note_array.get(pending_note_index).setAccent(true);
                            System.out.println("greater than prior note rule applied");
                            
                        }

                    }
                }
                
                //If the note isn't accented yet, there's still one test
                //regardless of its size, which is: if it's as long as the prior 
                //note and the prior note is not accented, then this pending note
                //is accented.
                if (pending_duration == prior_duration) {
                    if (!melody_note_array.get(prior_index).getAccent())
                            melody_note_array.get(pending_note_index).setAccent(true);    
                }

                //new pending and prior indexes and durations
                prior_index = pending_note_index;
                pending_note_index = pattern_index;
                prior_duration = pending_duration;
                pending_duration = duration;

                if (ticker == 0) {
                    syncopate = true; 
                    //if ticker == 0 this means that everything up to this point
                    //terminated on the first tick of an eigth note
                    //which means that THIS note STARTS on the second tick 
                    //of an eighth note, which means it will be a syncopation
                    //if it's held over into the next eighth note duration
                    System.out.println("syncopate flag set");
                }
            }

            //Else Just add the rest's duration to the previous duration 
            else {
                System.out.println("adding rest to pending_duration");
                pending_duration += duration;
            }
        }
        /*else {
            if ((note.getMusicString().contains("C0sa0d0") || note.getMusicString().contains("R"))) is_rest = true;
        }*/
        if (note.getMusicString().contains("A")) {
            accented = true;
            System.out.println("Start of Quarter Rule Applied");
        }
        MelodicNote my_melody_note = new MelodicNote();
        my_melody_note.setDuration(duration);
        my_melody_note.setStartTime(start_time);
        my_melody_note.setAccent(accented);
        my_melody_note.setTotalVoiceDuration(duration_tally);
        
        if (is_rest) my_melody_note.setRest(true);
        else if (pending_note_index == -1) {
            pending_note_index = 0;
            pending_duration = duration;
            System.out.println("creating pending note index");
        }
        melody_note_array.add(my_melody_note);
        pattern_index++;
    }   
    
    public void printArray(ArrayList<MelodicNote> melody_note_array ) {
        
             
        for(MelodicNote m_note : melody_note_array) {
            String pitch_string = "RR";
            String acc_string = "A";
            if(m_note.getRest() == false) pitch_string = "P";
            if(m_note.getAccent()== false) acc_string = "N";
            if(m_note.getAccent()== true) acc_string = "A";
            System.out.print( acc_string+ pitch_string + m_note.getDuration());
            
        }
    }
}
