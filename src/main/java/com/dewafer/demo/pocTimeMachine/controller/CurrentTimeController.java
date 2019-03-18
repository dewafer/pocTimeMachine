package com.dewafer.demo.pocTimeMachine.controller;

import java.time.Clock;
import java.time.DateTimeException;
import java.time.Instant;
import java.time.ZoneId;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class CurrentTimeController {

    @Autowired
    private Clock clock;

    @GetMapping("/now")
    public String now(@RequestParam(required = false, name = "zoneId") String zoneId) {

        if (!StringUtils.isEmpty(zoneId)) {
            try {
                ZoneId targetZoneId = ZoneId.of(zoneId, ZoneId.SHORT_IDS);
                return Instant.now(this.clock).atZone(targetZoneId).toString();
            } catch (DateTimeException e) {
                log.warn("invalid zone id: " + zoneId, e);
            }
        }
        return Instant.now(this.clock).toString();
    }

    @GetMapping("/zone")
    public String getZone() {
        return this.clock.getZone().toString();
    }

}
