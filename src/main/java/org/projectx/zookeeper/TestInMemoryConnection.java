package org.projectx.zookeeper;

import java.util.concurrent.atomic.AtomicInteger;

import org.I0Itec.zkclient.IZkConnection;
import org.I0Itec.zkclient.InMemoryConnection;
import org.I0Itec.zkclient.ZkConnection;
import org.apache.commons.lang.StringUtils;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An {@link IZkConnection} implementation to be used for unit tests where no
 * zookeeper server exists
 * 
 * @author erez
 * 
 */
public class TestInMemoryConnection extends InMemoryConnection {
  private static final Logger log = LoggerFactory.getLogger(TestInMemoryConnection.class);

  /**
   * Dummy constructor for symmetry with real {@link ZkConnection} constructor
   * as an instantiation abstraction (the connection class can be configured
   * differently for different environments) arguments are ignored
   * 
   * @param zkServers
   *          ignored
   * @param sessionTimeOut
   *          ignored
   */
  public TestInMemoryConnection(final String zkServers, final int sessionTimeOut) {
    log.info("Starting TestInMemoryConnection for zookeeper, should be used for testing only, servers and session timeout will be ignored");
  }

  public TestInMemoryConnection() {
  }

  private final AtomicInteger sequence = new AtomicInteger(0);

  @Override
  public String create(final String path, final byte[] data, final CreateMode mode) throws KeeperException, InterruptedException {
    String createdPath = path;
    if (mode == CreateMode.EPHEMERAL_SEQUENTIAL) {
      final int newSequence = sequence.incrementAndGet();
      createdPath = createdPath + StringUtils.leftPad(Integer.toString(newSequence), 10, "0");
    }

    return super.create(createdPath, (null == data) ? new byte[0] : data, mode);
  }
}
