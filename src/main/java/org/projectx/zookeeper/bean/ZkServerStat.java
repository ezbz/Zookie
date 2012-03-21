package org.projectx.zookeeper.bean;

import java.util.List;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

public class ZkServerStat {
  private final String version;
  private final String buildDate;
  private final List<ZkServerClient> clients;
  private final Integer minLatency;
  private final Integer avgLatency;
  private final Integer maxLatency;
  private final Integer received;
  private final Integer sent;
  private final Integer outstanding;
  private final String zxId;
  private final ZkServerMode mode;
  private final Integer nodes;

  public ZkServerStat(final String version, final String buildDate, final List<ZkServerClient> clients, final Integer minLatency,
      final Integer avgLatency, final Integer maxLatency, final Integer received, final Integer sent, final Integer outstanding, final String zxId,
      final ZkServerMode mode, final Integer nodes) {
    this.version = version;
    this.buildDate = buildDate;
    this.clients = clients;
    this.minLatency = minLatency;
    this.avgLatency = avgLatency;
    this.maxLatency = maxLatency;
    this.received = received;
    this.sent = sent;
    this.outstanding = outstanding;
    this.zxId = zxId;
    this.mode = mode;
    this.nodes = nodes;
  }

  public String getVersion() {
    return version;
  }

  public String getBuildDate() {
    return buildDate;
  }

  public List<ZkServerClient> getClients() {
    return clients;
  }

  public Integer getMinLatency() {
    return minLatency;
  }

  public Integer getAvgLatency() {
    return avgLatency;
  }

  public Integer getMaxLatency() {
    return maxLatency;
  }

  public Integer getReceived() {
    return received;
  }

  public Integer getSent() {
    return sent;
  }

  public Integer getOutstanding() {
    return outstanding;
  }

  public String getZxId() {
    return zxId;
  }

  public ZkServerMode getMode() {
    return mode;
  }

  public Integer getNodes() {
    return nodes;
  }

  @Override
  public String toString() {
    return new ReflectionToStringBuilder(this).toString();
  }
}
