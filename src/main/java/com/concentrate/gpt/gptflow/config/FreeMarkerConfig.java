package com.concentrate.gpt.gptflow.config;

import com.concentrate.gpt.gptflow.service.defdoc.DefDocTreeService;
import com.concentrate.gpt.gptflow.service.tree.defdoc.DefDoc;
import freemarker.template.Configuration;
import freemarker.template.TemplateModelException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;


/**
 * 给freemarker注入字典，可以直接使用字典内容，不需要前台controller重复注入
 * <body>
 *     <p>${__docMap[xxx]}</p>
 * </body>
 * 这样可以直接使用字典
 */
@Component
public class FreeMarkerConfig {

    @Autowired
    private Configuration configuration;

    @Autowired
    private ApplicationContext applicationContext;

    @PostConstruct
    public void setup() throws TemplateModelException {
        DefDocTreeService treeService = applicationContext.getBean(DefDocTreeService.class);
        Map<String, DefDoc> docMap =  treeService.getNodeMap();
        configuration.setSharedVariable("__docMap", docMap);
    }
}
