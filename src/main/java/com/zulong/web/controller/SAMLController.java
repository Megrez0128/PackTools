//package com.zulong.web.controller;
//
//import com.zulong.web.config.Config;
//import com.zulong.web.saml.Pair;
//import com.zulong.web.saml.SAMLBaseFunction;
//import com.zulong.web.utils.CommonTools;
//import com.zulong.web.utils.TokenUtils;
//import org.apache.http.client.utils.HttpClientUtils;
//import org.opensaml.saml.saml2.core.*;
//import com.zulong.web.log.LoggerManager;
//import org.opensaml.saml.saml2.core.Response;
//import org.opensaml.saml.saml2.core.impl.*;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//public class SAMLController {
//        @RequestMapping(value = "/saml/login")
//    public void samlLogin(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "goToURL", required = false) String goToURL) {
//        String ip = CommonTools.getRemoteIp(request);
//        try {
//            ///查看Idp登录信息是否存在
//            SAMLBaseFunction.UserInfo userInfo = Config.samlFunction.getUserInfoByCookie(request);
//            if(userInfo == null){
//                //IdP登录信息不存在，则直接回调至IdP
//                Config.samlFunction.redirectToIDPLoginURL(request, response, goToURL);//redirectToIdPLoginURL(request, response, goToURL);
//            }else{
//                ///do登录信息存在，根据IdP登录信息获取SP的登录信息，并回调至指定资源页
//                redirectToURL(userInfo, goToURL, request, response);
//            }
//        } catch (Exception e) {
//            LoggerManager.logger().error("samlLogin: exception,ip={}", ip, e);
//        }
//    }
//    private void redirectToURL(SAMLBaseFunction.UserInfo userInfo, String goToURL, HttpServletRequest request, HttpServletResponse response) throws IOException, IllegalAccessException {
//        String ip = CommonTools.getRemoteIp(request);
//        //根据TdP信息获取 SP系统的登录情况
//        String token = TokenUtils.getToken(userInfo.getUserName(), ip);
//        //Tokenindex
//        if(goToURL == null){
//            StringBuffer url = request.getRequestURL();
//            goToURL = url.delete(url.length() - request.getRequestURI().length(), url.length()).
//                    append("/").toString();
//        }
//        response.addHeader("Authentication", "Bearer " + token);
////        goToURL = HttpClientUtils.appendToURLByObject(goToURL, result);
//        response.sendRedirect(goToURL);
//    }
//
//    @RequestMapping(value = "/saml/auth")
//    public void samlAuth(HttpServletRequest request, HttpServletResponse response) {
//        String ip = CommonTools.getRemoteIp(request);
//        try {
//            //SAML ResponserequestIDIdP
//            Pair<String, SAMLBaseFunction.UserInfo> res = Config.samlFunction.
//                    getAndSetUserInfoByIDPResponse(request);
//            if(res == null){
////                logger.error("samlAuth: get userInfo failed,ip={}", ip);
//                return;
//            }
//            String requestID = res.first;
//            //IdPSP
//            redirectToURL(res.second, Config.samlFunction.getGoToURLFromSessionByRequestID
//                    (request, requestID), request, response);
//        } catch (Exception e) {
////            logger.error("samlAuth: exception,ip={}", ip, e);
//        }
//    }
//}
