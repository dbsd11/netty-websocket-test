package group.bison.netty.modules.statusreport.provider;

import org.springframework.stereotype.Service;

import com.alipay.sofa.runtime.api.annotation.SofaService;

import group.bison.netty.modules.statusreport.facade.StatusReportService;

@Service
@SofaService
public class StatusReportServiceImpl implements StatusReportService {

    {
        System.out.println("inited StatusReportService ClassLoader:" + Thread.currentThread().getContextClassLoader());
    }

    @Override
    public String onReport(String from, Object body) {
        if(body instanceof String) {
            String reportContent = (String) body;
            System.out.println("receive from " + from + " status report: " + reportContent);

            if("websocket".equals(from) && reportContent.contains("seq:")) {
                int seq = Integer.valueOf(reportContent.substring(reportContent.indexOf("seq:") + 4, reportContent.indexOf("seq:") + 7).trim()).intValue();
                return from + " ok " + "seq: " + seq;
            }
            return from + " ok ";
        }
        return null;
    }
}
