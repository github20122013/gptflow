<#include "flow/head/head.ftl"/>
<body>
    <div class="container-fluid mt-5">
        <div class="row">
            <div class="col mx-auto">
                <div class="card">
					<h6 class="col">查询条件</h6>
					<hr>

                    <div class="container-fluid">

                            <div class="form-inline">
								<div class="form-group">
									<label class="ml-4 mr-2" for="searchbox.id">id</label>
									<input class="form-control form-control-sm" name='searchbox.id' type='text' id='searchbox.id'  value='${searchbox["id"]}' />
								</div>
								<div class="form-group">
									<label class="ml-4 mr-2" for="searchbox.flowName">流程名称</label>
									<input class="form-control form-control-sm" name='searchbox.flowName' type='text' id='searchbox.flowName'  value='${searchbox["flowName"]}' />
								</div>
								<div class="form-group">
									<label class="ml-4 mr-2" for="searchbox.parentId">上级流程</label>
									<input class="form-control form-control-sm" name='searchbox.parentId' type='text' id='searchbox.parentId'  value='${searchbox["parentId"]}' />
								</div>
								<div class="form-group">
									<label class="ml-4 mr-2" for="searchbox.parentId">上级流程</label>
									<select class="form-control form-control-sm" name='searchbox.parentId' type='text' id='searchbox.parentId'  value='${searchbox["parentId"]}' >
										<option name="2">第一个</option>
										<option name="3">第二个</option>
									</select>
								</div>

							</div>

                    </div>

					<hr>

                    <div class="container-fluid">

                        <input type="hidden" name="exportFields" value="id,flowName,parentId"/>

                        <button id='addBtn' type="button" class="btn btn-sm btn-outline-primary"><span><span>新增</span></span></button>

                        <button id='batchDelBtn' type="button" class="btn btn-sm btn-outline-primary"><span><span>批量删除</span></span></button>

                        <button id='batchAddBtn' type="button" class="btn btn-sm btn-outline-primary" onclick="showBatchUpload()"><span><span>批量上传</span></span></button>

                        <button id="exportCsvBtn" type="button" class="btn btn-sm btn-outline-primary" onclick="exportCsv()"><span><span>批量导出</span></span></button>

                        <button id='queryBtn' type="button" class="btn btn-sm btn-outline-primary" onclick="doQuery()"><span><span>查询</span></span></button>

                    </div>

                    <div class="container-fluid p-2" >

                        <table class="table table-bordered">

                            <tr style="position:relative;">
                                <th style="width:70px;"><input name="checkall" id="checkall" type="checkbox" onclick="unselectAll()"/>全选</th>
                                <th>流程名称</th>
<th>上级流程</th>

                                <th style="">操作</th>
                            </tr>

                            <#list objs as obj>
                                <tr >
                                    <td align="left">
                                        <input type="checkbox"  name="chks"  value="${obj.id?c}"/>
                                        ${page.length*(page.currentPage-1)+obj_index+1}
                                    </td>
                                    <td>${obj.flowName}</td>
<td>${obj.parentId}</td>

                                    <td>

                                        <a  href="javascript:void(0)" onclick="doDelete({id:'${obj.id?c}'})">删除</a>&nbsp;|

                                        <a  href="javascript:void(0)" onclick="showSave({id:'${obj.id?c}'})">修改</a>
                                        
                                    </td>
                                </tr>
                            </#list>

                        </table>
                    </div>

                    <div class="container-fluid pagenation text-right">
                        <script type="text/javascript">
                            window.__page=new SnPage(document.getElementById("searchForm"),'/gptflow/flow/queryFlow.do','${page.totalPage?c}','${page.currentPage?c}');
                        </script>
                        每页&nbsp;<input type="text"  name="length" style="width:30px;" value="${page.length?c}"/>&nbsp;条记录&nbsp;|
                        总共<font color="red">${page.totalPage?c}</font>页，<font color="red">${page.total?c}</font>条记录&nbsp;|&nbsp;
                        第<font color="red">${page.currentPage?c}</font>页
                        <a href="#" onclick="__page.goToPage('1');" >首页</a>
                        <a href="#" onclick="__page.goToPage('${page.beforePage?c}');" >上一页</a>
                        <a href="#" onclick="__page.goToPage('${page.nextPage?c}');" >下一页</a>
                        跳转至&nbsp;<input type="text" id="currentPage" value="${page.currentPage?c}" name="currentPage" style="width:30px;text-align: center;"/>&nbsp;页
                        <a href="#" onclick="__page.goToPage(document.getElementById('currentPage').value);">跳转</a>
                        <a href="#" onclick="__page.goToPage('${page.totalPage?c}');" >尾页</a>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="modal" id="saveDialog" tabindex="-1" role="dialog">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">gpt流保存</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <input type="hidden" class="savebox" name="savebox.id" id="saveboxid" >
                    <div class="box-content">
                        <table class="table">
                            <tr style='height:35px;'>
