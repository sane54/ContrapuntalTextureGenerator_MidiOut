/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Hindemith.view;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tooltip;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;



/**
 *
 * @author alyssa
 */
public class FXMLDocumentController implements Initializable {
    
    @FXML
    ComboBox<String> modeModuleComboBox = new ComboBox<String>();
    @FXML
    ComboBox<String> rhythmModuleComboBox = new ComboBox<String>();
    
    @FXML
    TextField tempoField = new TextField();
    
    @FXML
    TextField pieceLengthField = new TextField();
    
    @FXML
    Button RunButton = new Button();
    
    @FXML
    Button fileChooserButton = new Button();
    
    @FXML
    CheckBox cons_min2_chk = new CheckBox();
    @FXML
    CheckBox cons_maj2_chk = new CheckBox();
    @FXML
    CheckBox cons_min3_chk = new CheckBox();    
    @FXML
    CheckBox cons_maj3_chk = new CheckBox();
    @FXML
    CheckBox cons_p4_chk = new CheckBox();
    @FXML
    CheckBox cons_tt_chk = new CheckBox();
    @FXML
    CheckBox cons_p5_chk = new CheckBox();
    @FXML
    CheckBox cons_min6_chk = new CheckBox();
    @FXML
    CheckBox cons_maj6_chk = new CheckBox();
    @FXML
    CheckBox cons_dom7_chk = new CheckBox();
    @FXML
    CheckBox cons_maj7_chk = new CheckBox();
    @FXML
    CheckBox cons_oct_chk = new CheckBox();    
    @FXML
    CheckBox pcons_min2_chk = new CheckBox();
    @FXML
    CheckBox pcons_maj2_chk = new CheckBox();
    @FXML
    CheckBox pcons_min3_chk = new CheckBox();    
    @FXML
    CheckBox pcons_maj3_chk = new CheckBox();
    @FXML
    CheckBox pcons_p4_chk = new CheckBox();
    @FXML
    CheckBox pcons_tt_chk = new CheckBox();
    @FXML
    CheckBox pcons_p5_chk = new CheckBox();
    @FXML
    CheckBox pcons_min6_chk = new CheckBox();
    @FXML
    CheckBox pcons_maj6_chk = new CheckBox();
    @FXML
    CheckBox pcons_dom7_chk = new CheckBox();
    @FXML
    CheckBox pcons_maj7_chk = new CheckBox();
    @FXML
    CheckBox pcons_oct_chk = new CheckBox();    
    
    
    @FXML
    Slider AccentDSlider = new Slider();
    @FXML
    Slider BadApproachDfromDSlider = new Slider();
    @FXML
    Slider BadApproachDfromCSlider = new Slider();
    @FXML
    Slider BothChangetoDSlider = new Slider();
    @FXML
    Slider DirectMotionIntoDSlider = new Slider();
    @FXML
    Slider Min9thSlider = new Slider();
    @FXML
    Slider BadApproachCfromDSlider = new Slider();
    @FXML
    Slider DirectMotionIntoPerfCSlider = new Slider();
    @FXML
    Slider SeqOfDSlider = new Slider();
    @FXML
    Slider SeqOfSameDSlider = new Slider();
    @FXML
    Slider ParallelDSlider = new Slider();
    @FXML
    Slider DWithPrevCFSlider = new Slider();
    @FXML
    Slider ParallelPerfCSlider = new Slider();
    @FXML
    Slider SeqOfSameCSlider = new Slider();
    @FXML
    Slider SeqOfSameTypeCSlider = new Slider();
    @FXML
    Slider OctaveSlider = new Slider();
    @FXML
    Slider PrevCPDWithCFUnresolvedSlider = new Slider();
    
    @FXML
    Slider MelodyRepeatsPeakSlider = new Slider(); 
    @FXML
    Slider ExcessSimilarSlider = new Slider();
    @FXML
    Slider SuccessiveLeapSlider = new Slider();
    @FXML
    Slider BadMotionAfterLeapSlider = new Slider();
    @FXML
    Slider OutsideVoiceRangeSlider = new Slider();
    @FXML
    Slider RemotePitchCenterSlider = new Slider();
    @FXML
    Slider OverlappingVoiceSlider = new Slider();
    
