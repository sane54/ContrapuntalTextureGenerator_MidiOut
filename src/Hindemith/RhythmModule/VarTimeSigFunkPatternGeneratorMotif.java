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

public class VarTimeSigFunkPatternGeneratorMotif  implements RhythmModule{
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
			int tsig = roll.nextInt(3);
			System.out.println("tsig " + tsig);
			if (tsig == 0) beatsInBars[iteration] = 2;//2
			if (tsig == 1) beatsInBars[iteration] = 3;//3
			if (tsig == 2) beatsInBars[iteration] = 4;//4
		}
		
		System.out.println("starting with # of bars = to " + pieceLength);
		for (int voice = 0; voice < numberOfVoices; voice++) {     //for each voice
                    System.out.println("voice " + voice);
                    Pattern jPattern = new Pattern();
                    String motifString = ("Rs C4s C4s Rs");
                    jPattern.addElement(new Tempo(tempo));
                        for (int barNum = 0; barNum < pieceLength; barNum++) { // for each bar
                            System.out.println("bar " + barNum);
                            int beat = 1;
                            measure = beatsInBars[barNum];
                            jPattern.add("|");
                            //should also add time signature token but there is no jFugue string for it except in v5
                            System.out.println("measure length " + measure);
                            while (beat <= measure) 	{                      //for each beat
                                    System.out.println("beat " + beat);
                                    if (voice == 0) {
                                        patternIndex = roll.nextInt(5);
                                        if (patternIndex == 0) jPattern.add("A4s Rs Rs Rs");
                                        if (patternIndex == 1) jPattern.add("Rs Rs Rs Rs");
                                        if (patternIndex > 1) jPattern.add("A4s Rs C4s Rs");
                                    }
                                    else {
                                        if (roll.nextInt(4)> 1) jPattern.add(motifString);
                                        else {
                                                patternIndex = (roll.nextInt(16));
                                                String motifString2 = new String();
                                                if (patternIndex == 0) motifString2 = ("Rs Rs Rs Rs");
                                                if (patternIndex == 1) motifString2 = ("Rs Rs Rs C4s");
                                                if (patternIndex == 2) motifString2 = ("Rs Rs C4s Rs");
                                                if (patternIndex == 3) motifString2 = ("Rs Rs C4s C4s");
                                                if (patternIndex == 4) motifString2 = ("Rs C4s Rs Rs");
                                                if (patternIndex == 5) motifString2 = ("Rs C4s Rs C4s");
                                                if (patternIndex == 6) motifString2 = ("Rs C4s C4s Rs");
                                                if (patternIndex == 7) motifString2 = ("Rs C4s C4s C4s");
                                                if (patternIndex == 8) motifString2 = ("A4s Rs Rs Rs");
                                                if (patternIndex == 9) motifString2 = ("A4s Rs Rs C4s");
                                                if (patternIndex == 10) motifString2 = ("A4s Rs C4s Rs");
                                                if (patternIndex == 11) motifString2 = ("A4s Rs C4s C4s");
                                                if (patternIndex == 12) motifString2 = ("A4s C4s Rs Rs");
                                                if (patternIndex == 13) motifString2 = ("A4s C4s Rs C4s");
                                                if (patternIndex == 14) motifString2 = ("A4s C4s C4s Rs");
                                                if (patternIndex == 15) motifString2 = ("A4s C4s C4s C4s");
                                                if (patternIndex == 16) motifString2 = ("A4q");   // you could rewrite the program where 
                                                if (patternIndex == 17) motifString2 = ("A4i Rs C4s");  // every beginning of a quarter is accented
                                                if (patternIndex == 18) motifString2 = ("A4i C4i");
                                                if (patternIndex == 19) motifString2 = ("A4i C4s C4s");
                                                if (patternIndex == 20) motifString2 = ("A4s C4i Rs");
                                                if (patternIndex == 21) motifString2 = ("A4s C4i C4s");
                                                if (patternIndex == 22) motifString2 = ("A4s C4s C4i");
                                                if (patternIndex == 23) motifString2 = ("A4s C4s C4s C4s");
                                                jPattern.add(motifString2);
                                                //if (roll.nextInt(4)== 0) motifString = motifString2;
                                                }
                                        }
                                    beat++;

                                    //System.out.println("patternIndex " + patternIndex);
                                    System.out.println(jPattern.getMusicString());
                            }
			
			}
                    VoiceArray[voice] = jPattern;
                    System.out.println("finished voice " + voice);
                   // Player my_player = new Player();
                   // my_player.play(jPattern);
		}
	return VoiceArray;
    }
}
