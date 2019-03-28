<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%>
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
<script type="text/javascript">
	if('${sessionScope.existUser.root}'==0){
		window.location.href = "base/goto/user/index.html";
	}
</script>
<title>管理员</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<meta name="viewport" content="width=device-width, initial-scale=1.0" />

<link href="assets/css/bootstrap.min.css" rel="stylesheet" />
<link rel="stylesheet" href="assets/css/font-awesome.min.css" />
<link rel="stylesheet"
	href="http://fonts.googleapis.com/css?family=Open+Sans:400,300" />
<link rel="stylesheet" href="assets/css/ace.min.css" />
<link rel="stylesheet" href="assets/css/ace-rtl.min.css" />
<link rel="stylesheet" href="assets/css/ace-skins.min.css" />

<script src="assets/js/ace-extra.min.js"></script>
<script type="text/javascript">
	try {
		ace.settings.check('navbar', 'fixed');
	} catch (e) {
	}
</script>

<script type="text/javascript">
	try {
		ace.settings.check('sidebar', 'fixed');
	} catch (e) {
	}
</script>
<script type="text/javascript">
	try {
		ace.settings.check('sidebar', 'collapsed');
	} catch (e) {
	}
</script>


</head>

<body>
	<div class="navbar navbar-default" id="navbar">


		<!-- 页面头 -->
		<div class="navbar-container" id="navbar-container"
			style="height: 50px;">
			<div class="navbar-header pull-left">
				<a href="#" class="navbar-brand"> <small> <i
						class="icon-leaf"></i> 宠物旅店管理系统
				</small>
				</a>
				<!-- /.brand -->
			</div>
			<!-- /.navbar-header -->

			<div class="navbar-header pull-right" role="navigation">
				<ul class="nav ace-nav">

					<li class="light-blue"><a data-toggle="dropdown" href="#"
						class="dropdown-toggle"><img class="nav-user-photo"
							src="assets/avatars/user.jpg" alt="Jason's Photo" />
					<span
							class="user-info"> <small>欢迎,</small>${sessionScope.existUser.name }
						</span> <i class="icon-caret-down"></i>
					</a>

						<ul
							class="user-menu pull-right dropdown-menu dropdown-yellow dropdown-caret dropdown-close">

							<li class="divider"></li>

							<li><a href="base/goto/login.html"> <i class="icon-off"></i>
									退出登录
							</a></li>
							<li><a href="base/goto/login.html"> <i class="icon-off"></i>
									修改密码
							</a></li>
						</ul></li>
				</ul>
				<!-- /.ace-nav -->
			</div>
			<!-- /.navbar-header -->
		</div>
		<!-- /.container -->
	</div>

	<!-- 菜单栏 -->
	<div class="main-container" id="main-container">

		<div class="main-container-inner">
			<a class="menu-toggler" id="menu-toggler" href="#"> <span
				class="menu-text"></span>
			</a>

			<div class="sidebar" id="sidebar">

				<div class="sidebar-shortcuts" id="sidebar-shortcuts">
					<div class="sidebar-shortcuts-large" id="sidebar-shortcuts-large">
						<p class="btn btn-success">
							菜单
							<!-- 	预留图标 -->
						</p>

					</div>


				</div>

				<!--   菜单栏详情 -->
				<ul class="nav nav-list">
					<li><a class="dropdown-toggle"> <i class="icon-tag"></i> <span
							class="menu-text"> 饲养员信息管理 </span> <b
							class="arrow icon-angle-down"></b>
					</a>

						<ul class="submenu">
							<li><a href="base/goto/common/selectInforList.html" target="myiframe"> <i
									class="icon-plus"></i> 查看饲养员
							</a></li>

							<li><a href="base/goto/common/viewAddInfor.html" target="myiframe"> <i
									class="icon-eye-open"></i> 添加饲养员
							</a></li>
						</ul></li>

					<li><a href="base/goto/admin/selectFoodCategoryList.html" target="myiframe" class="dropdown-toggle"> <i class="icon-desktop"></i>
							<span class="menu-text"> 食品信息管理 </span> <b
							class="icon-double-angle-right"></b>
					</a></li>
					
					<li><a class="dropdown-toggle"> <i class="icon-edit"></i>
							<span class="menu-text"> 房间管理 </span> <b
							class="arrow icon-angle-down"></b>
					</a>
							<ul class="submenu">
								<li><a href="base/goto/admin/viewAddRoom.html" target="myiframe"> <i
										class="icon-plus"></i> 添加房间
								</a></li>
								
								<li><a href="base/goto/admin/selectRoomList.html" target="myiframe"> <i
										class="icon-eye-open"></i> 查看房间
								</a></li>
							</ul></li>
						</li>

					<li><a class="dropdown-toggle"> <i class="icon-edit"></i>
							<span class="menu-text"> 宠物管理 </span> <b
							class="arrow icon-angle-down"></b>
					</a>

						<ul class="submenu">
							<li><a href="base/goto/admin/selectPetCategoryList.html" target="myiframe"> <i
									class="icon-double-angle-right"></i> 宠物类型
							</a></li>
							<li><a class="dropdown-toggle"> <i class="icon-pencil"></i>
									宠物信息 <b class="arrow icon-angle-down"></b>
							</a>

								<ul class="submenu">
									<li><a href="base/goto/admin/viewAddPet.html" target="myiframe"> <i
											class="icon-plus"></i> 添加宠物
									</a></li>
									
									<li><a href="base/goto/admin/selectPetList.html" target="myiframe"> <i
											class="icon-eye-open"></i> 宠物查看
									</a></li>
								</ul></li>
								
						</ul></li>
					
					
					<li><a class="dropdown-toggle"> <i class="icon-edit"></i>
							<span class="menu-text"> 订单管理 </span> <b
							class="arrow icon-angle-down"></b>
					</a>

						<ul class="submenu">
							<li><a href="base/goto/admin/selectOrderList.html" target="myiframe"> <i
									class="icon-double-angle-right"></i> 查看订单
							</a></li>

						</ul></li>

				</ul>
				<!--   菜单栏详情 -
			
				<!--   菜单栏隐藏按钮 -->
				<div class="sidebar-collapse" id="sidebar-collapse">
					<i class="icon-double-angle-left"
						data-icon1="icon-double-angle-left"
						data-icon2="icon-double-angle-right"></i>
				</div>
				<!--   菜单栏隐藏按钮 -->


			</div>

			<!--   主页面 -->
			<div class="main-content" style="width: 85%;">

				<iframe style="text-align:center;" name="myiframe" frameborder="0"
					width="100%" height="100%" src="base/goto/admin/selectOrderList.html"></iframe>
			</div>
			<!-- /.main-content -->


		</div>


	</div>
	<!-- /.main-container -->


	<script type="text/javascript">
		window.jQuery
				|| document
						.write("<script src='assets/js/jquery-2.0.3.min.js'>"
								+ "<"+"/script>");
	</script>


	<script type="text/javascript">
		if ("ontouchend" in document)
			document
					.write("<script src='assets/js/jquery.mobile.custom.min.js'>"
							+ "<"+"/script>");
	</script>
	<script src="assets/js/bootstrap.min.js"></script>
	<script src="assets/js/typeahead-bs2.min.js"></script>


	<script src="assets/js/ace-elements.min.js"></script>
	<script src="assets/js/ace.min.js"></script>



	<script type="text/javascript">
		jQuery(function($) {
			$('#loading-btn').on(ace.click_event, function() {
				var btn = $(this);
				btn.button('loading');
				setTimeout(function() {
					btn.button('reset');
				}, 2000);
			});

			$('#id-button-borders').attr('checked', 'checked').on('click',
					function() {
						$('#default-buttons .btn').toggleClass('no-border');
					});
		});
	</script>

</body>
</html>