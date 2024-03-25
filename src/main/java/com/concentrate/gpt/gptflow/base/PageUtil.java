package com.concentrate.gpt.gptflow.base;

import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public class PageUtil {
	private static final Logger logger = LoggerFactory
			.getLogger(PageUtil.class);

	public static <T> List<T> doPage(JdbcTemplate jdbcTemplate, String sql,
			RowMapper<T> rm, PageCond page) {
		sql = sql.toLowerCase();
		String sqlCount = "SELECT COUNT(*) FROM (" + replaceOrderBy(sql)
				+ ") AS T";
		int ALL = (Integer) jdbcTemplate.queryForObject(sqlCount,Integer.class);
		page.setTotal(ALL);
		page.reCalculate();

		String sqlPageBegin = "select * from (";
		String sqlPageEnd = ")as T limit " + page.getBegin()
				+ "," + page.getLength();
		return jdbcTemplate.query(sqlPageBegin + sql + sqlPageEnd, rm);
	}

	public static <T> List<T> doPageNotToLowerCase(JdbcTemplate jdbcTemplate,
			String sql, RowMapper<T> rm, PageCond page) {
		String sqlCount = "SELECT COUNT(*) FROM (" + replaceOrderBy(sql)
				+ ") AS T";
		int ALL = (Integer) jdbcTemplate.queryForObject(sqlCount,Integer.class);
		page.setTotal(ALL);
		page.reCalculate();

		String sqlPageBegin = "select * from ((";
		String sqlPageEnd = ")as T limit " + page.getBegin()
				+ "," + page.getLength();
		return jdbcTemplate.query(sqlPageBegin + sql + sqlPageEnd, rm);
	}

	public static List<Map<String, Object>> doPage(JdbcTemplate jdbcTemplate,
			String sql, PageCond page, Object... params) {
		sql = sql.toLowerCase();
		String sqlCount = "SELECT COUNT(*) FROM (" + replaceOrderBy(sql)
				+ ") AS T";
		int ALL = (Integer) jdbcTemplate.queryForObject(sqlCount,Integer.class);
		page.setTotal(ALL);
		page.reCalculate();

		String sqlPageBegin = "select * from (";
		String sqlPageEnd = ") as T limit " + page.getBegin()
				+ "," + page.getLength() ;
		return jdbcTemplate.queryForList(sqlPageBegin + sql + sqlPageEnd,
				params);
	}

	public static List<Map<String, Object>> doPageWithParam(
			JdbcTemplate jdbcTemplate, String sql, PageCond page,
			String condition, Object... params) {
		sql = sql.toLowerCase();
		String sqlCount = "SELECT COUNT(*) FROM (" + replaceOrderBy(sql)
				+ ") AS T WHERE 1=1 " + condition;
		int ALL = (Integer) jdbcTemplate.queryForObject(sqlCount,Integer.class);
		page.setTotal(ALL);
		page.reCalculate();

		String sqlPageBegin = "select * from (";
		String sqlPageEnd = ")as DK where 1=1 " + condition
				+ " limit " + page.getBegin() + ","
				+ page.getLength();
		return jdbcTemplate.queryForList(sqlPageBegin + sql + sqlPageEnd,
				params);
	}

	private static String replaceOrderBy(String sql) {

		String result = sql;
		result = result.toLowerCase();
		try {
			String tmp = result;
			while (tmp.indexOf("  ") > -1) {
				tmp = tmp.replace("  ", " ");
			}
            if(tmp.indexOf("order by")>-1){
                tmp = tmp.substring(0, tmp.indexOf("order by"));
            }

			result = tmp;
		} catch (Exception e) {
			logger.error("", e);
		}
		return result;

	}

	public static PageCond getPageFromContext(Map<String, Object> context) {
		PageCond page = new PageCond();
		page.setCurrentPage(context.get("currentPage") == null
				|| "".equals(context.get("currentPage"))
				|| !NumberUtils.isNumber(context.get("currentPage").toString()) ? 1
				: Integer.parseInt(context.get("currentPage").toString()));
		page.setLength(context.get("length") == null
				|| "".equals(context.get("length"))
				|| !NumberUtils.isNumber(context.get("length").toString()) ? 10
				: Integer.parseInt(context.get("length").toString()));
		return page;
	}

	public static void main(String[] args) {
		String sql = "SELECT ORDER FROM TTT ORDER    BY ORDER DESC LIMIT 0,1";
		String replaced = PageUtil.replaceOrderBy(sql);
		System.out.println(replaced);
	}

	public static PageCond getPageFromRequestContext(HttpServletRequest request) {
		PageCond page = new PageCond();
		page.setCurrentPage(request.getParameter("currentPage") == null
				|| "".equals(request.getParameter("currentPage"))
				|| !NumberUtils.isNumber(request.getParameter("currentPage")
						.toString()) ? 1 : Integer.parseInt(request
				.getParameter("currentPage").toString()));
		page.setLength(request.getParameter("length") == null
				|| "".equals(request.getParameter("length"))
				|| !NumberUtils.isNumber(request.getParameter("length")
						.toString()) ? 10 : Integer.parseInt(request
				.getParameter("length").toString()));
		return page;
	}

    public static List<Map<String, Object>> doPageWithNamedParam(JdbcTemplate jdbcTemplate, String sql, PageCond page, String condition, Map<String, Object> map) {
		List<Map<String,Object>> result = null;
		NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate.getDataSource());
		try{
			// 1 小写提高SQL缓存命中率，减少编译次数
			sql = sql.toLowerCase();
			// 2 统计结果数不需要排序
			String sqlCount = "SELECT COUNT(*) FROM (" + replaceOrderBy(sql)
					+ ") AS T WHERE 1=1 " + condition;
			// 3 执行结果数查询
			int ALL = (Integer) template.queryForObject(sqlCount,map,Integer.class);
			// 4 分页信息设置，并返回
			page.setTotal(ALL);
			page.reCalculate();

			String sqlPageBegin = "select * from (";
			String sqlPageEnd = ")as DK where 1=1 " + condition
					+ " limit " + page.getBegin() + ","
					+ page.getLength();
			// 5 执行查询
			result = template.queryForList(sqlPageBegin + sql + sqlPageEnd,
					map);
		}catch(Exception e){
			logger.error("错误",e);
		}
		return result;
    }
}
