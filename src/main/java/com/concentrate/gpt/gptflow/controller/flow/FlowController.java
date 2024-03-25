package com.concentrate.gpt.gptflow.controller.flow;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import com.concentrate.gpt.gptflow.base.Base;
import com.concentrate.gpt.gptflow.base.PageCond;
import com.concentrate.gpt.gptflow.util.JSONUtil;
import com.concentrate.gpt.gptflow.util.CsvReader;
import com.concentrate.gpt.gptflow.util.ResponseBuilder;
import com.concentrate.gpt.gptflow.vo.ResponseResult;
import com.concentrate.gpt.gptflow.vo.UploadResult;
import com.concentrate.gpt.gptflow.service.flow.FlowService;
import com.concentrate.gpt.gptflow.service.selector.DropDownSelectService;


@Controller
public class FlowController {

	public static final String MAIN_PAGE = "/flow/manageFlow";
	
	private static Logger logger = LoggerFactory
			.getLogger(FlowController.class);

	@Autowired
	private FlowService service;
	@Autowired
	private DropDownSelectService dropDownSelectService;


	@RequestMapping("/flow/manageFlow")
	public String flowManage(
			@RequestParam(required = false) Map<String, Object> context,
			ModelMap map) {
		PageCond page = getPageFromContext(context);
        Map<String, String> param = getParamFromContext(context, "searchbox.");
        Map<String, String> defaultbox = getParamFromContext(context, "defaultbox.");
        Map<String,String> opbox = getParamFromContext(context, "opbox.");
        if(defaultbox!=null){
            param.putAll(defaultbox);
        }
        map.put("objs", service.query(param, page,opbox));
        map.put("page", page);
        map.put("searchbox", param);
        map.put("defaultbox", defaultbox);
        map.put("opbox", opbox);
        
        return MAIN_PAGE;
	}

	@RequestMapping("/flow/queryFlow")
	public String queryFlow(
			@RequestParam(required = false) Map<String, Object> context,
			ModelMap map) {
		PageCond page =  getPageFromContext(context);
        Map<String,String> searchbox = getParamFromContext(context,"searchbox.");
        Map<String,String> defaultbox = getParamFromContext(context,"defaultbox.");
        Map<String,String> opbox = getParamFromContext(context,"opbox.");
        if(defaultbox!=null){
            searchbox.putAll(defaultbox);
        }
        map.put("objs", service.query(searchbox, page,opbox));
        map.put("page", page);
        map.put("searchbox", searchbox);
        map.put("defaultbox", defaultbox);
        map.put("opbox", opbox);
        
        return MAIN_PAGE;
	}

