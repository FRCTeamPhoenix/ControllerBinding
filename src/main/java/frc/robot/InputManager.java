package frc.robot;

import java.util.Map;
import com.grack.nanojson.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class InputManager{
    private Map<String, String> m_axes;

    public InputManager(){

    }

    public double getValue(String axisName){
        return 1.0;
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