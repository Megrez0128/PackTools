//package com.zulong.web.saml;
//
//import utils.JsonUtil;
//import utils.RedisUtil;
////RedisSAMLFunction中用到的的JsonUtil，RedisUtil
//import javax.servlet.http.HttpServletRequest;
//
///**
// * 不需要缓存请求及回复
// */
//public class RedisSAMLFunction extends SAMLBaseFunction{
//
//    private String SPRedisSign;
//
//    private String getGoToURLRedisKey(String requestID){
//        return SPRedisSign + ":goToURL:" + requestID;
//    }
//
//    private String getUserInfoRedisKey(String requestID){
//        return SPRedisSign + ":userInfo:" + requestID;
//    }
//
//    public RedisSAMLFunction (String SPEntityID, String IDPSSODestination, String AssertionConsumerEndpoint,
//                        String SPCertificate, String SPPrivateKey, String IDPCertificate, String SPRedisSign,
//                        Integer authInfoExpiredTime, Integer userInfoExpiredTime, String logoutURL, String errorPageURL) throws Exception {
//        if(SPCertificate == null || SPCertificate.length() == 0){
//            logger.error("init: SPCertificate is null");
//        }else{
//            this.SPRedisSign = SPRedisSign;
//        }
//        init(SPEntityID, IDPSSODestination, AssertionConsumerEndpoint, SPCertificate, SPPrivateKey,
//                IDPCertificate, authInfoExpiredTime, userInfoExpiredTime, logoutURL, errorPageURL);
//    }
//
//    @Override
//    public void setGoToURLToSessionByRequestID(HttpServletRequest request, String requestID, String goToURL) throws Exception{
//        if(goToURL != null && goToURL.length() != 0){
//            RedisUtil.set(getGoToURLRedisKey(requestID), goToURL, authInfoExpiredTime);
//        }
//    }
//
//    @Override
//    public String getGoToURLFromSessionByRequestID(HttpServletRequest request, String requestID) throws Exception{
//        return RedisUtil.getString(getGoToURLRedisKey(requestID));
//    }
//
//    @Override
//    public boolean delGoToURLFromSessionByRequestID(HttpServletRequest request, String requestID) throws Exception {
//        return RedisUtil.delete(getGoToURLRedisKey(requestID));
//    }
//
//    @Override
//    public void setUserInfoInSessionByRequestID(HttpServletRequest request, String requestID, UserInfo userInfo) throws Exception {
//        RedisUtil.set(getUserInfoRedisKey(requestID), JsonUtil.TransToJson(userInfo), userInfoExpiredTime);
//    }
//
//    @Override
//    public UserInfo getUserInfoFromSessionByRequestID(HttpServletRequest request, String requestID) throws Exception  {
//        String strUserInfo = RedisUtil.getString(getUserInfoRedisKey(requestID));
//        if(strUserInfo == null || strUserInfo.length() == 0){
//            return null;
//        }else{
//            return JsonUtil.TransToObject(strUserInfo, UserInfo.class);
//        }
//    }
//
//    @Override
//    public boolean delUserInfoFromSessionByRequestID(HttpServletRequest request, String requestID) throws Exception {
//        return RedisUtil.delete(getUserInfoRedisKey(requestID));
//    }
//}
