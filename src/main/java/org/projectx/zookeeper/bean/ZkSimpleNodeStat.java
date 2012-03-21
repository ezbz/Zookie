package org.projectx.zookeeper.bean;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.zookeeper.data.Stat;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

public class ZkSimpleNodeStat {
  private final Long czxid;
  private final Long mzxid;
  private final Long ctime;
  private final Long mtime;
  private final Integer version;
  private final Integer cversion;
  private final Integer aversion;
  private final Long ephemeralOwner;
  private final Integer dataLength;
  private final Integer numChildren;
  private final Long pzxid;

  public ZkSimpleNodeStat(final Long czxid, final Long mzxid, final Long ctime, final Long mtime,
      final Integer version, final Integer cversion, final Integer aversion,
      final Long ephemeralOwner, final Integer dataLength, final Integer numChildren,
      final Long pzxid) {
    this.czxid = czxid;
    this.mzxid = mzxid;
    this.ctime = ctime;
    this.mtime = mtime;
    this.version = version;
    this.cversion = cversion;
    this.aversion = aversion;
    this.ephemeralOwner = ephemeralOwner;
    this.dataLength = dataLength;
    this.numChildren = numChildren;
    this.pzxid = pzxid;
  }

  public ZkSimpleNodeStat(final Stat stat) {
    this.czxid = stat.getCzxid();
    this.mzxid = stat.getMzxid();
    this.ctime = stat.getCtime();
    this.mtime = stat.getMtime();
    this.version = stat.getVersion();
    this.cversion = stat.getCversion();
    this.aversion = stat.getAversion();
    this.ephemeralOwner = stat.getEphemeralOwner();
    this.dataLength = stat.getDataLength();
    this.numChildren = stat.getNumChildren();
    this.pzxid = stat.getPzxid();
  }

  @JsonSerialize(include = Inclusion.NON_NULL)
  public Long getCzxid() {
    return czxid;
  }

  @JsonSerialize(include = Inclusion.NON_NULL)
  public Long getMzxid() {
    return mzxid;
  }

  @JsonSerialize(include = Inclusion.NON_NULL)
  public Long getCtime() {
    return ctime;
  }

  @JsonSerialize(include = Inclusion.NON_NULL)
  public Long getMtime() {
    return mtime;
  }

  @JsonSerialize(include = Inclusion.NON_NULL)
  public Integer getVersion() {
    return version;
  }

  @JsonSerialize(include = Inclusion.NON_NULL)
  public Integer getCversion() {
    return cversion;
  }

  @JsonSerialize(include = Inclusion.NON_NULL)
  public Integer getAversion() {
    return aversion;
  }

  @JsonSerialize(include = Inclusion.NON_NULL)
  public Long getEphemeralOwner() {
    return ephemeralOwner;
  }

  @JsonSerialize(include = Inclusion.NON_NULL)
  public Integer getDataLength() {
    return dataLength;
  }

  @JsonSerialize(include = Inclusion.NON_NULL)
  public Integer getNumChildren() {
    return numChildren;
  }

  @JsonSerialize(include = Inclusion.NON_NULL)
  public Long getPzxid() {
    return pzxid;
  }

  @Override
  public String toString() {
    return new ReflectionToStringBuilder(this).toString();
  }
}
