package com.key.common.plugs.security;

import com.key.common.base.entity.User;
import com.key.common.base.service.AccountManager;
import com.key.common.utils.DiaowenProperty;
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
import org.springframework.stereotype.Component;

import javax.net.ssl.*;
import javax.security.cert.CertificateException;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.URL;
import java.security.cert.X509Certificate;

/**
 * Created by keyuan on 17/9/20.
 */
@Component
public class CheckToken {

    @Autowired
    private AccountManager accountManager;
    @Autowired
    private FormAuthenticationWithLockFilter formAuthFilter;

    public boolean exeLogin(HttpServletRequest request, String djyToken){
        boolean isAuth=false;
        String error = "";
        try{
            Subject subject = SecurityUtils.getSubject();
            isAuth = subject.isAuthenticated();
            if(!isAuth){
                //根据token取sip
                //String result = checkToken(djyToken.toString());
                String result = checkToken01(djyToken);
                System.out.println("result-checktToken-in filter:"+result);
                JSONObject tokenJson = JSONObject.fromObject(result);
                //result = "{\"result\": 0,\"msg\": \"验证成功！\",\"info\": [{\"sap\": \"testsap123\",\"username\": \"张三\"}]}";
                int isSuccess = tokenJson.getInt("result");
                if(isSuccess == 0){
                    JSONArray userInfos = tokenJson.getJSONArray("info");
                    JSONObject userInfo = userInfos.getJSONObject(0);
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
            }
        }catch (Exception e){
            e.printStackTrace();;
        }
        System.out.println("error in Filter:"+error);
        return isAuth;
    }


    public String checkToken01(String djyToken) throws IOException {
        String urlNameString = DiaowenProperty.CHECKTOKEN_URL+"?djyToken="+djyToken;
        System.out.println("request:"+urlNameString);

        if(urlNameString.startsWith("https")){
            return httpsRequest(urlNameString,"GET",null);
        }else{
            DefaultHttpClient httpclient = new DefaultHttpClient();

            httpclient.addRequestInterceptor(new HttpRequestInterceptor() {

                public void process(
                        final HttpRequest request,
                        final HttpContext context) throws HttpException, IOException {
                    if (!request.containsHeader("Accept-Encoding")) {
                        request.addHeader("Accept-Encoding", "gzip");
                    }
                }

            });

            httpclient.addResponseInterceptor(new HttpResponseInterceptor() {

                public void process(
                        final HttpResponse response,
                        final HttpContext context) throws HttpException, IOException {
                    HttpEntity entity = response.getEntity();
                    Header ceheader = entity.getContentEncoding();
                    if (ceheader != null) {
                        HeaderElement[] codecs = ceheader.getElements();
                        for (int i = 0; i < codecs.length; i++) {
                            if (codecs[i].getName().equalsIgnoreCase("gzip")) {
                                response.setEntity(new GzipDecompressingEntity(response.getEntity()));
                                return;
                            }
                        }
                    }
                }

            });

            HttpGet httpget = new HttpGet(urlNameString);

            // Execute HTTP request
            System.out.println("executing request " + httpget.getURI());
            HttpResponse response = httpclient.execute(httpget);

            System.out.println("----------------------------------------");
            System.out.println(response.getStatusLine());
            System.out.println(response.getLastHeader("Content-Encoding"));
            System.out.println(response.getLastHeader("Content-Length"));
            System.out.println("----------------------------------------");

            HttpEntity entity = response.getEntity();
            String content = null;
            if (entity != null) {
                content = EntityUtils.toString(entity);
                System.out.println(content);
                System.out.println("----------------------------------------");
                System.out.println("Uncompressed size: "+content.length());

            }

            // When HttpClient instance is no longer needed,
            // shut down the connection manager to ensure
            // immediate deallocation of all system resources
            httpclient.getConnectionManager().shutdown();
            System.out.println("content:"+content);
            return content;
        }

    }

    public static String httpsRequest(String requestUrl,String requestMethod,String outputStr){
        StringBuffer buffer=null;
        try{
            TrustManager[] tm={new TrustAnyTrustManager()};
            System.out.println("eewwww_MyX509TrustManager");
            //初始化
            //创建SSLContext
//            SSLContext sslContext=SSLContext.getInstance("SSL");
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, tm, new java.security.SecureRandom());;
            //获取SSLSocketFactory对象
            SSLSocketFactory ssf=sslContext.getSocketFactory();
            URL url=new URL(requestUrl);
            HttpsURLConnection conn=(HttpsURLConnection)url.openConnection();
            //设置当前实例使用的SSLSoctetFactory
            conn.setSSLSocketFactory(ssf);
            conn.setHostnameVerifier(new TrustAnyHostnameVerifier());
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod(requestMethod);
           // conn.connect();
            //往服务器端写内容
            if(null!=outputStr){
                OutputStream os=conn.getOutputStream();
                os.write(outputStr.getBytes("utf-8"));
                os.close();
            }

            //读取服务器端返回的内容
            InputStream is=conn.getInputStream();
            InputStreamReader isr=new InputStreamReader(is,"utf-8");
            BufferedReader br=new BufferedReader(isr);
            buffer=new StringBuffer();
            String line=null;
            while((line=br.readLine())!=null){
                buffer.append(line);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return buffer.toString();
    }


    private static class TrustAnyTrustManager implements X509TrustManager{

        @Override
        public void checkClientTrusted(java.security.cert.X509Certificate[] x509Certificates, String s) throws java.security.cert.CertificateException {

        }

        @Override
        public void checkServerTrusted(java.security.cert.X509Certificate[] x509Certificates, String s) throws java.security.cert.CertificateException {

        }

        public X509Certificate[] getAcceptedIssuers()
        {
            return new X509Certificate[]{};
        }
    }

    private static class TrustAnyHostnameVerifier implements HostnameVerifier
    {
        public boolean verify(String hostname, SSLSession session)
        {
            return true;
        }
    }
}