    @FXML
    CheckBox checkboxVoice1 = new CheckBox();
    @FXML
    CheckBox checkboxVoice2 = new CheckBox();
    @FXML
    CheckBox checkboxVoice3 = new CheckBox();
    @FXML
    CheckBox checkboxVoice4 = new CheckBox();
    @FXML
    CheckBox checkboxVoice5 = new CheckBox();
    
    @FXML
    ComboBox comboboxVoice1 = new ComboBox();
    @FXML
    ComboBox comboboxVoice2 = new ComboBox();
    @FXML
    ComboBox comboboxVoice3 = new ComboBox();
    @FXML
    ComboBox comboboxVoice4 = new ComboBox();
    @FXML
    ComboBox comboboxVoice5 = new ComboBox();
    
    @FXML
    ChoiceBox choiceboxDissOK = new ChoiceBox();
    
    Stage ctrlStage = new Stage(); //Do i need this?
    String filePath = new String(); //Do i need this?
    
    ArrayList<Integer> cons = new ArrayList();
    ArrayList<Integer> pcons = new ArrayList();
    ArrayList<String> voices = new ArrayList();
    
    Boolean firstRun = true;
    static Boolean runCancelled = false;
    
    static ProgressTicker progress = new ProgressTicker();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        
        modeModuleComboBox.setValue("Lydian");
        rhythmModuleComboBox.setValue("Drum and Bass Patterns 1");
        choiceboxDissOK.setValue("No");
        comboboxVoice1.setValue("bass");
        comboboxVoice2.setValue("tenor");
        comboboxVoice3.setValue("alto");
        comboboxVoice4.setValue("soprano");
        comboboxVoice5.setValue("ultra");
        checkboxVoice1.setSelected(true);
        //checkboxVoice2.setSelected(true);
        //checkboxVoice3.setSelected(true);
        
