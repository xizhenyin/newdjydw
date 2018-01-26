package com.key.dwsurvey.action.nologin;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.key.common.base.entity.User;
import com.key.common.base.service.AccountManager;
import com.key.dwsurvey.common.GroupCount;
import com.key.dwsurvey.entity.Question;
import com.key.dwsurvey.entity.SurveyDirectory;
import com.key.dwsurvey.entity.SurveyStats;
import com.key.dwsurvey.service.SurveyAnswerManager;
import com.key.dwsurvey.service.SurveyDirectoryManager;
import com.key.dwsurvey.service.SurveyStatsManager;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.struts2.convention.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import com.key.common.utils.web.Struts2Utils;
import com.opensymphony.xwork2.ActionSupport;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 公开分析 action
 * @author KeYuan(keyuan258@gmail.com)
 *
 * https://github.com/wkeyuan/DWSurvey
 * http://dwsurvey.net
 *
 */

@Namespace("/api")
@InterceptorRefs({ @InterceptorRef("paramsPrepareParamsStack")})
@Results({
	@Result(name=ReportAction.DEFAULT_REPORT,location="/WEB-INF/page/content/diaowen-da/default-report-pub.jsp",type=Struts2Utils.DISPATCHER)
})
@AllowedMethods({"groupCount"})
public class ReportAction extends ActionSupport{
	
	protected final static String DEFAULT_REPORT="default_report";
	
	@Autowired
	private SurveyStatsManager surveyStatsManager;
	@Autowired
	private SurveyDirectoryManager directoryManager;
	@Autowired
	private AccountManager accountManager;
	@Autowired
	private SurveyAnswerManager surveyAnswerManager;
	
	private SurveyStats surveyStats = new SurveyStats();
	private SurveyDirectory directory = new SurveyDirectory();
	
	private String sid;
	
	/*public String execute() throws Exception {
		// 得到频数分析数据
		directory=directoryManager.getSurveyBySid(sid);
		Integer viewAnswer=directory.getViewAnswer();

		if(viewAnswer!=null && viewAnswer.intValue()==1){
			List<Question> questions = surveyStatsManager.findFrequency(directory);
			surveyStats.setQuestions(questions);
		}else{
			Struts2Utils.getRequest().setAttribute("noview", 1);
		}
		return DEFAULT_REPORT;		
	}*/

	public String execute() throws  Exception {
		HttpServletRequest request = Struts2Utils.getRequest();
		HttpServletResponse response = Struts2Utils.getResponse();
		String beginDate = Struts2Utils.getParameter("beginDate");
		String endDate = Struts2Utils.getParameter("endDate");
//		yyyy-MM-dd HH:mm:ss
		String sap = request.getParameter("sap");
		User user = accountManager.findUserByLoginName(sap);
		String result = "{\"result\": 1,\"msg\": \"失败，无此用户！\",\"info\": {\"sap\": \""+sap+"\",\"answerCount\": \"0\",\"surveyCount\": \"0\"}}";
		if(user!=null){
			Long answerCount = surveyAnswerManager.userAnswerCount(user.getId(),beginDate,endDate);
			Long createSurveyCount = directoryManager.userSurveyCount(user.getId(),beginDate,endDate);
		    result = "{\"result\": 0,\"msg\": \"成功！\",\"info\": {\"sap\": \""+sap+"\",\"answerCount\": \""+answerCount+"\",\"surveyCount\":\""+createSurveyCount+"\"}}";
		}
		response.setContentType("text/html; charset=utf-8");
		response.getWriter().write(result);
		return null;
	}

	public String groupCount() throws  Exception {
		HttpServletRequest request = Struts2Utils.getRequest();
		HttpServletResponse response = Struts2Utils.getResponse();
		String sdate = request.getParameter("sdate");
		String beginDate = request.getParameter("beginDate");
		String endDate =  request.getParameter("endDate");
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String curDate = simpleDateFormat.format(new Date());
		if(beginDate==null){
			beginDate = getSpecifiedDayBefore(curDate);
			endDate = curDate;
		}
		List<Object[]> surveyAnswerCount = surveyAnswerManager.groupUser(beginDate,endDate);
		List<Object[]> surveyCount = directoryManager.groupUser(beginDate,endDate);

		GroupCount groupCount = new GroupCount();
		groupCount.setResult(1);
		groupCount.setMsg("OK");
		groupCount.setAnswerCount(surveyAnswerCount);
		groupCount.setSurveyCount(surveyCount);
		JSONObject jsonObject = JSONObject.fromObject(groupCount);
		String result = jsonObject.toString();
//		String result = "{\"result\": 1,\"msg\": \"失败，无此用户！\",\"info\": {\"answerGroup\": \""+jsonAnswer+"\",\"surveyGroup\": \""+jsonSurvey+"\"}}";
		System.out.println(result);
		response.setContentType("text/html; charset=utf-8");
		response.getWriter().write(result);
		return null;
	}

	public SurveyStats getSurveyStats() {
		return surveyStats;
	}
	
	public SurveyDirectory getDirectory() {
		return directory;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	/**
	 * 获得指定日期的前一天
	 * @param specifiedDay
	 * @return
	 * @throws Exception
	 */
	public static String getSpecifiedDayBefore(String specifiedDay){
	//SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		Date date=null;
		try {
			date = new SimpleDateFormat("yy-MM-dd").parse(specifiedDay);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		c.setTime(date);
		int day=c.get(Calendar.DATE);
		c.set(Calendar.DATE,day-1);

		String dayBefore=new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
		return dayBefore;
	}
	/**
	 * 获得指定日期的后一天
	 * @param specifiedDay
	 * @return
	 */
	public static String getSpecifiedDayAfter(String specifiedDay){
		Calendar c = Calendar.getInstance();
		Date date=null;
		try {
			date = new SimpleDateFormat("yy-MM-dd").parse(specifiedDay);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		c.setTime(date);
		int day=c.get(Calendar.DATE);
		c.set(Calendar.DATE,day+1);

		String dayAfter=new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
		return dayAfter;
	}
	
}
