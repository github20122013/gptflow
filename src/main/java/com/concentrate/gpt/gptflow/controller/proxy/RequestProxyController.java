package com.concentrate.gpt.gptflow.controller.proxy;

import com.concentrate.gpt.gptflow.logger.DebugAbleLogger;
import com.concentrate.gpt.gptflow.util.EncryptionUtil;
import com.concentrate.gpt.gptflow.util.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;

/**
 * Created by Administrator on 2024/3/3.
 */
public class RequestProxyController {

    private static Logger logger = new DebugAbleLogger(LoggerFactory.getLogger(RequestProxyController.class));


    @GetMapping("/json/proxy")
    public @ResponseBody String proxy(@RequestParam("param") String encryptedParam,@RequestParam("host") String encryptedHost) {
        String decryptedParam = EncryptionUtil.decrypt(encryptedParam);
        String decriptedHost = EncryptionUtil.decrypt(encryptedParam);

        // 解密后的参数可以继续处理
        return HttpUtil.doGet(HttpUtil.getDecodedUrl(decriptedHost,decryptedParam));
    }

    @PostMapping("/json/proxyPost")
    public String proxyPost(@RequestBody String encryptedRequestBody,@RequestParam(required = false) String encryptedHost) {
        String decryptedRequestBody = EncryptionUtil.decrypt(encryptedRequestBody);

        String decriptedHost = "https://api.openai.com/";
        if(encryptedHost!=null && !"".equals(encryptedHost.trim())){
            decriptedHost =  EncryptionUtil.decrypt(encryptedHost);
        }
        // 解密后的请求体可以继续处理
        return HttpUtil.postJsonData(decriptedHost,decryptedRequestBody);
    }


}
