package com.concentrate.gpt.gptflow.service.selector;

import com.concentrate.gpt.gptflow.base.PageCond;
import com.concentrate.gpt.gptflow.dao.selector.SelectorDAO;
import com.concentrate.gpt.gptflow.dao.selector.SqlContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SelectorService {

	@Autowired
	private SelectorDAO dao;
	
	public List<Map<String,Object>> query(String module, Map<String,String> param , PageCond page){
        List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
        String sql = SqlContext.getSqlByModule(module);
        if(sql!=null){
            result= dao.query(sql, param, page);
        }
        return result;
	}
	
}
