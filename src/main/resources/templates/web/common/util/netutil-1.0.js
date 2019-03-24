	//模板：<script type="text/javascript" src="common/util/netutil.js"></script>
	addScript("common/util/jquery-1.10.2.min.js");
	addScript("common/util/vue.min.js");
	function addScript(url){
		document.write("<script language=javascript src="+url+"></script>");
	}
	//全局变量
	globalData = {'items':[],'item':{},'page':{},'data':{},params:{}};
	loadUrls = [];
	loadEls = [];
	loadParameters = [];
	
	/*模板
	loadUrls =[{url:'',
	data:{},
	refName:'classifys'
	}];
	loadEls = ['#element1','#element2'];
	loadParameters = ["key",['key','default']]
	*/
	function request(config) {
		//模板
		/*
		return request({
			url: "user/login.do",
			form: "loginForm",
			successHandle:function(data){
				window.location.href = "user/index.do";
			},
			isMultipart:false,
			checkHandle:function(){return true;}
		});
		*/
		var url = config.url,
		form = config.form,
		successHandle = config.successHandle,
		isMultipart = config.isMultipart,
		checkHandle = config.checkHandle,
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
		
		data = config.data;
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

	/*模板
	loadUrls =[{url:'',
	data:{},
	refNames:['classifys','pageData']
	}];
	loadEls = ['#element1','#element2'];
	loadParameters = ["key",['key','default']]
	*/
	//渲染数据
	function applyEls(){
		if(loadEls.length != 0){
			for(var i = 0; i < loadEls.length; i++){
				//绑定vue(同一个id只有首次渲染起作用)
				new Vue({
					el: loadEls[i],
					data: globalData
				});
			}
		}
	}
	
	//更新数据
	function loadData(){
		for(var i = 0; i < loadUrls.length; i++){
			request({
				url: loadUrls[i].url,
				data: loadUrls[i].data==undefined? {} : loadUrls[i].data,
				successHandle: function(data){
					for(var j = 0; j < loadUrls[i].refNames.length; j++){
						if(loadUrls[i].refNames[j] != 'pageData'){
							globalData.data[loadUrls[i].refNames[j]] = data[loadUrls[i].refNames[j]];
						}else{
							globalData.page = data[loadUrls[i].refNames[j]];
						}
					}
					if(loadUrls[i].successHandle != undefined){
						loadUrls[i].successHandle(data);
					}
				}
			});
		}
	}
	
	//加载参数
	function loadParameter(){
		for(var i = 0; i < loadParameters.length; i++){
			var key = null;
			var value = null;
			if(typeof loadParameters[i] != 'string'){
				key = loadParameters[i][0];
				value = getParameter(loadParameters[i][0],loadParameters[i][1]);
			}else{
				key = loadParameters[i];
				value = getParameter(loadParameters[i]);
			}
			globalData.params[key] = {'value':value,'isDefault':isDefault};
		}
	}
	
	
	function init(){
		//加载页功能数据，如果存在的话
		
		//加载参数
		loadParameter();
		//加载数据
		loadData();
		//渲染标签
		applyEls();
	}
	
	
	
	
	//获取url上的参数
	var isDefault = false;
	function getParameter(name,defaultParam) { 
		isDefault = false;
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i"); 
        var r = window.location.search.substr(1).match(reg); 
        if (r != null){
        	var param = unescape(r[2]); 
        	if(param != ''){
        		return param
        	}
        }
        isDefault = true;
        if(defaultParam==undefined){
        	return null;
        }
        return defaultParam;
    } 