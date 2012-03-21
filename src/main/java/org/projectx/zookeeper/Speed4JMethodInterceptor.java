package org.projectx.zookeeper;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.util.Assert;

import com.ecyrd.speed4j.StopWatch;

/**
 * A method interceptor used to measure and report execution times of various methods on a target object.
 * @author erez
 *
 */
public class Speed4JMethodInterceptor implements MethodInterceptor {
  private final Speed4JMethodTimer zkClientTimer;

  public Speed4JMethodInterceptor(final Speed4JMethodTimer zkClientTimer) {
    Assert.notNull(zkClientTimer, "zkClientTimer cannot be null");
    this.zkClientTimer = zkClientTimer;
  }

  @Override
  public Object invoke(final MethodInvocation invocation) throws Throwable {
    final StopWatch timer = zkClientTimer.start();
    try {
      final Object returnValue = invocation.proceed();
      zkClientTimer.stop(timer, invocation.getMethod().getName(), true);
      return returnValue;
    } finally {
      zkClientTimer.stop(timer, invocation.getMethod().getName(), false);
    }
  }
}
