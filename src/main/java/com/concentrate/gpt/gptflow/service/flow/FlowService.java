package com.concentrate.gpt.gptflow.service.flow;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.concentrate.gpt.gptflow.base.Base;
import com.concentrate.gpt.gptflow.base.PageCond;
import com.concentrate.gpt.gptflow.dao.flow.FlowDAO;

@Service
public class FlowService {

    @Autowired
    private FlowDAO dao;

    public List<Map<String,Object>> query(Map<String,String> param,PageCond page){
        return dao.query(param, page);
    }

    public List<Map<String,Object>> query(Map<String,String> param,PageCond page,Map<String,String> op){
        return dao.query(param, page,op);
    }
    
    @Transactional
    public int save(Map<String,String> param){
        int result = 0;
        if(param!=null){
            String id=param.get("id");
            if(id!=null && !"".equals(id)){
                result = dao.update(param);
            }else{
                result = dao.insert(param);
            }
        }
        return result;
    }
    
    @Transactional
    public int delete(Map<String,String> param){
        int result = dao.delete(param);
        return result;
    }
    
    @Transactional
	public int update(Map<String, String> param) {
		return dao.updateWithoutId(param);
	}
    
    public boolean exists(Map<String,String> param){
        return dao.exists(param);
    }
    
    @Transactional
    public void batchDelete(List<Map<String, String>> params) {
		if(params!=null&& !params.isEmpty()){
			for(Map<String, String> param:params){
				dao.delete(param);
			}
		}
	}
    
    public List<Map<String,Object>> queryAll(Map<String,String> param){
    	return dao.queryAll(param);
    }
    
    public Map<String,Object> expand(Map<String,String> param){
    	return dao.expand(param);
    }
    
   public int batchUpdate(List<Map<String, String>> params) {
		List<Map<String, String>> addList = new ArrayList<Map<String, String>>();
		List<Map<String, String>> updateList = new ArrayList<Map<String, String>>();
		if (params != null && !params.isEmpty()) {
			for (Map<String, String> map : params) {
				boolean flag = dao.exist(map);
				if (flag) {
					updateList.add(map);
				} else {
					addList.add(map);
				}
			}
			for (Map<String, String> map : addList) {
				save(map);
			}
			for (Map<String, String> map : updateList) {
				update(map);
			}
		}
		return 0;
	}

    public void export(HttpServletResponse response,PageCond page,Map<String, String> param ,Map<String, String> opbox ,String[] columns){
        dao.export(response, page, param ,opbox,columns);
    }

    public void export(HttpServletResponse response,PageCond page,Map<String, String> param ,String[] columns){
        export(response, page, param ,null,columns);
    }
    
    public Set<String> getUniqueKey() {
		Set<String> set = new HashSet<String>();
		CollectionUtils.addAll(set, dao.getUniqKeys());
		return set;
	}
	
    public String getPrimaryKeyField(){
        return dao.getPrimaryKeyField();
    }

    public String getUpdateTimeField(){
        return dao.getUpdateTimeField();
    }

    public String getCreateTimeField(){
        return dao.getCreateTimeField();
    }

    public String getCreateUserField(){
        return dao.getCreateUserField();
    }

    public String getUpdateUserField(){
        return dao.getUpdateUserField();
    }
}
