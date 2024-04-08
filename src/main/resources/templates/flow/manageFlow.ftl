<#include "/flow/head/head.ftl"/>
<body>
<div class="container-fluid p-3">
    <form method="post" action="" id="searchForm">
        <div class="card">
            <#if defaultbox?exists>
                <#list defaultbox?keys as key>
                    <input type="hidden" name="defaultbox.${key}" value="${defaultbox["${key}"]}"/>
                </#list>
            </#if>
            <div class="col">
                <div class="row pt-2 pl-3 bg-primary text-white bg-gradient">
                    <h6>查询条件</h6>
                    <img class="ml-3" src="../images/searchlogo.png" width="20px" height="20px">
                </div>
            </div>
            <div class="container-fluid">
                <div class="form-inline mt-3">
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
                                    <label class="ml-4 mr-2" for="searchbox.status">流程状态</label>
                                    <select class="form-control form-control-sm" name="searchbox.status" id="searchbox.status">
                                        <option>请选择</option>
                                        <option key="draft" <#if searchbox["status"]=='draft'>selected</#if> >
                                            草稿
                                        </option>
                                        <option key="finalize" <#if searchbox["status"]=='finalize'>selected</#if> >
                                            封板
                                        </option>
                                        <option key="running" <#if searchbox["status"]=='running'>selected</#if> >
                                            运行
                                        </option>
                                        <option key="suspend" <#if searchbox["status"]=='suspend'>selected</#if> >
                                            暂停
                                        </option>
                                        <option key="closed" <#if searchbox["status"]=='closed'>selected</#if> >
                                            关闭
                                        </option>

                                    </select>
                                </div>
                                <div class="form-group">
                                    <label class="ml-4 mr-2" for="searchbox.createAt">创建时间</label>
                                    <input class="form-control form-control-sm" name='searchbox.createAt' type='text' id='searchbox.createAt'  value='${searchbox["createAt"]}' />
                                </div>


                </div>
            </div>
            <hr>
            <div class="container-fluid">
                <input type="hidden" name="exportFields" value="id,flowName,parentId"/>
                <button class="btn btn-shadow btn-outline-primary  btn-transition  btn-sm border-0" id="addBtn">新增</button>
                <button class="btn btn-shadow btn-outline-primary  btn-transition  btn-sm border-0" id="batchDelBtn">批量删除</button>
                <button class="btn btn-shadow btn-outline-primary  btn-transition  btn-sm border-0" id="batchAddBtn">批量上传</button>
                <button class="btn btn-shadow btn-outline-primary  btn-transition  btn-sm border-0" id="exportCsvBtn">批量导出</button>
                <button class="btn btn-shadow btn-outline-primary  btn-transition  btn-sm border-0" id="queryBtn">查询</button>
            </div>
            <hr>
            <div class="container-fluid p-2">
                <table class="table table-bordered">
                    <tr>
                        <th style="width:80px;">
                            <input name="checkall" id="checkall" type="checkbox" class="align-middle" onclick="unselectAll()"/>&nbsp;全选
                        </th>
                        <th>流程名称</th>
<th>上级流程</th>

                        <th style="">操作</th>
                    </tr>
                    <#list objs as obj>
                        <tr>
                            <td align="left">
                                <input type="checkbox" name="chks" value="${obj.id?c}"/>
                                ${page.length*(page.currentPage-1)+obj_index+1}
                            </td>
                            <td>${obj.flowName}</td>
