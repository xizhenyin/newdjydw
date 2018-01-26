<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%@include file="/common/taglibs.jsp" %>
<%
	response.setHeader("P3P","CP=\"IDC DSP COR ADM DEVi TAIi PSA PSD IVAi IVDi CONi HIS OUR IND CNT\"");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="${ctx }/js/plugs/jquery-ui-1.10.3.custom/css/mycss/jquery-ui-1.10.3.custom.css" rel="stylesheet" type="text/css" />

	<!-- 新 Bootstrap 核心 CSS 文件 -->
<!-- <link rel="stylesheet" href="http://cdn.bootcss.com/bootstrap/3.3.0/css/bootstrap.min.css"> -->
<link rel="stylesheet" href="${ctx }/js/plugs/bootstrap-3.3.0-dist/dist/css/bootstrap.css">
<!-- 可选的Bootstrap主题文件（一般不用引入） -->
<!-- <link rel="stylesheet" href="http://cdn.bootcss.com/bootstrap/3.3.0/css/bootstrap-theme.min.css"> -->
<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
<!-- <script src="http://cdn.bootcss.com/bootstrap/3.3.0/js/bootstrap.min.js"></script> -->
<script src="${ctx }/js/plugs/bootstrap-3.3.0-dist/dist/js/bootstrap.js"></script>
<!-- <link href="//maxcdn.bootstrapcdn.com/font-awesome/4.2.0/css/font-awesome.min.css" rel="stylesheet"> -->
<link href="${ctx }/js/plugs/font-awesome-4.2.0/css/font-awesome.css" rel="stylesheet">

<title>调查问卷</title>
<style type="text/css">
.surveyLeftBtnGroup a{
	color: #5A9ECD;
	color: #e71f19;
}
.btn-group{
	/* border: 1px solid #D1D1D1; */
}
.btn-group a{
	/* border-radius: 0px; */
	/* border-color: white; */
}
.btn-group a:hover{
	background: #317BCF;
	color: white;
	border-color: #317BCF;
	background: #e71f19;
	border-color: #e71f19;
}
.btn-group>.btn:not(:first-child) {
	margin-left: -1px;
}
.contacts-table tr td{
	font-size: 16px! important;
}
.dialogBtn1{
	border:none;
	font-size: 1em;
	font-weight:bold;
  	cursor: pointer;
  	padding: 8px 10px;
}
.dialogBtn1 .ui-button-text {
    padding: .4em 1em;
    display: block;
    line-height: normal;
}
.dialogBtn1Cencel{
	background: none;
}
.dialogBtn1Cencel:hover{
	background: #f6f6f6;
}
button {
	outline: none;
}

