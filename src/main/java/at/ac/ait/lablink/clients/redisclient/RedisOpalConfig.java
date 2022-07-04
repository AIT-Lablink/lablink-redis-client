//
// Copyright (c) AIT Austrian Institute of Technology GmbH.
// Distributed under the terms of the Modified BSD License.
//

package at.ac.ait.lablink.clients.redisclient;

import at.ac.ait.lablink.core.utility.Utility;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.net.MalformedURLException;
import java.net.URL;

import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class RedisOpalConfig {

  protected static final int MS_TIME_INTERVAL_DEFAULT = 5000;


  // public String cmdsFile;
  // public String measFile;
  public ArrayList<String> commands;
  public ArrayList<String> measurements;
  public String groupName;
  public String clientName;
  public String scenarioName;
  public String syncHostPropertiesUrl;
  public String labLinkPropertiesUrl;

  public String redisIpAddress;
  public int redisPort;

  public int msTimeInterval;

  /**
   * Constructor.
   *
   * @param configFileName config file
   */
  public RedisOpalConfig(String configFileName) {

    String rawConfig = getRawConfigString(configFileName);

    // Remove comments.
    rawConfig = rawConfig.replaceAll("#.*#", "");

    // Check if comments have been removed properly.
    int still = rawConfig.length() - rawConfig.replace("#", "").length();
    if (still > 0) {
      throw new IllegalArgumentException(
          String.format("Config file contains at least %1$d line(s) with incorrectly"
            + "started/terminated comments: %2$s", still, configFileName)
        );
    }

    try {
      // Parse raw configuration to JSON object.
      JSONParser jsonParser = new JSONParser();
      JSONObject conf = (JSONObject) jsonParser.parse(rawConfig);

      // Parse configuration.
      parseConfig(conf);
    } catch (Exception ex) {
      ex.printStackTrace();
      throw new IllegalArgumentException(
          String.format("Invalid configuration: %1$s", configFileName)
        );
    }
  }

  private void parseConfig(JSONObject config) {

    // Retrieve list of commands either from local file or from JSON array.
    if (config.containsKey("cmdsFile")) {
      this.commands = readFile((String) config.get("cmdsFile"));
    } else if (config.containsKey("commands")) {
      this.commands = readJsonArray((JSONArray) config.get("commands"));
    }

    // Retrieve list of measurements either from local file or from JSON array.
    if (config.containsKey("measFile")) {
      this.measurements = readFile((String) config.get("measFile"));
    } else if (config.containsKey("measurements")) {
      this.measurements = readJsonArray((JSONArray) config.get("measurements"));
    }

    this.groupName = (String) config.get("groupName");
    this.clientName = (String) config.get("clientName");
    this.scenarioName = (String) config.get("scenarioName");
    this.syncHostPropertiesUrl = (String) config.get("syncHostPropertiesUrl");
    this.labLinkPropertiesUrl = (String) config.get("labLinkPropertiesUrl");
    this.redisIpAddress = (String) config.get("redisIP");
    this.redisPort = Integer.parseInt((String) config.get("redisPort"));

    if (config.containsKey("msTimeInterval")) {
      this.msTimeInterval = (int) config.get("msTimeInterval");
    } else {
      this.msTimeInterval = MS_TIME_INTERVAL_DEFAULT;
    }
  }

  private ArrayList<String> readFile(String fileName) {
    ArrayList<String> list = new ArrayList<String>();

    try {
      String line;
      BufferedReader fileReader = new BufferedReader(new FileReader(fileName));

      while ((line = fileReader.readLine()) != null) {
        list.add(line);
      }

      fileReader.close();
    } catch (Exception ex) {
      ex.printStackTrace();
      throw new IllegalArgumentException(
          String.format("Invalid configuration input file: %1$s", fileName)
        );
    }

    return list;
  }

  private ArrayList<String> readJsonArray(JSONArray array) {
    ArrayList<String> list = new ArrayList<String>();

    @SuppressWarnings( "rawtypes" )
    Iterator arrayIter = array.iterator();

    while (arrayIter.hasNext()) {
      list.add((String) arrayIter.next());
    }

    return list;
  }

  private String getRawConfigString(String configFileName) {
    try {
      if (isLocalFilePath(configFileName)) {
        // Read raw content of config file to string.
        return Files.readString(Path.of(configFileName));
      } else {
        // Get configuration URL, resolve environment variables if necessary.
        URL fullConfigUrl = new URL(Utility.parseWithEnvironmentVariable(configFileName));

        // Read configuration to string.
        Scanner scanner = new Scanner(fullConfigUrl.openStream());
        return scanner.useDelimiter("\\Z").next();
      }
    } catch (MalformedURLException ex) {
      ex.printStackTrace();
    } catch (IOException ex) {
      ex.printStackTrace();
    }

    throw new IllegalArgumentException(
        String.format("Invalid configuration: %1$s", configFileName)
      );
  }

  private boolean isLocalFilePath(String configFileName) {
    try {
      if (Files.exists(Path.of(configFileName))) {
        return true;
      } else {
        return false;
      }
    } catch (InvalidPathException ex) {
      return false;
    }
  }
}
