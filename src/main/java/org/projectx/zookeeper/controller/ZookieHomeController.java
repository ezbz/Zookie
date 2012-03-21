package org.projectx.zookeeper.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/")
public class ZookieHomeController {
  @RequestMapping(method = RequestMethod.GET)
  public String home() {
    return "home";
  }
}
