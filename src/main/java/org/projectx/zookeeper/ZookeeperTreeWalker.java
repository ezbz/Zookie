package org.projectx.zookeeper;

import java.util.LinkedList;
import java.util.List;

import org.I0Itec.zkclient.ZkClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import org.projectx.zookeeper.bean.ZkSimpleNode;
import org.projectx.zookeeper.bean.ZkSimpleNodeStat;

/**
 * A recursive, configurable walker for the zookeeper node tree
 * @author erez
 *
 */
public class ZookeeperTreeWalker {
  private final ZkClient zkClient;
  private final ZookeeperStatsReader zookeeperStatsReader;
  private static final Logger log = LoggerFactory.getLogger(ZookeeperTreeWalker.class);

  public ZookeeperTreeWalker(final ZkClient zkClient, final ZookeeperStatsReader zookeeperStatsReader) {
    Assert.notNull(zkClient, "zkClient cannot be null");
    Assert.notNull(zookeeperStatsReader, "zookeeperStatsReader cannot be null");
    this.zkClient = zkClient;
    this.zookeeperStatsReader = zookeeperStatsReader;

  }

  public ZkSimpleNode walk(final String root, final boolean readData, final boolean readStat, final int maxDepth) {
    return walkRecursive(root, readData, readStat, maxDepth, 0);
  }

  private ZkSimpleNode walkRecursive(final String root, final boolean readData, final boolean readStat, final int maxDepth, Integer outCurrentDepth) {
    log.debug("Going to walk root [{}], show-data [{}], show-stat [{}], max-depth [{}], current-depth [{}]",
        new Object[] { root, readData, readStat, maxDepth, outCurrentDepth });

    final List<String> childPaths = zkClient.getChildren(root);
    final Object data = (readData) ? zkClient.readData(root, true) : null;
    final ZkSimpleNodeStat stat = (readStat) ? zookeeperStatsReader.readStats(root) : null;

    final List<ZkSimpleNode> children = new LinkedList<ZkSimpleNode>();

    outCurrentDepth++;

    for (final String childPath : childPaths) {

      final String childFullPath = normalizePath(root, childPath);
      if (maxDepth == -1 || outCurrentDepth <= maxDepth) {
        // recursive entry point
        final ZkSimpleNode child = walkRecursive(childFullPath, readData, readStat, maxDepth, outCurrentDepth);

        children.add(child);
      }
    }

    return new ZkSimpleNode(root, favorNulls(children), favorNulls(data), stat);
  }

  private String normalizePath(final String root, final String childPath) {
    return root.endsWith("/") ? root + childPath : root + "/" + childPath;
  }

  private String favorNulls(final Object data) {
    if (null == data) {
      return null;
    }

    final String string = data.toString();
    if (string.length() < 1) {
      return null;
    }
    return string;
  }

  private List<ZkSimpleNode> favorNulls(final List<ZkSimpleNode> children) {
    if (null == children) {
      return null;
    } else if (children.isEmpty()) {
      return null;
    }
    return children;
  }

}
