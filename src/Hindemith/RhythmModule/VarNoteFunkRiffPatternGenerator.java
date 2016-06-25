/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Hindemith.RhythmModule;

import Hindemith.RhythmModule.RhythmModule;

/**
 *
 * @author alyssa
 */

import org.jfugue.*;
import java.util.Random;

public class VarNoteFunkRiffPatternGenerator  implements RhythmModule{
    @Override
    public  Pattern[] generate(int pieceLength, int numberOfVoices) {
		Random roll = new Random();
		int patternIndex;
                int tempo = 80;
                int measure;

		
		
		int beatsInBars [] = new int[pieceLength];
		Pattern VoiceArray [] = new Pattern[numberOfVoices];
		
		//Loop to generate time signature changes in terms of bar lengths
		for (int iteration = 0; iteration < pieceLength; iteration++) {
                    beatsInBars[iteration] = 4;
		}
		
		System.out.println("starting with # of bars = to " + pieceLength);
		for (int voice = 0; voice < numberOfVoices; voice++) {     //for each voice
                    System.out.println("voice " + voice);
                    Pattern jPattern = new Pattern();
                    jPattern.addElement(new Tempo(tempo));
                        for (int barNum = 0; barNum < pieceLength-1; barNum++) {
                            jPattern.add("|");

                                if(voice == 0) jPattern.add("A4s Rs Rs Rs Rs Rs Rs Rs Rs Rs Rs Rs Rs Rs Rs Rs");
                                else if (voice == 1) {
                                        patternIndex = roll.nextInt(2);
                                        if (patternIndex == 0) jPattern.add("A4i C4s C4s A4i. A4q A4i C4i C4s");
                                        if (patternIndex == 1) jPattern.add("A4s C4s Rs A4i C4i C4S A4q A4q");
                                        if (patternIndex > 1) jPattern.add("Rw");
                                }       
                                else if (voice == 2) {
                                        patternIndex = roll.nextInt(2);
                                        if (patternIndex == 0) jPattern.add("A4q A4q. A4i A4q");
                                        if (patternIndex == 1) jPattern.add("Ri  A4q  C4i A4i. A4i. C4i");
                                        if (patternIndex >1) jPattern.add("Rw");
                                }
                                else  {
                                        patternIndex = roll.nextInt(3);
                                        if (patternIndex == 0) jPattern.add("Rq A4i. A4s Rq A4q");
                                        if (patternIndex == 1) jPattern.add("Ri A4q  C4s A4i C4s A4i A4i C4s C4s");
                                        if (patternIndex == 2) jPattern.add("Rs C4s C4s C4s A4s C4s C4s A4q Ri. A4s Cs");
                                        if (patternIndex > 2) jPattern.add("Rw");
                                }
			}
                    jPattern.add("A4w");
                    VoiceArray[voice] = jPattern;
                    System.out.println("finished voice " + voice);
                   // Player my_player = new Player();
                   // my_player.play(jPattern);
		}
	return VoiceArray;
    }
}
