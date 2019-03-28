<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">

<title>部门管理</title>

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

function add(form){
	return request({
		url: "songtype/saveOrUpdate.html",
		form: form,
		successHandle:function(data){
			loadData();
			form.reset();
		},
		isMultipart:true,
		checkHandle:function(){return true;}
	});
}

function deleteThis(id){
	request({
		url: "songtype/remove.html",
		data: {id: id},
		successHandle:function(data){
			loadData();
		}
	});
}



function viewOrUpdateThis(id){
	var inputEls = $("#tr"+id).find("input");
	var inputTextEl = $("#tr"+id).find("input")[0];
	var spanEl = $("#tr"+id).find("span")[0];
	if(inputTextEl.style.display == 'block'){
		//隐藏
		$("#tr"+id).find("input").css("display","none");
		$("#tr"+id).find("span").css("display","block");
	}else{
		//显示
		$("#tr"+id).find("input").css("display","block");
		$("#tr"+id).find("span").css("display","none");
		return false;
	}
	var form = $("<form/>");
	//克隆对象
	form.append(inputEls.clone());
	//改为dom对象
	form = form[0];
	
	request({
		url: "songtype/saveOrUpdate.html",
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
			url:"songtype/findList.html",
			refNames:['songtypes']
		}
	];
	//加载标签
	loadEls = ['#categoryTableTr'];
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
								<th width="20%;">图片</th>
								<th width="20%;">作者</th>
								<th width="20%;">描述</th>
								<th width="40%;">操作</th>
							</tr>
						</thead>
							<!-- vue要在父标签中使用 -->
							<tbody  id="categoryTableTr">
								<tr v-for="classify in data.songtypes" :id="'tr'+classify.id">
								<th>
									<input type='hidden' name='id' style="display: none;" :value="classify.id" />
									<input type="file" style="display: none;" name="picUrlimage">
									<span><img width="80" height="80" :src="classify.picUrl"/></sapn>
								</th>
								<th>
									<input type="text" name="songListAuthor" style="display: none;" :value="classify.songListAuthor">
									<span>{{classify.songListAuthor}}</span>
								</th>
								<th>
									<input type="text" name="songListDesc" style="display: none;" :value="classify.songListDesc">
									<span>{{classify.songListDesc}}</span>
								</th>
								<th style="text-align: center;">
									<button class="btn btn-app btn-grey btn-xs radius-4" @click="viewOrUpdateThis(classify.id)">			
												修改	
											</button>&nbsp;&nbsp;
									<button class="btn btn-app btn-grey btn-xs radius-4" @click="deleteThis(classify.id)">			
											删除	
										</button>
									</th>
								</tr>
							</tbody>
                	<form onsubmit="return add(this)" method="post">
							<tr>
								<th>
									<input type="file" name="picUrlimage">
								</th>
								<th>
									<input type="text" name="songListAuthor" :value="classify.songListAuthor">
								</th>
								<th>
									<input type="text" name="songListDesc" :value="classify.songListDesc">
								</th>
								<td style="text-align: center;">
									<div
										class="visible-md visible-lg hidden-sm hidden-xs action-buttons">
										
									<button class="btn btn-app btn-grey btn-xs radius-4" type="submit">		
												添加	
											</button>
									</div>
								</td> </tr>
						</form>
					</table>
				</div>
			</div>
			</div>
</body>
</html>
