	//模板：<script type="text/javascript" src="common/util/netutil.js"></script>
	addScript("common/util/jquery-1.10.2.min.js");
	addScript("common/util/vue.min.js");
	function addScript(url){
		document.write("<script language=javascript src="+url+"></script>");
	}
	//通过索引复制的时候需用用Vue.set(target,i,target[i])通知vue
	//全局变量
	globalData = {'items':[],'item':{},'page':{},'data':{},params:{}};
	loadUrls = [];
	loadEls = [];
	loadParameters = [];
	//params.idcard.isDefault
	/*模板
	//加载参数
	//ready函数没写的时候vue找不到id但，也不会报错
	//凡是用到id时都用id作为传参名
	$(function(){
		loadParameters = [['idcard','${sessionScope.existUser.idcard}']];
		loadParameter();
		//加载data
		loadUrls =[
			{
				url:"userAnswer/findList.html",
				data: {
					'userno': globalData.params.idcard.value,
					'typeno': 1
				},
				refNames:['userAnswers']
			},
			{
				url:"question/findList.html",
				data: {'typeno': 1},
				refNames:['questions']
			}
		];
		//加载标签
		loadEls = ['#mainData'];
		//初始化数据
		init();
	});
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
		//解决乱码问题
		var contentType = "application/x-www-form-urlencoded; charset=utf-8";
		if(form != undefined){
			//如果form和data同时存在则合并参数
			if(config.data != undefined){
				for(var x in config.data){
					$(form).append("<input type='hidden' name='"+x+"' value='"+config.data[x]+"'/>");
				}
			}
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
				}else if(result.message != null && result.message != undefined){
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
		//loadParameter();
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