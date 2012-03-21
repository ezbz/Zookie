package org.projectx.zookeeper.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.I0Itec.zkclient.IZkConnection;
import org.apache.commons.lang.StringUtils;
import org.projectx.zookeeper.bean.ZkServerEnvironment;
import org.projectx.zookeeper.bean.ZkServerStat;
import org.projectx.zookeeper.command.ZookeeperServerCommandManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/server")
public class ZookieServerController {
  private static final Logger log = LoggerFactory.getLogger(ZookieServerController.class);
  private final ZookeeperServerCommandManager serverCommandManager;
  private final IZkConnection zkConnection;

  ZookieServerController(final ZookeeperServerCommandManager serverCommandManager,
      final IZkConnection zkConnection) {
    Assert.notNull(serverCommandManager, "serverCommandManager may not be null");
    Assert.notNull(zkConnection, "zkConnection cannot be null");
    this.serverCommandManager = serverCommandManager;
    this.zkConnection = zkConnection;
  }

  @RequestMapping(value = "/list", method = RequestMethod.GET)
  public @ResponseBody
  List<String> list() {

    return getHosts();
  }

  private List<String> getHosts() {
    return Arrays.asList(StringUtils.split(zkConnection.getServers(), ","));
  }

  @RequestMapping(value = "/stat", method = RequestMethod.GET)
  public @ResponseBody
  Map<String, ZkServerStat> statDefault() {
    return stat(getHosts());
  }

  @RequestMapping(value = "/stat", method = RequestMethod.GET, params = { "hosts" })
  public @ResponseBody
  Map<String, ZkServerStat> stat(@RequestParam final List<String> hosts) {

    final Map<String, ZkServerStat> result = new HashMap<String, ZkServerStat>();
    for (final String host : hosts) {
      final String[] hostAndPort = StringUtils.split(host, ":");
      result.put(host, serverCommandManager.stat(hostAndPort[0], Integer.parseInt(hostAndPort[1])));
    }
    return result;
  }

  @RequestMapping(value = "/env", method = RequestMethod.GET)
  public @ResponseBody
  Map<String, ZkServerEnvironment> envDefault() {
    return env(getHosts());
  }

  @RequestMapping(value = "/env", method = RequestMethod.GET, params = { "hosts" })
  public @ResponseBody
  Map<String, ZkServerEnvironment> env(@RequestParam final List<String> hosts) {

    final Map<String, ZkServerEnvironment> result = new HashMap<String, ZkServerEnvironment>();
    for (final String host : hosts) {
      final String[] hostAndPort = StringUtils.split(host, ":");
      result.put(host,
          serverCommandManager.environment(hostAndPort[0], Integer.parseInt(hostAndPort[1])));
    }
    return result;
  }

  @ExceptionHandler(value = { Exception.class })
  public @ResponseBody
  String handleError(final Exception ex, final HttpServletResponse response) {
    log.warn("Exception while handling response", ex);
    return "got error while handling a rquest: " + ex.getMessage();
  }

}