package com.concentrate.gpt.gptflow.service.defdoc;

import com.concentrate.gpt.gptflow.dao.defdoc.DefDetailDAO;
import com.concentrate.gpt.gptflow.dao.defdoc.DefDocDAO;
import com.concentrate.gpt.gptflow.service.tree.AbstractStringTreeService;
import com.concentrate.gpt.gptflow.service.tree.Node;
import com.concentrate.gpt.gptflow.service.tree.defdoc.DefDoc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * 字典树服务类
 * 1 根据编码查找字典明细
 * 2 根据字典明细查找字典
 *
 */
@Service
public class DefDocTreeService extends AbstractStringTreeService {

    private static Logger logger = LoggerFactory.getLogger(DefDocTreeService.class);

    @Autowired
    DefDetailDAO detailDao ;

    @Autowired
    DefDocDAO docDao;

    @PostConstruct
    public void initial(){
        // 构造树形结构
        super.initial();

        // 构造业务字典内容
        initDefDoc();
    }

    @Override
    public Collection<Node> getAllTerminalNodes() {
        return docDao.getAllDefDocs();
    }

    private void initDefDoc(){
        Map<String, DefDoc> nodeMap = getNodeMap();
        Map<String,String> where = new HashMap<String,String>();
        List<Map<String, Object>> details = detailDao.queryAll(where);
        if(nodeMap!=null && nodeMap.size()>0 && details!=null && details.size()>0){
            //1 构建查找表 KEY 是 {DOC_CODE}  VALUE 是 {{DEF_KEY}:{DEF_VALUE}}
            Map<String,Map<String,Object>> detailTable = new HashMap<String,Map<String,Object>>();
            for(Map<String,Object> detail:details){
                Map<String,Object> hisDetail = null;
                if(detail!=null){
                    String docCode = (String) detail.get("DOC_CODE");
                    if(docCode!=null){
                        hisDetail = detailTable.get(docCode);
                        if(hisDetail == null){
                            hisDetail = new HashMap<String,Object>();
                            detailTable.put(docCode,hisDetail);
                        }
                        hisDetail.put((String) detail.get("DEF_KEY"),detail.get("DEF_VALUE"));
                    }
                }
            }
            //2 追加detail
            for(Map.Entry<String,DefDoc> e:nodeMap.entrySet()){
                String k = e.getKey();
                DefDoc doc = e.getValue();
                if(doc!=null){
                    String docCode = doc.getDocCode();
                    doc.setDetail(detailTable.get(docCode));
                }
            }
        }
    }


}
