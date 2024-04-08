package com.concentrate.gpt.gptflow.dao.flow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;

import com.concentrate.gpt.gptflow.base.AbstractedBaseDAO;

@Repository
public class FlowDAO extends AbstractedBaseDAO{
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    public String getTBName(){
    	return "gptflow";	
    }
    
    public String getQuerySql(){
    	return "select * from gptflow";
    }
	
	public JdbcTemplate getJdbcTemplate(){
		return 	jdbcTemplate;
	}
    
    public String[] getUniqKeys(){
    	return "id,parentId".split(",");
    }

    public String getPrimaryKeyField(){ return "id";}

    public String getCreateUserField(){ return "createBy";}

    public String getCreateTimeField(){ return "createAt";}

    public String getUpdateUserField(){ return "updateBy";}

    public String getUpdateTimeField(){ return "updateAt";}

}
