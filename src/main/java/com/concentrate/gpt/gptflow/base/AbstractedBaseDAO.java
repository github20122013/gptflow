package com.concentrate.gpt.gptflow.base;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;

import com.concentrate.gpt.gptflow.define.FeatureEnum;
import com.concentrate.gpt.gptflow.util.CsvReader;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;


public abstract class AbstractedBaseDAO {
	private static final Logger logger = LoggerFactory
			.getLogger(AbstractedBaseDAO.class);

	public abstract String getTBName();

	public String getPrimaryKeyField(){
		return Base.ID;
	}

	public String getUpdateTimeField(){
		return Base.UPDATE_TIME;
	}

	public String getCreateTimeField(){
		return Base.CREATE_TIME;
	}

	public String getCreateUserField(){
		return Base.CREATE_USER;
	}

	public String getUpdateUserField(){
		return Base.UPDATE_USER;
	}

	public abstract String getQuerySql();

	public abstract JdbcTemplate getJdbcTemplate();

	public abstract String[] getUniqKeys();

	public  LinkedHashMap<String,Map<String, String>> getAllFields() {
		return null;
	}


	public LinkedHashMap<String, Map<String, String>> getFixedSelectFields() {
		return null;
	}

	private Map<String, String> coloumnInfo = new HashMap<String, String>();
	private static Map<String, String> typeMap = new HashMap<String, String>();
	private static Map<String, String> orgTypeMap = new HashMap<String, String>();
	private  Map<String, Map<String, String>> tableInfo = new HashMap<String, Map<String, String>>();
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

	protected Map<String, String> getColumnInfoMap() {
		if (coloumnInfo == null || coloumnInfo.size() < 1) {
			initMap();
		}
		return coloumnInfo;
	}

	protected Map<String, String> getOrgTypeMap() {
		if (orgTypeMap == null || orgTypeMap.size() < 1) {
			initMap();
		}
		return orgTypeMap;
	}

	@PostConstruct
	private void initMap() {
		// List<Map<String,Object>> columns =
		// getJdbcTemplate().queryForList("select COLUMN_NAME,DATA_TYPE from sysibm.columns where UPPER(TABLE_NAME) = '"+getTBName()+"'");
		Thread t = new Thread() {
			public void run() {
				try {
					SqlRowSetMetaData srsmd = getJdbcTemplate().queryForRowSet(
							getQuerySql().replaceAll("#queryParams#", "")
									+ " limit 1")
							.getMetaData();
					int columnCount = srsmd.getColumnCount();
					logger.info("Abstraction Table Initial Function : querysql column search job finish ,querysql="+getQuerySql()+" column_count="+columnCount);
					for (int i = 1; i <= columnCount; i++) {
						String name = srsmd.getColumnName(i);
						String fieldType = String.valueOf(srsmd
								.getColumnType(i));
						coloumnInfo.put(name, typeMap.get(fieldType));
						orgTypeMap.put(name, fieldType);
					}
				} catch (Exception e) {
					logger.error("", e);
				}

				try {
					SqlRowSetMetaData srsmd = getJdbcTemplate().queryForRowSet(
									"SELECT * FROM "+getTBName()+""
											+ " limit 1")
							.getMetaData();
					int columnCount = srsmd.getColumnCount();
					logger.info("Abstraction Table Initial Function : table column search job finish ,table="+getTBName()+" column_count="+columnCount);
					for (int i = 1; i <= columnCount; i++) {
						String name = srsmd.getColumnName(i).toUpperCase();
						String fieldType = String.valueOf(srsmd
								.getColumnType(i));
						Map<String, String> col = new HashMap<String, String>();
						col.put("NAME", name);
						col.put("TYPE", fieldType);
						tableInfo.put(name, col);
					}
				} catch (Exception e) {
					logger.error("", e);
				}
			}
		};
		t.setDaemon(true);
		t.start();

	}

	protected String getQueryColumnValue(String key, String value) {
		String type = getOrgTypeMap().get(key);
		if (type != null) {
			if ("CHARACTER VARYING".equals(type) || "CHARACTER".equals(type)) {
				return "'" + value + "%'";
			}
		}
		return getColumnValue(key, value);

	}

	protected String getColumnValue(String key, String value) {
		if ("STRING".equals(getColumnInfoMap().get(key))) {
			return "'" + value + "'";
		} else {
			return value;
		}
	}

