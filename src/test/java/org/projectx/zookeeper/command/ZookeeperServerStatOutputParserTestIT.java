package org.projectx.zookeeper.command;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.projectx.zookeeper.bean.ZkServerStat;
import org.springframework.core.io.FileSystemResource;

public class ZookeeperServerStatOutputParserTestIT {
  private List<String> input334;
  private List<String> input340;

  private ZookeeperServerStatOutputParser outputParser;

  @Before
  public void before() throws IOException {
    input334 = IOUtils.readLines(new FileSystemResource("src/test/resources/stat334.txt").getInputStream());
    input340 = IOUtils.readLines(new FileSystemResource("src/test/resources/stat340.txt").getInputStream());

    outputParser = new ZookeeperServerStatOutputParser();
  }

  @Test
  public void testParse() {
    ZkServerStat stat = outputParser.parse(input334);
    assertNotNull("result object is null", stat);
    stat = outputParser.parse(input340);
    assertNotNull("result object is null", stat);
  }
}
