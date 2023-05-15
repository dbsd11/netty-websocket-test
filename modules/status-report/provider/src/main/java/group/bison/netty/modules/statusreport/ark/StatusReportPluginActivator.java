package group.bison.netty.modules.statusreport.ark;

import com.alipay.sofa.ark.spi.model.PluginContext;
import com.alipay.sofa.ark.spi.service.PluginActivator;

import group.bison.netty.modules.statusreport.facade.StatusReportService;

// @Component
public class StatusReportPluginActivator implements PluginActivator {

    // @Autowired
    private StatusReportService statusReportService;

    @Override
    public void start(PluginContext context) {
        System.out.println("start plugin status report");
        context.publishService(StatusReportService.class, statusReportService);
    }

    @Override
    public void stop(PluginContext context) {
    }
}