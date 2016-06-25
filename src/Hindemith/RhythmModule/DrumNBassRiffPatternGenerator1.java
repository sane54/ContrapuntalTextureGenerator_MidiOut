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

public class DrumNBassRiffPatternGenerator1  implements RhythmModule{
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
            beatsInBars[iteration] = 8;
        }

        System.out.println("starting with # of bars = to " + pieceLength);
        for (int voice = 0; voice < numberOfVoices; voice++) {     //for each voice
            System.out.println("voice " + voice);
            Pattern jPattern = new Pattern();
            jPattern.addElement(new Tempo(tempo));
            String voicePatternString = "Rw Rw";
            for (int barNum = 0; barNum < pieceLength-1; barNum++) {
		if(voice == 0) {//harmony - this may not work - may have to use 16th notes
                    patternIndex = roll.nextInt(5);
                    //patternIndex = 3;
                    switch (patternIndex) {
                        case 0: 	voicePatternString = "A4s Rs Rs Rs Rs Rs Rs Rs Rs Rs Rs Rs Rs Rs Rs Rs Rs Rs Rs Rs Rs Rs Rs Rs Rs Rs Rs Rs Rs Rs Rs Rs";
                                                break;
                        case 1:		voicePatternString = "A4s Rs Rs Rs A4s Rs Rs Rs Rs Rs Rs Rs Rs Rs Rs Rs Rs Rs Rs Rs Rs Rs Rs Rs Rs Rs Rs Rs Rs Rs Rs Rs";
                                                break;
                        case 2:		voicePatternString = "A4s Rs Rs Rs A4s Rs Rs Rs Rs Rs Rs Rs Rs Rs Rs Rs A4s Rs Rs Rs Rs Rs Rs Rs Rs Rs Rs Rs Rs Rs Rs Rs";
                                                break;
                        case 3:		voicePatternString = "A4s Rs Rs Rs Rs Rs A4s Rs Rs Rs Rs Rs Rs Rs Rs Rs Rs Rs Rs Rs Rs Rs Rs Rs Rs Rs Rs Rs Rs Rs Rs Rs";
                                                break;
                        case 4:		voicePatternString = "A4s Rs Rs Rs Rs Rs A4s Rs Rs Rs Rs Rs Rs Rs Rs A4s Rs Rs Rs Rs Rs Rs Rs Rs Rs Rs Rs Rs Rs Rs Rs Rs";
                                                break;
                        default: 	voicePatternString = "A4s Rs Rs Rs Rs Rs Rs Rs Rs Rs Rs Rs A4s Rs Rs Rs Rs Rs Rs Rs Rs Rs Rs Rs Rs Rs Rs Rs Rs Rs Rs Rs";	
                    }
                jPattern.add(voicePatternString);
                }
                if(voice == 1) { //sub voice
                    if (roll.nextInt(1) == 0) {//roll for Repeat
                        patternIndex = roll.nextInt(20);
                        //patternIndex = 1;
                        switch (patternIndex) {
                            case 0:		voicePatternString = "Ri A4q C4i A4s Rs C4s Rs A4i Ri Rw";
                                                break;
                            case 1:             voicePatternString = "A4i C4i C4i A4i Ri A4h Ri Rh.";
                                                break;
                            case 2:		voicePatternString = "Ri A4i A4i. A4i. C4i A4i. A4i. A4i Rh.";
                                                break;
                            case 3:		voicePatternString = "A4q Rq Rs A4i. A4i. A4i. C4i A4q Rh";
                                                break;
                            case 4:		voicePatternString = "A4i. C4i A4i. Ri. A4i. C4i A4i. C4i A4i. Rh";
                                                break;
                            case 5:		voicePatternString = "Rh. A4h. Rh";
                                                break;
                            case 6:		voicePatternString = "Rq A4q. A4q. Rw";
                                                break;
                            case 7:		voicePatternString = "A4q c4q A4h A4q. A4q. Rq";
                                                break;
                            case 8:             voicePatternString = "Rq A4h C4i C4i Rq A4q. A4q.";
                                                break;
                            case 9:		voicePatternString = "Ri A4h Ri C4i A4i Rw";
                                                break;
                            case 10:            voicePatternString = "Rh. Ri A4i. A4i. A4h.";
                                                break;
                            case 11:            voicePatternString = "Ri. A4i. A4i. A4i. Ri C4i A4i. C4s A4q Rh";
                                                break;
                            default:            voicePatternString = "Rw Rw";
                        }
                    }
                    jPattern.add(voicePatternString);
                }

                if(voice == 2) { //reese voice
                    if (roll.nextInt(2) == 0) {//roll for Repeat
                        patternIndex = roll.nextInt(20);
                        switch (patternIndex) {
                            case 0:		voicePatternString = "A4s Rs Rs C4s Rh. Rw";
                                                    break;
                            case 1:             voicePatternString = "A4s Rs Rs C4s Rq. C4i A4s A4s Ri A4i. A4i. A4q C4i";
                                                    break;
                            case 2:		voicePatternString = "A4s Rs Rs C4s Rs Rs A4i Rs A4i Rs  A4s Rs A4s Rs A4s Rs Rs C4s Rs Rs A4i Rs A4i Rs  A4s Rs A4s Rs";
                                                    break;
                            case 3:		voicePatternString = "A4i C4s C4s C4s A4s Rs C4s  A4i C4s C4s C4s A4s Rs C4s A4i C4s C4s C4s A4s Rs C4s A4i C4s C4s C4s A4s Rs C4s";
                                                    break;
                            case 4:		voicePatternString = "A4q. Rs A4q A4q Rs A4q. Rs A4q A4q Rs";
                                                    break;
                            case 5:		voicePatternString = "A4i. A4q Rs A4i. A4q Rs Rw";
                                                    break;
                            case 6:		voicePatternString = "A4q. A4q. A4q. A4q Rh";
                                                    break;
                            case 7:		voicePatternString = "A4q c4q A4h A4q. A4q. Rq";
                                                 break;
                            case 8: 	voicePatternString = "Rs A4i. A4s Ri A4i. A4i Rq Rs A4i. A4s Ri A4i. A4i Rq";
                                        break;
                            case 9:	voicePatternString = "C4i. A4i. Ri A4i A4i Rh A4q Rh";
                                        break;
                            case 10:	voicePatternString = "A4i. C4i. A4i. C4i. Ri C4i A4i C4i A4q A4q Rq";
                                        break;
                            case 11:	voicePatternString = "Ri. A4i. A4i. A4i. Ri C4i A4i. C4s A4q Rh";
                                        break;
                            default:	voicePatternString = "Rw Rw";
                        }
                    }
                    jPattern.add(voicePatternString);
                }
								
                if(voice == 3) { //main voice
                    if (roll.nextInt(2) == 0) {//roll for Repeat
                        patternIndex = roll.nextInt(11);
                        switch (patternIndex) {
                            case 0:		voicePatternString = "A4q A4s Rs Rs A4s Rs C4s C4s A4s Ri C4s Rs A4q A4s Rs Rs A4s Rs C4s C4s A4s Ri C4s Rs";
                                            break;
                            case 1:		voicePatternString = "A4s Rs A4s Rs A4s Rs C4s A4s Rs C4s C4i A4i C4i A4s Rs A4s Rs A4s Rs C4s A4s Rs C4s C4i A4q";
                                            break;
                            case 2:		voicePatternString = "A4s Rs A4s Rs A4s Rs Rs A4s Rs Rs C4i A4q A4s Rs A4s Rs A4s Rs C4s A4s Rs C4s C4i A4q";
                                            break;
                            case 3:		voicePatternString = "A4i C4s C4s A4i C4s C4s C4s C4s A4i A4s C4s C4s C4s A4i C4s C4s A4i C4s C4s C4s C4s A4q C4i";
                                            break;
                            case 4:		voicePatternString = "A4q A4i. C4s A4s C4s A4i C4s C4s C4s C4s A4q A4i. C4s A4s C4s A4i A4q";
                                            break;
                            case 5:		voicePatternString = "A4q A4q. C4i A4q A4q A4q. C4i A4q";
                                            break;
                            case 6:		voicePatternString = "A4q A4i. C4i. C4i A4q A4q A4q A4q C4s C4s C4s C4s";
                                            break;
                            case 7:		voicePatternString = "A4q C4i C4i A4i A4i Ri C4i A4q C4i A4q A4i Ri C4i";
                                            break;
                            case 8:		voicePatternString = "A4i C4i A4i. A4i C4s C4i A4q A4i C4i A4i. A4i C4s C4i A4q";
                                            break;
                            case 9:		voicePatternString = "A4i C4i A4s A4i A4i C4s C4i A4i C4s C4s A4i C4i A4s A4i A4i C4s C4i A4q";
                                            break;
                            default:	voicePatternString = "A4w Rw";
                        }

                    }
                    jPattern.add(voicePatternString);
                }

                if(voice == 4) { //melody
                    if (roll.nextInt(2) == 0) {//roll for Repeat
                    patternIndex = roll.nextInt(11);
                    switch (patternIndex) {
                        case 0: 	voicePatternString = "A4w.. A4q";
                                        break;
                        case 1:		voicePatternString = "A4w.. Rq";
                                        break;
                        case 2: 	voicePatternString = "A4i C4i A4h. A4q. A4h Ri";
                                        break;
                        case 3: 	voicePatternString = "A4i C4i A4h. A4w";
                                        break;
                        case 4: 	voicePatternString = "A4i C4i A4w..";
                                        break;
                        case 5: 	voicePatternString = "A4i. C4i. C4i A4h C4i. C4i. C4i A4h";
                                        break;
                        case 6: 	voicePatternString = "A4q. A4w. Ri";
                                        break;
                        case 7: 	voicePatternString = "A4q. A4q. C4q A4w";
                                        break;
                        case 8: 	voicePatternString = "A4q. C4i A4q. C4i A4q. C4i A4q. C4i";
                                        break;
                        case 9: 	voicePatternString = "A4q. C4i A4q. C4i A4w";
                                        break;
                        case 10:	voicePatternString = "A4i C4i A4h. A4i C4i A4q A4h";
                                        break;
                        default:	voicePatternString = "Rw Rw"; 		
                    }
                    jPattern.add(voicePatternString);
                }								
            }
            if(voice == 5) { //hats
                if (roll.nextInt(2) == 0) {//roll for Repeat
                patternIndex = roll.nextInt(11);
                    switch (patternIndex) {
                        case 0: 	voicePatternString = "A4s C4s C4s A4s C4s C4s A4s C4s A4s C4s C4s C4s A4s Ri. A4s C4s C4s A4s C4s C4s A4s C4s A4s C4s C4s C4s A4s Ri.";
                                        break;
                        case 1:		voicePatternString = "Ri C4s Rs A4s C4s A4s Rs A4s C4s A4s Rs Ri C4s Rs Ri C4s Rs A4s C4s A4s Rs A4s C4s A4s Rs Ri C4s Rs";
                                        break;
                        case 2: 	voicePatternString = "A4s Rs C4s Rs A4s C4s C4s C4s A4s Rs C4s C4s A4s Rs C4s Rs A4s Rs C4s Rs A4s C4s C4s C4s A4s Rs C4s C4s A4s Rs C4s Rs";
                                        break;
                        case 3: 	voicePatternString = "A4s Rs C4s Rs A4s Rs C4s C4s A4s Rs C4s Rs A4s Rs C4s Rs A4s Rs C4s Rs A4s Rs C4i A4s Rs C4s Rs A4s Rs C4s Rs";;
                                        break;
                        case 4: 	voicePatternString = "A4s C4s A4i  A4i C4s C4s A4s Rs  C4s Rs A4s Rs A4i A4s C4s A4i  A4i C4s C4s A4s Rs  C4s Rs A4s Rs A4i";;
                                        break;
                        case 5: 	voicePatternString = "A4s C4s C4s C4s A4i C4s C4s A4s C4s C4s C4s A4s Rs A4i A4s C4s C4s C4s A4i C4s C4s A4s C4s C4s C4s A4s Rs A4i";
                                        break;
                        case 6: 	voicePatternString = "A4s Rs A4s Rs A4s Rs C4s Cs A4i C4i A4i C4i A4s Rs A4s Rs A4s Rs C4s Cs A4i C4i A4i C4i";
                                        break;
                        case 7: 	voicePatternString = "A4s C4s C4i A4i C4s C4s C4s C4s A4q C4i A4i C4i A4i C4s C4s C4s C4s C4i A4i C4i";
                                        break;
                        case 8: 	voicePatternString = "A4q C4s C4s Ri. C4s C4s C4s A4s C4s C4s C4s A4i C4i C4s C4s Ri. C4s C4s C4s A4s C4s C4s C4s";
                                        break;
                        case 9: 	voicePatternString = "A4i C4i C4i C4i A4i C4i C4i C4i";
                                        break;
                        default:	voicePatternString = "Rw Rw"; 		
                    }   
                jPattern.add(voicePatternString);
                }
            }
        }
        jPattern.add("A4w");
        VoiceArray[voice] = jPattern;
        //System.out.println("finished voice " + voice);
        // Player my_player = new Player();
        // my_player.play(jPattern);
               
    }
    return VoiceArray;
    
    }
}
