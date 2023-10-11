package group.bison.netty.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/index")
public class IndexController {

    @RequestMapping("")
    public Mono reportFromHttp(@RequestBody(required = false) String body) {
        return Mono.just(StringUtils.isEmpty(body) ? "Hi" : ("Hi, I received request body :" + body));
    }
}
