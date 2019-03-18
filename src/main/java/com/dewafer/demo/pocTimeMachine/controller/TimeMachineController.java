package com.dewafer.demo.pocTimeMachine.controller;

import java.time.Clock;
import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.dewafer.demo.pocTimeMachine.config.ClockConfig;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/time-machine/")
@Profile("timeMachine")
public class TimeMachineController {

    @Autowired
    private ClockConfig.DelegateClock delegateClock;

    @PostMapping("/reset")
    public String reset() {
        this.delegateClock.setDelegateClock(Clock.systemDefaultZone());
        return "Done, clock reset to default.";
    }

    @PostMapping("/at-zone")
    public String atZone(@RequestParam("zoneId") String zoneId) {
        ZoneId targetedZoneId = ZoneId.of(zoneId, ZoneId.SHORT_IDS);
        this.delegateClock.setDelegateClock(this.delegateClock.getDelegateClock().withZone(targetedZoneId));
        return "OK, set timezone at: " + targetedZoneId;
    }

    @PostMapping("/fixed-at")
    public String fixedAt(@RequestParam("at") String atTimeStamp,
            @RequestParam(name = "zone", required = false) String zone) {

        LocalDateTime localDateTime = LocalDateTime.from(DateTimeFormatter.ISO_LOCAL_DATE_TIME.parse(atTimeStamp));
        ZoneId atZone;
        if (StringUtils.isEmpty(zone)) {
            atZone = ZoneId.systemDefault();
        } else {
            atZone = ZoneId.of(zone, ZoneId.SHORT_IDS);
        }

        Instant atInstant = localDateTime.atZone(atZone).toInstant();

        this.delegateClock.setDelegateClock(Clock.fixed(atInstant, atZone));
        return "OK, set fixed time at: " + atTimeStamp + " at zone: " + atZone;

    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DateTimeException.class)
    public String handleDateTimeException(DateTimeException e) {
        log.warn("DateTimeException", e);
        return e.getLocalizedMessage();
    }
}
