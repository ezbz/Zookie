package org.projectx.zookeeper.bean;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

public class ZkServerClient {
  private final String host;
  private final Integer port;
  private final Integer ops;
  private final Integer queued;
  private final Integer received;
  private final Integer sent;

  public ZkServerClient(final String host, final Integer port, final Integer ops, final Integer queued, final Integer received, final Integer sent) {
    this.host = host;
    this.port = port;
    this.ops = ops;
    this.queued = queued;
    this.received = received;
    this.sent = sent;
  }

  public String getHost() {
    return host;
  }

  public Integer getPort() {
    return port;
  }

  public Integer getOps() {
    return ops;
  }

  public Integer getQueued() {
    return queued;
  }

  public Integer getReceived() {
    return received;
  }

  public Integer getSent() {
    return sent;
  }

  @Override
  public String toString() {
    return new ReflectionToStringBuilder(this).toString();
  }

}
