/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Hindemith.RhythmModule;


import org.jfugue.*;

import java.util.Random;
/**
 *A Simple interface with only one method to generate an array of unpitched
 * melodic voices. 
 * @author Trick's Music Boxes
 */
public interface RhythmModule {
    Pattern[] generate(int pieceLength, int numberOfVoices);
    
}
