package org.projectx.zookeeper;

import com.ecyrd.speed4j.StopWatch;

/**
 * A Speed4J Method Timer wrapper interface which expose basic operations for time-based,
 *  success/failure oriented method executions.
 * @author erez
 *
 */
public interface Speed4JMethodTimer {
  /**
   * Create a new {@link StopWatch} object
   * @return an instance of a {@link StopWatch} to be used to measure execution times
   */
  StopWatch start();

  /**
   * Stop a {@link StopWatch} instance and report the execution method name and the success flag
   * @param stopWatch the {@link StopWatch} instance used to measure execution
   * @param methodName the measured method name
   * @param success the success flag indicating a successful/failed execution
   */
  void stop(StopWatch stopWatch, String methodName, boolean success);
}
