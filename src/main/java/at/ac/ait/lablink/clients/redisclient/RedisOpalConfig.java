//
// Copyright (c) AIT Austrian Institute of Technology GmbH.
// Distributed under the terms of the Modified BSD License.
//

package at.ac.ait.lablink.clients.redisclient;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


public class RedisOpalConfig {

  public String cmdsFile;
  public String measFile;
  public String groupName;
  public String clientName;
  public String scenarioName;
  public String syncHostPropertiesUrl;
  public String labLinkPropertiesUrl;

  public String redisIpAddress;
  public int redisPort;

  /**
   * Constructor.
   *
   * @param configFile config file
   */
  public RedisOpalConfig(String configFile) {
    JSONParser jsonParser = new JSONParser();

    try (FileReader reader = new FileReader(configFile)) {
      //Read JSON file
      JSONObject conf = (JSONObject) jsonParser.parse(reader);
      parseConfig(conf);
    } catch (FileNotFoundException ex) {
      ex.printStackTrace();
    } catch (IOException ex) {
      ex.printStackTrace();
    } catch (ParseException ex) {
      ex.printStackTrace();
    }
  }

  private void parseConfig(JSONObject config) {
    this.cmdsFile = (String) config.get("cmdsFile");
    this.measFile = (String) config.get("measFile");
    this.groupName = (String) config.get("groupName");
    this.clientName = (String) config.get("clientName");
    this.scenarioName = (String) config.get("scenarioName");
    this.syncHostPropertiesUrl = (String) config.get("syncHostPropertiesUrl");
    this.labLinkPropertiesUrl = (String) config.get("labLinkPropertiesUrl");
    this.redisIpAddress = (String) config.get("redisIP");
    this.redisPort = Integer.parseInt((String) config.get("redisPort"));
  }
}
