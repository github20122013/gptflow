package com.concentrate.gpt.gptflow.service.selector;

import com.concentrate.gpt.gptflow.dao.selector.DropDownSelectDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DropDownSelectService {
	@Autowired
	private DropDownSelectDao dropDownSelectDao;

	public DropDownSelectDao getRopDownSelectDao() {
		return dropDownSelectDao;
	}

	public void setRopDownSelectDao(DropDownSelectDao dropDownSelectDao) {
		this.dropDownSelectDao = dropDownSelectDao;
	}

	public Map<String, String> query(String sql, String k, String v) {
		Map<String, String> map = new HashMap<String, String>();
		List<Map<String, Object>> list = dropDownSelectDao.query(sql);
		for (Map<String, Object> m : list) {
			String key = String.valueOf(m.get(k));
			String value = String.valueOf(m.get(v));
			map.put(key, value);
		}
		return map;
	}
}
