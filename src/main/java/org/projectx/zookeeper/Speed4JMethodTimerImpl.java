package org.projectx.zookeeper;

import com.ecyrd.speed4j.StopWatch;
import com.ecyrd.speed4j.StopWatchFactory;
import com.ecyrd.speed4j.log.PeriodicalLog;
import com.ecyrd.speed4j.log.PeriodicalLog.Mode;

/**
 * A configurable implementation of the {@link Speed4JMethodTimer} intended to work with provided properties for domain (serviceName),
 *  {@link Mode mode}, logger name, sample period, etc.
 * @author erez
 *
 */
public class Speed4JMethodTimerImpl implements Speed4JMethodTimer {

  private static final String FAILURE = ".failure";
  private static final String SUCCESS = ".success";
  private static final String ZK_CLIENT_PREFIX = "zkClient-";
  private final StopWatchFactory stopWatchFactory;

  public Speed4JMethodTimerImpl(final Mode mode, final String jmxString, final String serviceName, final String slf4jLogName, final int period) {
    final PeriodicalLog periodicalLog = new PeriodicalLog();
    periodicalLog.setName(ZK_CLIENT_PREFIX + serviceName);
    periodicalLog.setMode(mode);
    periodicalLog.setJmx(jmxString);
    periodicalLog.setPeriod(period);
    periodicalLog.setSlf4jLogname(slf4jLogName);

    stopWatchFactory = StopWatchFactory.getInstance(periodicalLog);
  }

  @Override
  public StopWatch start() {
    return stopWatchFactory.getStopWatch();
  }

  @Override
  public void stop(final StopWatch stopWatch, final String methodName, final boolean success) {
    stopWatch.stop(methodName.concat(success ? SUCCESS : FAILURE));
  }

}
