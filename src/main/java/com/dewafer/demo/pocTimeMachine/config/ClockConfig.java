package com.dewafer.demo.pocTimeMachine.config;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Configuration
public class ClockConfig {

    @Bean
    @Profile("timeMachine")
    public DelegateClock delegatingClock() {
        return new DelegateClock(Clock.systemDefaultZone());
    }

    @Bean
    @Profile("!timeMachine")
    public Clock systemClock() {
        return Clock.systemDefaultZone();
    }

    @RequiredArgsConstructor
    public static class DelegateClock extends Clock {

        @Setter
        @Getter
        @NonNull
        private Clock delegateClock;

        @Override public ZoneId getZone() {
            return this.delegateClock.getZone();
        }

        @Override public Clock withZone(ZoneId zone) {
            return this.delegateClock.withZone(zone);
        }

        @Override public Instant instant() {
            return this.delegateClock.instant();
        }
    }
}
