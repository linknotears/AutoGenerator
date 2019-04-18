<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
## 首字母小写
#macro(tolowercase $str)$str.substring(0,1).toLowerCase()$str.substring(1)#end
## 后部分转换为小写
#macro(touppercase $str)$str.substring(0,1)$str.substring(1).toLowerCase()#end
## 首字母大写
#macro(toheaduppercase $str)$str.substring(0,1).toUpperCase()$str.substring(1)#end
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>

<base href="<%=basePath%>">

<title>更新</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="gdescription" content="This is my page">
<link href="assets/css/bootstrap.min.css" rel="stylesheet" />
<link rel="stylesheet" href="assets/css/font-awesome.min.css" />
<link rel="stylesheet" href="assets/css/ace.min.css" />
<link rel="stylesheet" href="assets/css/ace-rtl.min.css" />
<link rel="stylesheet" href="assets/css/ace-skins.min.css" />
<script src="assets/js/ace-extra.min.js"></script>
<script type="text/javascript" src="common/util/netutil-1.0.js"></script>
<style type="text/css">
select{
width: 160px;
}
input {
width: 160px;
}
</style>

<script type="text/javascript">
$(function(){
	//加载data
	loadUrls =[
		{
			url:"#tolowercase($entity)/findById.html",
			data:{
				'id':getParameter("id","${existUser.id}")
			},
			refNames:['#tolowercase($entity)']
		}
##判断select
#foreach($field in ${table.fields})
#if($cfg.propertyType.get($table.name).get($field.name) == 'select' || (${field.propertyName.contains('Id')} && !$cfg.propertyType.get($table.name).get($field.name)))
#set($propertyName = $field.propertyName.replace('Id',''))
		,
		{
			url:"${propertyName}/findList.html",
			refNames:['${propertyName}s'],
			successHandle: data => { 				
				//改造pettypes
				globalData.data.${propertyName}obj = arrJsonToMapping(globalData.data.${propertyName}s,"id","name");
			}
		}
#end
#end
	];
	//加载标签
	loadEls = ['#mainData'];
	//初始化数据
	init();

});

function viewOrUpdateThis(){
	var inputEls = $("#mainData").find("input");
	var inputTextEl = $("#mainData").find("input,select")[0];
	var spanEl = $("#mainData").find("span")[0];
	if(inputTextEl.style.display == 'block'){
		//隐藏
		$("#mainData").find("input,select").css("display","none");
		$("#mainData").find("span").css("display","block");
	}else{
		//显示
		$("#mainData").find("input,select").css("display","block");
		$("#mainData").find("span").css("display","none");
		return false;
	}
	//form和data一起提交
	//处理select
	//克隆对象(克隆的副作用，克隆后select选中的值永远是第一个)
	//解决方法，遍历所用的select获取它们的value和name放入data中
	var data = {};
	$("#mainData").find("select").each(function(){
		data[this.name] = this.value;
	});
	
	//处理表单
	var form = $("<form/>");
	form.append(inputEls.clone());
	//改为dom对象
	form = form[0];
	
	request({
		url: "#tolowercase($entity)/saveOrUpdate.html",
		data: data,
		form: form,
		successHandle:function(data){
			loadData();
		},
		isMultipart:true
	});
}
</script>
</head>

<body>
	<div class="breadcrumbs" id="breadcrumbs">
		<script type="text/javascript">
			try {
				ace.settings.check('breadcrumbs', 'fixed')
			} catch (e) {
			}
		</script>

		<ul class="breadcrumb">
		</ul>
		<!-- .breadcrumb -->

	</div>

	<div class="row">
		<center>
#set($temp = 1)
			<table id="mainData"
					class="table table-striped table-bordered table-hover"
					style="width:70%">
					<tr>
					<input type="hidden" name="id" style="display: none;" :value="data.#tolowercase($entity).${table.fields[0].propertyName}">
#foreach($field in ${table.fields})
##尽量不要在这里排除字段，显示会乱
#if(!$cfg.colExclude.get($table.name).get($field.name))
#if(!${field.keyFlag} || $cfg.propertyType.get($table.name).get($field.name))
##判断文件类型
#if($cfg.propertyType.get($table.name).get($field.name) == 'file' || ${field.propertyName.contains('image')})
						<th style="text-align: center;">${field.comment}</th>
						<th>
							<input type="file" style="display: none;" name="${field.propertyName}file">
							<span><img width="80" height="80" :src="data.#tolowercase($entity).${field.propertyName}"/></sapn>
						</th>
#elseif($cfg.propertyType.get($table.name).get($field.name) == 'select' || (${field.propertyName.contains('Id')} && !$cfg.propertyType.get($table.name).get($field.name)))
						<th style="text-align: center;">${field.comment}</th>
						<th>
							<select name="${field.propertyName}" style="display: none;" :value="data.#tolowercase($entity).${field.propertyName}">
#set($propertyName = $field.propertyName.replace('Id',''))
								<option v-for="${propertyName}temp in data.${propertyName}s" :value="${propertyName}temp.id">{{ ${propertyName}temp.name }}</option>
							</select>
							<span>{{data.${propertyName}obj[data.#tolowercase($entity).${field.propertyName}]}}</span>
						</th>
#elseif($cfg.propertyType.get($table.name).get($field.name) == 'sex' || $field.propertyName == 'sex')
						<th style="text-align: center;">${field.comment}</th>
						<th>
							<select name="${field.propertyName}" style="display: none;" :value="data.#tolowercase($entity).${field.propertyName}">
								<option value="true">男</option>
								<option value="false">女</option>
							</select>
							<span>{{data.#tolowercase($entity).${field.propertyName} == true? '男' : '女'}}</span>
						</th>
#elseif($field.type.contains('int') || $field.type.contains('double') || $field.type.contains('decimal'))
						<th>
							<input type="number" name="${field.propertyName}" style="display: none;" :value="data.#tolowercase($entity).${field.propertyName}">
							<span>{{data.#tolowercase($entity).${field.propertyName}}}</span>
						</th>
#elseif($field.type == 'date')
						<th>
							<input type="date" name="${field.propertyName}" style="display: none;" :value="formatDate(data.#tolowercase($entity).${field.propertyName})">
							<span>{{ formatDate(data.#tolowercase($entity).${field.propertyName} }}</span>
						</th>
#elseif($field.type == 'datetime')
						<th>
							<input type="datetime-local" name="${field.propertyName}" style="display: none;" :value="dateFormat(data.#tolowercase($entity).${field.propertyName})">
							<span>{{ formatDateTime(data.#tolowercase($entity).${field.propertyName},false) }}</span>
						</th>
#else
						<th style="text-align: center;">${field.comment}</th>
						<th>
							<input type="text" name="${field.propertyName}" style="display: none;" :value="data.#tolowercase($entity).${field.propertyName}">
							<span>{{data.#tolowercase($entity).${field.propertyName} }}</span>
						</th>
#end
#if(($temp % 2) == 0)
					</tr>
					<tr>
#end
##自增
#set($temp = $temp + 1)
#end
#end
#end
					</tr>
				</table>
			<input class="btn btn-primary" type="submit" value="修改" onclick="viewOrUpdateThis()" />
		</center>

	</div>
</body>
</html>