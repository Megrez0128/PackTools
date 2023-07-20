//package com.zulong.web.saml;
//
//import org.opensaml.saml.saml2.core.*;
//import com.zulong.web.log.LoggerManager;
//import org.opensaml.saml.saml2.core.Response;
//import org.opensaml.saml.saml2.core.impl.*;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
////
////
//public class SAMLService {
//    @RequestMapping(value = "/saml/login")
//    public void samlLogin(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "goToURL", required = false) String goToURL) {
//        String ip = CommonTools.getRemoteIp(request);
//        try {
//            //IdP
//            SAMLBaseFunction.UserInfo userInfo = Config.samlFunction.getUserInfoByCookie(request);
//            if(userInfo == null){
//                //IdPIdP
//                Config.samlFunction.redirectToIdPLoginURL(response, goToURL);
//            }else{
//                //IdpIdPSP
//                redirectToURL(userInfo, goToURL, request, response);
//            }
//        } catch (Exception e) {
//            LoggerManager.logger().error("samlLogin: exception,ip={}", ip, e);
//        }
//    }
////    private void redirectToURL(SAMLBaseFunction.UserInfo userInfo, String goToURL, HttpServletRequest request, HttpServletResponse response) throws IOException, IllegalAccessException {
////        String ip = CommonTools.getRemoteIp(request);
////        //IdPSP
////        AbstractResult result = getLoginResult(userInfo.getUserName(), ip);
////        //Tokenindex
////        if(goToURL == null){
////            StringBuffer url = request.getRequestURL();
////            goToURL = url.delete(url.length() - request.getRequestURI().length(), url.length()).
////                    append("/").toString();
////        }
////        goToURL = HttpClientUtils.appendToURLByObject(goToURL, result);
////        response.sendRedirect(goToURL);
////    }
////
////    @RequestMapping(value = "/saml/auth")
////    public void samlAuth(HttpServletRequest request, HttpServletResponse response) {
////        String ip = CommonTools.getRemoteIp(request);
////        try {
////            //SAML ResponserequestIDIdP
////            Pair<String, SAMLBaseFunction.UserInfo> res = Config.samlFunction.
////                    getAndSetUserInfoByIDPResponse(request);
////            if(res == null){
//////                logger.error("samlAuth: get userInfo failed,ip={}", ip);
////                return;
////            }
////            String requestID = res.first;
////            //IdPSP
////            redirectToURL(res.second, Config.samlFunction.getGoToURLFromSessionByRequestID
////                    (requestID), request, response);
////        } catch (Exception e) {
//////            logger.error("samlAuth: exception,ip={}", ip, e);
////        }
////    }
//}