        tempoField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable,
                String oldValue, String newValue) {
                int tempo= 120;
                try {
                    tempo = Integer.parseInt(newValue);
                }//endd try
                catch (NumberFormatException e) {
                    tempoField.setText("120");
                }//end catch
                    
                if (tempo < 1 || tempo > 2000) {
                Platform.runLater(() -> { tempoField.clear();});
                //tempoField.setText("120")
                }//end if block
            }//ends changed method
        });
        tempoField.setTooltip(new Tooltip 
                        ("has to be between 1 and 2000"));
        
        pieceLengthField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable,
                String oldValue, String newValue) {
                int pclen= 4;
                try {
                    pclen = Integer.parseInt(newValue);
                }//endd try
                catch (NumberFormatException e) {
                    pieceLengthField.setText("4");
                }//end catch
                    
                if (pclen < 1 || pclen > 2000) {
                Platform.runLater(() -> { pieceLengthField.clear();});
                //pieceLengthField.setText("4")
                }//end if block
            }//ends changed method
        });
        pieceLengthField.setTooltip(new Tooltip 
                        ("has to be between 1 and 2000"));
        
        
        
    }
    
    @FXML
    public void handleFileChooserButton () {
        if (fileChooserButton.isDisabled()) {
            MessageBox.show( "You have to wait until the current run is over", "Be Patient");

        }
        else {
            final FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("MIDI", "*.mid"), 
                new FileChooser.ExtensionFilter("ALL", "*.*")    
            );
            File file = fileChooser.showSaveDialog(ctrlStage);
            if (file != null) {
                File midi_file = new File(file.getAbsolutePath() + ".mid");
                Hindemith.InputParameters.setFilePath(midi_file);
            }        
        }
    }
    
    
    @FXML
    public void handleRunButton (ActionEvent event) {
        boolean proceed = true;
        if(Hindemith.InputParameters.getFilePath() == null){
            CancelBox.show("Midi will be saved with default name and location. OK?", "No file chosen");
            proceed = CancelBox.getProceed();
        }
        else if (Hindemith.InputParameters.getFilePath().isFile()) {
            CancelBox.show("File will be overwritten. OK?", "Overwrite existing file");
            proceed = CancelBox.getProceed();
        }
        
        if (proceed == false) return;
        //Set input and decrement parameters
        String my_mode_choice = modeModuleComboBox.getValue();
        Hindemith.InputParameters.setModeModule(my_mode_choice);
        String my_rhythm_choice = rhythmModuleComboBox.getValue();
        Hindemith.InputParameters.setJames(my_rhythm_choice);
        Hindemith.InputParameters.setTempo(Integer.parseInt(tempoField.getText()));
        Hindemith.InputParameters.setPieceLength(Integer.parseInt(pieceLengthField.getText()));
        
        if (checkboxVoice1.isSelected()) voices.add(comboboxVoice1.getValue().toString());
        if (checkboxVoice2.isSelected()) voices.add(comboboxVoice2.getValue().toString());    
        if (checkboxVoice3.isSelected()) voices.add(comboboxVoice3.getValue().toString());
        if (checkboxVoice4.isSelected()) voices.add(comboboxVoice4.getValue().toString());
        if (checkboxVoice5.isSelected()) voices.add(comboboxVoice5.getValue().toString());
        
        if (cons_min2_chk.isSelected()) cons.add(1);
        if (cons_maj2_chk.isSelected()) cons.add(2);
        if (cons_min3_chk.isSelected()) cons.add(3);
        if (cons_maj3_chk.isSelected()) cons.add(4);
        if (cons_p4_chk.isSelected()) cons.add(5);
        if (cons_tt_chk.isSelected()) cons.add(6);
        if (cons_p5_chk.isSelected()) cons.add(7);
        if (cons_min6_chk.isSelected()) cons.add(8);
        if (cons_maj6_chk.isSelected()) cons.add(9);
        if (cons_dom7_chk.isSelected()) cons.add(10);
        if (cons_maj7_chk.isSelected()) cons.add(11);
        if (cons_oct_chk.isSelected()) cons.add(0);
        if (pcons_min2_chk.isSelected()) pcons.add(1);
        if (pcons_maj2_chk.isSelected()) pcons.add(2);
        if (pcons_min3_chk.isSelected()) pcons.add(3);
        if (pcons_maj3_chk.isSelected()) pcons.add(4);
        if (pcons_p4_chk.isSelected()) pcons.add(5);
        if (pcons_tt_chk.isSelected()) pcons.add(6);
        if (pcons_p5_chk.isSelected()) pcons.add(7);
        if (pcons_min6_chk.isSelected()) pcons.add(8);
        if (pcons_maj6_chk.isSelected()) pcons.add(9);
        if (pcons_dom7_chk.isSelected()) pcons.add(10);
        if (pcons_maj7_chk.isSelected()) pcons.add(11);
        if (pcons_oct_chk.isSelected()) pcons.add(0);
        
        if (cons.isEmpty()) {
            if (firstRun) MessageBox.show("You have to define at least 1 consonance", "Using Default Consonance Array");
            else  MessageBox.show("You have to define at least 1 consonance", "Using Previous Consonance Array");
        }
        else Hindemith.InputParameters.setConsonances(cons.toArray(new Integer[cons.size()]));
        
        if (pcons.isEmpty())  {
            if (firstRun) MessageBox.show("You have to define at least 1 perfect consonance", "Using Default Perfect Consonance Array");
            else MessageBox.show("You have to define at least 1 perfect consonance", "Using Previous Perfect Consonance Array");

        }
        else Hindemith.InputParameters.setPerfectConsonances(pcons.toArray(new Integer[pcons.size()]));
        
        if (voices.isEmpty()) {
            if (firstRun) MessageBox.show("Your counterpoint has to have at least 1 voice", "Using Default Voice Array");
            else MessageBox.show("Your counterpoint has to have at least 1 voice", "Using Previous Voice Array");

        }
        else {
            voices.add(0, "bass"); // this bass voice is your harmonic progression
            Hindemith.InputParameters.setVoiceArray(voices.toArray(new String[voices.size()]));
        }
        
        if(choiceboxDissOK.getValue() == "Yes") Hindemith.InputParameters.setLargeDissonanceBad(true);
        if(choiceboxDissOK.getValue() == "No")  Hindemith.InputParameters.setLargeDissonanceBad(false);
        
        Hindemith.Decrements.setAccented_Dissonance((int)AccentDSlider.getValue());
        Hindemith.Decrements.setBad_Cons_Approach_From_Diss((int)BadApproachCfromDSlider.getValue());
        Hindemith.Decrements.setBad_Diss_Approach_From_Cons((int)BadApproachDfromCSlider.getValue());
        Hindemith.Decrements.setBad_Diss_Approach_From_Diss((int)BadApproachDfromDSlider.getValue());
        Hindemith.Decrements.setDirect_Motion_Into_Diss((int)DirectMotionIntoDSlider.getValue());
        Hindemith.Decrements.setDirect_Motion_Perf_Cons((int)DirectMotionIntoPerfCSlider.getValue());
        Hindemith.Decrements.setDiss_Cp_Previous_Cf((int)DWithPrevCFSlider.getValue());
        Hindemith.Decrements.setDiss_Prev_Cp_Next_Cf_Unresolved((int)PrevCPDWithCFUnresolvedSlider.getValue());
        Hindemith.Decrements.setMinor_9th((int)Min9thSlider.getValue());
        Hindemith.Decrements.setMotion_Into_Diss_Both_Voices_Change((int)BothChangetoDSlider.getValue());
        Hindemith.Decrements.setOctave((int)OctaveSlider.getValue());
        Hindemith.Decrements.setParallel_Dissonance((int)ParallelDSlider.getValue());
        Hindemith.Decrements.setParallel_Perf_Consonance((int)ParallelPerfCSlider.getValue());
        Hindemith.Decrements.setSeq_Of_Diss((int)SeqOfDSlider.getValue());
        Hindemith.Decrements.setSeq_Of_Same_Cons((int)SeqOfSameCSlider.getValue());
        Hindemith.Decrements.setSeq_Same_Type_Cons((int)SeqOfSameTypeCSlider.getValue());
        Hindemith.Decrements.setSeq_Same_Type_Diss((int)SeqOfSameDSlider.getValue());
        Hindemith.Decrements.setRemote_From_Pitchcenter((int)RemotePitchCenterSlider.getValue());
        Hindemith.Decrements.setPeak_Trough_Quota_Exceed((int)MelodyRepeatsPeakSlider.getValue());
        Hindemith.Decrements.setBad_Motion_After_Leap((int)BadMotionAfterLeapSlider.getValue());
        Hindemith.Decrements.setMelodic_Motion_Quota_Exceed((int)ExcessSimilarSlider.getValue());
        Hindemith.Decrements.setOutside_Range((int)OutsideVoiceRangeSlider.getValue());
        Hindemith.Decrements.setOverlap((int)OverlappingVoiceSlider.getValue());
        Hindemith.Decrements.setSuccessive_Leaps((int)SuccessiveLeapSlider.getValue());
        
        //clearing the temporary variables after assigning them to input parameters
        cons.clear();
        pcons.clear();
        voices.clear();
        
        RunButton.setDisable(true);
        fileChooserButton.setDisable(true);
        
        //Starting the counterpoint
        Hindemith.Model model = new Hindemith.Model();
        Thread generatorThread = new Thread((Runnable) model.worker);
        generatorThread.start();
        
        //Buttons disabled when thread is running - re-enabled when thread stops
        model.worker.runningProperty().addListener(
            (observable, oldvalue, newvalue) -> {
         
                if(newvalue.equals(false)) {
                    RunButton.setDisable(false);
                    fileChooserButton.setDisable(false);
                } 
        });
        
        //build the cancel screen
        Stage stage = new Stage();
        stage.setMinWidth(250);
        stage.setTitle("Generating Counterpoint");
        
        ProgressBar pb = new ProgressBar();
        pb.progressProperty().bind(model.worker.progressProperty());
        pb.progressProperty().addListener(
            (observable, oldvalue, newvalue) -> {
                if((double)newvalue == 1.0) {
                    stage.close();
                }    
        });        

        Label mylabel = new Label("");
        mylabel.textProperty().bind(model.worker.messageProperty());
        
        Button cancelButton = new Button("Cancel");
        cancelButton.setCancelButton(true);
        cancelButton.setOnAction(c -> {
            mylabel.textProperty().unbind();
            mylabel.setText("Cancelling... please wait");
            model.worker.cancel();
            model.setSTOP();
            //generatorThread.stop();
            stage.close();
            //System.out.println("Cancelled");
        });
        
        VBox pane = new VBox(20);
        pane.getChildren().addAll(pb, mylabel, cancelButton);
        pane.setAlignment(Pos.CENTER);
        pane.setMinWidth(400);
        pane.setStyle("-fx-font: 11px \"Lucida Sans Unicode\"; -fx-padding: 10; -fx-background-color: beige;");
        
        Group mystuff = new Group();
        mystuff.getChildren().addAll(pane);
        stage.setScene(new Scene(mystuff));
        stage.show();  
        
        firstRun = false;
            
    }// end of handleRunButtonCode 
    
}//end of FXMLDocumentController class
