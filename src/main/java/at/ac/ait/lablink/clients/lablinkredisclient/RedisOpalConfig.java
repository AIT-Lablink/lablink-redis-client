package at.ac.ait.lablink.clients.lablinkredisclient;

import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
 

public class RedisOpalConfig {
  
  public String cmdsFile;  
  public String measFile;
  public String groupName;
  public String clientName;  
  public String scenarioName;  
  public String syncHostPropertiesUrl;  
  public String labLinkPropertiesUrl;    

  public String redisIP;  
  public int redisPort;  

  public RedisOpalConfig(String configFile) {
    JSONParser jsonParser = new JSONParser();
         
    try (FileReader reader = new FileReader(configFile))
    {
        //Read JSON file
        JSONObject conf = (JSONObject) jsonParser.parse(reader);

        parseConfig(conf);

    } catch (FileNotFoundException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    } catch (ParseException e) {
        e.printStackTrace();
    }
  }

  private void parseConfig(JSONObject config) 
  {             
    this.cmdsFile = (String) config.get("cmdsFile");          
    this.measFile = (String) config.get("measFile");          
    this.groupName = (String) config.get("groupName");          
    this.clientName = (String) config.get("clientName");          
    this.scenarioName = (String) config.get("scenarioName");          
    this.syncHostPropertiesUrl = (String) config.get("syncHostPropertiesUrl");          
    this.labLinkPropertiesUrl = (String) config.get("labLinkPropertiesUrl");          
    this.redisIP = (String) config.get("redisIP");          
    this.redisPort = Integer.parseInt((String) config.get("redisPort"));          
  }
}
