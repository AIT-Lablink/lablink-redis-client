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

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Class RedisClient.
 */
public class RedisClientBasic {

  /** Logger. */
  private static final Logger logger = LogManager.getLogger( "RedisClientTest" );  

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
      ServiceTypeDoesNotMatchClientType, ConfigurationException{    

     // Scenario name.
     String scenarioName = "RedisClientScenario";

     // Group name.
     String groupName = "RedisClientDemo";
 
     // Client name.
     String clientName = "RedisClient";
 
     // Client description.
     String clientDesc = "Client for interfacing with a REDIS database.";
 
     // General Lablink properties configuration.
     String llprop = "$LLCONFIG$ait.example.all.llproperties";
 
     // Sync properties configuration (Redis).
     String llsync = "$LLCONFIG$ait.example.all.sync-host.properties";

    RedisClient redisClient = new RedisClient(
      "10.111.202.11", 6379, 
      scenarioName, groupName, clientName, clientDesc,
      llprop, llsync
    );
    

    redisClient.addRedisKeyAsSensor("test/1", 5000);
    redisClient.addRedisKeyAsActuator("test/2");
   
    redisClient.start();                
  }  
}
