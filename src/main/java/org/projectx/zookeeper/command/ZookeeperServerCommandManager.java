package org.projectx.zookeeper.command;

import java.util.List;

import org.springframework.util.Assert;

import org.projectx.zookeeper.bean.ZkServerCommand;
import org.projectx.zookeeper.bean.ZkServerEnvironment;
import org.projectx.zookeeper.bean.ZkServerStat;

public class ZookeeperServerCommandManager {
  private final ZookeeperTcpCommandExcutor zookeeperTcpCommandExcutor;

  public ZookeeperServerCommandManager(final ZookeeperTcpCommandExcutor zookeeperTcpCommandExcutor) {
    Assert.notNull(zookeeperTcpCommandExcutor, "zookeeperTcpCommandExcutor cannot be null");
    this.zookeeperTcpCommandExcutor = zookeeperTcpCommandExcutor;
  }

  public ZkServerStat stat(final String host, final Integer port) {
    final List<String> result = zookeeperTcpCommandExcutor.execute(host, port, ZkServerCommand.stat.toString());
    final ZookeeperServerStatOutputParser parser = new ZookeeperServerStatOutputParser();
    return parser.parse(result);
  }

  public ZkServerEnvironment environment(final String host, final Integer port) {
    final List<String> result = zookeeperTcpCommandExcutor.execute(host, port, ZkServerCommand.envi.toString());
    final ZookeeperEnviromentOutputParseer parser = new ZookeeperEnviromentOutputParseer();
    return parser.parse(result);
  }
}
