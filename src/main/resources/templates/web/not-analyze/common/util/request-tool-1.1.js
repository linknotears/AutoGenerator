	//模板：<script type="text/javascript" src="common/util/request-tool-1.1.js"></script>
	addScript("/common/util/jquery-1.10.2.min.js");
	addScript("/common/util/vue.min.js");
	function addScript(url){
		document.write("<script language='javascript' src='"+url+"'></script>");
	}
	function addMoudle(url){
		document.write("<script type='module' src='"+url+"'></script>");
	}
	//通过索引复制的时候需用用Vue.set(target,i,target[i])通知vue
	//全局变量
	globalData = {'items':[],'item':{},'page':{},'data':{},params:{}};
	vueMap = {};
	loadUrls = [];
	loadEls = [];
	loadParameters = [];
	//定义全局变量
	function define(defineData){
		Object.assign(globalData,defineData);
	}
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
	var deferObj = {
		squNo: 0,
		deferreds: [],
		current: null,
		getCurrent: function(){
			console.log("this.squNo="+ this.squNo);
			this.current = this.deferreds[this.squNo] || null;
			return this.current;
		},
		next: function(){
			this.current = this.deferreds[this.squNo] || null;
			let next = null;
			if(this.current){
				this.squNo++;
				next = this.deferreds[this.squNo] || null;
			}
			return next;
		},
		previous: function(){
			this.current = this.deferreds[this.squNo-1] || null;
			if(this.current){
				this.squNo--;
			}
			return this.current;
		},
		add: function(deferred){
			this.deferreds.push(deferred);
		},
		/*getFirst: function(){
			if(!this.first){
				this.first = $.Deferred();
			}
			return this.first;
		},*/
		done: function(call){
			let index = this.deferreds.length-1;
			this.deferreds[index].call = call;
		},
		clear: function(){
			this.deferreds = []
			this.squNo = 0;
			this.current = null;
		},
		start: function(){
			console.log("this.deferreds.length="+this.deferreds.length);
			if(this.deferreds.length != 0){
				//把已经解析的删除掉
				for(var x in this.deferreds){
					//获取状态
					var state = this.deferreds[x].state();
					if(state=="resolved"){
						console.log("删除deferred"+x);
						this.deferreds.splice(x,1);
					}
				}
				//链式解析
				if(this.deferreds[0]){
					this.deferreds[0].resolve();
				}
			}
		}
	};
	
	function request(config) {
		//模板
		/*
		return request({
			url: "user/login.do",
			form: "loginForm",
			success:function(data){
				window.location.href = "user/index.do";
			},
			multipart:false,
			check:function(){return true;}
		});
		*/
		var _this = this;
		this.url = config.url;
		this.data = config.data;
		this.form = config.form;
		this.success = config.success;
		this.async = config.async==undefined? true : config.async;
		this.error = config.error;
		this.multipart = config.multipart;
		this.check = config.check;
		this.prep = config.prep==undefined? true : config.prep;//preprocess是否预处理
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
		//标签数组
		var els = [];
		if(form != undefined){
			//如果form和data同时存在则合并参数
			if(config.data != undefined){
				for(var x in config.data){
					var el = $("<input type='hidden' name='"+x+"' value='"+config.data[x]+"'/>");
					els.push(el);
					$(form).append(el);
				}
			}
			//两种选择提交方式
			data = $(form).serialize();
			if(multipart != undefined && multipart == true){
				data = new FormData(form);
				processData = false;
				contentType = false;
			}
		}
		
		//校验表单
		var checkFlag = true;
		if(check != undefined){
			checkFlag = check(form);
		}
		if(!checkFlag){
			return false;
		}
		function ajaxSubmit(preRes){
			$.ajax({
				url: url,
				data: data,
				type: 'post', 
				//async: false,//异步阻塞容易卡死
				//FormData上传时要加这两个配置项，不然会报错
		        processData: processData,
		        contentType: contentType,
		        dataType: 'json',
				success: function(res){
					//判断预处理
					if(prep==false){
						_this.success(res,preRes);
					}else if(res.code==0){
						_this.success(res.data,preRes);
					}else if(res.message != null && res.message != undefined){
						alert(res.message);
						if(_this.error){
							_this.error(res,preRes);
						}
					}else{
						alert("访问服务器时出现异常！");
						if(_this.error){
							_this.error(res,preRes);
						}
					}
					
					//获取当前deferred
					let current = deferObj.getCurrent();
					console.log("current="+current);
					//deferred.call回调
					if(current){
						if(current.call){
							console.log("执行done方法回调");
							current.call(res,preRes);
						}
					}
					//解析下一个deferred
					let deferred = deferObj.next();
					if(deferred){
						deferred.resolve(res);
						console.log("解析下一个deferred");
					}else{
						console.log("deferreds全部解析完成");
					}
				},
				traditional: true,
				error:function(res){
					alert("请求服务器失败!");
					if(_this.error){
						_this.error(res);
					}
					deferred.resolve(res);
	 			}
			});
		}
		//判断是否加入提交序列
		if(async == true){
			ajaxSubmit();
		}else{
			let deferred = $.Deferred();
			deferObj.add(deferred);
			$.when(deferred).done( function(res){
				ajaxSubmit(res);
			});
	    }
		
		//删除刚提交使用的临时input标签
		if(els.length>0){
			for(var x in els){
				els[x].remove();
			}
		}
		
		
		return false;
	}

	
	//渲染数据
	function applyEls(){
		if(loadEls.length != 0){
			for(var i = 0; i < loadEls.length; i++){
				//绑定vue(同一个id只有首次渲染起作用)
				Object.assign(vueMap,
				{
					[loadEls[i]]: new Vue({
						el: loadEls[i],
						data: globalData,
						methods:{
							dateFormat: function(date, fmt) {
								//如果等于空，则使用现在的时间
								if(!date){// "",null,undefined,NaN
									date = new Date();
								}else if(!(date instanceof Date)){
									date = new Date(date);
								}
								if(!fmt){
									fmt = "yyyy-MM-ddThh:mm";
								}
								if (/(y+)/.test(fmt)) {
								  fmt = fmt.replace(RegExp.$1, (date.getFullYear() + '').substr(4 - RegExp.$1.length))
							  	}
							  	let o = {
							  			'M+': date.getMonth() + 1,
							  			'd+': date.getDate(),
							  			'h+': date.getHours(),
							  			'm+': date.getMinutes(),
							  			's+': date.getSeconds()
							  	}
								  for (let k in o) {
									  if (new RegExp(`(${k})`).test(fmt)) {
										  let str = o[k] + ''
										  fmt = fmt.replace(RegExp.$1, (RegExp.$1.length === 1) ? str : this.padLeftZero(str))
									  }
								  }
								  return fmt
							},
							
							formatDateTime: function(time,isShowSecond){
								var formatStr = "yyyy-MM-dd hh:mm";
								if(isShowSecond){
									formatStr = "yyyy-MM-dd hh:mm:ss";
								}
								return this.dateFormat(time, formatStr)
							},
							
							formatDate: function(time) {
								return this.dateFormat(time, "yyyy-MM-dd")
							},
							
							padLeftZero: function(str) {
							  return ('00' + str).substr(str.length)
							},
							//数组搜索对象
							findObj: function(arr,condition,refName){
								var conditionCount = Object.getOwnPropertyNames(condition).length;
								for(var x in arr){
									var arrObjCount = 0;
									var arrObj = arr[x];
									for(var key in condition){
										var arrValue = arrObj[key];
										var value = condition[key];
										if(value == arrValue){
											arrObjCount++;
										}
									}
									//如果条件都符合则返回对象
									if(arrObjCount == conditionCount){
										//添加vue参照
										if(!refName){
											//清空对象属性
											for(let x in this['item']){
												delete this['item'][x];
											}
											//item的值被改变了，重新渲染视图导致无限循环
											Object.assign(this.item,arrObj);
										}else{
											//初始化变量
											if(!this[refName]){
												this[refName] = {};
											}else{
												//清空对象属性
												for(let x in this[refName]){
													delete this[refName][x];
												}
											}
											Object.assign(this[refName], arrObj);
										}
										return arrObj;
									}
								}
								//如果没找这则返回null
								return null;
							},
							invert: function(event,invertName) {
								var checked = event.currentTarget.checked;
								if(checked){
									globalData[invertName] = [];
								}
							}
						}
					})
				});
			}
		}
	}
	
	//更新数据
	function loadData(){
		//记录索引
		loadIndex = 0;
		for(var i = 0; i < loadUrls.length; i++){
			request({
				url: loadUrls[i].url,
				data: loadUrls[i].data==undefined? {} : loadUrls[i].data,
				prep: loadUrls[i].prep,
				async: false,
				success: function(data){
					console.log("loadIndex=" + loadIndex);
					if(loadUrls[loadIndex].refNames){
						for(var j = 0; j < loadUrls[loadIndex].refNames.length; j++){
							globalData.data[loadUrls[loadIndex].refNames[j]] = data[loadUrls[loadIndex].refNames[j]];
						}
					}
					if(loadUrls[loadIndex].success != undefined){
						//this是数组对象
						var success = loadUrls[loadIndex].success;
						success(data);
					}
					loadIndex++;
					if(loadIndex==loadUrls.length){
						applyEls();
					}
				},
				error: function(){
					loadIndex++;
				}
			});
		}
		//启动伪同步提交提交
		deferObj.start();
	}
	/**页分页模板
	 * <ul> 
        <li v-if="page.page>1"><a href="javascript:void(0);" onclick="getGoods(undefined,undefined,1)"> 首页 </a></li> 
        <li v-if="page.page>1"><a href="javascript:void(0);" @click="getGoods(undefined,undefined,page.page-1)"> 上一页 </a></li> 
        <li v-if="page.currentIndex>0"><a href="javascript:void(0);" @click="getGoods(undefined,undefined,page.startPage - 1)"> <<< </a></li> 
        <li v-for="p,i in page.pages" v-if="i <= (page.showPage-1)">
			<a href="javascript:void(0);" @click="getGoods(undefined,undefined,index)">
				<strong :style="page.page==p? 'color:red;' : ''">{{p}}</strong>
			</a>
		</li>
        <li v-if="page.currentIndex<page.maxIndex"><a href="javascript:void(0);" @click="getGoods(undefined,undefined,page.startPage + page.showPage)"> >>> </a></li> 
        <li v-if="page.page<page.totalPage"><a href="javascript:void(0);" @click="getGoods(undefined,undefined,page.page+1)"> 下一页 </a></li> 
        <li v-if="page.page<page.totalPage"><a href="javascript:void(0);" @click="getGoods(undefined,undefined,page.totalPage)"> 最后页 </a></li> 
       </ul>
	 */
	function pageWrap(data,showPage = 8){
		globalData.page = data;
		
		var currentIndex = Math.floor(((data.page-1)*1.0)/showPage);
		var maxIndex = Math.floor((data.totalPage*1.0)/showPage);
		var startPage = showPage*currentIndex + 1;
		//页数组
		var pages = [];
		for(var i = startPage; i<=data.totalPage; i++){
			pages.push(i);
		}
		console.log(`maxIndex=${maxIndex},currentIndex=${currentIndex},startPage=${startPage}`)
		//页分页数据
		data["currentIndex"] = currentIndex;//当前页分页索引
		data["maxIndex"] = maxIndex; //最大页分页索引
		data["showPage"] = showPage; //显示页数
		data["startPage"] = startPage; //当前页开始页数
		data["pages"] = pages;//页数组
		
		var url = window.location.href.split("?")[0];
		globalData.page["url"] = url;
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
		//加载数据
		loadData();
		//渲染标签//还没得到数据就渲染了
		if(loadUrls.length==0){
			applyEls();
		}
	}
	
	
	
	
	//获取url上的参数
	var isDefault = false;
	function getParameter(name,defaultParam) { 
		isDefault = false;
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i"); 
        var r = window.location.search.substr(1).match(reg); 
        if (r != null){
        	//var param = unescape(r[2]); 
        	var param = decodeURI(r[2]); 
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
	
	
	//把json数组中的json的key和value，转换成对应的映射
	//模板
	
	function objArrToMap(array,refKey,refValue){
		var mapping = {};
		for(var x in array){
			var key = this.getField(array[x], refKey);
  			var value = this.getField(array[x],refValue);
			mapping[key] = value;
		}
		return mapping;
	}
	
	//对象中的属性获取方法
	function getField(data, fields) {
		var arr = fields.split('.');

	    for (var x in arr) {
	      if(data != undefined && data != null) {
	        data = data[arr[x]];
	      }
	    }
	    return data;
	}