package com.concentrate.gpt.gptflow.dao.selector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class DropDownSelectDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<Map<String, Object>> query(String sql) {
		return jdbcTemplate.queryForList(sql);
	}
}
