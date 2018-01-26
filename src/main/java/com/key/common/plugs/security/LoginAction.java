/*
 * $HeadURL: $
 * $Id: $
 * Copyright (c) 2011 by Ericsson, all rights reserved.
 */

package com.key.common.plugs.security;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cnpc.ruixin.sdk.RuiXinAPI;
import com.cnpc.ruixin.sdk.auth.UserInfo;
import com.cnpc.ruixin.sdk.envirenment.EnvType;
import com.key.common.base.entity.User;
import com.key.common.base.service.AccountManager;
import com.key.common.utils.DiaowenProperty;
import com.key.common.utils.web.Struts2Utils;
import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.GzipDecompressingEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.apache.poi.util.SystemOutLogger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.struts2.convention.annotation.AllowedMethods;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.zip.GZIPInputStream;

/**
 * @author keyuan(keyuan258@gmail.com)
 *
 * https://github.com/wkeyuan/DWSurvey
 * http://dwsurvey.net
 */
@Namespace("/")
@Results({
	@Result(name = "login", location = "login.jsp", type = "redirect"),
	@Result(name = "loginSuccess", location = "/design/my-survey.action", type = "redirect"),
		@Result(name = "loginSuccessM", location = "/wenjuan.action", type = "redirect"),
		@Result(name = "loginSuccess_pc", location = "/wenjuan!listSurvey.action", type = "redirect")
	})
@AllowedMethods({"login","logout","loginpc","rxlogin","loginForZBSH"})
public class LoginAction extends ActionSupport {

	private static final long serialVersionUID = 7392913081177740732L;
	@Autowired
	private FormAuthenticationWithLockFilter formAuthFilter;
	@Autowired
	protected AccountManager accountManager;
	@Autowired
	private CheckToken checkToken;
	
