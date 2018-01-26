<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1" />
	<title>调查问卷</title>
	<link href="${ctx }/css/sur-mobile.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="${ctx }/js/plugs/jquery-ui-1.10.3.custom/js/jquery-1.10.1.js"></script>
	<%--<link rel="stylesheet" href="${ctx }/js/plugs/jquery.mobile-1.4.5/jquery.mobile-1.4.5.min.css">
    <script src="${ctx }/js/plugs/jquery.mobile-1.4.5/jquery.mobile-1.4.5.min.js"></script>--%>
	<link href="${ctx }/js/plugs/font-awesome-4.2.0/css/font-awesome.css" rel="stylesheet">
	<link href="${ctx}/css/weui.min.css" type="text/css" rel="stylesheet" />
	<script type="text/javascript">
		$(document).ready(function(){

		});
	</script>
	<style type="text/css">
		.ui-page {
			background:white;
		}
		.ui-header{
			background-color: red! important;
			text-shadow:0 1px 0 #3D586C! important;
			padding-bottom: 5px;
		}
		.ui-footer{
			color: #3D586C! important;
			background: none! important;
		}
		.ui-header, .ui-footer{
			border: none! important;
		}
		.starRating{
			font-size: 26px;
		}
		.starRating .fa{
			cursor: pointer;
		}
		.starRating .fa-star{
			color: #3388CC;
		}
		.subbtn{
			opacity:1! important;
			color: white;
		}
		.quTitleNum{
			/* position: absolute; */
		}
		.quTitleText{
			/* text-indent: 2em; */
		}
		#dwSurveyNote{
			padding-top: 0px;
		}
		.m_quOrderByUi{
			margin: 5px 0 5px 0;
			padding: 0;
			border: 1px solid #d5d5d5;
			border-radius: 3px;
		}
		.m_quOrderByUi li{
			border-color: #fff;
			font-size: 16px;
			min-height: 41px;
			position: relative;
			padding-right: 45px!important;
			border-bottom: 1px solid #EBEBEB!important;
		}
		.m_orderby_num{
			position: absolute;
			right: 10px;
			top: 55%;
			margin-top: -15px;
			min-width: 26px;
			height: 26px;
			background: #85C8FF;
			color: #fff;
			text-align: center;
			line-height: 26px;
			border-radius: 15px;
			z-index: 100;
			display: none;
		}
		.m_orderby_sel{
			position: absolute;
			top: 0px;
			left: 0px;
			width: 100%;
			height: 100%;
			opacity: 0;
			font-size: 30px;
			z-index: 9999;

			background-color: rgb(248, 248, 248);
			border: 1px solid rgb(166, 166, 166);
			border-image-source: initial;
			border-image-slice: initial;
			border-image-width: initial;
			border-image-outset: initial;
			border-image-repeat: initial;

			display: inline-block;
		}
		.surveyName{
			font-weight: normal;
		}
		.ui-btn-icon-notext:after, .ui-btn-icon-left:after, .ui-btn-icon-right:after{
			display: none;
		}
		.weui-cell_access .weui-cell__ft:after{
			display: none;
		}
		.ui-listview .ui-btn-icon-right .ui-li-count{
			background: none;
			border: none;
			right: 1.2em;
			font-weight: 300;
			text-shadow: none;
			color: #f56363;
		}
		.ui-listview>.ui-li-has-count>.ui-btn-icon-right{
			font-size: 18px;
			line-height: 36px;
			font-weight: 300;
			text-shadow: none;
		}
		.ui-listview>li{
			/*background: white!important;*/
			/*border: none!important;*/
		}
		.ui-listview>li a {
			background: white!important;
			color: black!important;
			border-color: #ddd!important;
		}
		.ui-listview>li a:hover{
			background: red!important;
			color: white!important;
		}
		.weui-navbar__item.weui-bar__item_on{
			color: #dd000e;
			background: white;
		}
		.weui-navbar__item.weui-bar__item_on span{
			padding: 6px;
			border-bottom: 3px solid #dd000e;
			border-radius: 3px;
		}
		.weui-navbar__item:after{
			display: none;
		}
		.weui-navbar__item{
			background: white;
		}
		.weui-cell{
			padding: 20px 15px;
			color: #000000;
		}
		.weui-cells{
			margin-top: 0px;
		}
		*{
			font-family: "Hiragino Sans GB","Microsoft YaHei","微软雅黑",tahoma,arial,simhei,"黑体";
		}
	</style>
</head>
<body>
<input type="hidden" id="surveyId" name="surveyId" value="${survey.id }">
<input type="hidden" name="form-from" value="mobile" >


<div class="container" id="container">

	<div class="page home js_show" style="display: none;">

		<div class="page__hd">
			<%--<h1 class="page__title" style="text-align: center;">
                <img src="./images/logo.png" alt="调查问卷" height="21px">
            </h1>--%>
			<%--<p class="page__desc">WeUI 是一套同微信原生视觉体验一致的基础样式库，由微信官方设计团队为微信内网页和微信小程序量身设计，令用户的使用感知更加统一。</p>--%>
			<div style="padding: 12px;text-align: center;color: white;background: #dd000e;"><h2 class="weui-msg__title" style="font-family:Microsoft YaHei;word-spacing:12px; letter-spacing: 3px;font-weight: 900;font-size: 22px;">调查问卷</h2></div>
		</div>
	</div>

	<div>
		<a href="${ctx}/wenjuan.action?surveyState=1" class="surveyTab" ><span style="letter-spacing: 3px;font-size: 18px;">进行中</span></a>
		<a href="${ctx}/wenjuan.action?surveyState=1" class="surveyTab" ><span style="letter-spacing: 3px;font-size: 18px;">已结束</span></a>
	</div>

	<div class="page navbar js_show">
		<div class="page__bd" style="height: 100%;">
			<div class="weui-tab">
				<div class="weui-navbar">
					<div class="weui-navbar__item" id="tab1">
						<a href="${ctx}/wenjuan.action?surveyState=1" ><span style="letter-spacing: 3px;font-size: 18px;">进行中</span></a>
					</div>
					<div class="weui-navbar__item" id="tab2">
						<a href="${ctx}/wenjuan.action?surveyState=2" ><span style="letter-spacing: 3px;font-size: 18px;">已结束</span></a>
					</div>
				</div>
				<div class="weui-tab__panel">

				</div>
			</div>
		</div>
	</div>

	<%--
	<div class="weui-cells" id="tab1_content">
		<c:forEach items="${page.result}" var="en" >
			<a class="weui-cell weui-cell_access" href="${ctx}/response!answerMobile.action?surveyId=${en.id}">
				<div class="weui-cell__bd">
					<p>${en.surveyName}</p>
				</div>
				<div class="weui-cell__ft">
						${en.surveyState eq 0 ? '设计':en.surveyState eq 1?'进行中':en.surveyState eq 2?'收集完成':'' }
				</div>
			</a>
		</c:forEach>
	</div>--%>


	<script type="text/javascript" class="navbar js_show">
		$(function(){
			/*$('.weui-navbar__item').on('click', function () {
				$(this).addClass('weui-bar__item_on').siblings('.weui-bar__item_on').removeClass('weui-bar__item_on');
				$(".weui-cells").hide();
				var thId = $(this).attr("id");
				$("#"+thId+"_content").slideDown();
			});*/
			$("#tab${param.surveyState}").addClass("weui-bar__item_on");
		});
	</script>

</div>


</body>
</html>