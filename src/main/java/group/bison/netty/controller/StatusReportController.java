package group.bison.netty.controller;

import com.alipay.sofa.runtime.api.annotation.SofaReference;
import group.bison.netty.modules.statusreport.facade.StatusReportService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/status")
public class StatusReportController {

    @SofaReference
    private StatusReportService statusReportService;

    @RequestMapping("/report")
    public Mono reportFromHttp(@RequestBody String body) {
        String processResult = statusReportService.onReport("http", body);
        return Mono.just(processResult);
    }
}