.dialogMessage select, .dialogMessage input {
    padding: 5px;
    color: #333333;
    border: 1px solid #98C5C3;
	color: #e71f19;
	border: 1px solid #e71f19;
}
.tab_a{
	color: black!important;
}
.a_item_on{
	color: red!important;
	/*font-weight: bold;*/
}
</style>
</head>
<body>
<div id="wrap" >

	<div id="header" style="display: none;" >
		<div id="headerCenter"  class="bodyCenter">
			<div class="header_Item header_logo">
				<%--
                <a href="${ctx }/">
                    <img alt="调问网" src="${ctx }/images/logo/LOGO.png" >
                </a>
                --%>
					<a href="${ctx }/design/my-survey.action" style="font-size: 28px;"><img alt="调问网" src="${ctx }/images/logo/LOGO-2.png" align="middle" height="46" ><span class="titleTempSpan"></span></a>
			</div>
			<shiro:guest>
				<div class="header_Item header_menu">
					<ul>
							<%--<li><a href="/" class="active dw-menu-a" id="indexMenu">首页</a></li>--%>
							<%--<li><a href="/feature.jsp" class="dw-menu-a" id="featureMenu">功能</a></li>--%>
							<%--<li><a href="${ctx }/survey-model.action" class="dw-menu-a">下载</a></li>--%>
							<%--<li><a href="${ctx }/survey-model.action" class="dw-menu-a">帮助</a></li>--%>
							<%--<li><a href="${ctx }/survey-model.action" class="dw-menu-a">GITHUB</a></li>--%>
						<!--
                        <li><a href="http://support.diaowen.net/" class="dw-menu-a" id="helpMenu">帮助</a></li>
                        -->
					</ul>
				</div>
				<div class="header_Item header_user" style="float: right;">
						<%--<a href="${ctx }/login.jsp" class="btn-a-1">登录</a>--%>
				</div>
			</shiro:guest>

			<shiro:user>
				<div class="header_Item header_menu">
					<ul>
							<%-- <li><a href="${ctx }/" >首页</a></li> --%>
							<%--<li><a href="${ctx }/design/my-survey.action" id="mysurvey">问卷</a></li>
                                <shiro:hasRole name="admin" >
                            <li><a href="${ctx }/sy/user/user-admin.action" id="usermanager">用户</a></li>
                            <li><a href="${ctx }/sy/system/sys-property!input.action" id="systemset">设置</a></li>
                                </shiro:hasRole>
                            <li><a href="http://support.diaowen.net/">帮助</a></li>--%>
					</ul>
				</div>
				<div class="header_Item header_user" style="float: right;margin-top: 12px;position: relative;zoom: 1;z-index: 165;">
						<span class="head_use_name" style="margin-right: 20px;">
						<shiro:principal></shiro:principal>
						</span>
						<%--<a href="#" class="clickHideUserMenu">
						<span class="head_use_name">
						<shiro:principal></shiro:principal>
						</span>
						<span class="header_user_icon">&nbsp;</span>
					</a>--%>
					<%--<div class="a-w-sel a-w-sel-head" style="display: none;">
						<div class="w-sel" style="margin-top: 16px;">
							<div class="selc">
								<div class="selcc tbtag">
									<div class="seli"><a class="nx-1" href="${ctx }/ic/user!myaccount.action">修改密码</a></div>
									<div class="seli"><a class="nx-7" href="http://support.diaowen.net/">帮助及反馈</a></div>
									<div class="seli"><a class="nx-8" href="${ctx }/login!logout.action">退出</a></div>
								</div>
							</div>
						</div>
					</div>--%>
				</div>
			</shiro:user>
		</div>
		<div style="clear: both;"></div>
	</div>

	<input type="hidden" id="id" name="id" value="${survey.id }">
	<div style="clear: both;"></div>
	<div id="dwBody" style="">
		<div id="dwBodyContent" class="bodyCenter" style="">
			<div id="dwBodyUser">
				<div class="surveyCollectMiddle">
					<div class="surveyCollectMiddleContent">
						<div style="padding: 25px 45px;overflow: auto;padding-top: 20px;">
							<%--<div style="padding: 5px;color: #666565;letter-spacing: 2px;">--%>
							<%--所有问卷&nbsp;&nbsp;|&nbsp;&nbsp;--%>
							<%--<button href="${ctx }/design/my-survey-create!save.action" id="surveyAdd-a" class="sbtn25 sbtn25_1" style="outline: none;text-decoration: none;" target="_blank" ><i class="fa fa-plus " aria-hidden="true"></i>&nbsp;新建问卷</button>--%>
							<%--</div>--%>

							<form action="${ctx}/wenjuan!listSurvey.action&djyToken=${djyToken}" method="post" >

								<%--<div class="contacts_search" style="padding: 5px;color:#666565;" >
								<div style="padding-left: 20px;padding-top: 8px;padding-bottom: 8px;">
									<span style="font-size: 14px;vertical-align: middle;">状态&nbsp;</span>
									<select name="surveyState" style="vertical-align: middle;">
										<option value="">不限</option>
										&lt;%&ndash;<option value="0">设计</option>&ndash;%&gt;
										<option value="1">进行中</option>
										<option value="2">已结束</option>
									</select>
									&nbsp;&nbsp;
									&lt;%&ndash;<span style="font-size: 14px;vertical-align: middle;">名称&nbsp;</span>&ndash;%&gt;
									&lt;%&ndash;<input type="text" class="inputS1" name="surveyName" value="${surveyName}">&ndash;%&gt;
									<input type="submit" value="查询" class="sbtn25 sbtn25_1" style="font-size: 16px;"/>
								</div>
							</div>--%>
								<div style="padding-left: 30px;font-size: 18px;">
									<a href="${ctx}/wenjuan!listSurvey.action?surveyState=1&djyToken=${djyToken}" id="tab1" class="tab_a a_item_on" style="margin-right: 30px;">进行中</a>
									<a href="${ctx}/wenjuan!listSurvey.action?surveyState=2&djyToken=${djyToken}" id="tab2" class="tab_a" style="">已结束</a>
								</div>
							</form>

							<div style="margin-top: 15px;">
								<!-- <div style="padding: 5px;color: #666565;text-align: right;">
                                    <a href="" class="export-contacts active"><span>&nbsp;</span>导出联系人</a>
                                </div> -->
								<div style="padding: 5px;color:#666565; ">
									<table class="contacts-table" width="100%" cellpadding="0" cellspacing="0">
										<tr>
											<th style="text-align: center;" width="30"><!-- <input type="checkbox">  --></th>
											<th align="left" >问卷名称</th>
											<th align="left" width="300">创建时间</th>
											<%--<th align="left" width="60">答卷</th>--%>
											<th align="left" width="120">状态</th>
											<th align="center" width="220" style="padding-left: 10px;">操作</th>
										</tr>
										<c:choose>
											<c:when test="${page.totalItems > 0}">
												<c:forEach items="${page.result }" var="en">
													<tr>
														<td align="center">
															<input type="hidden" name='surveyId' value="${en.id }">
														</td>
														<td align="left">
																<%--<a target="_blank" href="${ctx }/wenjuan/${en.sid }.html" class="titleTag">${en.surveyName }</a>--%>
																${en.surveyName }
														</td>
														<td align="left">
															<fmt:formatDate value="${en.createDate }" pattern="yyyy年MM月dd日 HH:mm"/>
														</td>
															<%--<td align="left">${empty(en.answerNum) ? '0':en.answerNum  }&nbsp;</td>--%>
														<td align="left" >
																${en.surveyState eq 0 ? '设计':en.surveyState eq 1?'进行中':en.surveyState eq 2?'收集完成':'' }
														</td>
														<td align="left">
															<div class="btn-group surveyLeftBtnGroup">
																<c:choose>
																	<c:when test="${en.surveyState eq 1}">
																		<a class="btn btn-default" href="${ctx }/wenjuan/${en.sid }.html?djyToken=${djyToken}" title="点击进入答卷" data-toggle="tooltip" data-placement="top" target="_blank" >立即答卷</a>
																	</c:when>
																	<c:otherwise>
																		暂无操作
																	</c:otherwise>
																</c:choose>
															</div>&nbsp;
															<div class="btn-group" style="display: none;">
																<!-- <a class="btn btn-default" href="#"><i class="fa fa-eye"></i></a> -->
																<a class="btn btn-default" href="#"><i class="fa fa-trash-o fa-fw"></i></a>
															</div>
														</td>
													</tr>
												</c:forEach>
											</c:when>
											<c:otherwise>
												<tr>
													<td colspan="7">

														<div style="padding: 60px;font-size: 16px;text-align: center;color: black;">暂无信息！</div>

													</td>
												</tr>
											</c:otherwise>
										</c:choose>
									</table>
									<div style="padding-top: 15px;text-align: center;">
										<div class="btn-group">
											<c:if test="${page.pageNo > 1}">
												<a href="${ctx }/wenjuan!listSurvey.action?surveyState=${param.surveyState}&page.pageNo=${page.pageNo-1}&djyToken=${djyToken}" class="btn btn-default">&lt;</a>
											</c:if>
											<c:if test="${page.startpage > 1}">
												<a href="${ctx }/wenjuan!listSurvey.action?surveyState=${param.surveyState}&page.pageNo=1&djyToken=${djyToken}" class="btn btn-default">1</a>
												<c:if test="${page.startpage > 2 }">
													<span>...</span>
												</c:if>
											</c:if>
											<c:forEach begin="${page.startpage }" end="${page.endpage }" var="en">
												<c:choose>
													<c:when test="${page.pageNo eq en }"><a href="${ctx }/wenjuan!listSurvey.action?surveyState=${param.surveyState}&page.pageNo=${en }&djyToken=${djyToken}" class="btn btn-default" style="background: #efbfbe;">${en }</a></c:when>
													<c:otherwise><a href="${ctx }/wenjuan!listSurvey.action?surveyState=${param.surveyState}&page.pageNo=${en}&djyToken=${djyToken}" class="btn btn-default">${en }</a></c:otherwise>
												</c:choose>
											</c:forEach>
											<c:if test="${page.totalPage > (page.endpage)}">
												<c:if test="${page.totalPage > (page.endpage+1)}">
													<span>...</span>
												</c:if>
												<a href="${ctx }/wenjuan!listSurvey.action?surveyState=${param.surveyState}&page.pageNo=${page.totalPage}&djyToken=${djyToken}" class="btn btn-default">${page.totalPage }</a>
											</c:if>
											<c:if test="${page.totalPage > page.pageNo}">
												<a href="${ctx }/wenjuan!listSurvey.action?surveyState=${param.surveyState}&page.pageNo=${page.pageNo+1}&djyToken=${djyToken}" class="btn btn-default">&gt;</a>
											</c:if>

										</div>
									</div>
								</div>
							</div>
						</div>

					</div>
				</div>

			</div>
		</div>
	</div>

</div>

	<%--批量添加 --%>


<script type="text/javascript">

	$(document).ready(function(){

		currentMenu("mysurvey");

		$("select[name='surveyState']").val("${surveyState}");

		if(${!empty param.surveyState}){
			$(".a_item_on").removeClass("a_item_on");
			$("#tab${param.surveyState}").addClass("a_item_on");
		}

	});


function setSelectText(el) {
    try {
        window.getSelection().selectAllChildren(el[0]); //全选
        window.getSelection().collapseToEnd(el[0]); //光标置后
    } catch (err) {
        //在此处理错误
    }
}


</script>
</body>
</html>