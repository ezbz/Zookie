package org.projectx.zookeeper.bean;

public class ServiceMethodDefinition {

  private final String patterns;

  private final String methods;

  private final String params;

  private final String headers;

  private final String consumes;

  private final String produces;

  private final String custom;

  private final String controllerMethod;

  public ServiceMethodDefinition(final String patterns, final String methods, final String params,
      final String headers, final String consumes, final String produces, final String custom,
      final String controllerMethod) {
    this.patterns = patterns;
    this.methods = methods;
    this.params = params;
    this.headers = headers;
    this.consumes = consumes;
    this.produces = produces;
    this.custom = custom;
    this.controllerMethod = controllerMethod;
  }

  public String getPatterns() {
    return patterns;
  }

  public String getMethods() {
    return methods;
  }

  public String getParams() {
    return params;
  }

  public String getHeaders() {
    return headers;
  }

  public String getConsumes() {
    return consumes;
  }

  public String getProduces() {
    return produces;
  }

  public String getCustom() {
    return custom;
  }

  public String getControllerMethod() {
    return controllerMethod;
  }

}
