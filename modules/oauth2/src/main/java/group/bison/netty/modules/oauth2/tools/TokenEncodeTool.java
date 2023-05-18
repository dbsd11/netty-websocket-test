package group.bison.netty.modules.oauth2.tools;


import org.springframework.util.StringUtils;

import java.util.Base64;

public class TokenEncodeTool {

    public static String ENV = null;

    public static String getBase64CodeOrTokenWithEnv(String codeOrToken) {
        if (StringUtils.isEmpty(ENV) || StringUtils.isEmpty(codeOrToken)) {
            return null;
        }

        String base64CodeOrTokenWithEnv = null;
        try {
            base64CodeOrTokenWithEnv = Base64.getEncoder().encodeToString(String.join("_", ENV, codeOrToken).getBytes("utf-8"));
        } catch (Exception e) {
        }
        return base64CodeOrTokenWithEnv;
    }
}
