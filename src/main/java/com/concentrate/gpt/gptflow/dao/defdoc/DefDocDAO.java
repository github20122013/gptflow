package com.concentrate.gpt.gptflow.dao.defdoc;


import com.concentrate.gpt.gptflow.base.AbstractedBaseDAO;
import com.concentrate.gpt.gptflow.service.tree.Node;
import com.concentrate.gpt.gptflow.service.tree.defdoc.DefDoc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Component
public class DefDocDAO extends AbstractedBaseDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public String getTBName() {
        return "TF_DEFINITION_DOC";
    }

    @Override
    public String getQuerySql() {
        return "SELECT ID,DOC_CODE,DOC_NAME,PARENT_ID,REMARK,CREATE_TIME,UPDATE_TIME,UPDATE_USER FROM  TF_DEFINITION_DOC ORDER BY UPDATE_TIME DESC";
    }

    @Override
    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    @Override
    public String[] getUniqKeys() {
        return "DOC_CODE".split(",");
    }


    public Collection<Node> getAllDefDocs() {
        //SELECT ID,DOC_CODE,DOC_NAME,PARENT_ID,REMARK,CREATE_TIME,UPDATE_TIME,UPDATE_USER
        return jdbcTemplate.query("SELECT A.ID AS DOC_ID,A.DOC_CODE AS ID,A.DOC_CODE,A.DOC_NAME,B.DOC_CODE AS PARENT_ID,A.REMARK,A.CREATE_TIME,A.UPDATE_TIME,A.UPDATE_USER FROM  TF_DEFINITION_DOC A LEFT JOIN TF_DEFINITION_DOC B ON A.PARENT_ID=B.ID ORDER BY A.UPDATE_TIME", new RowMapper<Node>() {

            @Override
            public DefDoc mapRow(ResultSet rs, int rowNum) throws SQLException {
                DefDoc result = new DefDoc();
                result.setDocId(rs.getInt("DOC_ID"));
                result.setId(rs.getString("ID"));
                result.setDocCode(rs.getString("DOC_CODE"));
                result.setDocName(rs.getString("DOC_NAME"));
                result.setParentId(rs.getString("PARENT_ID"));
                result.setRemark(rs.getString("REMARK"));
                result.setCreateTime(rs.getDate("CREATE_TIME"));
                result.setUpdateTime(rs.getDate("UPDATE_TIME"));
                result.setUpdateUser(rs.getString("UPDATE_USER"));
                return result;
            }
        });

    }


    public List<Map<String, Object>> queryDefDocs(String docCode) {
        return jdbcTemplate.queryForList(
                "SELECT A.DEF_KEY,A.DEF_VALUE FROM TF_DEFINITION_DETAIL A JOIN TF_DEFINITION_DOC B ON A.DOC_ID = B.ID WHERE B.DOC_CODE = '"
                        + docCode + "' AND A.STATE = 1 ORDER BY A.ID ");
    }

}
