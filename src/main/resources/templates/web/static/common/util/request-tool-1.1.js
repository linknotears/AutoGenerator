	//模板：<script type="text/javascript" src="common/util/request-tool-1.1.js"></script>
	addScript("/common/util/jquery-1.10.2.min.js");
	addScript("/common/util/vue.min.js");
	addScript("/js/plugins/layer/layer.js");
	function addScript(url){
		document.write("<script language='javascript' src='"+url+"'></script>");
	}
	function addMoudle(url){
		document.write("<script type='module' src='"+url+"'></script>");
	}
	//通过索引复制的时候需用用Vue.set(target,i,target[i])通知vue
	//全局变量
	vue = {'items':[],'item':{},'page':{},'data':{},params:{}};
	vueMap = {};
	loadUrls = [];
	loadEls = []; 
	loadParameters = [];
	showloading = true;
	//定义全局变量
	function define(defineData){
		Object.assign(vue,defineData);
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
					'userno': vue.params.idcard.value,
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
			//console.log("currentIndex="+ this.squNo);
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
			if(index>-1){
				let calls = this.deferreds[index].calls;
				if(calls){
					calls.push(call);
				}else{
					this.deferreds[index].calls = [];
					this.deferreds[index].calls.push(call);
				}
			}else{
				call();
			}
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
						console.log("删除deferred-"+x);
						this.deferreds.splice(x,1);
					}
				}
				//链式解析
				if(this.deferreds[0]){
					this.squNo = 0;
					this.deferreds[this.squNo].resolve();
				}
			}
		}
	};
	
	function request(config) {
		var _this = this;
		/*
			config.url;
			config.error;
			config.multipart;
			config.check;
			config.data;
			config.form;
			config.success;
		*/
		config.async = config.async==undefined? true : config.async;
		config.prep = config.prep==undefined? true : config.prep;//preprocess是否预处理
		config.showloading = config.showloading==undefined? true : config.showloading;
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
		if(config.data == undefined){
			config.data = {}
		}
		if(typeof(config.form)=="string"){
			config.form = document.getElementById(config.form);
		}
		
		config.processData = true;
		//解决乱码问题
		config.contentType = "application/x-www-form-urlencoded; charset=utf-8";
		//标签数组
		var els = [];
		if(config.form != undefined){
			//如果form和data同时存在则合并参数
			if(config.data != undefined){
				for(var x in config.data){
					var el = $("<input type='hidden' name='"+x+"' value='"+config.data[x]+"'/>");
					els.push(el);
					$(config.form).append(el);
				}
			}
			//两种选择提交方式
			config.data = $(config.form).serialize();
			if(config.multipart != undefined && config.multipart == true){
				config.data = new FormData(config.form);
				config.processData = false;
				config.contentType = false;
			}
		}

		//校验表单
		var checkFlag = true;
		if(config.check != undefined){
			checkFlag = config.check(config.form);
		}
		if(!checkFlag){
			return false;
		}
		function ajaxSubmit(preRes){
			let index = undefined;
			if(typeof(layer)!="undefined" && config.async && config.showloading) {
				index = layer.load(1);//显示加载图标
			}
			$.ajax({
				url: config.url,
				data: config.data,
				type: config.type? config.type : 'post',
				//async: false,//异步阻塞容易卡死
				//FormData上传时要加这两个配置项，不然会报错
		        processData: config.processData,
		        contentType: config.contentType,
		        dataType: 'json',
				success: function(res){
					//判断预处理
					if(config.prep==false){
						config.success(res,preRes);
					}else if(res.code==0){
						config.success(res.data,preRes);
					}else if(res.msg != null && res.msg != undefined){
						if(typeof(layer)!="undefined"){
							layer.msg(res.msg);
						}else{
							alert(res.msg);
						}
						if(config.error){
							config.error(res,preRes);
						}
					}else{
						if(typeof(layer)!="undefined"){
							layer.msg("访问服务器时出现异常！");
						}else{
							alert("访问服务器时出现异常！");
						}
						if(config.error){
							config.error(res,preRes);
						}
					}

					//链式执行操作
					if(config.async==false) {
						//获取当前deferred
						let current = deferObj.getCurrent();
						//deferred.call回调
						if(current){
							if(current.calls){
								for(let x in current.calls){
									console.log("执行deferreds["+deferObj.squNo+"].done[" + x + "]方法回调");
									current.calls[x](res,preRes);
								}
							}
						}
						//解析下一个deferred
						let deferred = deferObj.next();
						if(deferred){
							deferred.resolve(res);
							console.log("解析下一个deferred");
						}else{
							console.log("deferreds同步链执行完成");
						}
					}
					
					//关闭加载图标
					if(typeof(layer)!="undefined" && config.async) {
						layer.close(index);//关闭加载图标
					}
				},
				traditional: true,
				error:function(res){
					if(typeof(layer)!="undefined"){
						layer.msg("请求服务器失败!");
					}else{
						alert("请求服务器失败!");
					}
					if(config.error){
						config.error(res);
					}
					//解析下一个deferred
					let deferred = deferObj.next();
					if(deferred){
						deferred.resolve(res);
						console.log("本次请求失败,解析下一个deferred");
					}else{
						console.log("本次请求失败,deferreds同步链执行完成");
					}

					//关闭加载图标
					if(typeof(layer)!="undefined" && config.async &&  config.showloading) {
						layer.close(index);//关闭加载图标
					}
	 			}
			});
		}
		//判断是否加入提交序列
		if(config.async == true){
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
						data: vue,
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
							
							formatDateTime: function(time,isShowSecond = true){
								var formatStr = "yyyy-MM-dd hh:mm";
								if(isShowSecond){
									formatStr = "yyyy-MM-dd hh:mm:ss";
								}
								return this.dateFormat(time, formatStr)
							},
							
							formatDate: function(time) {
								if((time+"").indexOf("-") > -1){
									return time.substring(0,10);
								}else{
									return this.dateFormat(time, "yyyy-MM-dd")
								}
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
									vue[invertName] = [];
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
		if(typeof(layer)!="undefined" && showloading){
			var index = layer.load(1);//显示加载图标
		}
		for(var i = 0; i < loadUrls.length; i++){
			request({
				url: loadUrls[i].url,
				type: loadUrls[i].type==undefined? loadUrls[i].type : 'post',
				data: loadUrls[i].data==undefined? {} : loadUrls[i].data,
				prep: loadUrls[i].prep,
				async: false,
				success: function(data){
					console.log("loadIndex=" + loadIndex);
					if(loadUrls[loadIndex].refNames){
						for(var j = 0; j < loadUrls[loadIndex].refNames.length; j++) {
							/*
							let refData1 = vue.data[loadUrls[loadIndex].refNames[j]];
							let refData2 = data[loadUrls[loadIndex].refNames[j]];
							console.log("refData1="+refData1)
							if(refData1){
								if(refData1.isArray){
									//清空原数组
									refData1.splice(0,refData1.length);
									Object.assign(refData1,refData2);
								}else{
									//清空对象
									for(var key in refData1) {
										delete refData1[key];
									}
									console.log("pre refData1="+JSON.stringify(refData1));
									Object.assign(refData1,refData2);
									console.log("after refData1="+JSON.stringify(refData1))
								}
							}else{}*/
							vue.data[loadUrls[loadIndex].refNames[j]] = data[loadUrls[loadIndex].refNames[j]];
						}
					}
					if(loadUrls[loadIndex].success != undefined) {
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
		deferObj.done(function(){
			if(typeof(layer) != "undefined" && typeof(index) != "undefined" && showloading) {
				layer.close(index);
			}
		})
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
		vue.page = data;
		
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
		vue.page["url"] = url;
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
			vue.params[key] = {'value':value,'isDefault':isDefault};
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