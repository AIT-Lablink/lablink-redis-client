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

import java.util.List;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Class RedisClient.
 */
public class RedisClient {

  private class SensorConf{
    String name;
    int timeInterval;
    SensorConf(String name, int timeInterval)
    {
      this.name = name;
      this.timeInterval = timeInterval;
    }
  }

  /** Logger. */
  private static final Logger logger = LogManager.getLogger( "RedisClient" );

  private static JedisPool jedisPool = null;

  // LLClient properties
  private String scenarioName;
  private String groupName;
  private String clientName;
  private String clientDesc;

  // General Lablink properties configuration.
  private String llprop;
  
  // Sync properties configuration.
  private String llsync;

  private boolean giveShell = true;
  private boolean isPseudo = false;

  private List<SensorConf> sensorList = new ArrayList<SensorConf>();  

  private LlClient client;

  public RedisClient(){

  };

  public RedisClient(String redisIP, int redisPort, 
                    String scenarioName, String groupName, String clientName, String clientDesc, 
                    String llprop, String llsync) throws ClientNotReadyException, CommInterfaceNotSupportedException,
                    DataTypeNotSupportedException, NoServicesInClientLogicException,
                    NoSuchCommInterfaceException, ServiceIsNotRegisteredWithClientException,
                    ServiceTypeDoesNotMatchClientType, ConfigurationException
  {
    // create REDIS connection pool
    jedisPool = new JedisPool(new JedisPoolConfig(), redisIP, redisPort);    
    this.scenarioName = scenarioName;
    this.groupName = groupName;
    this.clientName = clientName;
    this.clientDesc = clientDesc;
    this.llprop = llprop;
    this.llsync = llsync;
                      
    // Declare the client with required interface.
    this.client = new LlClient( clientName,
        MqttCommInterfaceUtility.SP_ACCESS_NAME, giveShell, isPseudo );
  
    // Specify client configuration (no sync host).
    MqttCommInterfaceUtility.addClientProperties(this.client, clientDesc,
        scenarioName, groupName, clientName, llprop, llsync, null );  
        // Declare the client with required interface.    
  }

  public void addRedisKeyAsSensor(String key, int msTimeInterval) throws ServiceTypeDoesNotMatchClientType
  {

    String sensorName = key;        
    String sensorDesc = "Sends values from " + key + " every " + msTimeInterval + " ms.";    
    String sensorUnit = "";

    // Create new sensor service.
    RedisSensor sensorService = new RedisSensor();
    sensorService.setName( sensorName );    

    // Specify data service properties.
    MqttCommInterfaceUtility.addDataPointProperties( sensorService,
        sensorName, sensorDesc, sensorName, sensorUnit );

    // Add notifier.
    sensorService.addStateChangeNotifier( new RedisClientNotifier() );

    // Add service to the client.
    this.client.addService( sensorService );     
    
    sensorList.add(new SensorConf(sensorName, msTimeInterval));    
  }

  public void addRedisKeyAsActuator(String key) throws ServiceTypeDoesNotMatchClientType
  {
    String actuatorName = key;    
    String actuatorDesc = "Sends values to " + key + ".";    
    String actuatorUnit = "none";

    // Create new actuator service.
    RedisActuator actuatorService = new RedisActuator();
    actuatorService.setName( actuatorName );   
    
    MqttCommInterfaceUtility.addDataPointProperties( actuatorService,
        actuatorName, actuatorDesc, actuatorName, actuatorUnit );

    // Add notifier.    
    actuatorService.addStateChangeNotifier( new RedisClientNotifier() );
        
    // Add service to the client.    
    client.addService( actuatorService );        
  }

  public void start() throws ServiceTypeDoesNotMatchClientType, ClientNotReadyException, NoSuchCommInterfaceException, ConfigurationException,
                            NoServicesInClientLogicException, DataTypeNotSupportedException
  {
     // Create the client.
     client.create();

     // Initialize the client.
     client.init();
 
     //start the sensors
     for (int i=0; i<this.sensorList.size(); i++)
     {
        Timer timer = new Timer();
        timer.schedule(new RedisReader(this.client, sensorList.get(i).name), 0, sensorList.get(i).timeInterval);
     }
     
     // Start the client.
     client.start();
     
     // ToDo: Find a way to close the Jedis connection pool gracefully
     // jedisPool.close();
  }

  private static class RedisReader extends TimerTask {            
    private LlClient client;    
    private IImplementedService<Double> service;
    private String sensorName;
    
    public RedisReader(LlClient client, String sensorName) {
      this.client = client;
      this.sensorName = sensorName;
      this.service =
        ( IImplementedService<Double> ) client.getImplementedServices().get( sensorName );                  
    }

    public void run() {           
      try (Jedis jedis = jedisPool.getResource()) {
        Double value = Double.parseDouble(jedis.get(sensorName));        
        this.service.setValue(value);       
      }       
    }
  }

  /**
   * Class RedisSensor.
   * Subclass of LLServiceDouble. Provides access to a redis key
   * 
   */
  class RedisSensor extends LlServiceDouble {

    /**
     * @see at.ac.ait.lablink.core.service.LlService#get()
     */
    @Override
    public Double get() {
      return this.getCurState();
    }

    /**
     * @see at.ac.ait.lablink.core.service.LlService#set( java.lang.Object )
     */
    @Override
    public boolean set( Double newVal ) {
      logger.info( "{}: set new value to '{}'", this.getName(), newVal );
      this.setCurState( newVal );    

      return true;
    }
  }

  /**
   * Class RedisActuator.
   * Subclass of LLServiceDouble. Provides access to a redis key
   */
  class RedisActuator extends LlServiceDouble {
    /**
     * @see at.ac.ait.lablink.core.service.LlService#get()
     */
    @Override
    public Double get() {
      return this.getCurState();
    }

    /**
     * @see at.ac.ait.lablink.core.service.LlService#set( java.lang.Object )
     */
    @Override
    public boolean set( Double newVal ) {
      logger.info( "{}: set new value to '{}'", this.getName(), newVal );
      try (Jedis jedis = jedisPool.getResource()) {
        jedis.set(this.getName(), newVal.toString());
        Double redisVal = Double.parseDouble(jedis.get(this.getName()));
        if (redisVal == newVal)
        {
          this.setCurState( newVal );    
        }
        //else Throw value cannot be set      
      }                        
      return true;
    }
  }

  /**
   * Class RedisClientNotifier.
   */
  class RedisClientNotifier implements IServiceStateChangeNotifier<LlService, Double> {
    /**
     * @see at.ac.ait.lablink.core.service.IServiceStateChangeNotifier#stateChanged(
     * java.lang.Object, java.lang.Object, java.lang.Object 
     * )
     */
    @Override
    public void stateChanged( LlService service, Double oldVal, Double newVal ) {
      logger.info( "{}: notifier -> state Changed from '{}' to '{}'",
          service.getName(), oldVal, newVal);
    }
  }
}
