package org.projectx.zookeeper.bean;

import java.util.List;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

public class ZkSimpleNode {
  private final String path;
  private final Object data;
  private final ZkSimpleNodeStat stat;

  private final List<ZkSimpleNode> children;

  public ZkSimpleNode(final String path, final List<ZkSimpleNode> children, final Object data,
      final ZkSimpleNodeStat stat) {
    this.path = path;
    this.data = data;
    this.children = children;
    this.stat = stat;
  }

  public String getPath() {
    return path;
  }

  @JsonSerialize(include = Inclusion.NON_NULL)
  public List<ZkSimpleNode> getChildren() {
    return children;
  }

  @JsonSerialize(include = Inclusion.NON_NULL)
  public Object getData() {
    return data;
  }

  @JsonSerialize(include = Inclusion.NON_NULL)
  public ZkSimpleNodeStat getStat() {
    return stat;
  }

  @Override
  public String toString() {
    return new ReflectionToStringBuilder(this).toString();
  }
}