	public String login() throws Exception {
		
		System.out.println("username1-1");
		HttpServletRequest request = Struts2Utils.getRequest();
		HttpServletResponse response = Struts2Utils.getResponse();
		
		Subject subject = SecurityUtils.getSubject();
		boolean isAuth = subject.isAuthenticated();
		// 返回成功与否
		String error="";
		Long resetnum=0L;
		try{
//			if (!isAuth) {
//			String username = request.getParameter("username");
//			String password = request.getParameter("password");
				Object djyToken = request.getSession().getAttribute("djyToken");
				System.out.println(" djyToken: "+djyToken);
				if (djyToken != null) {
					//根据token取sip
//					String result = checkToken(djyToken.toString());
					String result = checkToken.checkToken01(djyToken.toString());
					System.out.println("result-checktToken:"+result);
					JSONObject tokenJson = JSONObject.fromObject(result);
					//result = "{\"result\": 0,\"msg\": \"验证成功！\",\"info\": [{\"sap\": \"testsap123\",\"username\": \"张三\"}]}";
					int isSuccess = tokenJson.getInt("result");
					if(isSuccess == 0){
						JSONArray userInfos = tokenJson.getJSONArray("info");
						JSONObject userInfo = userInfos.getJSONObject(0);

//					String username = "service@diaowen.net";
//					String password = "123456";
						String loginname = userInfo.getString("sap");
						String name = userInfo.getString("username");

						System.out.println(loginname+":"+name);

						//判断本地库有无，没有则添加一条，有直接登录a
						User user = accountManager.findUserByLoginName(loginname);
						if(user==null){
							user = new User();
							user.setLoginName(loginname);
							user.setPlainPassword(loginname+"_pwd");
							user.setStatus(1);
							user.setName(name);
							user.setCreateBy("djyToken");
							accountManager.saveUser(user);
						}

						String username = loginname;
						String password = loginname+"_pwd";
						UsernamePasswordToken token = new UsernamePasswordToken(username, password);
						subject.getSession().setAttribute("userRole","admin");//可以管理卷的用户
						if (!formAuthFilter.checkIfAccountLocked(request)) {
							try {
								subject.login(token);
								formAuthFilter.resetAccountLock(username);
								isAuth = true;
							} catch (IncorrectCredentialsException e) {
								formAuthFilter.decreaseAccountLoginAttempts(request);
								isAuth = false;
								error = "IncorrectCredentialsException";
								resetnum = formAuthFilter.getAccountLocked(username);
							} catch (AuthenticationException e) {
								isAuth = false;
								error = "AuthenticationException";
							}
						} else {
							//ExcessiveAttemptsException超过登录次数
							error = "ExcessiveAttemptsException";
						}
					}else{
						error = "djyTokenCheckAttemptsException";
					}
				} else {
					error = "djyTokenAttemptsException";
				}
			/*}else{
				error = "Auth";
			}*/
		}catch (Exception e){
			e.printStackTrace();
		}
		//PrintWriter writer = response.getWriter();    
		//writer.write(isAuth + ","+error);//此种方式，在$.getJson()进行仿问时会出现不执行回调函数
//		System.out.println(isAuth+","+error);
		//response.setContentType("text/plain");// 1.设置返回响应的类型
		//2. 01 一定要注意：要包括jsoncallback参数，不然回调函数不执行。
		//2. 02 返回的数据一定要是严格符合json格式 ，不然回调函数不执行。
		//response.getWriter().write( request.getParameter("jsoncallback") + "({isAuth:'"+isAuth+"',error:'"+error+"',resetnum:'"+resetnum+"'})" );
		System.out.println("error:"+error);
		if(isAuth){
			// 跳转到成功页面
			return "loginSuccess";
		}
		return "login";
	}

	
	//pc端答卷
	public String loginpc() throws Exception {

		System.out.println("username1-1");
		HttpServletRequest request = Struts2Utils.getRequest();
		HttpServletResponse response = Struts2Utils.getResponse();

		Subject subject = SecurityUtils.getSubject();
	//	boolean isAuth = subject.isAuthenticated();
		boolean isAuth = false;
		// 返回成功与否
		String error="";
		Long resetnum=0L;
		try{
			if (!isAuth) {
//			String username = request.getParameter("username");
//			String password = request.getParameter("password");
				Object djyToken = request.getSession().getAttribute("djyToken");
				System.out.println(" djyToken: "+djyToken);
				if (djyToken != null) {
					//根据token取sip
//					String result = checkToken(djyToken.toString());
					String result = checkToken.checkToken01(djyToken.toString());
					System.out.println("result-checktToken:"+result);
					JSONObject tokenJson = JSONObject.fromObject(result);
					//result = "{\"result\": 0,\"msg\": \"验证成功！\",\"info\": [{\"sap\": \"testsap123\",\"username\": \"张三\"}]}";
					int isSuccess = tokenJson.getInt("result");
					if(isSuccess == 0){
						JSONArray userInfos = tokenJson.getJSONArray("info");
						JSONObject userInfo = userInfos.getJSONObject(0);

//					String username = "service@diaowen.net";
//					String password = "123456";
						String loginname = userInfo.getString("sap");
						String name = userInfo.getString("username");

						System.out.println(loginname+":"+name);

						//判断本地库有无，没有则添加一条，有直接登录a
						User user = accountManager.findUserByLoginName(loginname);
						if(user==null){
							user = new User();
							user.setLoginName(loginname);
							user.setPlainPassword(loginname+"_pwd");
							user.setStatus(1);
							user.setName(name);
							user.setCreateBy("djyToken");
							accountManager.saveUser(user);
						}

						String username = loginname;
						String password = loginname+"_pwd";
						UsernamePasswordToken token = new UsernamePasswordToken(username, password);
						if (!formAuthFilter.checkIfAccountLocked(request)) {
							try {
								subject.login(token);
								formAuthFilter.resetAccountLock(username);
								isAuth = true;
							} catch (IncorrectCredentialsException e) {
								formAuthFilter.decreaseAccountLoginAttempts(request);
								isAuth = false;
								error = "IncorrectCredentialsException";
								resetnum = formAuthFilter.getAccountLocked(username);
							} catch (AuthenticationException e) {
								isAuth = false;
								error = "AuthenticationException";
							}
						} else {
							//ExcessiveAttemptsException超过登录次数
							error = "ExcessiveAttemptsException";
						}
					}else{
						error = "djyTokenCheckAttemptsException";
					}
				} else {
					error = "djyTokenAttemptsException";
				}
			}else{
				error = "Auth";
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		//PrintWriter writer = response.getWriter();
		//writer.write(isAuth + ","+error);//此种方式，在$.getJson()进行仿问时会出现不执行回调函数
//		System.out.println(isAuth+","+error);
		//response.setContentType("text/plain");// 1.设置返回响应的类型
		//2. 01 一定要注意：要包括jsoncallback参数，不然回调函数不执行。
		//2. 02 返回的数据一定要是严格符合json格式 ，不然回调函数不执行。
		//response.getWriter().write( request.getParameter("jsoncallback") + "({isAuth:'"+isAuth+"',error:'"+error+"',resetnum:'"+resetnum+"'})" );
		System.out.println("error:"+error);
		if(isAuth){
			// 跳转到成功页面
			return "loginSuccess_pc";
		}
		return "login";
	}


	public String rxlogin() throws Exception {

		System.out.println("username1-rxlogin");
		HttpServletRequest request = Struts2Utils.getRequest();
		HttpServletResponse response = Struts2Utils.getResponse();
		boolean isAuth = false;
		// 返回成功与否
		String error="";
		Long resetnum=0L;
		try{
			Subject subject = SecurityUtils.getSubject();
			isAuth = subject.isAuthenticated();
			if (!isAuth) {
				Object rxtoken = request.getSession().getAttribute("rxToken");
				System.out.println(" rxToken in session: "+rxtoken);
				if (rxtoken != null) {
					//根据token取sip
					EnvType envType = EnvType.DEV;
					if("PROD".equals(DiaowenProperty.ENV_TYPE)){
						envType = EnvType.PROD;
					}else if("TRAINING".equals(DiaowenProperty.ENV_TYPE)){
						envType = EnvType.TRAINING;
					};
					System.out.println(DiaowenProperty.RX_APPID +" : " + DiaowenProperty.RX_SECRET +" : "+ envType);
					RuiXinAPI api=RuiXinAPI.CreateAPI(DiaowenProperty.RX_APPID, DiaowenProperty.RX_SECRET, envType);
					UserInfo ui= api.getAuthAPI().getUserInfoByToken(rxtoken.toString());
					if(ui!=null){
						String loginname0 = ui.getLoginID();//瑞信唯一标示
						String loginname = loginname0.substring(0, 8);
						String email = ui.getEmail();//邮箱
						String name = ui.getUserName();//姓名
						String company = ui.getCompany();//所属公司
						String juName = ui.getJuName();//根机构代码
						String userNumber = ui.getUserNumber();//员工编号

						System.out.println(loginname+":"+email+":"+name+":"+ui.toString());


						//判断本地库有无，没有则添加一条，有直接登录a
						User user = accountManager.findUserByLoginName(loginname);
						if(user==null){
							user = new User();
							user.setLoginName(loginname);
							user.setPlainPassword(loginname+"_pwd");
							user.setStatus(1);
							user.setName(name);
							user.setEmail(email);
							user.setCreateBy("rxToken");
							accountManager.saveUser(user);
						}

						String username = loginname;
						String password = loginname+"_pwd";
						UsernamePasswordToken token = new UsernamePasswordToken(username, password);
						if (!formAuthFilter.checkIfAccountLocked(request)) {
							try {
								subject.login(token);
								formAuthFilter.resetAccountLock(username);
								isAuth = true;
							} catch (IncorrectCredentialsException e) {
								formAuthFilter.decreaseAccountLoginAttempts(request);
								isAuth = false;
								error = "IncorrectCredentialsException";
								resetnum = formAuthFilter.getAccountLocked(username);
							} catch (AuthenticationException e) {
								isAuth = false;
								error = "AuthenticationException";
							}
						} else {
							//ExcessiveAttemptsException超过登录次数
							error = "ExcessiveAttemptsException";
						}
					}else {
						System.out.println("rx invalid token");
						error = "rx invalid token";
					}
				} else {
					error = "rxTokenAttemptsException";
				}
			}else{
				error = "Auth";
			}
		}catch (Exception e){
			error = e.getMessage();
		}

		//PrintWriter writer = response.getWriter();
		//writer.write(isAuth + ","+error);//此种方式，在$.getJson()进行仿问时会出现不执行回调函数
//		System.out.println(isAuth+","+error);
		//response.setContentType("text/plain");// 1.设置返回响应的类型
		//2. 01 一定要注意：要包括jsoncallback参数，不然回调函数不执行。
		//2. 02 返回的数据一定要是严格符合json格式 ，不然回调函数不执行。
		//response.getWriter().write( request.getParameter("jsoncallback") + "({isAuth:'"+isAuth+"',error:'"+error+"',resetnum:'"+resetnum+"'})" );
		System.out.println("error:"+error);
		if(isAuth){
			// 跳转到成功页面
			return "loginSuccessM";
		}
		return "login";
	}



	/**
	 * 解析json接口
	 * @param url
	 * @return JSONObject
	 */
	public static String loadJson (String url) {
		StringBuilder jsonSb = new StringBuilder();
		try {
			URL urlObject = new URL(url);
			URLConnection uc = urlObject.openConnection();
			uc.setRequestProperty("Charset", "utf-8");
			BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream(),"utf-8"));
			String inputLine = null;
			while ( (inputLine = in.readLine()) != null) {
				jsonSb.append(inputLine);
			}
			in.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonSb.toString();
	}


	public String checkToken(String djyToken) throws IOException {
		String urlNameString = DiaowenProperty.CHECKTOKEN_URL+"?djyToken="+djyToken;
		System.out.println("request:"+urlNameString);
		//return loadJson(urlNameString);

		// 根据地址获取请求
		HttpGet request = new HttpGet(urlNameString);//这里发送get请求
		//request.addHeader("Content-Type", "text/html;charset=UTF-8");
		// 获取当前客户端对象
		HttpClient httpClient = new DefaultHttpClient();
		// 通过请求对象获取响应对象
		HttpResponse response = httpClient.execute(request);

		String result = "";
		// 判断网络连接状态码是否正常(0--200都数正常)
		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			//result= EntityUtils.toString(response.getEntity(),"UTF-8");
			//result= EntityUtils.toString(response.getEntity(), "UTF-8");
			//System.out.println("result-utf-8:"+result);
//			result = EntityUtils.toString(response.getEntity(),"utf-8");
			//解决HttpClient获取中文乱码 ，用String对象进行转码
//			System.out.println("Response content:" + new String(result.getBytes("ISO-8859-1"),"utf-8"));
//			System.out.println("result:"+result);
			//result = "{\"result\": 0,\"msg\": \"验证成功！\",\"info\": [{\"sap\": \"testsap123\",\"username\": \"张三\"}]}";
			//result = "{\"result\": 1,\"msg\": \"验证失败！\"}";



		}
		return result;
	}

	public String logout() throws Exception {
		System.out.println("............logout..................");
		if (SecurityUtils.getSubject() != null) {
			SecurityUtils.getSubject().logout();
		}
		Struts2Utils.getSession().invalidate();
		return "login";
	}
	/* 给某个锁定的账号开锁,管理员使用 */
	
	public String lockout() throws Exception {
		HttpServletRequest request=Struts2Utils.getRequest();
		HttpServletResponse response=Struts2Utils.getResponse();
		String username=request.getParameter("username");
		//确认有没账号
		boolean isup=false;
		String error="用户不存在";
		
		if(username!=null){
//			User user=accountManager.findUserByLoginName(username);
			User user = accountManager.findUserByLoginNameOrEmail(username);
			if(user!=null){
				formAuthFilter.resetAccountLock(username);
				isup=true;
			}
		}
		response.getWriter().write(isup?username+"解锁成功":username+"，"+error);
		return null;
	}
	
	public String register() throws Exception {
		
		return "";
	}

}