<td>${obj.parentId}</td>

                            <td>
                                <a href="javascript:void(0)"
                                   onclick="doDelete({id:'${obj.id?c}'})">删除</a>&nbsp;|
                                <a href="javascript:void(0)"
                                   onclick="showSave({id:'${obj.id?c}'})">修改</a>
                                
                            </td>
                        </tr>
                    </#list>
                </table>
            </div>
            <hr>
            <div class="container-fluid">
                <script type="text/javascript">
                    window.__page = new Page(document.getElementById("searchForm"), '/gptflow/flow/queryFlow', '${page.totalPage?c}', '${page.currentPage?c}');
                </script>
                <div class="row justify-content-around pb-2">
                    <div class="col-auto">
                        总计<span class="text-danger">${page.total?c}</span>条记录 | <span class="text-danger">${page.totalPage?c}</span>页
                    </div>
                    <div class="col text-right">
                        每页&nbsp;<input type="text" name="length" class="page-input" value="${page.length?c}"/>&nbsp;条记录&nbsp;
                        <a href="#" onclick="__page.goToPage('1');">首页</a>
                        <a href="#" onclick="__page.goToPage('${page.beforePage?c}');">上一页</a>
                        <a href="#" onclick="__page.goToPage('${page.nextPage?c}');">下一页</a>
                        <a href="#" onclick="__page.goToPage('${page.totalPage?c}');">尾页</a>
                        跳至 <input type="text" id="currentPage" value="${page.currentPage?c}" class="page-input" aria-label=""> 页
                        <a href="#" onclick="__page.goToPage(document.getElementById('currentPage').value);">GO</a>
                    </div>
                </div>
            </div>
        </div>
    </form>
</div>

<#-- 保存弹窗 -->
<div id="saveDialog" title="gpt流保存" class="d-none">
    <input type="hidden" class="savebox" name="savebox.id" id="saveboxid">
    <#if defaultbox?exists>
        <#list defaultbox?keys as key>
            <input type="hidden" class="defaultbox" name="defaultbox.${key}" value="${defaultbox[" ${key}"]}"/>
        </#list>
    </#if>
    <div class="box-content">
        <div class="container-fluid">
                                            <div class="form-group">
                                    <label class="ml-4 mr-2" for="savebox.id">id</label>
                                    <input class="form-control form-control-sm" name='savebox.id' type='text' id='savebox.id'  value='${savebox["id"]}' />
                                </div>
                                <div class="form-group">
                                    <label class="ml-4 mr-2" for="savebox.flowName">流程名称</label>
                                    <input class="form-control form-control-sm" name='savebox.flowName' type='text' id='savebox.flowName'  value='${savebox["flowName"]}' />
                                </div>
                                <div class="form-group">
                                    <label class="ml-4 mr-2" for="savebox.parentId">上级流程</label>
                                    <input class="form-control form-control-sm" name='savebox.parentId' type='text' id='savebox.parentId'  value='${savebox["parentId"]}' />
                                </div>
                                <div class="form-group">
                                    <label class="ml-4 mr-2" for="savebox.status">流程状态</label>
                                    <select class="form-control form-control-sm" name="savebox.status" id="savebox.status">
                                        <option>请选择</option>
                                        <option key="draft" <#if savebox["status"]=='draft'>selected</#if> >
                                            草稿
                                        </option>
                                        <option key="finalize" <#if savebox["status"]=='finalize'>selected</#if> >
                                            封板
                                        </option>
                                        <option key="running" <#if savebox["status"]=='running'>selected</#if> >
                                            运行
                                        </option>
                                        <option key="suspend" <#if savebox["status"]=='suspend'>selected</#if> >
                                            暂停
                                        </option>
                                        <option key="closed" <#if savebox["status"]=='closed'>selected</#if> >
                                            关闭
                                        </option>

                                    </select>
                                </div>


        </div>
    </div>
    <div class="box-foot text-right">
        <button name="save" id="save" class="btn btn-sm btn-primary">保存</button>
        <button name="cancel" id="cancel" class="btn btn-sm btn-outline-primary">取消</button>
    </div>
</div>