	public int insert(Map<String, String> param) {
		int result = 0;
		if (param != null) {
			StringBuffer fields = new StringBuffer();
			StringBuffer values = new StringBuffer();
			List<Object> data = new ArrayList<Object>();
			// System.out.println("插入参数："+mapToString(param));
			int i = 0;
			for (Map.Entry<String, String> e : param.entrySet()) {
				String key = e.getKey().toUpperCase();
				String v = e.getValue();
				if (getPrimaryKeyField().equals(key) || tableInfo.get(key) == null) {
					continue;
				}
				if (i != 0) {
					fields.append(" , ");
					values.append(" , ");
				}
				fields.append(key);
				values.append(" ? ");
				if ("".equals(v)) {
					data.add(null);
				} else {
					data.add(v);
				}
				i++;
			}

			String sql = "INSERT INTO " + getTBName() + "(" + fields
					+ ") VALUES (" + values + ")";
			result = getJdbcTemplate().update(sql, data.toArray());
		}
		return result;
	}

	public List<Map<String, Object>> query(Map<String, String> param,
										   PageCond page) {
		return query(param, page,null);
	}

	public List<Map<String, Object>> query(Map<String, String> param,
										   PageCond page,Map<String,String> ops) {
		String sql = getQuerySql();
		return querySql(sql, param, page,ops);
	}

	public List<Map<String, Object>> querySql(String sql,
											  Map<String, String> param, PageCond page) {
		return querySql(sql,param,page,null);
	}


	public List<Map<String, Object>> querySql(String sql,
											  Map<String, String> param, PageCond page,Map<String,String> ops) {
		if(page==null){
			page= PageCond.ROWS1000();
		}
		List<Object> values = new ArrayList<Object>();
		Map<String,Object> map = new HashMap<String,Object>();
		StringBuffer queryParams = new StringBuffer();

		if (param != null) {
			if(ops==null){
				ops = new LinkedHashMap<String,String>();
			}
			for (Map.Entry<String, String> e : param.entrySet()) {
				String key = e.getKey().toLowerCase();
				String v = e.getValue();
				if ("".equals(v)) {
					continue;
				}
				map.put(key,v);
				// queryParams.append(" AND ").append(key).append(getOp(key)).append(getQueryColumnValue(key,v));
				String op = ops.get(key);//先取小写
				if(op == null){
					op = ops.get(key.toUpperCase());//再取大写
				}
				if(op == null){//取默认值
					op = getOp(key);
				}
				if("IN".equalsIgnoreCase(op.toUpperCase())){
					String vrepalce = v.replaceAll("\\\r\\\n",",").replaceAll("\\r\\n",",").replaceAll("\r\n",",");
					map.put(key,Arrays.asList(vrepalce.split(",")));
					queryParams.append(" AND BINARY ").append(key).append(" ").append(op)
							.append(" (:"+key+")");
				}else if("LIKE".equals(op.toUpperCase())||"NOT LIKE".equals(op.toUpperCase())){
					map.put(key,"%"+v+"%");
					queryParams.append(" AND BINARY ").append(key).append(" ").append(op)
							.append(" :"+key+"");
				}else if(">=".equals(op) || "<=".equals(op)  || ">".equals(op)  || "<".equals(op) ){
					if(key.indexOf("@")>0){
						String[] k= key.split("@",-1);
						if(k!=null&&k.length>0){
							String keyField = k[0];
							if(keyField!=null){
								queryParams.append(" AND BINARY ").append(keyField).append(" ").append(op)
										.append(" :"+key+"");
							}
						}
					}
				}else{
					queryParams.append(" AND BINARY ").append(key).append(" ").append(op)
							.append(" :"+key);
				}

			}
		}
		return PageUtil.doPageWithNamedParam(getJdbcTemplate(), sql, page,
				queryParams.toString(), map);
	}

	public String getOp(String key) {
		key = key.substring(key.lastIndexOf(".") + 1);
		String result = "=";
		String type = getOrgTypeMap().get(key);
		if (type != null) {
			if (String.valueOf(Types.VARCHAR).equals(type)
					|| String.valueOf(Types.CHAR).equals(type)) {
				result = " LIKE ";
			}
			if ("START_TIME".equals(key)) {
				result = ">=";
			}
			if ("END_TIME".equals(key)) {
				result = "<=";
			}
		}
		return result;
	}

