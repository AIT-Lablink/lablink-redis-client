//
// Copyright (c) AIT Austrian Institute of Technology GmbH.
// Distributed under the terms of the Modified BSD License.
//

package at.ac.ait.lablink.clients.lablinkredisclient;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Unit tests for class RedisClient.
 */
public class RedisClientTest {

  @Test
  public void redis_test() {
    RedisClient c = new RedisClient();
    assertEquals(true, c != null);
  }
}
