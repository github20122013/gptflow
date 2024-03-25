package com.concentrate.gpt.gptflow.dao.selector;

import com.concentrate.gpt.gptflow.base.PageCond;
import com.concentrate.gpt.gptflow.base.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;
import org.springframework.stereotype.Component;

import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SelectorDAO {


	@Autowired
	private JdbcTemplate jdbcTemplate;
	private static Map<String, String> typeMap = new HashMap<String, String>();

	static {
		typeMap.put(String.valueOf(Types.VARCHAR), "STRING");
		typeMap.put(String.valueOf(Types.LONGVARCHAR), "STRING");
		typeMap.put(String.valueOf(Types.CHAR), "STRING");
		typeMap.put(String.valueOf(Types.TIMESTAMP), "STRING");
		typeMap.put(String.valueOf(Types.INTEGER), "NUMBER");
		typeMap.put(String.valueOf(Types.DOUBLE), "NUMBER");
        typeMap.put(String.valueOf(Types.DECIMAL), "NUMBER");
        typeMap.put(String.valueOf(Types.BIGINT), "NUMBER");
    }

	public List<Map<String, Object>> query(String sql,
			Map<String, String> param, PageCond page) {
		return querySql(sql, param, page);
	}

	public List<Map<String, Object>> querySql(String sql,
			Map<String, String> param, PageCond page) {
		sql = sql.replaceAll("\r\n"," ");
		sql = sql.replaceAll("\\r\\n"," ");
		sql = sql.replaceAll("\\\\r\\\\n"," ");
		String tmpSql = sql.replaceAll("#queryParams#","");
		Map<String, String> map = new HashMap<String, String>();
		SqlRowSetMetaData srsmd = jdbcTemplate.queryForRowSet(
				tmpSql + " LIMIT 1").getMetaData();
		int columnCount = srsmd.getColumnCount();
		for (int i = 1; i <= columnCount; i++) {
			String name = srsmd.getColumnName(i);// 字段
			String fieldType = String.valueOf(srsmd.getColumnType(i));// 字段类型
			map.put(name, fieldType);
			map.put(name.toLowerCase(), fieldType);
		}
		List<Object> values = new ArrayList<Object>();
		StringBuffer queryParams = new StringBuffer();
		if (param != null) {
			for (Map.Entry<String, String> e : param.entrySet()) {
				String key = e.getKey();
				String v = e.getValue();
				if ("".equals(v)) {
					continue;
				}
				String op = "";
				if ("STRING".equals(typeMap.get(map.get(key)))) {
					op = " LIKE ";
				} else {
					op = " = ";
				}
				queryParams.append(" AND ").append(key).append(op)
						.append(" ? ");
				if (" LIKE ".equals(op)) {
					v = "%" + v + "%";
				}
				values.add(v);
			}
		}
		//sql = sql.replaceAll("#queryParams#", queryParams.toString());
		return PageUtil.doPageWithParam(jdbcTemplate, sql, page,
				queryParams.toString(), values.toArray());
	}

}
