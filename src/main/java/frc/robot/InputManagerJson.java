package frc.robot;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import com.grack.nanojson.*;

public class InputManagerJson{
    public InputManagerJson(){
        
    }

    Map<String,String> getAxisMap(String file, String config){
        Map<String, String> axes = new HashMap<String, String>();
        try{
            String content = new String(Files.readAllBytes(Paths.get(file)));

            //get root of doc
            JsonObject root = null;
            try {
                root = JsonParser.object().from(content);
            } catch (JsonParserException e) {
                System.out.println("Falied to find root in json file");
            }
            
            //get the specific config
            JsonObject jsonConfig = root.getObject(config);
            if(jsonConfig == null){
                return null;
            }

            String keys[] = (String[])jsonConfig.keySet().toArray();
            for(int i = 0; i < keys.length; i++){
                axes.put(keys[i], jsonConfig.getString(keys[i]));
            }

        }catch(Exception e){
            System.out.println(e.getMessage());
        };

        return axes;
    }
}