<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
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

<title>管理</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">

<link href="assets/css/bootstrap.min.css" rel="stylesheet" />
<link rel="stylesheet" href="assets/css/font-awesome.min.css" />
<link rel="stylesheet" href="assets/css/ace.min.css" />
<link rel="stylesheet" href="assets/css/ace-rtl.min.css" />
<link rel="stylesheet" href="assets/css/ace-skins.min.css" />

<script src="assets/js/ace-extra.min.js"></script>
<script type="text/javascript" src="common/util/netutil-1.0.js"></script>

<script type="text/javascript">
//添加方法
function add(){
	var inputEls = $("#addBlock").find("input");
	
	var data = {};
	$("#addBlock").find("select").each(function(){
		data[this.name] = this.value;
	});
	
	//处理表单
	var form = $("<form/>");
	form.append(inputEls.clone());
	//改为dom对象
	form = form[0];
	
	return request({
		url: "#tolowercase($entity)/saveOrUpdate.html",
		data: data,
		form: form,
		successHandle:function(data){
			loadData();
			inputEls.val("");//有效
		},
		isMultipart:true,
		checkHandle:function(){return true;}
	});
}
//删除方法
function deleteThis(id){
	request({
		url: "#tolowercase($entity)/remove.html",
		data: {id: id},
		successHandle:function(data){
			loadData();
		}
	});
}



function viewOrUpdateThis(id){
	var inputEls = $("#tr"+id).find("input");
	var inputTextEl = $("#tr"+id).find("input,select")[0];
	var spanEl = $("#tr"+id).find("span")[0];
	if(inputTextEl.style.display == 'block'){
		//隐藏
		$("#tr"+id).find("input,select").css("display","none");
		$("#tr"+id).find("span").css("display","block");
	}else{
		//显示
		$("#tr"+id).find("input,select").css("display","block");
		$("#tr"+id).find("span").css("display","none");
		return false;
	}
	//form和data一起提交
	//处理select
	//克隆对象(克隆的副作用，克隆后select选中的值永远是第一个)
	//解决方法，遍历所用的select获取它们的value和name放入data中
	var data = {};
	$("#tr"+id).find("select").each(function(){
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


//首次更新
$(function(){
	//加载data
	loadUrls =[
		{
			url:"#tolowercase($entity)/findList.html",
			refNames:['#tolowercase($entity)s']
		}
##判断select
#foreach($field in ${table.fields})
#if($cfg.propertyType.get($table.name).get($field.name) == 'select' || ${field.propertyName.contains('Id')})
#set($propertyName = $field.propertyName.replace('Id',''))
		,
		{
			url:"${propertyName}/findList.html",
			refNames:['${propertyName}s'],
			successHandle: data =>{				
				//改造pettypes
				globalData.data.${propertyName}obj = arrJsonToMapping(globalData.data.${propertyName}s,"id","name");
			}
		}
#end
#end
	];
	//加载标签
	loadEls = ['#categoryTableTr',"#addBlock"];
	//初始化数据
	init();
});
</script>
</head>

<body>

	<div class="breadcrumbs" id="breadcrumbs">
		<script type="text/javascript">
							try{ace.settings.check('breadcrumbs' , 'fixed');}catch(e){}
						</script>

		<ul class="breadcrumb">
		</ul>
	</div>
		

		<div class="row">
			<div class="col-xs-12">
				<div class="table-header">所有类型</div>
				<div class="table-responsive">
					<table id="sample-table-2"
						class="table table-striped table-bordered table-hover">
						<thead>
							<tr>
#foreach($field in ${table.fields})
##判断排除字段
#if(!$cfg.colExclude.get($table.name).get($field.name))
#if(!${field.keyFlag})
								<th>${field.comment}</th>
#end
#end
#end
								<th width="50%;">操作</th>
							</tr>
						</thead>
							<!-- vue要在父标签中使用 -->
							<tbody  id="categoryTableTr">
								<tr v-for="classify in data.#tolowercase($entity)s" :id="'tr'+classify.${table.fields[0].propertyName}">
								<input type='hidden' name='id' style="display: none;" :value="classify.$table.fields[0].propertyName" />
								
#foreach($field in ${table.fields})
##判断排除字段
#if(!$cfg.colExclude.get($table.name).get($field.name))
##判断主键
#if(!${field.keyFlag})
##判断文件类型
#if($cfg.propertyType.get($table.name).get($field.name) == 'file' || ${field.propertyName.contains('file')})
								<th>
									<input type="file" style="display: none;" name="${field.propertyName}file">
									<span><img width="80" height="80" :src="classify.${field.propertyName}"/></sapn>
								</th>
#elseif($cfg.propertyType.get($table.name).get($field.name) == 'select' || ${field.propertyName.contains('Id')})
								<th>
									<select name="${field.propertyName}" style="display: none;" :value="data.#tolowercase($entity).${field.propertyName}">
#set($propertyName = $field.propertyName.replace('Id',''))
										<option v-for="${propertyName}temp in data.${propertyName}s" :value="${propertyName}temp.id">{{ ${propertyName}temp.name }}</option>
									</select>
									<span>{{data.${propertyName}obj[classify.${field.propertyName}]}}</span>
								</th>
#elseif($cfg.propertyType.get($table.name).get($field.name) == 'sex' || ${field.propertyName == 'sex'})
								<th>
									<select name="${field.propertyName}" style="display: none;" :value="classify.${field.propertyName}">
										<option value="true">男</option>
										<option value="false">女</option>
									</select>
									<span>{{classify.${field.propertyName} == true? '男' : '女'}}</span>
								</th>
#else
								<th>
									<input type="text" name="${field.propertyName}" style="display: none;" :value="classify.${field.propertyName}">
									<span>{{classify.${field.propertyName} }}</span>
								</th>
#end
#end
#end
#end
								<th style="text-align: center;">
									<button class="btn btn-app btn-grey btn-xs radius-4" @click="viewOrUpdateThis(classify.${table.fields[0].propertyName})">			
												修改	
											</button>&nbsp;&nbsp;
									<button class="btn btn-app btn-grey btn-xs radius-4" @click="deleteThis(classify.${table.fields[0].propertyName})">			
											删除	
										</button>
									</th>
								</tr>
							</tbody>
							<tr id="addBlock">
#foreach($field in ${table.fields})
##判断排除字段
#if(!$cfg.colExclude.get($table.name).get($field.name))
##判断主键
#if(!${field.keyFlag})
##判断文件类型
#if($cfg.propertyType.get($table.name).get($field.name) == 'file' || ${field.propertyName.contains('file')})
								<th>
									<input type="file" name="${field.propertyName}file">
								</th>
#elseif($cfg.propertyType.get($table.name).get($field.name) == 'select' || ${field.propertyName.contains('Id')})
								<th>
									<select name="${field.propertyName}">
#set($propertyName = $field.propertyName.replace('Id',''))
										<option v-for="${propertyName}temp in data.${propertyName}s" :value="${propertyName}temp.id">{{ ${propertyName}temp.name }}</option>
									</select>
								</th>
#else
								<th>
									<input type="text" name="${field.propertyName}">
								</th>
#end
#end
#end
#end
								<td style="text-align: center;">
									<div
										class="visible-md visible-lg hidden-sm hidden-xs action-buttons">
										
									<button class="btn btn-app btn-grey btn-xs radius-4" type="submit" onclick="add()">		
												添加	
											</button>
									</div>
								</td> 
							</tr>
					</table>
				</div>
			</div>
			</div>
</body>
</html>
