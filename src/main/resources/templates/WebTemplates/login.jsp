<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">
<title>用户登录</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">

<link href="assets/css/bootstrap.min.css" rel="stylesheet" />
<link rel="stylesheet" href="assets/css/font-awesome.min.css" />
<script type="text/javascript" src="assets/js/jquery-1.10.2.min.js"></script>
<script type="text/javascript" src="common/util/netutil.js"></script>

<script type="text/javascript">
	function loginForm(){
		return formSubmit({
			url: "user/login.html",
			form: "loginForm",
			successHandle:function(data){
				window.location.href = "user/index.html";
			},
			isMultipart:false,
			checkHandle:function(){return true;}
		});
	}
</script>




<link rel="stylesheet" href="assets/css/ace.min.css" />
<link rel="stylesheet" href="assets/css/ace-rtl.min.css" />
</head>

<body class="login-layout">
	<div class="main-container">
		<div class="main-content">
			<div class="row">
				<div class="col-sm-10 col-sm-offset-1">
					<div class="login-container">
						<div class="center">
							<h1>
								<i class="icon-leaf green"></i> <span class="red">宠物旅店管理系统</span>
								<span class="white"></span>
							</h1>
						</div>

						<div class="space-6"></div>

						<div class="position-relative">
							<div id="login-box"
								class="login-box visible widget-box no-border">
								<div class="widget-body">
									<div class="widget-main">
										<h4 class="header blue lighter bigger">
											<i class="icon-coffee green"></i> 用户登录
										</h4>

										<div class="space-6"></div>

										<form action="user/login.html" method="post" id="loginForm"
											onsubmit="return loginForm()">
											<fieldset>
												<label class="block clearfix"> <span
													class="block input-icon input-icon-right"> <input
														type="text" name="username" class="form-control"
														placeholder="账号" /> <i class="icon-user"></i>
												</span>
												</label> <label class="block clearfix"> <span
													class="block input-icon input-icon-right"> <input
														type="password" name="password" class="form-control"
														placeholder="密码" /> <i class="icon-lock"></i>
												</span>
												</label>
												<!-- <label class="block clearfix"> <span
													class="block input-icon input-icon-right"> -->



												<div class="space"></div>

												<div class="clearfix">
													<label class="inline"> <input type="checkbox"
														class="ace" /> <span class="lbl"> 记住用户名</span>
													</label> <input type="submit"
														class="width-35 pull-right btn btn-sm btn-primary"
														value="登录">
												</div>

												<div class="space-4"></div>
											</fieldset>
										</form>

									</div>
									<!-- /-->

									<div class="toolbar clearfix">
										<div>
											<a href="#" onclick="show_box('forgot-box'); return false;"
												class="forgot-password-link"> <i class="icon-arrow-left"></i>
												注册
											</a>
										</div>

									</div>
								</div>
								<!-- /widget-body -->
							</div>
							<!-- /login-box -->

							<div id="forgot-box" class="forgot-box widget-box no-border">
								<div class="widget-body">
									<div class="widget-main">
										<h4 class="header green lighter bigger">
											<i class="icon-key"></i> 注册
										</h4>

										<div class="space-6"></div>
										
										<center><span id="message"><font style="color:green;">&nbsp;</font></span></center>
										<p>输入账号</p>

										<form id="registId" action="http://baidu.com">
											<fieldset>
												<label class="block clearfix"> <span
													class="block input-icon input-icon-right"> <input
														type="text" class="form-control" name="username" placeholder="用户名" />
														<i class="icon-envelope"></i>
												</span>
												</label>
												<p>真实姓名</p>
												<label class="block clearfix"> <span
													class="block input-icon input-icon-right"> <input
														type="text" class="form-control" name="name" placeholder="姓名" />
														<i class="icon-envelope"></i>
												</span>
												</label>
												<p>输入密码</p>
												<label class="block clearfix"> <span
													class="block input-icon input-icon-right"> <input
														type="password" class="form-control" name="password" placeholder="密码" />
														<i class="icon-envelope"></i>
												</span>
												</label>
												<p>选择权限</p>
												<label class="block clearfix"> <span
													class="block input-icon input-icon-right"> 
														<select name="root">
															<option value="0">普通用户</option>
															<option value="1">管理员</option>
															<option value="2">饲养员</option>
														</select>
														<i class="icon-envelope"></i>
												</span>
												</label>
												<div class="clearfix">
													<input type="hidden" name="root" value="1">
													<button type="button" onclick="register()"
														class="width-35 pull-right btn btn-sm btn-danger">
														<i class="icon-lightbulb"></i> 注册
													</button>
												</div>
											</fieldset>
										</form>
									</div>
									
									<script type="text/javascript">
										function register(){
											return formSubmit({
												url: "user/regist.html",
												form: "registId",
												successHandle:function(data){
													alert('注册成功！');
													window.location.href = "user/index.html";
												},
												isMultipart:false,
												checkHandle:function(){return true;}
											});
										}
									</script>
									<!-- /widget-main -->

									<div class="toolbar center">
										<a href="#" onclick="show_box('login-box'); return false;"
											class="back-to-login-link"> 返回登录 <i
											class="icon-arrow-right"></i>
										</a>
									</div>
								</div>
								<!-- /widget-body -->
							</div>
							<!-- /forgot-box -->


							<!-- /position-relative -->
						</div>
					</div>
					<!-- /.col -->
				</div>
				<!-- /.row -->
			</div>
		</div>
		<!-- /.main-container -->

		<!-- basic scripts -->

		<!--[if !IE]> -->

		<!-- <![endif]-->

		<!--[if IE]>
<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
<![endif]-->

		<!--[if !IE]> -->

		<script type="text/javascript">
			window.jQuery
					|| document
							.write("<script src='assets/js/jquery-2.0.3.min.js'>"
									+ "<"+"/script>");
		</script>

		<!-- <![endif]-->

		<!--[if IE]>
<script type="text/javascript">
 window.jQuery || document.write("<script src='assets/js/jquery-1.10.2.min.js'>"+"<"+"/script>");
</script>
<![endif]-->
		<script type="text/javascript">
			
		</script>

		<script type="text/javascript">
			if ("ontouchend" in document)
				document
						.write("<script src='assets/js/jquery.mobile.custom.min.js'>"
								+ "<"+"/script>");
		</script>

		<!-- inline scripts related to this page -->

		<script type="text/javascript">
			function show_box(id) {
				jQuery('.widget-box.visible').removeClass('visible');
				jQuery('#' + id).addClass('visible');
			}
		</script>
		<div style="display:none">
		</div>
</body>
</html>
</html>