	@RequestMapping(value="/flow/queryFlowJson", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public void queryFlowJson(HttpServletRequest request, HttpServletResponse response,
			@RequestBody  Map<String, Object> context) {
			Map<String,Object> map = new LinkedHashMap<String,Object>();
            Map<String,String> searchbox = (Map<String, String>) context.get("searchbox");
            Map<String,String> defaultbox =(Map<String, String>) context.get("defaultbox");
            Map<String,String> opbox =(Map<String, String>) context.get("opbox");
            PageCond page =  getPageFromAjaxContext(context);

            if(defaultbox!=null){
                searchbox.putAll(defaultbox);
            }
            String status = ResponseResult.SUCCESS;
            String info = "";
            try{
                map.put("objs", service.query(searchbox, page ,opbox));
                map.put("page", page);
                
            }catch(Exception e){
                logger.error("查询失败!",e);
                status = ResponseResult.FAILED;
                info=e.getMessage();
            }
            writeJsonObject(response, new ResponseResult(status,map,info));
	}

	private PageCond getPageFromAjaxContext(Map<String, Object> context){
        PageCond p = new PageCond();
        Object po = context.get("page");
        if(po!=null){
            String jstr = JSON.toJSONString(po);
            if(jstr!=null){
                p = JSON.parseObject(jstr,PageCond.class);
            }
        }
        return p;
    }

	private PageCond getPageFromContext(Map<String, Object> context) {
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

	private Map<String, String> getParamFromContext(
			Map<String, Object> context, String keyprefix) {
		Map<String, String> result = new HashMap<String, String>();
		if (context != null && !context.isEmpty()) {
			for (Entry<String, Object> e : context.entrySet()) {
				String k = e.getKey();
				String v  = (String) e.getValue();
				if (k.startsWith(keyprefix)&&v!=null && !"".equals(v.trim())) {
					int index = keyprefix.length();
					String realKey = k.substring(index);
					result.put(realKey, (String) v.trim());
				}
			}
		}
		return result;
	}

	@RequestMapping(value="/flow/saveFlow", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public void save(HttpServletRequest request, HttpServletResponse response,
    			@RequestBody  Map<String, Object> context,
    			ModelMap map) {
        Map<String, String> param = (Map<String, String>) context.get("savebox");
        Map<String, String> defaultbox = (Map<String, String>) context.get("defaultbox");
        if(defaultbox!=null){
            param.putAll(defaultbox);
        }
		String user = request.getSession().getAttribute("userId").toString();
		param.put(service.getUpdateUserField(), user);
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String now = df.format(new Date());
		param.put(service.getUpdateTimeField(), now);
		if(param.get("id") == null ||"".equals(param.get("id"))){
			param.put(service.getCreateUserField(), now);
			param.put(service.getCreateUserField(), user);
		}
		String status = ResponseResult.SUCCESS;
		Object result = null;
		String info = "";
		try {
			if (service.exists(param)) {
				status = ResponseResult.EXIESTS;
			} else {
				result = service.save(param);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			status = ResponseResult.FAILED;
			info = e.getMessage();
		}
		writeJsonObject(response, new ResponseResult(status,result,info));
	}

	@RequestMapping(value="/flow/deleteFlow", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public void delete(HttpServletRequest request,
			HttpServletResponse response,
			@RequestBody  Map<String, Object> context,
			ModelMap map) {

        Map<String, String> param = getParamFromContext(context, "");

        String status = ResponseResult.SUCCESS;
        Object result = null;
        String info = "";
        try {
            result = service.delete(param);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            status = ResponseResult.FAILED;
            info = e.getMessage();
        }
        writeJsonObject(response, new ResponseResult(status,result,info));
	}

	
	@RequestMapping("/flow/expandFlow")
	public void expandFlow(HttpServletRequest request,
			HttpServletResponse response,
			@RequestBody  Map<String, Object> context,
			ModelMap map) {
		Map<String, String> param = getParamFromContext(context, "");
		Map<String, Object> obj = service.expand(param);
		writeJsonObject(response, obj);
		
	}

	@RequestMapping(value="/flow/batchDeleteFlow", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public void  batchDelete(@RequestBody Map<String,Object> context,ModelMap map,HttpServletResponse response){
        String status=ResponseResult.SUCCESS;
        String info = "";
        List<Map<String,String>> params = new ArrayList<Map<String,String>>();
        try{
            String ids = (String) context.get("ids");
            if(!StringUtils.isEmpty(ids)){
                ids = ids.replaceAll("，", ",");
                String[] idAry = ids.split(",");
                for(int i=0;i<idAry.length;i++){
                    String id = idAry[i];
                    if(!StringUtils.isEmpty(id)){
                        Map<String,String> param = new HashMap<String,String>();
                        param.put("id", id);
                        params.add(param);
                    }
                }
                service.batchDelete(params);
            }

        }catch(Exception e){
            logger.error(e.getMessage(), e);
            status=ResponseResult.FAILED;
            info = e.getMessage();
        }
        writeJsonObject(response, new ResponseResult(status,null,info));
	}

	
	@RequestMapping("/flow/uploadFlow")
    @ResponseBody
	public ResponseResult uploadPage(@RequestParam MultipartFile scan, HttpServletRequest request,
            HttpServletResponse response) {
        SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String importFields = request.getParameter("importFields");
	    if(importFields==null ||"".equals(importFields)){
            return null;
        }
	    String [] importAry = importFields.split(",");
        int columnLimit = importAry.length;
        UploadResult result = new UploadResult();
        result.setType(UploadResult.SUCCESS);
        result.setMessage("上传成功");
        List<Map<String,String>> params = new ArrayList<Map<String, String>>();
        InputStream is;
        try {
            is = scan.getInputStream();
            CsvReader csvReader = new CsvReader(is, Charset.forName("GBK"));
            int count = 0;
            Set<String> set = service.getUniqueKey();
			Map<String, Integer> map = new HashMap<String, Integer>();// key-上传每条数据唯一键字段，value-
																		// 唯一字段所在行数
			int[] arr = new int[set.size()];// 存放唯一键在上传字段的位置
            while (csvReader.readRecord()) {
            	Map<String, String> flowMap = new HashMap<String, String>();
                count++;
                if (csvReader.getColumnCount() != columnLimit) {
                    result.setType(UploadResult.FILE_ERROR);
                    result.setMessage("第" + count + "行必须是"+columnLimit+"列！");
                    return ResponseBuilder.ok(result);
                }
                if (count > 2000) {
                    result.setType(UploadResult.FILE_ERROR);
                    result.setMessage("每次上传不能超过2000条！");
                    return ResponseBuilder.ok(result);
                }
                String startTime = "";
				String endTime = "";
				int index = 0;
                for (int i = 0; i < importAry.length; i++) {
                	String t = csvReader.get(i).trim();
					if ("null".equals(t.toLowerCase())) {
						t = "";
					}
                	if (set.contains(importAry[i])) {
						arr[index] = i;
						index++;
					}
					if ("START_TIME".equals(importAry[i])) {
						startTime = t;
					} else if ("END_TIME".equals(importAry[i])) {
						endTime = t;
					}
					flowMap.put(importAry[i], t);
				}
				String uniqueKey = "";
				for (int j = 0; j < arr.length; j++) {
					uniqueKey = uniqueKey + csvReader.get(arr[j]).trim();
				}
				if (map.containsKey(uniqueKey)) {
					result.setType(UploadResult.FILE_ERROR);
					result.setMessage("第" + count + "行数据与第"
							+ map.get(uniqueKey) + "行重复！");
					return ResponseBuilder.ok(result);
				}else{
					map.put(uniqueKey, count);
				}
				if (!"".equals(startTime) && !"".equals(endTime)) {
						Date start = sim.parse(startTime);
						Date end = sim.parse(endTime);
						if (end.before(start)) {
							result.setType(UploadResult.FILE_ERROR);
							result.setMessage("第" + count + "行开始时间大于结束时间！");
							return ResponseBuilder.ok(result);
						}else {
							flowMap.put("START_TIME",
								sim.format(start));
							flowMap.put("END_TIME", sim.format(end));
						}
					}
                String user = request.getSession().getAttribute("user").toString();
                flowMap.put(service.getUpdateUserField(), user);
                String now = sim.format(new Date());
                flowMap.put(service.getUpdateTimeField(), now);
                if(flowMap.get("id") == null ||"".equals(flowMap.get("id"))){
                    flowMap.put(service.getCreateUserField(), now);
                }
                params.add(flowMap);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            logger.error("表格必须是"+columnLimit+"列", e);
            result.setType(UploadResult.ERROR);
            result.setMessage("表格必须是"+columnLimit+"列");
        } catch (ParseException e) {
			logger.error("时间格式错误", e);
			result.setType(UploadResult.ERROR);
			result.setMessage("时间格式错误");
		} catch (FileNotFoundException e) {
            logger.error("没找到csv文件", e);
            result.setType(UploadResult.ERROR);
            result.setMessage("没找到csv文件");
        } catch (IOException e) {
            logger.error("读取出错", e);
            result.setType(UploadResult.ERROR);
            result.setMessage("读取出错");
        }
        if (result.getType() != 1) {
			return ResponseBuilder.ok(result);
		}
        try {
			service.batchUpdate(params);
			if (params.size() != 0) {
				result.setMessage("上传成功！");
			} else {
				result.setMessage("上传文件为空，上传失败！");
			}
		} catch (Exception e) {
			result.setMessage("上传失败！");
		}
        return ResponseBuilder.ok(result);
	}

	    @RequestMapping("/flow/exportFlow")
	    public void exportFlow(HttpServletRequest request, HttpServletResponse response, @RequestParam(required = false) Map<String, Object> context) {
	        PageCond page = getPageFromContext(context);
	        page.setLength(10000);
			Map<String, String> param = getParamFromContext(context, "searchbox.");
			Map<String, String> opbox = getParamFromContext(context, "opbox.");
			String[] columns = request.getParameter("exportFields").split(",");
	        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
	        String outputfilename = df.format(new Date());// new Date()为获取当前系统时间
	        response.setCharacterEncoding("GBK");
	        String filename = "Flow_" + outputfilename;
	        response.addHeader("Content-Disposition", "attachment;filename=" + filename + ".csv");
	        service.export(response, page, param,opbox,columns);
	    }
	/**
	 * 输出返回信息
	 * 
	 * @param response
	 * @param msg
	 */
	public void writeMsg(HttpServletResponse response, String msg) {
		PrintWriter pw = null;
		try {
			response.setContentType("text/html;charset=utf-8");
			pw = response.getWriter();
			pw.write(msg);
		} catch (IOException e) {
			logger.info(e.getMessage(), e);
		} finally {
			if (pw != null) {
				pw.close();
			}
		}
	}

	/**
     * 输出返回信息
     *
     * @param response
     * @param obj
     */
    public void writeJsonObject(HttpServletResponse response, Object obj) {
        PrintWriter pw = null;
        try {
            response.setContentType("application/json;charset=utf-8");
            pw = response.getWriter();
            pw.write(JSONUtil.toJSONString(obj, "","yyyy-mm-dd HH:mm:ss"));
        } catch (IOException e) {
            logger.info(e.getMessage(), e);
        } finally {
            if (pw != null) {
                pw.close();
            }
        }
    }
}