	public int update(Map<String, String> param) {
		if (param != null && param.get(getPrimaryKeyField()) != null) {
			StringBuffer setPairs = new StringBuffer();
			int i = 0;
			List<Object> values = new ArrayList<Object>();
			for (Map.Entry<String, String> e : param.entrySet()) {
				String key = e.getKey();
				String v = e.getValue();
				if (getPrimaryKeyField().equals(key) || Base.CREATE_TIME.equals(key)) {
					continue;
				}
				if (tableInfo.get(key) != null) {
					if (i != 0) {
						setPairs.append(" , ");
					}
					// setPairs.append(key).append("=").append(getColumnValue(key,v));
					setPairs.append(key).append("=").append("?");
					values.add(v);
					i++;
				}
			}
			String sql = "update " + getTBName() + " set " + setPairs
					+ " where id= ? ";
			values.add(param.get(getPrimaryKeyField()));
			return getJdbcTemplate().update(sql, values.toArray());
		}
		return 0;
	}

	// 没有ID的更新，用于批量上传
	public int updateWithoutId(Map<String, String> param) {
		StringBuffer setPairs = new StringBuffer();
		StringBuffer wherePairs = new StringBuffer();
		Set<String> set = new HashSet<String>();
		CollectionUtils.addAll(set, getUniqKeys());
		List<Object> setValues = new ArrayList<Object>();
		List<Object> whereValues = new ArrayList<Object>();
		for (Map.Entry<String, String> e : param.entrySet()) {
			String key = e.getKey();
			String v = e.getValue();
			if (Base.CREATE_TIME.equals(key)) {
				continue;
			}
			if (tableInfo.get(key) != null) {// 表里有这个字段
				if (set.contains(key)) {// 唯一字段包含当前字段
					if (StringUtils.isEmpty(v)) {
						wherePairs.append(key).append(" is null and ");
					} else {
						wherePairs.append(key).append("=").append("? and ");
						whereValues.add(v);
					}
				} else {
					setPairs.append(key).append("=").append("?,");
					setValues.add(v);
				}
			}
		}
		String w = wherePairs.substring(0, wherePairs.lastIndexOf("and"));
		String s = setPairs.substring(0, setPairs.lastIndexOf(","));
		String sql = "update " + getTBName() + " set " + s + " where " + w;
		setValues.addAll(whereValues);
		return getJdbcTemplate().update(sql, setValues.toArray());
	}

	public int updateFieldsWithParam(Map<String, String> setParam,Map<String, String> whereParam,boolean omitNullVal) {
		StringBuffer setPairs = new StringBuffer();
		StringBuffer wherePairs = new StringBuffer();
		Set<String> set = new HashSet<String>();
		CollectionUtils.addAll(set, getUniqKeys());
		List<Object> setValues = new ArrayList<Object>();
		List<Object> whereValues = new ArrayList<Object>();
		for (Map.Entry<String, String> e : setParam.entrySet()) {
			String key = e.getKey();
			if(StringUtils.isEmpty(key)){
				continue;
			}
			key=key.toUpperCase();
			String v = e.getValue();
			if(omitNullVal){
				if(StringUtils.isEmpty(v)){
					continue;
				}
			}
			if (Base.CREATE_TIME.equals(key)) {
				continue;
			}
			if (tableInfo.get(key) != null) {// 表里有这个字段
				setPairs.append(key).append("=").append("?,");
				setValues.add(v);
			}
		}

		for (Map.Entry<String, String> e : whereParam.entrySet()) {
			String key = e.getKey();
			if(StringUtils.isEmpty(key)){
				continue;
			}
			key=key.toUpperCase();
			String v = e.getValue();
			if(omitNullVal){
				if(StringUtils.isEmpty(v)){
					continue;
				}
			}
			if (tableInfo.get(key) != null) {// 表里有这个字段
				if (StringUtils.isEmpty(v)) {
					wherePairs.append(key).append(" is null and ");
				} else {
					wherePairs.append(key).append("=").append("? and ");
					whereValues.add(v);
				}
			}
		}
		String w = "where ";
		if(wherePairs.length()==0){
			throw new RuntimeException("过滤条件where不能为空");
		}
		if(wherePairs.indexOf("and")!=-1){
			w = w + wherePairs.substring(0, wherePairs.lastIndexOf("and"));
		}
		String s = setPairs.substring(0, setPairs.lastIndexOf(","));
		String sql = "update " + getTBName() + " set " + s  + w;
		setValues.addAll(whereValues);
		return getJdbcTemplate().update(sql, setValues.toArray());
	}

