/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Hindemith;


import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import org.jfugue.DeviceThatWillReceiveMidi;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;

import org.jfugue.Player;


/**
 *
 * @author alyssa
 */
public class SynthOut {
    public static DeviceThatWillReceiveMidi device = null;
    public static Synthesizer synth;
    
    
    public static void setDevice () {
        MidiDevice.Info[] devices = MidiSystem.getMidiDeviceInfo();
        for (int i=0;i<devices.length;i++) {
            System.out.println("name: " + devices[i].getName() + "    Description: " + devices[i].getDescription());
            if (devices[i].getName().equals("Out To MIDI Yoke:  1") || devices[i].getName().startsWith("loopMIDI") ) {
                try {
                    device = new DeviceThatWillReceiveMidi(devices[i]);
                    MidiDevice mydevice = MidiSystem.getMidiDevice(devices[i]);
                    synth = MidiSystem.getSynthesizer();
                    //System.out.println("Assigned Midi Yoke");
                } 
                catch (MidiUnavailableException e) {
                }  
            }
        }
    }
    
    public static DeviceThatWillReceiveMidi getDevice () {
        return device;
    }
    
}