<td width='140px' align='right'>id：</td>
<td> <input type='text' class='savebox'  name='savebox.id' id='saveboxid' ></td>
</tr>
<tr style='height:35px;'>
<td width='140px' align='right'>流程名称：</td>
<td> <input type='text' class='savebox'  name='savebox.flowName' id='saveboxflowName' ></td>
</tr>
<tr style='height:35px;'>
<td width='140px' align='right'>上级流程：</td>
<td> <input type='text' class='savebox'  name='savebox.parentId' id='saveboxparentId' ></td>
</tr>

                        </table>
                    </div>
                </div>
                <div class="modal-footer">
                    <button name="save" id="save"  type="button" class="btn btn-primary"><span><span>保存</span></span></button>
                    <button name="cancel" class="btn btn-secondary" id="cancel"  type="button" data-dismiss="modal"><span><span>取消</span></span></button>
                </div>
            </div>
        </div>
    </div>

    <!-- 批量上传 -->
    <div class="modal" id="batchupload" tabindex="-1" role="dialog">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-body">
                    <form action="/gptflow/flow/uploadFlow.do" method="POST" id="sguploadForm" name="sguploadForm" enctype="multipart/form-data" onsubmit="return false;">
                        <div style="text-align: center;margin: 10px 0;">
                            <span>路径：</span>
                            <input type="hidden" name="importFields" value="id,flowName,parentId"/>
                            <input type="file" name="scan" id="scan"  style="height:26px;width:220px"></input>
                            <button name="send" id="sendFileBtn" type="button" class="btn btn-primary"><span><span >上传</span></span></button>
                        </div>
                    </form>
                    <div id="errorMessage" style="color:red;text-align:center;"></div>
                    <div align="center">
                        <div class="add" >
                            <div class="rTitle" align="left">
                                <h3>记录字段顺序</h3>
                            </div>
                            <table width="100%" class="up_table" border="1">
                                <th>id</th><th>流程名称</th><th>上级流程</th>
                            </table>
                            <div class="rTitle" align="left">
                                <h3>上传注意事项</h3>
                            </div>
                            <ol style="list-style-type:decimal;text-align:left;margin-left:20px;">
                                <li> 请保证上传数据列数正确</li><li> 每次上传限制1000条</li><li> 上传文件为CSV格式，GBK编码</li>
                                <li><span style="color:red">上传文件必须为csv文件</span></li>
                                <li><span style="color:red">文件需要表头，字段分隔符用英文逗号（,）</span></li>
                            </ol>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