	public int updateAllFields(Map<String, String> param) {
		if (param != null && param.get(getPrimaryKeyField()) != null) {
			StringBuffer setPairs = new StringBuffer();
			int i = 0;
			List<Object> values = new ArrayList<Object>();
			for (Map.Entry<String, Map<String, String>> e : tableInfo
					.entrySet()) {
				String key = e.getKey();
				if (getPrimaryKeyField().equals(key) || Base.CREATE_TIME.equals(key)) {
					continue;
				}
				if (i != 0) {
					setPairs.append(" , ");
				}
				String v = param.get(key);
				// setPairs.append(key).append("=").append(getColumnValue(key,v));
				setPairs.append(key).append("=").append("?");
				values.add(v);
				i++;

			}
			String sql = "update " + getTBName() + " set " + setPairs
					+ " where id= ? ";
			values.add(param.get(getPrimaryKeyField()));
			return getJdbcTemplate().update(sql, values.toArray());
		}
		return 0;
	}

	public int delete(Map<String, String> param) {
		if (param != null && param.get(getPrimaryKeyField()) != null) {
			String sql = "DELETE FROM " + getTBName() + " WHERE ID= "
					+ param.get(getPrimaryKeyField());
			return getJdbcTemplate().update(sql);
		}
		return 0;
	}

	// 检测存在时，主键都中有数值类型，不能为空，有空则无法判断唯一性，直接返回false
	public boolean exists(Map<String, String> param) {
		String sql = "SELECT 1 FROM " + getTBName()
				+ " WHERE 1=1 #queryParams#";
		StringBuffer queryParams = new StringBuffer();
		List<Object> values = new ArrayList<Object>();
		if (param != null) {
			String[] uniqKeys = getUniqKeys();
			if (uniqKeys != null && uniqKeys.length > 0) {

				for (String k : uniqKeys) {
					if (param.get(k) == null || "".equals(param.get(k))) {// 唯一键没值
																			// 不做存在判断
						return false;
					}
					queryParams.append(" AND ");
					// queryParams.append(k).append("=").append(getColumnValue(k,param.get(k)));
					queryParams.append(k).append("=?");
					values.add(param.get(k));
				}
				if (param.get(getPrimaryKeyField()) != null
						&& !"".equals(param.get(getPrimaryKeyField()))) {// 修改时的重复判断，不能与自己比
					// queryParams.append(" AND ").append(Base.ID).append(" != ").append(getColumnValue(Base.ID,param.get(Base.ID)));
					queryParams.append(" AND ").append(getPrimaryKeyField()).append(" != ?");
					values.add(param.get(getPrimaryKeyField()));
				}
			} else {
				return false;
			}
		}
		sql = sql.replaceAll("#queryParams#", queryParams.toString());

		return getJdbcTemplate().queryForList(sql, values.toArray()).size() > 0;
	}

	// 检测存在时，主键都中有数值类型，不能为空，有空则无法判断唯一性，直接返回false
	public boolean existsByParam(Map<String, String> param) {
		String sql = "SELECT 1 FROM " + getTBName()
				+ " WHERE 1=1 #queryParams# limit 1";
		StringBuffer queryParams = new StringBuffer();
		List<Object> values = new ArrayList<Object>();
		if (param != null) {
			for (Map.Entry<String, String> entry : param.entrySet()) {
				String k = entry.getKey();
				String v = entry.getValue();
				queryParams.append(" AND ");
				// queryParams.append(k).append("=").append(getColumnValue(k,param.get(k)));
				queryParams.append(k).append("=?");
				values.add(param.get(k));
			}
		}else {
			return false;
		}
		sql = sql.replaceAll("#queryParams#", queryParams.toString());

		return getJdbcTemplate().queryForList(sql, values.toArray()).size() > 0;
	}

