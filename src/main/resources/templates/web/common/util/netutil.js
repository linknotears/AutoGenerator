	//模板：<script type="text/javascript" src="common/util/netutil.js"></script>
	addScript("common/util/jquery-1.10.2.min.js");
	addScript("common/util/vue.min.js");
	function addScript(url){
		document.write("<script language=javascript src="+url+"></script>");
	}
	//全局变量
	globalData = {'items':[],'item':{},'page':{},'data':{}};
	loadProps = [];
	/*模板
	loadProps =[{url:'',
	parameterMap:{},
	bindId:'#aa',
	refName:'classifys'
	}]*/
	function formSubmit(config) {
		var url = config.url,
		form = config.form,
		successHandle = config.successHandle,
		isMultipart = config.isMultipart,
		checkHandle = config.checkHandle,
		data = config.data;
		//模板
		/*
		return formSubmit({
			url: "user/login.do",
			form: "loginForm",
			successHandle:function(data){
				window.location.href = "user/index.do";
			},
			isMultipart:false,
			checkHandle:function(){return true;}
		});
		*/
		//如果form为null这则不传参数
		if(data == undefined){
			data = {}
		}
		if(typeof(form)=="string"){
			form = document.getElementById(form);
		}
		
		var processData = true;
		var contentType = "application/x-www-form-urlencoded";
		if(form != undefined){
			//两种选择提交方式
			data = $(form).serialize();
			if(isMultipart != undefined && isMultipart == true){
				data = new FormData(form);
				processData = false;
				contentType = false;
			}
		}
		
		//校验表单
		var checkFlag = true;
		if(checkHandle != undefined){
			checkFlag = checkHandle(form);
		}
		if(!checkFlag){
			return false;
		}
		//模板
		/* 
		for (var index = 0; index < form.length; index++) {
			if (form[index].value == "") {
				if(form[index].tagName!="BUTTON"){
					alert("有部分选项未填！");
					return false;
				}
			}
		}
		*/
		
		$.ajax({
			url: url,
			data: data,
			type: 'post', 
			async: false,
			//FormData上传时要加这两个配置项，不然会报错
	        processData: processData,
	        contentType: contentType,
	        dataType:'json',
			success:function(result){
				if(result.code==0){
					successHandle(result.data);
				}else if(result.data.message != undefined){
					alert(result.data.message);
				}else{
					alert("访问服务器时出现异常！");
				}
			},
			error:function(){
				alert("请求服务器失败!");
 			}
		});
		return false;
	}
	
	//初始化数据
	/*$(function(){
		alert(1);
		if(loadProps.length != 0){
			//绑定vue
			new Vue({
				el: "#classSelect",
				data: globalData
			});
			//更新数据
			loadData();
		}
		alert(2);
	});
	模板
	[{url:'',
	parameterMap:{},
	refName:'classifys'
	}]
	
	//更新数据
	function loadData(){
		for(var i = 0; i < loadProps.length; i++){
			formSubmit({
				url: loadProps[i].url,
				data: loadProps[i].parameterMap,
				successHandle: function(data){
					globalData.data[loadProps[i].refName] = data[loadProps[i].refName];
				}
			});
		}
	}*/
	
	
	
	
	
	
	
	
	
	//获取url上的参数
	function getQueryString(name) { 
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i"); 
        var r = window.location.search.substr(1).match(reg); 
        if (r != null) return unescape(r[2]); 
        return null; 
    } 