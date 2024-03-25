package com.concentrate.gpt.gptflow.controller.tree;

import com.concentrate.gpt.gptflow.service.defdoc.DefDocTreeService;
import com.concentrate.gpt.gptflow.service.tree.Node;
import com.concentrate.gpt.gptflow.util.HttpUtil;
import com.concentrate.gpt.gptflow.util.JSONUtil;
import com.concentrate.gpt.gptflow.util.ResponseBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;

@Controller
public class DefDocTreeController {

    private static final Logger logger = LoggerFactory.getLogger(DefDocTreeController.class);



    @Autowired
    DefDocTreeService treeService;

    /**
     * 生成目录树数据，tree.ftl页面调用
     * 
     * @param request
     * @param response
     * @param map
     * @return
     */
    @RequestMapping("/defdoc/tree")
    public String tree(HttpServletRequest request,
            HttpServletResponse response, ModelMap map) {
        treeService.initial();
        Collection<Node> roots = treeService.getRoots();
        String rootStr = JSONUtil.toJSONString(roots, "parent");
        map.put("rootStr", rootStr);
        map.put("checkedNode", "");
        return "/defdoc/tree";
    }

    @RequestMapping("/defdoc/initialTree")
    public String initialTree(HttpServletRequest request,
            HttpServletResponse response, ModelMap map) {
        String checkedNodeId = request.getParameter("checkNodeId");
        String checkedNode = JSONUtil.toJSONString(treeService.getNodeByNodeId(checkedNodeId), "parent");
        treeService.initial();
        Collection<Node> roots = treeService.getRoots();
        String rootStr = JSONUtil.toJSONString(roots, "parent");
        map.put("rootStr", rootStr);
        map.put("checkedNode", checkedNode);
        return "/defdoc/tree";
    }

    @RequestMapping("/defdoc/toSaveDefDoc")
    public String toSaveDefDoc(HttpServletRequest request,
            HttpServletResponse response, ModelMap map) {
        String id = request.getParameter("ID");
        String parentId = request.getParameter("PARENT_ID");
        map.put("ID", id);
        map.put("PARENT_ID", parentId);
        return "/defdoc/saveDefDoc";
    }

    @RequestMapping("/defdoc/frame")
    public String frame(HttpServletRequest request,
            HttpServletResponse response, ModelMap map) {
        return "/defdoc/frame";
    }


    @RequestMapping("/defdoc/initTree")
    public void initTree(HttpServletRequest request,
            HttpServletResponse response, ModelMap map) {
        try{
            treeService.initial();
        }catch(Exception e){
            logger.error("初始化字典树异常！",e);
        }
        Collection<Node> roots1 = treeService.getRoots();
        map.put("roots:",roots1);
        ResponseBuilder.resultObjectJson(response, map, request);
    }

    @RequestMapping("/defdoc/initNewTree")
    public void initNewTree(HttpServletRequest request,
                            HttpServletResponse response, ModelMap map) {
        //获得SCM配置的IP 更新对应IP的内存
        String ipStr = "10.101.141.120:8080,10.101.141.111:8080";
        try {
            String[] ipArr = ipStr.split(",");
            for(int i = 0;i < ipArr.length; i++){
                String addr ="http://"+ipArr[i]+"/defdoc/initTree.do";
                String result = HttpUtil.doGet(addr);
                map.put("addr"+i,addr);
                map.put("result"+i,result);
            }
        } catch (Exception e) {
            logger.error("请求 rsf 异常", e);
        }
        logger.info("initNewTree初始化字典树map:{}",map);
        ResponseBuilder.resultObjectJson(response, map, request);
    }
    
}
