package org.projectx.zookeeper;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;

import org.I0Itec.zkclient.ZkClient;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.projectx.zookeeper.ZookeeperStatsReader;
import org.projectx.zookeeper.ZookeeperTreeWalker;

@RunWith(MockitoJUnitRunner.class)
public class ZookeeperTreeWalkerTest {

  ZookeeperTreeWalker classUnderTest;

  @Mock
  private ZkClient mockZkClient;
  @Mock
  private ZookeeperStatsReader mockZookeeperStatsReader;

  @Before
  public void before() {
    classUnderTest = new ZookeeperTreeWalker(mockZkClient, mockZookeeperStatsReader);
  }

  @Test
  public void testWalk() {
    when(mockZkClient.getChildren("/")).thenReturn(Collections.singletonList("test"));
    classUnderTest.walk("/", true, true, -1);
    verify(mockZkClient).getChildren("/");
    verify(mockZkClient).readData("/", true);
    verify(mockZookeeperStatsReader).readStats("/");
    verify(mockZkClient).getChildren("/test");
    verify(mockZkClient).readData("/test", true);
    verify(mockZookeeperStatsReader).readStats("/test");
  }

  @Test
  public void testWalkNoData() {
    when(mockZkClient.getChildren("/")).thenReturn(Collections.singletonList("test"));
    classUnderTest.walk("/", false, true, 1);
    verify(mockZkClient).getChildren("/");
    verify(mockZkClient, times(0)).readData("/", true);
    verify(mockZkClient).getChildren("/test");
    verify(mockZkClient, times(0)).readData("/test", true);
  }

  @Test
  public void testWalkNoDepth() {
    when(mockZkClient.getChildren("/")).thenReturn(Collections.singletonList("test"));
    classUnderTest.walk("/", true, false, 0);
    verify(mockZkClient).getChildren("/");
    verify(mockZkClient).readData("/", true);
    verify(mockZkClient, times(0)).getChildren("/test");
    verify(mockZkClient, times(0)).readData("/test", true);
  }

  @Test
  public void testWalkNonRoot() {
    when(mockZkClient.getChildren("/test")).thenReturn(Collections.singletonList("test"));
    classUnderTest.walk("/test", true, false, 0);
    verify(mockZkClient).getChildren("/test");
    verify(mockZkClient).readData("/test", true);
    verify(mockZkClient, times(0)).getChildren("/test/test");
    verify(mockZkClient, times(0)).readData("/test/test", true);
  }
}
