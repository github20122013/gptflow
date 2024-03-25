package com.concentrate.gpt.gptflow.dao.defdoc;

import com.concentrate.gpt.gptflow.base.AbstractedBaseDAO;
import com.concentrate.gpt.gptflow.service.tree.defdoc.DefDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Component
public class DefDetailDAO extends AbstractedBaseDAO {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public String getTBName(){
    	return "TF_DEFINITION_DETAIL";	
    }
    @Override
    public String getQuerySql(){
    	return "SELECT A.DOC_CODE,A.DOC_NAME,B.ID,B.DOC_ID,B.DEF_KEY,B.DEF_VALUE,B.STATE,B.REMARK,B.CREATE_TIME,B.UPDATE_TIME,B.UPDATE_USER FROM TF_DEFINITION_DOC A JOIN TF_DEFINITION_DETAIL B ON A.ID = B.DOC_ID ORDER BY A.DOC_CODE,B.DEF_KEY,B.UPDATE_TIME DESC";
    }
    @Override
	public JdbcTemplate getJdbcTemplate(){
		return 	jdbcTemplate;
	}
    @Override
    public String[] getUniqKeys(){
    	return "DOC_ID,DEF_KEY,STATE".split(",");
    }

    public List<DefDetail> batchQueryByDocId(Map<String, Object> params) {
        return namedParameterJdbcTemplate.query("SELECT ID,DOC_ID,DEF_KEY,DEF_VALUE,STATE,REMARK FROM tf_definition_detail where STATE = 1 and DOC_ID in (:docIds)", params, new RowMapper<DefDetail>() {
            @Override
            public DefDetail mapRow(ResultSet resultSet, int rowNum) throws SQLException {
                DefDetail result = new DefDetail();
                result.setId(resultSet.getString("ID"));
                result.setDocId(resultSet.getString("DOC_ID"));
                result.setDefKey(resultSet.getString("DEF_KEY"));
                result.setDefValue(resultSet.getString("DEF_VALUE"));
                result.setState(resultSet.getInt("STATE"));
                result.setRemark(resultSet.getString("REMARK"));
                return result;
            }
        });
    }

    public List<Map<String, Object>> queryByDocCode(String docCode) {
        String sql = "SELECT tc.DOC_CODE as DOC_CODE,td.ID as ID ,td.DOC_ID as DOC_ID ,td.DEF_KEY as DEF_KEY ,td.DEF_VALUE as DEF_VALUE,td.REMARK as REMARK FROM tf_definition_detail td inner join tf_definition_doc tc ON td.DOC_ID = tc.ID where td.STATE = 1 and td.DEF_KEY =\"spe_sql\" and tc.DOC_CODE =\""+docCode+"\"";
        return jdbcTemplate.queryForList(sql);
    }

    /**
     * 查询所有docNode以及对应的nodeDetail
     * @return
     */
    public List<Map<String, Object>> queryAllDocDetails() {
        return jdbcTemplate.queryForList(getQuerySql());

    }
}
