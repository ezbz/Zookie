package org.projectx.zookeeper.contrller;

import org.springframework.test.web.server.request.DefaultRequestBuilder;

public interface RequestBuilderCallback {

  void doInBuilder(DefaultRequestBuilder requestBuilder);

}
