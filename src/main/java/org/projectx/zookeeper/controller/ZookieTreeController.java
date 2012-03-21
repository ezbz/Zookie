package org.projectx.zookeeper.controller;

import javax.servlet.http.HttpServletResponse;

import org.projectx.zookeeper.ZookeeperTreeWalker;
import org.projectx.zookeeper.bean.ZkSimpleNode;
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
@RequestMapping("/tree")
public class ZookieTreeController {
  private static final Logger log = LoggerFactory.getLogger(ZookieTreeController.class);
  private final ZookeeperTreeWalker zookeeperTreeWalker;

  ZookieTreeController(final ZookeeperTreeWalker zookeeperTreeWalker) {
    Assert.notNull(zookeeperTreeWalker, "zookeeperTreeWalker may not be null");
    this.zookeeperTreeWalker = zookeeperTreeWalker;
  }

  @RequestMapping(method = RequestMethod.GET)
  public @ResponseBody
  ZkSimpleNode tree(@RequestParam(defaultValue = "/", required = false) final String root,
      @RequestParam(defaultValue = "true", required = false) final Boolean data,
      @RequestParam(defaultValue = "true", required = false) final Boolean stat,
      @RequestParam(defaultValue = "1", required = false) final Integer depth) {

    log.debug("Going to walk root [{}], show-data [{}], show-stat [{}], max-depth [{}]",
        new Object[] { root, data, stat, depth });
    try {
      return zookeeperTreeWalker.walk(root, data, stat, depth);
    } catch (final Throwable e) {
      log.error("error while walking tree", e);
      throw new IllegalStateException(e);
    }
  }

  @ExceptionHandler(value = { Exception.class })
  public @ResponseBody
  String handleError(final Exception ex, final HttpServletResponse response) {
    return "got error while handling a rquest: " + ex.getMessage();
  }

}