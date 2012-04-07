package org.projectx.zookeeper.contrller;

import org.springframework.test.web.server.request.DefaultRequestBuilder;

public class NoopBuilderCallback implements RequestBuilderCallback {

  @Override
  public void doInBuilder(final DefaultRequestBuilder requestBuilder) {
  }

}
