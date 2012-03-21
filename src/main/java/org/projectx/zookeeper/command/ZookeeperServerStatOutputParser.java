package org.projectx.zookeeper.command;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import org.projectx.zookeeper.bean.ZkServerClient;
import org.projectx.zookeeper.bean.ZkServerMode;
import org.projectx.zookeeper.bean.ZkServerStat;

/**
 * A necessary evil, parses a server response string of the following format (without the [] line numbers):
 * 
 * 
 * For client list some servers have been known to response with or without the enclosing ')' character thus the pattern mess
 * <pre>
[0]Zookeeper version: 3.4.0-1142383, built on 07/03/2011 07:48 GMT
[1]Clients:
[2] /127.0.0.1:64947[0](queued=0,recved=1,sent=0)
[3]Latency min/avg/max: 0/0/233
[4]Received: 1622
[5]Sent: 1621
[6]Outstanding: 0
[7]Zxid: 0x1700000045
[8]Mode: follower
[9]Node count: 42
 * </pre>
 * @author erez
 *
 */
public class ZookeeperServerStatOutputParser {
  private static final String ATTRIBUTE_DELIMITER = "=";
  private static final String PROP_DELIMITER = ":";
  private final Pattern versionLinePattern = Pattern.compile(".*: (\\d+\\.\\d+\\.\\d+.*),.* built on (.*)");
  private final Pattern ipv4ClientLinePattern = Pattern.compile("/(\\d+\\.\\d+\\.\\d+\\.\\d+):(\\d+)\\[(\\d+)\\]\\((.*)");
  private final Pattern ipv6ClientLinePattern = Pattern.compile("/(\\d+:\\d+:\\d+:\\d+:\\d+:\\d+:\\d+:\\d+\\%\\d+):(\\d+)\\[(\\d+)\\]\\((.*)");
  private final Pattern latenciesPattern = Pattern.compile(".*: (\\d+)/(\\d+)/(\\d+)");

  public ZkServerStat parse(final List<String> lines) {
    final Iterator<String> iterator = lines.iterator();

    final Matcher versionMatcher = versionLinePattern.matcher(iterator.next());
    versionMatcher.find();
    final String version = versionMatcher.group(1);
    final String buildDate = versionMatcher.group(2);
    iterator.next(); // Clients:
    final List<ZkServerClient> clients = parseClientLines(version, iterator);

    final Matcher latenciesMatcher = latenciesPattern.matcher(iterator.next());
    latenciesMatcher.find();
    final Integer minLatency = Integer.parseInt(latenciesMatcher.group(1));
    final Integer avgLatency = Integer.parseInt(latenciesMatcher.group(2));
    final Integer maxLatency = Integer.parseInt(latenciesMatcher.group(3));

    final Integer received = parseIntFromLine(iterator.next(), PROP_DELIMITER);
    final Integer sent = parseIntFromLine(iterator.next(), PROP_DELIMITER);
    final Integer outstanding = parseIntFromLine(iterator.next(), PROP_DELIMITER);
    final String zxid = parseStringFromLine(iterator.next(), PROP_DELIMITER);
    final ZkServerMode mode = ZkServerMode.valueOf(StringUtils.capitalize(parseStringFromLine(iterator.next(), PROP_DELIMITER)));
    final Integer nodeCount = parseIntFromLine(iterator.next(), PROP_DELIMITER);

    return new ZkServerStat(version, buildDate, clients, minLatency, avgLatency, maxLatency, received, sent, outstanding, zxid, mode, nodeCount);

  }

  /**
   * Messy but some zookeeper server versions return all clients on the same line and some return them in separate lines, support both
   */
  private List<ZkServerClient> parseClientLines(final String version, final Iterator<String> iterator) {
    if (version.contains("3.4.")) {
      return parse34Clients(iterator);
    } else {
      return parse33Clients(iterator);
    }

  }

  private List<ZkServerClient> parse33Clients(final Iterator<String> iterator) {
    final List<String> clientLines = new LinkedList<String>();
    String clientLine = iterator.next();
    while (iterator.hasNext() && clientLine.trim().length() > 0 && clientLine.startsWith(" /")) {
      clientLines.add(clientLine);
      clientLine = iterator.next();
    }
    return createClients(clientLines);
  }

  private List<ZkServerClient> parse34Clients(final Iterator<String> iterator) {
    final String clientLine = iterator.next();

    return createClients(Arrays.asList(StringUtils.split(clientLine, " ")));
  }

  private List<ZkServerClient> createClients(final List<String> clientLines) {
    final List<ZkServerClient> clients = new LinkedList<ZkServerClient>();
    for (final String clientLine : clientLines) {
      if (!StringUtils.isWhitespace(clientLine)) {
        clients.add(parseClient(clientLine));
      }
    }
    return clients;

  }

  private String parseStringFromLine(final String line, final String delimiter) {
    return StringUtils.split(line, delimiter)[1].trim();
  }

  private Integer parseIntFromLine(final String line, final String delimiter) {
    final String value = StringUtils.split(line, delimiter)[1];
    return Integer.parseInt(value.trim());
  }

  private ZkServerClient parseClient(final String line) {
    Matcher matcher = ipv4ClientLinePattern.matcher(line);
    final boolean ipv4found = matcher.find();
    if (!ipv4found) {
      matcher = ipv6ClientLinePattern.matcher(line);
      matcher.find();
    }

    final String host = matcher.group(1);
    final Integer port = Integer.parseInt(matcher.group(2));
    final Integer ops = Integer.parseInt(matcher.group(3));
    String statSegment = matcher.group(4);
    if (statSegment.endsWith(")")) {
      statSegment = statSegment.substring(0, statSegment.length() - 1);
    }
    final String[] stats = StringUtils.split(statSegment, ",");
    final Integer queued = parseIntFromLine(stats[0], ATTRIBUTE_DELIMITER);
    final Integer received = parseIntFromLine(stats[1], ATTRIBUTE_DELIMITER);
    final Integer sent = parseIntFromLine(stats[2], ATTRIBUTE_DELIMITER);
    final ZkServerClient client = new ZkServerClient(host, port, ops, queued, sent, received);
    return client;

  }

}
