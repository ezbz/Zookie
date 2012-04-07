package org.projectx.zookeeper.controller;

import javax.servlet.http.HttpServletResponse;

import org.I0Itec.zkclient.ZkClient;
import org.projectx.zookeeper.ZookeeperTreeWalker;
import org.projectx.zookeeper.bean.ZkSimpleNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/tree")
public class ZookieTreeController {
  private static final Logger log = LoggerFactory.getLogger(ZookieTreeController.class);
  private final ZookeeperTreeWalker zookeeperTreeWalker;
  private final ZkClient zkClient;

  ZookieTreeController(final ZookeeperTreeWalker zookeeperTreeWalker, final ZkClient zkClient) {
    Assert.notNull(zookeeperTreeWalker, "zookeeperTreeWalker may not be null");
    Assert.notNull(zkClient, "zkClient cannot be null");
    this.zookeeperTreeWalker = zookeeperTreeWalker;
    this.zkClient = zkClient;
  }

  @RequestMapping(method = RequestMethod.GET)
  public @ResponseBody
  ZkSimpleNode tree(@RequestParam(defaultValue = "/", required = false) final String root,
      @RequestParam(defaultValue = "true", required = false) final Boolean data,
      @RequestParam(defaultValue = "true", required = false) final Boolean stat,
      @RequestParam(defaultValue = "1", required = false) final Integer depth) {

    log.debug("Going to walk root [{}], show-data [{}], show-stat [{}], max-depth [{}]",
        new Object[] { root, data, stat, depth });
    return zookeeperTreeWalker.walk(root, data, stat, depth);

  }

  @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
  public @ResponseBody
  void delete(@RequestParam final String path,
      @RequestParam(defaultValue = "false", required = false) final boolean recursive) {
    log.debug("Going to delete path [{}]", path);
    if (recursive) {
      zkClient.deleteRecursive(path);
    } else {
      zkClient.delete(path);
    }
  }

  @RequestMapping(value = "/update", method = RequestMethod.POST)
  public @ResponseBody
  void update(@RequestParam final String path, @RequestBody final Object data) {
    log.debug("Going to update path [{}] with data [{}]", path, data);
    zkClient.writeData(path, data);
  }

  @ExceptionHandler(value = { Exception.class })
  public @ResponseBody
  String handleError(final Exception ex, final HttpServletResponse response) {
    return "got error while handling a rquest: " + ex.getMessage();
  }

}