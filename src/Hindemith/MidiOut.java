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


import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import org.jfugue.DeviceThatWillReceiveMidi;
import javax.sound.midi.MidiUnavailableException;


/**
 * If MIDI-Yoke or loopMIDI ports are installed on the machine, this class 
 * contains methods for finding, storing and accessing properties of that
 * MIDI ports. This enables music to be routed to an external DAW or standalone 
 * synthesizer or sampler. 
 * @author Trick's Music Boxes
 */
public class MidiOut {
    public static DeviceThatWillReceiveMidi device = null;
    /**
    * Currently the first port found is chosen. MIDI-Yoke
    * is 32 - bit software and is not visible as a Midi device in 64 bit IDE 
    * environments. Note that this method does not throw a MidiUnavailableException.
    * It will, in fact, not assign a working device unless the catch 
    * MidiUnavailableException is blank. 
     */
    public static void setDevice () {
        MidiDevice.Info[] devices = MidiSystem.getMidiDeviceInfo();
        for (int i=0;i<devices.length;i++) {
            System.out.println("name: " + devices[i].getName() + "    Description: " + devices[i].getDescription());
            if (devices[i].getName().equals("Out To MIDI Yoke:  1") || devices[i].getName().startsWith("loopMIDI") || devices[i].getName().startsWith("JW") ) {
                System.out.println("found midi port");
                try {
                    device = new DeviceThatWillReceiveMidi(devices[i]);
                    System.out.println("Assigned Midi Port");
                } 
                catch (MidiUnavailableException e) {
                }  
            }
        }
    }
    /**
     * @return device a JFugue DeviceThatWillReceiveMidi
     */
    public static DeviceThatWillReceiveMidi getDevice () {
        return device;
    }
    
}
