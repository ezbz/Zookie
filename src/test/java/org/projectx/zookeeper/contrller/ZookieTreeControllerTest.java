package org.projectx.zookeeper.contrller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.I0Itec.zkclient.ZkClient;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.server.ResultActions;
import org.springframework.test.web.server.request.DefaultRequestBuilder;

public class ZookieTreeControllerTest extends ControllerTestBase {
  private static final String TEST_DATA = "TEST_DATA";
  @Autowired
  private ZkClient zkClient;

  public String createTestNode(final String path, final Object data) {
    zkClient.createPersistent(path);
    zkClient.writeData(path, data);
    return path;
  }

  public void removeNodes(final String... paths) {
    for (final String path : paths) {
      zkClient.deleteRecursive(path);
    }
  }

  @Test
  public void testTree() throws Exception {
    final String rootNode = createTestNode("/" + UUID.randomUUID().toString(), TEST_DATA);
    final ResultActions action = getWithBuilder(new RequestBuilderCallback() {

      @Override
      public void doInBuilder(final DefaultRequestBuilder requestBuilder) {
        requestBuilder.param("root", rootNode);
        requestBuilder.param("data", Boolean.TRUE.toString());
      }
    }, "/tree");
    assertBasicOkJson(action);
    action.andExpect(jsonPath("$.path").value(rootNode));
    action.andExpect(jsonPath("$.data").value(TEST_DATA));
    removeNodes(rootNode);
  }

  @Test
  public void testDepth() throws Exception {
    final String rootNode = createTestNode("/" + UUID.randomUUID().toString(), TEST_DATA);
    final String childLevel1 = createTestNode(rootNode + "/child1", TEST_DATA);
    final String childLevel2 = createTestNode(childLevel1 + "/child2", TEST_DATA);
    final String childLevel3 = createTestNode(childLevel2 + "/child2", TEST_DATA);
    final ResultActions action = getWithBuilder(new RequestBuilderCallback() {

      @Override
      public void doInBuilder(final DefaultRequestBuilder requestBuilder) {
        requestBuilder.param("root", rootNode);
        requestBuilder.param("data", Boolean.TRUE.toString());
        requestBuilder.param("depth", "3");
      }
    }, "/tree");
    assertBasicOkJson(action);
    action.andExpect(jsonPath("$.path").value(rootNode));
    action.andExpect(jsonPath("$.children[0].path").value(childLevel1));
    action.andExpect(jsonPath("$.children[0].children[0].path").value(childLevel2));
    action.andExpect(jsonPath("$.children[0].children[0].children[0].path").value(childLevel3));
    removeNodes(childLevel3, childLevel2, childLevel1, rootNode);
  }

  @Test
  public void testDepthLimit() throws Exception {
    final String rootNode = createTestNode("/" + UUID.randomUUID().toString(), TEST_DATA);
    final String childLevel1 = createTestNode(rootNode + "/child1", TEST_DATA);
    final String childLevel2 = createTestNode(childLevel1 + "/child2", TEST_DATA);
    final String childLevel3 = createTestNode(childLevel2 + "/child2", TEST_DATA);
    final ResultActions action = getWithBuilder(new RequestBuilderCallback() {

      @Override
      public void doInBuilder(final DefaultRequestBuilder requestBuilder) {
        requestBuilder.param("root", rootNode);
        requestBuilder.param("data", Boolean.TRUE.toString());
        requestBuilder.param("depth", "2");
      }
    }, "/tree");
    assertBasicOkJson(action);
    action.andExpect(jsonPath("$.path").value(rootNode));
    action.andExpect(jsonPath("$.children[0].path").value(childLevel1));
    action.andExpect(jsonPath("$.children[0].children[0].path").value(childLevel2));
    action.andExpect(jsonPath("$.children[0].children[0].children").doesNotExist());
    removeNodes(childLevel3, childLevel2, childLevel1, rootNode);
  }

  @Test
  public void testUpdate() throws Exception {
    final String rootNode = createTestNode("/" + UUID.randomUUID().toString(), TEST_DATA);
    final String modifiedData = "TEST_DATA_MODIFIED";
    final ResultActions action = postJsonWithBuilder(new RequestBuilderCallback() {

      @Override
      public void doInBuilder(final DefaultRequestBuilder requestBuilder) {
        requestBuilder.param("path", rootNode);

      }
    }, modifiedData, "/tree/update");
    action.andExpect(status().isOk());
    final String actual = zkClient.readData(rootNode);
    assertEquals("data wasn't modified", modifiedData, actual);
    removeNodes(rootNode);
  }

  @Test
  public void testDelete() throws Exception {
    final String rootNode = createTestNode("/" + UUID.randomUUID().toString(), TEST_DATA);
    final DefaultRequestBuilder request = delete("/tree/delete");
    request.param("path", rootNode);
    request.param("recursive", Boolean.TRUE.toString());
    final ResultActions action = getMvc().perform(request);
    action.andExpect(status().isOk());
    printResult(action);

    assertFalse("node wasn't deleted", zkClient.exists(rootNode));
    removeNodes(rootNode);
  }
}
