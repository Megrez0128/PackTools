//package com.zulong.web.saml;
//
//import javax.servlet.http.HttpServletRequest;
///**
// * 不需要缓存请求及回复
// */
//public class ServletSessionSAMLFunction extends SAMLBaseFunction{
//
//    private String getGoToURLRedisKey(){
//        return "goToURL";
//    }
//
//    private String getUserInfoRedisKey(){
//        return "userInfo";
//    }
//
//    public ServletSessionSAMLFunction(String SPEntityID, String IDPSSODestination, String AssertionConsumerEndpoint,
//                                      String SPCertificate, String SPPrivateKey, String IDPCertificate, String SPRedisSign,
//                                      Integer authInfoExpiredTime, Integer userInfoExpiredTime, String logoutURL, String errorPageURL) throws Exception {
//        init(SPEntityID, IDPSSODestination, AssertionConsumerEndpoint, SPCertificate, SPPrivateKey,
//                IDPCertificate, authInfoExpiredTime, userInfoExpiredTime, logoutURL, errorPageURL);
//    }
//
//    @Override
//    public void setGoToURLToSessionByRequestID(HttpServletRequest request, String requestID, String goToURL) throws Exception{
//        if(goToURL != null && goToURL.length() != 0){
//            request.getSession().setAttribute(getGoToURLRedisKey(), goToURL);
//        }
//    }
//
//    @Override
//    public String getGoToURLFromSessionByRequestID(HttpServletRequest request, String requestID) throws Exception{
//        return (String)request.getSession().getAttribute(getGoToURLRedisKey());
//    }
//
//    @Override
//    public boolean delGoToURLFromSessionByRequestID(HttpServletRequest request, String requestID) throws Exception {
//        request.getSession().setAttribute(getGoToURLRedisKey(), null);
//        return true;
//    }
//
//    @Override
//    public void setUserInfoInSessionByRequestID(HttpServletRequest request, String requestID, UserInfo userInfo) throws Exception {
//        request.getSession().setAttribute(getUserInfoRedisKey(), userInfo);
//    }
//
//    @Override
//    public UserInfo getUserInfoFromSessionByRequestID(HttpServletRequest request, String requestID) throws Exception  {
//        return (UserInfo)request.getSession().getAttribute(getUserInfoRedisKey());
//    }
//
//    @Override
//    public boolean delUserInfoFromSessionByRequestID(HttpServletRequest request, String requestID) throws Exception {
//        request.getSession().setAttribute(getUserInfoRedisKey(), null);
//        return true;
//    }
//}
