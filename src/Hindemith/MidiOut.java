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


/**
 *
 * @author alyssa
 */
public class MidiOut {
    public static DeviceThatWillReceiveMidi device = null;
    
    public static void setDevice () {
        MidiDevice.Info[] devices = MidiSystem.getMidiDeviceInfo();
        for (int i=0;i<devices.length;i++) {
            //System.out.println("name: " + devices[i].getName() + "    Description: " + devices[i].getDescription());
            if (devices[i].getName().equals("Out To MIDI Yoke:  1") ) {
                try {
                    device = new DeviceThatWillReceiveMidi(devices[i]);
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
