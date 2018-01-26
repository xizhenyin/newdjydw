package com.key.dwsurvey.action.nologin;

import com.key.common.plugs.page.Page;
import com.key.dwsurvey.entity.SurveyDirectory;
import com.key.dwsurvey.service.SurveyDirectoryManager;
import org.apache.struts2.convention.annotation.*;

import com.key.common.utils.web.Struts2Utils;
import com.opensymphony.xwork2.ActionSupport;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.net.URLEncoder;
import java.util.List;

@Namespace("/")
@InterceptorRefs({ @InterceptorRef("paramsPrepareParamsStack")})
@Results({
	@Result(name=SurveyAction.ANSERSURVEY_MOBILE,location="/WEB-INF/page/content/diaowen-design/answer-survey-mobile.jsp",type=Struts2Utils.DISPATCHER),
	@Result(name = WenjuanAction.SUCCESS, location = "/WEB-INF/page/content/diaowen-survey/surveys-mobile.jsp", type = Struts2Utils.DISPATCHER),
		@Result(name ="list", location = "/WEB-INF/page/content/diaowen-survey/surveys.jsp", type = Struts2Utils.DISPATCHER)
})
@AllowedMethods({"listSurvey","checkAnswerdAjax"})
public class WenjuanAction extends ActionSupport{
	@Autowired
	private SurveyDirectoryManager surveyDirectoryManager;

	private Page<SurveyDirectory> page = new Page<SurveyDirectory>();

	@Override
	public String execute() throws Exception {
		SurveyDirectory entity = new SurveyDirectory();
		HttpServletRequest request = Struts2Utils.getRequest();
		String surveyState = request.getParameter("surveyState");
		if(surveyState==null){
			surveyState = "1";
		}
		page = surveyDirectoryManager.findPage(page, entity, surveyState);
		return SUCCESS;
	}

	public String listSurvey() throws Exception {
		SurveyDirectory entity = new SurveyDirectory();
		HttpServletRequest request = Struts2Utils.getRequest();
		String surveyState = request.getParameter("surveyState");
		if(surveyState==null){
			surveyState = "1";
		}
		//先判断用户是否登录
		//page.setPageSize(19);
		page = surveyDirectoryManager.findPage(page, entity, surveyState);
		return "list";
	}

	public String checkAnswerdAjax() throws Exception {
		HttpServletResponse response= Struts2Utils.getResponse();
		String ret = "false";
		HttpServletRequest request = Struts2Utils.getRequest();
		String qId = request.getParameter("qId");
		List<String> ids = surveyDirectoryManager.currentUserAnsered(qId);
		if(ids!=null && ids.size()>0) {
			ret =  "true";
		}
		response.getWriter().write(ret);
		return null;
	}
	

	public Page<SurveyDirectory> getPage() {
		return page;
	}

	public void setPage(Page<SurveyDirectory> page) {
		this.page = page;
	}
}