</body>
<script type="text/javascript">
    //提示信息
    if('${_msg}'!=null&&'${_msg}'!=""){
       alert('${_msg}');
    }

    $(function() {
        $("#saveDialog").hide();
        $("#addBtn").click(function(){
            showSave()
        });
        $("#cancel").click(function(){
            $("#saveDialog").dialog('close');
        });
        $("#save").click(function(){
             doSave();
        });
        $("#batchDelBtn").click(function(){
             doBtchDel();
        });
        $("#exportBtn").click(function(){
             doExport();
        });
        $("#sendFileBtn").click(function(e){
                e.preventDefault();
                doBatchUpload();
            });
    });

    //字段schema
	var allFiledsStr = '[{"_KEY_":"id","_CN_":"id"},{"_KEY_":"flowName","_CN_":"流程名称","_NOT_NULL_":"1"},{"_KEY_":"parentId","_CN_":"上级流程","_NOT_NULL_":"1"},{"_KEY_":"createAt","_CN_":"创建时间"},{"_KEY_":"updateAt","_CN_":"更新时间"},{"_KEY_":"createBy","_CN_":"创建人"},{"_KEY_":"updateBy","_CN_":"更新人"}]';
	var allFileds = eval('('+allFiledsStr+')');

	//弹出保存框
	function showSave(obj){
		if(!obj){
            obj = new Object();
        }
        //回填默认值
        $(".defaultbox").each(function(index,ele){
                var field = getField('defaultbox.',this.name);
                obj[field]= $.trim(this.value);
            });
		$(".savebox").each(function(index,ele){
				if(ele.getAttribute("id")!="saveboxTYPE"){
					ele.value='';
				}
			});
		var modify=false;
		if(obj){
			if(obj.id){
				expand(obj);
				modify=true;
			}
			for(attr in obj){
				if(document.getElementById('savebox'+attr)){
					document.getElementById('savebox'+attr).value=obj[attr];
				}else if(document.getElementById('showbox'+attr)){
					document.getElementById('showbox'+attr).value=obj[attr];
				}
			}
		}
		if(allFileds&&allFileds.length>0){
			for(var i=0;i<allFileds.length;i++){
				var field = allFileds[i];
				//唯一索引字段不允许修改
				if(field._UNIQ_&&document.getElementById('savebox'+field._KEY_)){
					document.getElementById('savebox'+field._KEY_).disabled=modify;
				}
			}
		}
		$("#saveDialog").dialog({
			resizable : false,
			width : 450,
			modal : true
		});
	}

    //按ID查询业务数据
    function expand(obj){
    	if(obj.id){
			$.ajax({
				url: '/gptflow/flow/expandFlow.do',
				type: 'POST',
				dataType: 'json',
				contentType: 'application/json',
				timeout: 5000,
				data:JSON.stringify(obj),
				async: false,
				success:function(response){
							if(response){
			    				for(attr in response){
			    					obj[attr] = response[attr]
			    				}
			    			}
					    }
				    });
    	}
    }

    //AJAX批量删除
	function doBtchDel(){
		var ids=getCheckedIds("chks");
		if(''==ids||!ids){
			alert('请至少选择一条记录！');
			return;
		}
		$.ajax(
		        {
                    url: '/gptflow/flow/batchDeleteFlow.do',
                    type: 'POST',
                    dataType: 'json',
                    contentType: 'application/json',
                    timeout: 5000,
                    data:JSON.stringify({ids:ids}),
                    async: false,
                    success:function(response,status){
                                if('success' == response.status){
                                    alert('删除成功');
                                    doQuery();
                                    return;
                                }else{
                                    alert('删除失败，您可能无权限');
                                    return;
                                }
                            }
                }
		    );
	}

    function getField(prefix,allName){
    	var result = allName;
    	if(prefix){
    		var idx = allName.indexOf(prefix);
    		//alert(idx);
    		if(idx==0){
    			result = allName.substring(prefix.length);
    		}
    	}
    	return result;
    }

    //AJAX保存
	function doSave(){
		var params = new Object();
        params.savebox=new Object();
        params.defaultbox=new Object();
        $(".savebox").each(function(index,ele){
                var field = getField('savebox.',this.name);
                params.savebox[field]= $.trim(this.value);
            });
        $(".defaultbox").each(function(index,ele){
                var field = getField('defaultbox.',this.name);
                params.defaultbox[field]= $.trim(this.value);
            });

		if(!checkValid(params.savebox)){
			return;
		}

		//alert(222);
		$.ajax({
				url: '/gptflow/flow/saveFlow.do',
				type: 'post',
				dataType: 'json',
				contentType: 'application/json',
				timeout: 5000,
				data:JSON.stringify(params),
				async: false,
				success:function(response,status){
							if('success' == response.status){
								alert('保存成功');
								doQuery();
								return;
							}else if('exists' == response.status){
								alert('数据库已经存在该记录喔，请检查后重新录入');
								return;
							}else{
							    alert('保存失败，您可能无权限!');
								return;
							}
						}
				});
	}

	//AJAX删除
	function doDelete(obj){
		$.ajax({
				url: '/gptflow/flow/deleteFlow.do',
				type: 'post',
				dataType: 'json',
				contentType: 'application/json',
				timeout: 5000,
				data:JSON.stringify(obj),
				async: false,
				success:function(response,status){
							if('success' == response.status){
								alert('删除成功');
								doQuery();
								return;
							}else{
								alert('删除失败!'+JSON.stringify(response));
								return;
							}
						}
				});
	}

	//获取选中的值
	function getCheckedIds(name){
		var ids="";
		var checks = document.getElementsByName(name);
		if(checks.length){
			for(var i=0;i<checks.length;i++){
				if(checks[i].checked){
					ids+=checks[i].value+",";
				}
			}
		}
		return ids;
	}
	//查询
	function doQuery(){

		var searchForm = $("#searchForm");
		var tmp = searchForm.attr("action");
		searchForm.attr("action","/gptflow/flow/queryFlow.do");
		searchForm.submit();
	}

	function checkValid(obj){
		if(allFileds!=null&&allFileds.length){
			for(var i=0;i<allFileds.length;i++){
				var fn = allFileds[i]._KEY_;
				var fncn = allFileds[i]._CN_;
				var notNull = allFileds[i]._NOT_NULL_;
				//alert(fn);
				//alert(obj[fn]);
				if(notNull&&(!obj[fn]||''==obj[fn])){
					alert(fncn+'不能为空！');
					return false;
				}
			}
		}
		if(obj.END_TIME&&obj.START_TIME){
			var ee = moment(obj.END_TIME);
			var ss = moment(obj.START_TIME);
			var now = moment();
			if(now.isAfter(ss)){
				alert('开始时间不能小于当前时间！');
				return;
			}
			if(ss.isAfter(ee)){
				alert('开始时间不能大于结束时间！');
				return;
			}
		}

		return true;
	}


	//批量上传
	function showBatchUpload(){
		$("#batchupload").dialog({modal:true,minWidth:650,minHeight:250,
			close:function(){//定义窗口关闭时的函数，$("#batchupload").dialog("close")调用时，实际调用的是此处定义的方法
				doQuery();
			},
			buttons:{
				"确定":function(){
					$("#batchupload").dialog("close");
				}
			}
		});
	}

	function doBatchUpload() {
		var files = $("#scan").val();
		if(files==null || files==""){
			alert("请选择CSV文件");
			return false;
		}else if(files.substr(files.length-4,4).toLowerCase().match("^.csv") == null){
			alert("请选择.csv格式文件");
			return false;
		}
		$('#sguploadForm').ajaxSubmit({
                dataType:'json',
                success:function(data){
                	$("#errorMessage").html(data.obj.message);
                	alert(data.obj.message);
                	if (data.obj.message == "上传成功!") {
                	   window.location.href="/gptflow/flow/manageFlow.do";
                	}
                },
                error:function(xhr){
                    alert('上传失败，请检查文件格式，或者您可能无权限!');
                }
        });
	}

	function exportCsv() {
		var searchForm = $("#searchForm");
		var tmp = searchForm.attr("action");
		searchForm.attr("action","/gptflow/flow/exportFlow.do");
		searchForm.submit();
		searchForm.attr("action",tmp);
	}

	function unselectAll(){
		if($("#checkall").prop("checked")){
			$("[name='chks']").prop("checked",true);//全选
		}else{
			$("[name='chks']").prop("checked",false);//取消全选
		}
	}

	$(document).ready(function (e) {
	  e('.btn-minimize') .click(function (t) {
	    t.preventDefault();
	    var n = e(this) .parent() .parent() .next('.description-body');
	    n.is(':visible') ? e('i', e(this)) .removeClass('fa-caret-up') .addClass('fa-caret-down')  : e('i', e(this)) .removeClass('fa-caret-down') .addClass('fa-caret-up');
	    n.slideToggle('slow')
	  });

	  var functionName = $("#title").text();
	  $.ajax({
				url: '/gptflow/description/queryDescriptionJson.do',
				type: 'POST',
				dataType: 'json',
				contentType: 'application/json',
				timeout: 5000,
				data:JSON.stringify({searchbox:{"FUNCTION_NAME":functionName}}),
				async: false,
				success:function(result){
							if(result&&result.obj&&result.obj.objs&&result.obj.objs[0]){
                                var sample = result.obj.objs[0];
                                $("#showDescriptionSpan").html(sample.description.replace(new RegExp("\n","g"),"<br/>"));
                                $("#_DESCRIPTION_ID").val(sample.id);
                                $("#descriptionInput").val(sample.description);
                                return;
                            }
						},
				error:function(xhr){
                    return;
                }
			});
	});

	function edit(){
		$("#showDescription").hide();
		$("#descriptionInput").val($("#showDescriptionSpan").html().replace(new RegExp("<br>","g"),"\n"));
		$("#editDescription").show();
	}

	function closeEdit(){
		$("#showDescription").show();
		$("#editDescription").hide();
	}

	function submitEdit(){
		var description = $("#descriptionInput").val();//人工输入类型的用val
		var functionName = $("#title").text();//显示类型的用text，包含html标签的用html
		var descId = $("#_DESCRIPTION_ID").val();
		if(description.length>500){
			alert("内容大于500个字符");
			return;
		}
		$.ajax({
				url: '/gptflow/description/saveDescription.do',
				type: 'POST',
				dataType: 'json',
				contentType: 'application/json',
				timeout: 5000,
				data:JSON.stringify({savebox:{FUNCTION_NAME:functionName,DESCRIPTION:description,ID:descId}}),
				async: false,
				success:function(result){
							if('success' == result.status){
								$("#showDescriptionSpan").html(description.replace(new RegExp("\n","g"),'<br/>'));
								closeEdit();
								return;
							}else{
								alert('失败');
								closeEdit();
								return;
							}
						},
				error:function(xhr){
                    alert('失败!');
                }
				});
	}

	function countWordNum(){
		$("#totalNum").html($("#descriptionInput").val().length+"/");
	}


	function checkNumber(){
		var number = $("#saveboxSORT").val();
		var reg1 =  /^\d+$/;
		if(number.match(reg1) == null)  {
			$("#tip").html("，必须为大于0的整数");
			$("#save").attr("disabled",true);
			$("#save").css("color","gray");
		}else{
			$("#save").removeAttr("disabled");
			$("#save").css("color","white");
			$("#tip").html("");
		}
	}
</script>
</html>