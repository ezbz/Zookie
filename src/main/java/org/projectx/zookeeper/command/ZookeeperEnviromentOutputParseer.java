package org.projectx.zookeeper.command;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.projectx.zookeeper.bean.ZkServerEnvironment;

/**
 * Parses the following server response (without the [] line numbers):
 * 
 * <pre>
 * [0]Environment:
 * [1]zookeeper.version=3.4.0-1142383, built on 07/03/2011 07:48 GMT
 * [2]host.name=192.168.0.108
 * [3]java.version=1.6.0_29
 * [4]java.vendor=Apple Inc.
 * [5]java.home=/System/Library/Java/JavaVirtualMachines/1.6.0.jdk/Contents/Home
 * [6]java.class.path=/Users/erez/bin/zookeeper-q3/jline.jar:/Users/erez/bin/zookeeper-q3/log4j-1.2.15.jar: ...
 * [8]java.io.tmpdir=/var/folders/dc/tl1b9mvn0h55tqk9qqngqmxh0000gn/T/
 * [9]java.compiler=<NA>
 * [10]os.name=Mac OS X
 * [11]os.arch=x86_64
 * [12]os.version=10.7.3
 * [13]user.name=erez
 * [14]user.home=/Users/erez
 * [15]user.dir=/Users/erez/dev/
 * </pre>
 * 
 * @author erez
 * 
 */
public class ZookeeperEnviromentOutputParseer {

  public ZkServerEnvironment parse(final List<String> result) {
    final Iterator<String> iterator = result.iterator();
    iterator.next();
    final ZkServerEnvironment environment = new ZkServerEnvironment();
    while (iterator.hasNext()) {
      final String line = iterator.next();
      final String[] parts = StringUtils.split(line, "=");
      environment.add(parts[0], parts[1]);
    }
    return environment;
  }
}
