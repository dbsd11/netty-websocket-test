package group.bison.netty.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/{profile}")
public class ProfileTestController {

    @RequestMapping("/index")
    public Mono reportFromHttp(@PathVariable("profile") String profile, @RequestBody(required = false) String body) {
        return Mono.just(profile + " " + (StringUtils.isEmpty(body) ? "Hi" : ("Hi, I received request body :" + body)));
    }
}
