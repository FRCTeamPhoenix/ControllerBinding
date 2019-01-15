package frc.robot;

import java.util.Map;
import com.grack.nanojson.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;

public class InputManager{
    private Map<String, String> m_axes;

    public InputManager(){
        
        Scanner controlChoice = new Scanner(System.in);
        System.out.println("Enter the Function: ");
        String controlFunction = controlChoice.nextString();
        String controlAssign = new Scanner(System.in);
        System.out.println("Enter the Binding: ");
        String controlBind = controlAssign.nextString();
        reader.close();

        m_axes.put(controlFunction, controlBind);
    }

    public double getValue(String axisName){

        this.stick = new Joystick(0);

        String test = m_axes.indexOf(controlBind);
        String[] tokens = test.split(" ");

        for(String t : tokens) {
            if(t = "Axis") {

            }
            else if(t = "Button") {

            }
            else {
                System.out.println("Invalid Control Binding");
            }
        }

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