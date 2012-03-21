package org.projectx.zookeeper;

import org.I0Itec.zkclient.IZkConnection;
import org.I0Itec.zkclient.ZkConnection;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import org.projectx.zookeeper.bean.ZkSimpleNodeStat;

/**
 * A node stats reader capable of reading a zookeeper stat object and creating a more presentation friendly, immutable {@link ZkSimpleNodeStat}.
 * <p>
 * <b>Note</b>: this class will 'gracefully' produce nulls when using a non {@link ZkConnection} 
 * instance of the {@link IZkConnection} interface since it doesn't provide access to the underlying {@link ZooKeeper}'s {@link Stat}s
 * @author erez
 *
 */
public class ZookeeperStatsReader {
  private final IZkConnection zkConnection;
  private final boolean testConnection;
  private static final Logger log = LoggerFactory.getLogger(ZookeeperStatsReader.class);

  public ZookeeperStatsReader(final IZkConnection zkConnection) {
    Assert.notNull(zkConnection, "zkConnection cannot be null");
    this.zkConnection = zkConnection;
    testConnection = !(zkConnection instanceof ZkConnection);
    if (!testConnection) {
      log.warn("Non-ZkConnection connection detected, stat operations for zookeeper are disabled");
    }
  }

  public ZkSimpleNodeStat readStats(final String root) {
    return getNonEmptyStat(readStatsInternal(root));
  }

  private Stat readStatsInternal(final String root) {
    if (!testConnection) {
      final ZooKeeper zookeeper = ((ZkConnection) zkConnection).getZookeeper();
      try {
        return zookeeper.exists(root, false);
      } catch (final Exception e) {
        log.error("Got exception trying to stat path: " + root, e);
        throw new IllegalStateException(e);
      }
    }
    return null;
  }

  private ZkSimpleNodeStat getNonEmptyStat(final Stat stat) {
    return (null == stat) ? null : new ZkSimpleNodeStat(stat);
  }

}
