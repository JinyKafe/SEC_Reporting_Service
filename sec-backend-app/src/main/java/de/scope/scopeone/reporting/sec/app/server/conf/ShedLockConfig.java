package de.scope.scopeone.reporting.sec.app.server.conf;

import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.provider.redis.spring.RedisLockProvider;
import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@EnableSchedulerLock(defaultLockAtMostFor = "5m")
public class ShedLockConfig {

  @Value("${app.config.schedule.env}")
  private String scheduledEnv;

  @Bean
  public LockProvider lockProvider(JedisConnectionFactory jedisConnectionFactory) {
    return new RedisLockProvider(jedisConnectionFactory, scheduledEnv);
  }
}
