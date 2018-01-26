package com.key.dwsurvey.common;

import com.key.common.base.entity.User;
import com.key.common.base.service.AccountManager;
import com.key.common.plugs.security.CheckToken;
import com.key.common.plugs.security.FormAuthenticationWithLockFilter;
import com.key.common.utils.DiaowenProperty;
import com.key.common.utils.SpringContextHolder;
import com.key.common.utils.web.Struts2Utils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.http.*;
import org.apache.http.client.entity.GzipDecompressingEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by keyuan on 17/9/8.
 */
public class TokenFilter implements Filter {

    private CheckToken checkToken;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest)request;
        String djytoken = req.getParameter("djyToken");
      //  String meetingId = req.getParameter("meetingId");
        System.out.println("djyToken....:"+djytoken);
//        if(meetingId !=null) {
//        	 req.getSession().setAttribute("meetingId",meetingId);
//        }
        if(djytoken!=null){
            req.getSession().setAttribute("djyToken",djytoken);
           
          //  boolean isSucc = getCheckToken().exeLogin(req,djytoken);
          //  System.out.println("isSuccess:"+isSucc);
            String reqURL = req.getRequestURI();
            if(reqURL.contains("login!login.action")){

            }
        }

        String rxtoken = req.getParameter("rx_token");
        System.out.println("rxtoken....:"+rxtoken);
        if(rxtoken!=null){
            req.getSession().setAttribute("rxToken",rxtoken);
        }
        chain.doFilter(req,response);
    }

    @Override
    public void destroy() {

    }

    public CheckToken getCheckToken() {
        if(checkToken==null){
            checkToken = SpringContextHolder.getBean(CheckToken.class);
        }
        return checkToken;
    }
}
