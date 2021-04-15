//
// Copyright (c) AIT Austrian Institute of Technology GmbH.
// Distributed under the terms of the Modified BSD License.
//

package at.ac.ait.lablink.clients.lablinkredisclient;

import at.ac.ait.lablink.core.client.ci.mqtt.impl.MqttCommInterfaceUtility;
import at.ac.ait.lablink.core.client.ex.ClientNotReadyException;
import at.ac.ait.lablink.core.client.ex.CommInterfaceNotSupportedException;
import at.ac.ait.lablink.core.client.ex.DataTypeNotSupportedException;
import at.ac.ait.lablink.core.client.ex.InvalidCastForServiceValueException;
import at.ac.ait.lablink.core.client.ex.NoServicesInClientLogicException;
import at.ac.ait.lablink.core.client.ex.NoSuchCommInterfaceException;
import at.ac.ait.lablink.core.client.ex.ServiceIsNotRegisteredWithClientException;
import at.ac.ait.lablink.core.client.ex.ServiceTypeDoesNotMatchClientType;
import at.ac.ait.lablink.core.client.impl.LlClient;
import at.ac.ait.lablink.core.service.IImplementedService;
import at.ac.ait.lablink.core.service.IServiceStateChangeNotifier;
import at.ac.ait.lablink.core.service.LlService;
import at.ac.ait.lablink.core.service.LlServiceDouble;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;

import at.ac.ait.lablink.clients.lablinkredisclient.RedisOpalConfig;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;



/**
 * Class RedisClient.
 */
public class RedisOpalClient {

  private static final String DEFAULT_JSON_CONFIG_FILE = "opalconfig.json";  
    
  private static final String USAGE = "[-c <url> | -h ]";
  
  private static final String HEADER = "Redis Client for the ROADB (Opal data layer) -  v0.0.1";

  /** The Constant FOOTER. */
  private static final String FOOTER = "For more information, check https://github.com/AIT-Lablink";

  private static void printUsage(Options options) {
    HelpFormatter helpFormatter = new HelpFormatter();
    helpFormatter.setWidth(80);
    helpFormatter.printHelp(USAGE, HEADER, options, FOOTER);
  }


  /** Logger. */
  private static final Logger logger = LogManager.getLogger( "RedisClientOpal" );

  /**
   * The main method.
   *
   * @param args arguments to main method
   * @throws ClientNotReadyException client not ready
   * @throws CommInterfaceNotSupportedException comm interface not supported
   * @throws DataTypeNotSupportedException data type not supported
   * @throws NoServicesInClientLogicException no services in client logic
   * @throws NoSuchCommInterfaceException no such comm interface
   * @throws ServiceIsNotRegisteredWithClientException service is not registered with client
   * @throws ServiceTypeDoesNotMatchClientType service type does not match client type
   * @throws ConfigurationException bad configuration
   */
  public static void main( String[] args )
      throws ClientNotReadyException, CommInterfaceNotSupportedException,
      DataTypeNotSupportedException, NoServicesInClientLogicException,
      NoSuchCommInterfaceException, ServiceIsNotRegisteredWithClientException,
      ServiceTypeDoesNotMatchClientType, ConfigurationException, ParseException, FileNotFoundException, IOException  {    

    Options cliOptions = new Options();
    CommandLineParser parser = new BasicParser();
    
    cliOptions.addOption("h", "help", false, "print usage information");    
    cliOptions.addOption("c", "config file", true,
      "URL to configuration file (" + DEFAULT_JSON_CONFIG_FILE + ")");

    CommandLine commandLine = parser.parse(cliOptions, args);
    String configFile = DEFAULT_JSON_CONFIG_FILE;


    if (commandLine.hasOption("c")) {
      configFile = commandLine.getOptionValue("c");
    }
  
    if (commandLine.hasOption("h")) {
      printUsage(cliOptions);
      System.exit(0);
    }

    RedisOpalConfig config = new RedisOpalConfig(configFile);
    String clientDesc = HEADER;    
    // Client description.
    
   RedisClient redisClient = new RedisClient(
     config.redisIP, config.redisPort, 
     config.scenarioName, config.groupName, config.clientName, clientDesc,
     config.labLinkPropertiesUrl, config.syncHostPropertiesUrl
   );

  String signal;
  BufferedReader csvReader = new BufferedReader(new FileReader(config.measFile));
  while ((signal = csvReader.readLine()) != null) {
    redisClient.addRedisKeyAsSensor(signal, 5000);    
  }
  csvReader.close();
  String row;
  csvReader = new BufferedReader(new FileReader(config.cmdsFile));
  while ((row = csvReader.readLine()) != null) {
    String[] data = row.split(",");
    signal = data[0];
    redisClient.addRedisKeyAsActuator(signal);    
  }
  csvReader.close();      
  redisClient.start();                  
  }



  
}
