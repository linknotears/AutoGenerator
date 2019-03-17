	function checkform(url,form,successMethod) {
		if(typeof(form)=="string"){
			this.form = document.getElementById(form);
		}else{
			this.form = form
		}
		
		//input非空校验
		/* for (var index = 0; index < form.length; index++) {
			if (form[index].value == "") {
				alert("Empty input");
				return false;
			}
		} */
		var isSuccess = false;
		$.ajax({
			url:url,
			data:$(this.form).serialize(),
			type: 'post', 
			sync:false,
			success:function(result){
				if(result.code==0){
					successMethod(result.data)
					isSuccess = true;
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
		return isSuccess;
	}