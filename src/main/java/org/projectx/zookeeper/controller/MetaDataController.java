package org.projectx.zookeeper.controller;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.projectx.zookeeper.bean.ServiceMethodDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.config.annotation.DelegatingWebMvcConfiguration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.condition.RequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * Handles requests for the application home page.
 */
@Controller
@RequestMapping(value = "/meta")
@EnableWebMvc
public class MetaDataController {
  @Autowired
  private DelegatingWebMvcConfiguration delegatingWebMvcConfiguration;

  @RequestMapping(method = RequestMethod.GET)
  @ResponseBody
  public List<ServiceMethodDefinition> get() {
    final RequestMappingHandlerMapping requestMappingHandlerMapping = delegatingWebMvcConfiguration.requestMappingHandlerMapping();
    final Map<RequestMappingInfo, HandlerMethod> handlerMethods = requestMappingHandlerMapping.getHandlerMethods();

    final List<ServiceMethodDefinition> urls = new LinkedList<ServiceMethodDefinition>();
    for (final Entry<RequestMappingInfo, HandlerMethod> element : handlerMethods.entrySet()) {

      final RequestMappingInfo mappingInfo = element.getKey();
      final HandlerMethod handlerMethod = element.getValue();
      urls.add(new ServiceMethodDefinition(nullSafeString(mappingInfo.getPatternsCondition()),
          nullSafeString(mappingInfo.getMethodsCondition()),
          nullSafeString(mappingInfo.getParamsCondition()),
          nullSafeString(mappingInfo.getHeadersCondition()),
          nullSafeString(mappingInfo.getConsumesCondition()),
          nullSafeString(mappingInfo.getProducesCondition()),
          nullSafeString(mappingInfo.getCustomCondition()), handlerMethod.getMethod()
                                                                         .toGenericString()));
    }

    return urls;
  }

  private String nullSafeString(final RequestCondition<?> patternsCondition) {
    return patternsCondition == null ? null : patternsCondition.toString();
  }

}
