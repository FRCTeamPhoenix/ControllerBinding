package frc.robot;

import java.util.HashMap;
import java.util.Map;
import com.grack.nanojson.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class InputManager{
    private Map<String, String> m_axes = new HashMap<String, String>();
    private InputManagerJson m_jsonInput = new InputManagerJson();
    private String m_fileName = "";
    public InputManager(){
    }

    public double getValue(String axisName){

        Joystick stick = new Joystick(0);

        String test = m_axes.get(axisName);
        if(test != null){
            String[] tokens = test.split(" ");

            if(tokens[0].equals("Axis")) {
                int axisNumber = Integer.parseInt(tokens[1]);
                
                return stick.getRawAxis(axisNumber);
            }else if(tokens[0].equals("Button")){
                int buttonNumber = Integer.parseInt(tokens[1]);
                
                return stick.getRawButton(buttonNumber) ? 1.0 : 0.0;
            }else if(tokens[0].equals("DPad")){
                //don't know how this works
            }
        }

        return 0.0;
    }

    public void loadFromFile(String fileName){
        m_fileName = fileName;
        
        
    }

    public void saveAs(String fileName){

    }

    public void useConfig(String name){
        m_axes = m_jsonInput.getAxisMap(m_fileName, name);
    }
};