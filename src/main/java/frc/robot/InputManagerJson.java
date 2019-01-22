package frc.robot;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import com.grack.nanojson.*;

public class InputManagerJson{
    public InputManagerJson(){
        
    }

   public Map<String,String> getAxisMap(String file, String config){
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
                System.out.println("Failed to find config: "+config);
                return null;
            }

            for(String key : jsonConfig.keySet()){
                axes.put(key, jsonConfig.getString(key));
            }

        }catch(InvalidPathException e){
            System.out.println("Error loading path: "+e.getMessage());
        }catch(IOException e){
            System.out.println("Error loading file: "+e.toString());
        };

        return axes;
    }

    public void updateConfig(String configName, String file, Map<String,String> values){
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
            JsonObject jsonConfig = new JsonObject();
            Iterator<Map.Entry<String, String>> it = values.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, String> pair = (Map.Entry<String, String>)it.next();
                jsonConfig.put(pair.getKey(), pair.getValue());
            }

            root.put(configName, jsonConfig);
            
            String json = JsonWriter.string(root).toString();
            System.out.println(json);
        }catch(InvalidPathException e){
            System.out.println("Error loading path: "+e.getMessage());
        }catch(IOException e){
            System.out.println("Error loading file: "+e.toString());
        };
        
        
    }
}