<#--批量上传-->
<div class="showBox d-none" id="batchupload" title="gpt流批量上传">
    <form action="/gptflow/flow/uploadFlow" method="POST" id="uploadForm"
          name="uploadForm"
          enctype="multipart/form-data" onsubmit="return false;">
        <div class="text-center mt-1">
            <span>路径：</span>
            <input type="hidden" name="importFields" value="id,flowName,parentId"/>
            <input type="file" name="scan" id="scan" class="form-control form-control-sm"
                   style="height:26px;width:220px"></input>
            <button name="send" id="sendFileBtn" class="btn btn-sm btn-primary" type="button">上传</button>
        </div>
    </form>
    <div id="row errorMessage text-center text-danger"></div>
    <div class="row">
        <div class="col">
            <div class="row text-left">
                <h3>记录字段顺序</h3>
            </div>
            <div class="row">
                <table width="100%" class="up_table" border="1">
                    <th>id</th><th>流程名称</th><th>上级流程</th>
                </table>
            </div>
            <div class="row" align="left">
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
</body>
<script type="text/javascript">
    //提示信息
    if ('${_msg}' != null && '${_msg}' != "") {
        alert('${_msg}');
    }

    $(function () {
        $("#addBtn").click(function () {
            showSave()
        });
        $("#batchAddBtn").click(function () {
            showBatchUpload()
        });
        $("#save").click(function () {
            doSave();
        });
        $("#batchDelBtn").click(function () {
            doBtchDel();
        });
        $("#queryBtn").click(function () {
            doQuery();
        });
        $("#exportCsvBtn").click(function () {
            doExport();
        });
        $("#cancel").click(function () {
            $("#saveDialog").dialog('close');
        });
        $("#sendFileBtn").click(function (e) {
            e.preventDefault();
            doBatchUpload();
        });
    });

    //字段schema
    var allFiledsStr = '[{"_KEY_":"id","_CN_":"id"},{"_KEY_":"flowName","_CN_":"流程名称","_NOT_NULL_":"1"},{"_KEY_":"parentId","_CN_":"上级流程","_NOT_NULL_":"1"},{"_KEY_":"status","_CN_":"流程状态","_NOT_NULL_":"1"},{"_KEY_":"createAt","_CN_":"创建时间"},{"_KEY_":"updateAt","_CN_":"更新时间"},{"_KEY_":"createBy","_CN_":"创建人"},{"_KEY_":"updateBy","_CN_":"更新人"}]';
    var allFileds = eval('(' + allFiledsStr + ')');

    //弹出保存框
    function showSave(obj) {
        if (!obj) {
            obj = new Object();
        }
        //回填默认值
        $(".defaultbox").each(function (index, ele) {
            var field = getField('defaultbox.', this.name);
            obj[field] = $.trim(this.value);
        });
        var modify = false;
        if (obj) {
            if (obj.id)
            {
                expand(obj);
                modify = true;
            }
            for (attr in obj) {
                if (document.getElementById('savebox' + attr)) {
                    document.getElementById('savebox' + attr).value = obj[attr];
                } else if (document.getElementById('showbox' + attr)) {
                    document.getElementById('showbox' + attr).value = obj[attr];
                }
            }
        }
        if (allFileds && allFileds.length > 0) {
            for (var i = 0; i < allFileds.length; i++) {
                var field = allFileds[i];
                //唯一索引字段不允许修改
                if (field._UNIQ_ && document.getElementById('savebox' + field._KEY_)) {
                    document.getElementById('savebox' + field._KEY_).disabled = modify;
                }
            }
        }
        $("#saveDialog").dialog({
            resizable: false,
            width: 450,
            modal: true
        });
    }

    //按ID查询业务数据
    function expand(obj) {
        if (obj.id)
        {
            $.ajax({
                url: '/gptflow/flow/expandFlow',
                type: 'POST',
                dataType: 'json',
                contentType: 'application/json',
                timeout: 5000,
                data: JSON.stringify(obj),
                async: false,
                success: function (response) {
                    if (response) {
                        for (attr in response) {
                            obj[attr] = response[attr]
                        }
                    }
                }
            });
        }
    }

    //AJAX批量删除
    function doBtchDel() {
        var ids = getCheckedIds("chks");
        if ('' == ids || !ids) {
            alert('请至少选择一条记录！');
            return;
        }
        $.ajax(
            {
                url: '/gptflow/flow/batchDeleteFlow',
                type: 'POST',
                dataType: 'json',
                contentType: 'application/json',
                timeout: 5000,
                data: JSON.stringify({ids: ids}),
                async: false,
                success: function (response, status) {
                    if ('success' == response.status) {
                        alert('删除成功');
                        doQuery();
                        return;
                    } else {
                        alert('删除失败，您可能无权限');
                        return;
                    }
                }
            }
        );
    }

    function getField(prefix, allName) {
        var result = allName;
        if (prefix) {
            var idx = allName.indexOf(prefix);
            //alert(idx);
            if (idx == 0) {
                result = allName.substring(prefix.length);
            }
        }
        return result;
    }

    /**
     * ajax 保存
     */
    function doSave() {
        var params = new Object();
        params.savebox = new Object();
        params.defaultbox = new Object();
        $(".savebox").each(function (index, ele) {
            var field = getField('savebox.', this.name);
            params.savebox[field] = $.trim(this.value);
        });
        $(".defaultbox").each(function (index, ele) {
            var field = getField('defaultbox.', this.name);
            params.defaultbox[field] = $.trim(this.value);
        });

        if (!checkValid(params.savebox)) {
            return;
        }

        $.ajax({
            url: '/gptflow/flow/saveFlow',
            type: 'post',
            dataType: 'json',
            contentType: 'application/json',
            timeout: 5000,
            data: JSON.stringify(params),
            async: false,
            success: function (response, status) {
                if ('success' == response.status) {
                    alert('保存成功');
                    doQuery();
                    return;
                } else if ('exists' == response.status) {
                    alert('数据库已经存在该记录喔，请检查后重新录入');
                    return;
                } else {
                    alert('保存失败，您可能无权限!');
                    return;
                }
            }
        });
    }

    //AJAX删除
    function doDelete(obj) {
        $.ajax({
            url: '/gptflow/flow/deleteFlow',
            type: 'post',
            dataType: 'json',
            contentType: 'application/json',
            timeout: 5000,
            data: JSON.stringify(obj),
            async: false,
            success: function (response, status) {
                if ('success' == response.status) {
                    alert('删除成功');
                    doQuery();
                    return;
                } else {
                    alert('删除失败!' + JSON.stringify(response));
                    return;
                }
            }
        });
    }

    /**
     * 获取选中的值
     * @param name
     * @returns {string}
     */

    function getCheckedIds(name) {
        var ids = "";
        var checks = document.getElementsByName(name);
        if (checks.length) {
            for (var i = 0; i < checks.length; i++) {
                if (checks[i].checked) {
                    ids += checks[i].value + ",";
                }
            }
        }
        return ids;
    }

    /**
     * 查询
     */
    function doQuery() {
        var searchForm = $("#searchForm");
        searchForm.attr("action", "/gptflow/flow/queryFlow");
        searchForm.submit();
    }

    /**
     * 字段校验
     * @param obj
     * @returns {boolean}
     */
    function checkValid(obj) {
        if (allFileds != null && allFileds.length) {
            for (let i = 0; i < allFileds.length; i++) {
                let fn = allFileds[i]._KEY_;
                let fncn = allFileds[i]._CN_;
                let notNull = allFileds[i]._NOT_NULL_;
                if (notNull && (!obj[fn] || '' == obj[fn])) {
                    alert(fncn + '不能为空！');
                    return false;
                }
            }
        }
        return true;
    }

    //批量上传
    function showBatchUpload() {
        $("#batchupload").dialog({
            modal: true, minWidth: 650, minHeight: 250,
            close: function () {//定义窗口关闭时的函数，$("#batchupload").dialog("close")调用时，实际调用的是此处定义的方法
                doQuery();
            },
            buttons: {
                "确定": function () {
                    $("#batchupload").dialog("close");
                }
            }
        });
    }

    function doBatchUpload() {
        let files = $("#scan").val();
        if (files == null || files == "") {
            alert("请选择CSV文件");
            return false;
        } else if (files.substr(files.length - 4, 4).toLowerCase().match("^.csv") == null) {
            alert("请选择.csv格式文件");
            return false;
        }
        $('#uploadForm').ajaxSubmit({
            dataType: 'json',
            success: function (data) {
                $("#errorMessage").html(data.obj.message);
                alert(data.obj.message);
                if (data.obj.message == "上传成功!") {
                    window.location.href = "/gptflow/flow/manageFlow";
                }
            },
            error: function (xhr) {
                alert('上传失败，请检查文件格式，或者您可能无权限!');
            }
        });
    }

    function doExport() {
        let searchForm = $("#searchForm");
        let tmp = searchForm.attr("action");
        searchForm.attr("action", "/gptflow/flow/exportFlow");
        searchForm.submit();
        searchForm.attr("action", tmp);
    }

    function unselectAll() {
        if ($("#checkall").prop("checked")) {
            $("[name='chks']").prop("checked", true);//全选
        } else {
            $("[name='chks']").prop("checked", false);//取消全选
        }
    }

</script>