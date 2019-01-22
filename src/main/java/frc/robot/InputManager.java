package frc.robot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.EntryListenerFlags;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class InputManager{
    private Map<String, String> m_axes = new HashMap<String, String>();
    private InputManagerJson m_jsonInput = new InputManagerJson();
    private String m_fileName = "";
    private String m_lastAxis = "";
    private boolean m_buttonPreviouslyDown = false;
    private String m_inputNumber = "";
    private String m_gamepadNumber = "";

    public InputManager(){
        SmartDashboard.putString("Errors", "Axis rebinding disabled");
        SmartDashboard.putString("Config Name","default");

        //add listeners for updates
        NetworkTable table = NetworkTableInstance.getDefault().getTable("SmartDashboard");
        table.getEntry("Input #").addListener(event -> {
            m_inputNumber = event.value.getString();
        }, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
        

        table.getEntry("Gamepad #").addListener(event -> {
            m_gamepadNumber = event.value.getString();
        }, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
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
        if(validateAxisName()){
            String axisName = SmartDashboard.getString("Axis Name", "");
            if(!axisName.equals(m_lastAxis)){
                String[] tokens = m_axes.get(axisName).split(" ");
                SmartDashboard.putString("Select Type", tokens[0]);
                SmartDashboard.putString("Input #", tokens[1]);
                SmartDashboard.putString("Gamepad #", tokens[2]);
            }
            m_lastAxis = axisName;
            if(validateDashboard()){
                String axisType = SmartDashboard.getString("Input Type","");

                //System.out.println("Input #"+SmartDashboard.getString("Input #", ""));
                m_axes.put(axisName, axisType + " "+m_inputNumber+" "+m_gamepadNumber);
                SmartDashboard.putString("DB/String 0", axisType + " "+m_inputNumber+" "+m_gamepadNumber);
                SmartDashboard.putString("DB/String 1", m_axes.get(axisName));

                if(SmartDashboard.getBoolean("Save",false)){
                    if(!m_buttonPreviouslyDown){
                        m_buttonPreviouslyDown = true;
                        m_jsonInput.updateConfig(SmartDashboard.getString("Config Name", ""), m_fileName, m_axes);
                    }
                }else if(SmartDashboard.getBoolean("Load", false)){
                    if(!m_buttonPreviouslyDown){
                        m_buttonPreviouslyDown = true;
                        System.out.println("Loading");
                        useConfig(SmartDashboard.getString("Config Name", ""));
                        
                        //force reload
                        m_lastAxis = "";
                    }
                }else{
                    m_buttonPreviouslyDown = false;
                }
            }
        }
    }

    private boolean validateAxisName(){
        String axisName = SmartDashboard.getString("Axis Name", "");
        if(!axisName.equals("")){
            if(m_axes.get(axisName) == null){
                SmartDashboard.putString("Errors","Axis "+axisName+" not found!");
                return false;
            }
            
            return true;

        }else{
            SmartDashboard.putString("Errors", "No axis selected!");
            return false;
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
            

            try{
                Integer.parseInt(m_inputNumber);
            }catch(Exception e){
                SmartDashboard.putString("Errors",m_inputNumber+" is not a number!");
                return false;
            }

            try{
                Integer.parseInt(m_gamepadNumber);
            }catch(Exception e){
                SmartDashboard.putString("Errors",m_gamepadNumber+" is not a number!");
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