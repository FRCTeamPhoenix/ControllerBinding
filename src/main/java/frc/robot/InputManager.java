package frc.robot;

import java.util.Map;
import com.grack.nanojson.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import edu.wpi.first.wpilibj.Joystick;

public class InputManager{
    private Map<String, String> m_axes;

    public InputManager(){

        m_axes.put("Move Forward", "Axis 1");
    }

    public double getValue(String axisName){

        this.stick = new Joystick(0);

        String test = m_axes.indexOf(axisName);
        String[] tokens = test.split(" ");

        if(tokens[0] = "Axis") {
            int axisNumber = Integer.parseInt(tokens[1]);
        }
        
        return 0.0;
    }

    public void loadFromFile(String fileName){
        try{
            String content = new String(Files.readAllBytes(Paths.get(fileName)));

            JsonObject root = JsonParser.object().from(content);
        }catch(Exception e){
            
        };
        
    }

    public void saveAs(String fileName){

    }

    public void useConfig(String name){

    }
};