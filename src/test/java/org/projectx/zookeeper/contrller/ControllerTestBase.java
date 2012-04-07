package org.projectx.zookeeper.contrller;

import static org.springframework.test.web.server.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.server.MockMvc;
import org.springframework.test.web.server.ResultActions;
import org.springframework.test.web.server.request.DefaultRequestBuilder;
import org.springframework.test.web.server.result.PrintingResultHandler;
import org.springframework.test.web.server.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = TestGenericWebXmlContextLoader.class, locations = {
                                                                                  "file:src/main/webapp/WEB-INF/spring/Zookie-servlet.xml",
                                                                                  "classpath:applicationContext-Zookie-all.xml" })
public class ControllerTestBase {
  private static final Logger log = LoggerFactory.getLogger(ControllerTestBase.class);

  static {
    System.setProperty("org.projectx.zookeeper.connection.class",
        "org.projectx.zookeeper.TestInMemoryConnection");
  }

  public static final String JSON_TYPE = "application/json;charset=UTF-8";

  @Autowired
  private WebApplicationContext webApplicationContext;

  private MockMvc mockMvc;

  private final ObjectMapper mapper = new ObjectMapper();

  @Before
  public void setup() {
    mockMvc = MockMvcBuilders.webApplicationContextSetup(webApplicationContext).build();
  }

  protected MockMvc getMvc() {
    return mockMvc;
  }

  protected String toJson(final Object object) throws IOException, JsonGenerationException,
      JsonMappingException {
    if (null == object) {
      return "{}";
    }
    final Writer strWriter = new StringWriter();
    mapper.writeValue(strWriter, object);
    return strWriter.toString();
  }

  protected void printAsJson(final Object entity) {
    if (null == entity) {
      return;
    }
    try {
      log.info(toJson(entity));
    } catch (final Exception e) {
      log.warn("Could not print entity as json: [{}]", entity, e);
    }

  }

  protected void printResult(final ResultActions action) {
    try {
      action.andDo(new PrintingResultHandler(System.out));
    } catch (final Exception e) {
      log.error("Error printing action results", e);
    }
  }

  protected ResultActions doGet(final String uri) {
    return getWithBuilder(new NoopBuilderCallback(), uri);
  }

  protected ResultActions getWithBuilder(final RequestBuilderCallback callback, final String uri) {
    try {
      final DefaultRequestBuilder requestBuilder = get(uri);
      callback.doInBuilder(requestBuilder);
      final ResultActions action = getMvc().perform(requestBuilder);
      printResult(action);
      return action;
    } catch (final Exception e) {
      throw new IllegalStateException(e);
    }
  }

  protected ResultActions doGet(final String uri, final Object... args) {
    return doGet(String.format(uri, args));
  }

  protected ResultActions postJsonWithBuilder(final RequestBuilderCallback callback,
      final Object entity, final String uri) {
    try {
      printAsJson(entity);
      final DefaultRequestBuilder request = post(uri);
      callback.doInBuilder(request);
      request.body(toJson(entity).getBytes());
      request.contentType(MediaType.APPLICATION_JSON);
      final ResultActions action = getMvc().perform(request);
      printResult(action);
      return action;
    } catch (final Exception e) {
      throw new IllegalStateException(e);
    }
  }

  protected ResultActions doPostJson(final Object entity, final String uri) {
    return postJsonWithBuilder(new NoopBuilderCallback(), entity, uri);
  }

  protected void assertBasicOkJson(final ResultActions action) throws Exception {
    action.andExpect(status().isOk());
    action.andExpect(content().type(JSON_TYPE));
  }
}

class TestGenericWebXmlContextLoader extends GenericWebXmlContextLoader {
  public TestGenericWebXmlContextLoader() {
    super("file:src/main/webapp/", true);
  }

}
