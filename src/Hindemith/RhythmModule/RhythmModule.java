/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Hindemith.RhythmModule;


import org.jfugue.*;

import java.util.Random;
/**
 *
 * @author alyssa
 */
public interface RhythmModule {
    Pattern[] generate(int pieceLength, int numberOfVoices);
    
}