	// 检测存在时，主键都为字符串类型，可以为空(数据库对应null)
	public boolean exist(Map<String, String> param) {
		String sql = "SELECT 1 FROM " + getTBName()
				+ " WHERE 1=1 #queryParams#";
		StringBuffer queryParams = new StringBuffer();
		List<Object> values = new ArrayList<Object>();
		if (param != null) {
			String[] uniqKeys = getUniqKeys();
			if (uniqKeys != null && uniqKeys.length > 0) {
				for (String k : uniqKeys) {
					if (param.get(k) == null || "".equals(param.get(k).trim())) {
						queryParams.append(" AND ");
						queryParams.append(k).append(" is null");
					} else {
						queryParams.append(" AND ");
						queryParams.append(k).append("=?");
						values.add(param.get(k));
					}
				}
				if (param.get(getPrimaryKeyField()) != null
						&& !"".equals(param.get(getPrimaryKeyField()))) {// 修改时的重复判断，不能与自己比
					// queryParams.append(" AND ").append(Base.ID).append(" != ").append(getColumnValue(Base.ID,param.get(Base.ID)));
					queryParams.append(" AND ").append(getPrimaryKeyField()).append(" != ?");
					values.add(param.get(getPrimaryKeyField()));
				}
			} else {
				return false;
			}
		}
		sql = sql.replaceAll("#queryParams#", queryParams.toString());

		return getJdbcTemplate().queryForList(sql, values.toArray()).size() > 0;
	}

	public List<Map<String, Object>> queryAll(Map<String, String> param) {
		String sql = getQuerySql();
		List<Object> values = null;
		if (param != null) {
			values = new ArrayList<Object>();
			StringBuffer queryParams = new StringBuffer();
			for (Map.Entry<String, String> e : param.entrySet()) {
				String key = e.getKey();
				String v = e.getValue();
				if ("".equals(v)) {
					continue;
				}
				String op = "=";
				if ("START_TIME".equals(key)) {
					op = ">=";
				}
				if ("END_TIME".equals(key)) {
					op = "<=";
				}

				// queryParams.append(" AND ").append(key).append(op).append(getColumnValue(key,v));
				queryParams.append(" AND ").append(key).append(op)
						.append(" ? ");
				values.add(v);
			}
			sql = sql.replaceAll("#queryParams#", queryParams.toString());

		}
		return getJdbcTemplate().queryForList(sql,
				values == null ? null : values.toArray());
	}

	public Map<String, Object> expand(Map<String, String> param) {
		Map<String, Object> result = null;
		if (param.get("ID") != null) {
			result = getJdbcTemplate().queryForMap(
					"SELECT * FROM ("
							+ getQuerySql().replaceAll("#queryParams#", "")
							+ ") AS _T WHERE ID=" + param.get("ID"));
		}
		return result;
	}

	public List<Map<String, Object>> executeQuery(String sql) {
		return getJdbcTemplate().queryForList(sql);
	}

	public void export(HttpServletResponse response, PageCond page,
					   Map<String, String> param,String[] columns) {
		export( response,  page,
				param,new HashMap<String,String>(),columns);
	}

