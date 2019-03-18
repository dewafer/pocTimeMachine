package com.dewafer.demo.pocTimeMachine;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;

import java.time.ZoneId;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

import io.restassured.RestAssured;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PocTimeMachineApplicationTests {

    @LocalServerPort
    private int port;

    @Before
    public void setUp() {
        RestAssured.port = this.port;
    }

    @Test
    public void contextLoads() {
        log.info("Context Up!");
    }

    @Test
    public void testCurrentTime() throws Exception {

        // @formatter:off
        given()
            .log().all()
        .when()
            .get("/now")
        .then()
            .log().all()
            .body(endsWith("Z")) // ends with Z indicating UTC
        ;

        // at zone
        given()
            .log().all()
            .params("zoneId", "EAT")
        .when()
            .get("/now")
        .then()
            .log().all()
            .body(endsWith("[" + ZoneId.of("EAT", ZoneId.SHORT_IDS) + "]")) // ends with zone id of EAT
        ;

        // @formatter:on

    }

    @Test
    public void testCurrentTime_invalidTimeZone() throws Exception {

        // @formatter:off
        // at zone
        given()
            .log().all()
            .params("zoneId", "xxxx-invalid-timezone-yyyy")
        .when()
            .get("/now")
        .then()
            .log().all()
            .body(endsWith("Z")) // if timezone is invalid, UTC will be used
        ;

        // @formatter:on
    }

    @Test
    public void testAtZone_PST_and_reset() throws Exception {

        // @formatter:off
        given()
            .log().all()
        .when()
            .get("/zone")
        .then()
            .log().all()
            .body(is(ZoneId.systemDefault().toString()))
        ;

        given()
            .log().all()
            .formParam("zoneId", "PST")
        .when()
            .post("/time-machine/at-zone")
        .then()
            .log().all()
            .body(startsWith("OK, set timezone at: " + ZoneId.of("PST", ZoneId.SHORT_IDS).toString()))
        ;

        given()
            .log().all()
        .when()
            .get("/zone")
        .then()
            .log().all()
            .body(is(ZoneId.of("PST", ZoneId.SHORT_IDS).toString()))
        ;

        // reset

        given()
            .log().all()
        .when()
            .post("/time-machine/reset")
        .then()
            .log().all()
            .body(equalTo("Done, clock reset to default."))
        ;

        given()
            .log().all()
        .when()
            .get("/zone")
        .then()
            .log().all()
            .body(is(ZoneId.systemDefault().toString()))
        ;
        // @formatter:on
    }

    @Test
    public void testAtZone_invalid() throws Exception {

        // @formatter:off
        given()
            .log().all()
            .formParam("zoneId", "xxxx-invalid-timezone-yyyy")
        .when()
            .post("/time-machine/at-zone")
        .then()
            .log().all()
            .statusCode(HttpStatus.BAD_REQUEST.value())
        ;
        // @formatter:on
    }

    @Test
    public void testSetFixedAt() throws Exception {

        // @formatter:off
        given()
            .log().all()
            .formParams("at", "1998-12-21T05:06:07",
                    "zone", "AET")
        .when()
            .post("/time-machine/fixed-at")
        .then()
            .log().all()
            .body(is("OK, set fixed time at: 1998-12-21T05:06:07 at zone: Australia/Sydney"))
        ;

        given()
            .log().all()
        .when()
            .get("/now")
        .then()
            .log().all()
            .body(equalTo("1998-12-20T18:06:07Z")) // 98-12-21 05:06:07 at Australia/Sydney is
                                                            // 98-12-20 18:06:07 at UTC (-11 hrs at Dec. AEDT)
        ;

        // set fixed at without zone
        given()
            .log().all()
            .formParams("at", "1992-05-06T07:08:09")
        .when()
            .post("/time-machine/fixed-at")
        .then()
            .log().all()
            .body(is("OK, set fixed time at: 1992-05-06T07:08:09 at zone: " + ZoneId.systemDefault()))
        ;

        // wait for 3 seconds
        TimeUnit.SECONDS.sleep(3);

        // fixed time will not tick, that's why it's called fixed
        given()
            .log().all()
            .params("zoneId", ZoneId.systemDefault().toString())
        .when()
            .get("/now")
        .then()
            .log().all()
            .body(startsWith("1992-05-06T07:08:09")) // still 1992-05-06T07:08:09 at system default zone
            .body(endsWith("[" + ZoneId.systemDefault() + "]"))
        ;

        // reset

        given()
            .log().all()
        .when()
            .post("/time-machine/reset")
        .then()
            .log().all()
            .body(equalTo("Done, clock reset to default."))
        ;

        // @formatter:on

    }

    @Test
    public void testSetFixed_invalid() throws Exception {

        // @formatter:off

        given()
            .log().all()
            .formParams("at", "abcdefg-higjklmn-ope", // invalid time
                    "zone", "AET")
        .when()
            .post("/time-machine/fixed-at")
        .then()
            .log().all()
            .statusCode(HttpStatus.BAD_REQUEST.value())
        ;

                given()
            .log().all()
            .formParams("at", "1998-12-21T05:06:07",
                    "zone", "qerhqrthwqrtgwrtgqergqergqergqergqergqerg") // invalid zone
        .when()
            .post("/time-machine/fixed-at")
        .then()
            .log().all()
            .statusCode(HttpStatus.BAD_REQUEST.value())
        ;

        // @formatter:on

    }

}
