package frc.robot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class InputManager{
    private Map<String, String> m_axes = new HashMap<String, String>();
    private InputManagerJson m_jsonInput = new InputManagerJson();
    private String m_fileName = "";
    private String m_lastAxis = "";
    private boolean m_buttonPreviouslyDown = false;

    public InputManager(){
        SmartDashboard.putString("Errors", "Axis rebinding disabled");
        SmartDashboard.putString("Config Name","default");
    }

    public double getValue(String axisName){

        String test = m_axes.get(axisName);
        
        if(test != null){
            String[] tokens = test.split(" ");
            
            int joystickNumber = Integer.parseInt(tokens[2]);
            Joystick stick = new Joystick(joystickNumber);

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

    //call every iteration to enable dashboard input
    public void refreshDashboard(){
        if(validateDashboard()){
            String axisName = SmartDashboard.getString("Axis Name", "");
            if(!axisName.equals(m_lastAxis)){
                String[] tokens = m_axes.get(axisName).split(" ");
                SmartDashboard.putString("Set Input", tokens[0]);
                SmartDashboard.putString("Input #", tokens[1]);
                SmartDashboard.putString("Gamepad #", tokens[2]);
            }else{
                String axisType = SmartDashboard.getString("Input Type","");

                String inputNumber = SmartDashboard.getString("Input #", "");
                String gamepadNumber = SmartDashboard.getString("Gamepad #","");
            
                m_axes.put(axisName, axisType + " "+inputNumber+" "+gamepadNumber);
            }
            m_lastAxis = axisName;

            if(SmartDashboard.getBoolean("Save",false)){
                if(!m_buttonPreviouslyDown){
                    m_buttonPreviouslyDown = true;
                }
            }else if(SmartDashboard.getBoolean("Load", false)){
                if(!m_buttonPreviouslyDown){
                    m_buttonPreviouslyDown = true;
                    useConfig(SmartDashboard.getString("Config Name", ""));
                    
                    //force reload
                    m_lastAxis = "";
                }
            }else{
                m_buttonPreviouslyDown = false;
            }
        }
    }

    //validates input and sets any errors
    private boolean validateDashboard(){
        String axisName = SmartDashboard.getString("Axis Name", "");
        if(!axisName.equals("")){
            if(m_axes.get(axisName) == null){
                SmartDashboard.putString("Errors","Axis "+axisName+" not found!");
                return false;
            }

            String axisType = SmartDashboard.getString("Input Type","");
            if(!axisType.equals("Axis") && !axisType.equals("Button")){
                SmartDashboard.putString("Errors", "Incorrect axis type "+axisType+"!");
                return false;
            }

            //verify both values are numbers
            String inputNumber = SmartDashboard.getString("Input #", "");
            String gamepadNumber = SmartDashboard.getString("Gamepad #","");
            
            try{
                Integer.parseInt(inputNumber);
            }catch(Exception e){
                SmartDashboard.putString("Errors",inputNumber+" is not a number!");
                return false;
            }

            try{
                Integer.parseInt(gamepadNumber);
            }catch(Exception e){
                SmartDashboard.putString("Errors",gamepadNumber+" is not a number!");
                return false;
            }

            return true;

        }else{
            SmartDashboard.putString("Errors", "No axis selected!");
            return false;
        }
    }

    public void loadFromFile(String fileName){
        m_fileName = fileName;
        
        
    }

    public void saveAs(String fileName){

    }

    public void useConfig(String name){
        Map<String, String> temp = m_jsonInput.getAxisMap(m_fileName, name);
        if(temp != null){
            //only update axes if temp
            m_axes = temp;
        }else{
            SmartDashboard.putString("Errors", "Config "+name+" failed to load");
            return;
        }

        ArrayList<String> axisList = new ArrayList<String>();
        SmartDashboard.putString("Errors", "");

        Iterator<Map.Entry<String, String>> it = m_axes.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> pair = (Map.Entry<String, String>)it.next();
            axisList.add(pair.getKey());
        }
        String tempArray[] = new String[axisList.size()];
        SmartDashboard.putStringArray("Axis List",axisList.toArray(tempArray));

        String axisTypes[] = {"Axis", "Button"};
        SmartDashboard.putStringArray("Input List", axisTypes);
    }
};