	public void export(HttpServletResponse response, PageCond page,
			Map<String, String> param,Map<String,String>opbox,  String[] columns) {
		response.setContentType("application/octet-stream;charset=GB18030");
		Writer writer = null;
		try {
			String seperator = param.get("__seperator");
			if (seperator == null) {
				seperator = ",";
			}
			param.remove("__seperator");
			writer = response.getWriter();
			BufferedWriter bufferedWriter = new BufferedWriter(writer);
			List<Map<String, Object>> list = querySql(getExportSql(), param,
					page,opbox);
			bufferedWriter.write(createHeader(columns,seperator));
			bufferedWriter.write("\n");
			Map<String,Map<String,String>> translator = getFixedSelectFields();
			for (Map<String, Object> item : list) {
				for (String col : columns) {
					bufferedWriter.write(translate(item,col,translator) + seperator);
				}
				bufferedWriter.write("\n");
				bufferedWriter.flush();
			}
			bufferedWriter.close();
			writer.close();
		} catch (Exception e) {
			logger.error(e.toString(), e);
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					logger.error("", e);
				}
			}
		}
	}


	public void exportES(HttpServletResponse response, PageCond page,
					   Map<String, String> param,Map<String,String>opbox,  String[] columns,List<Map<String, Object>> objs) {
		response.setContentType("application/octet-stream;charset=GB18030");
		Writer writer = null;
		try {
			String seperator = param.get("__seperator");
			if (seperator == null) {
				seperator = ",";
			}
			param.remove("__seperator");
			writer = response.getWriter();
			BufferedWriter bufferedWriter = new BufferedWriter(writer);
//			List<Map<String, Object>> list = querySql(getExportSql(), param,page,opbox);

			List<Map<String, Object>> list = objs;

			bufferedWriter.write(createHeader(columns,seperator));
			bufferedWriter.write("\n");
			Map<String,Map<String,String>> translator = getFixedSelectFields();
			for (Map<String, Object> item : list) {
				for (String col : columns) {
					bufferedWriter.write(translate(item,col,translator) + seperator);
				}
				bufferedWriter.write("\n");
				bufferedWriter.flush();
			}
			bufferedWriter.close();
			writer.close();
		} catch (Exception e) {
			logger.error(e.toString(), e);
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					logger.error("", e);
				}
			}
		}
	}

	/**
	 * 字典翻译
	 * @param item
	 * @param col
	 * @param translator
	 * @return
	 */
	public String translate(Map<String, Object> item, String col, Map<String, Map<String, String>> translator){
		String result = "";
		Object obj = item.get(col);

		if(obj!=null){
			result = obj.toString();
			if(translator!=null){//处理字典映射
				Map<String, String> thisTranslater = translator.get(col);
				if(thisTranslater!=null && thisTranslater.get(result.toString())!=null){
					result = thisTranslater.get(result.toString());
				}
			}

			// 处理字段值，使其符合CSV格式
			result = CsvReader.processCsvValue(result);//处理字符格式
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				Date date = dateFormat.parse(result);
				String resultNew = dateFormat.format(date);
				result = resultNew;//处理日期格式
			} catch (ParseException e) {
			}

		}
		return result;
	}

	/**
	 * 创建CSV表头
	 * @param columns
	 * @param seperator
	 * @return
	 */
	private String createHeader(String[] columns, String seperator) {
		StringBuilder head = new StringBuilder();
		if(columns!=null && columns.length>0){
			String colTmp;
			Map<String, String> colObj;
			LinkedHashMap<String,Map<String, String>>  allFields = getAllFields();
			for(String col:columns){
				colTmp = col;
				if(allFields!=null){
					colObj = allFields.get(col);
					if(allFields!=null&&colObj!=null&&colObj.get("_CN_")!=null){
						colTmp = colObj.get("_CN_");
					}
				}
				head.append(colTmp).append(",");
			}
		}
		return head.toString();
	}

	public String getExportSql() {
		return getQuerySql();
	}


	/**
	 *
	 * @param key
	 * @param cn
	 * @param features
	 * UNIQ 唯一
	 * NOT_NULL 不允许为空
	 * TIME 日期类型，前台会有选择框
	 * @return
	 */
	public void appendFields(LinkedHashMap<String, Map<String, String>> fields, String key, String cn,
							 FeatureEnum... features) {
		Map<String, String> result = newFieldEnum(key,cn,features);
		fields.put(key,result);
	}

	/**
	 *
	 * @param key
	 * @param cn
	 * @param features
	 * UNIQ 唯一
	 * NOT_NULL 不允许为空
	 * TIME 日期类型，前台会有选择框
	 * @return
	 */
	public Map<String, String> newField(String key, String cn,
										String... features) {
		Map<String, String> result = new HashMap<String, String>();
		result.put("_KEY_", key);
		result.put("_CN_", cn);
		if (features != null) {
			for (String f : features) {
				result.put("_" + f + "_", "1");
			}
		}
		return result;
	}


	/**
	 *
	 * @param key
	 * @param cn
	 * @param features
	 * UNIQ 唯一
	 * NOT_NULL 不允许为空
	 * TIME 日期类型，前台会有选择框
	 * @return
	 */
	public Map<String, String> newFieldEnum(String key, String cn,
											FeatureEnum... features) {
		Map<String, String> result = new HashMap<String, String>();
		result.put("_KEY_", key);
		result.put("_CN_", cn);
		if (features != null) {
			for (FeatureEnum f : features) {
				result.put("_" + f + "_", "1");
			}
		}
		return result;
	}

	public LinkedHashMap<String, Map<String, String>> getDefaultFields() {
		LinkedHashMap<String, Map<String, String>> results = new LinkedHashMap<String, Map<String, String>>();
		results.put("ID", newField("ID", "ID"));
		results.put("CREATE_USER",
				newField("CREATE_USER", "创建人"));
		results.put("UPDATE_USER",
				newField("UPDATE_USER", "更新人"));
		results.put("CREATE_TIME",
				newField("CREATE_TIME", "创建时间"));
		results.put("UPDATE_TIME",
				newField("UPDATE_TIME", "更新时间"));
		return results;
	}


}
