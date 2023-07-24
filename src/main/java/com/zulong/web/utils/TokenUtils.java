package com.zulong.web.utils;

import java.util.ArrayList;
import java.util.Date;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.zulong.web.entity.User;
import com.zulong.web.log.LoggerManager;



public class TokenUtils {//token到期时间10小时

    private static final long EXPIRE_TIME = 10 * 60 * 60 * 1000;
    //密钥盐
    private static final String TOKEN_SECRET = "ljdyaishijin**3nkjnj??";

    public static String getCurrUserId(String token) {
        try {
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            JWTVerifier jwtVerifier=JWT.require(Algorithm.HMAC256(TOKEN_SECRET)).withIssuer("auth0").build();
            DecodedJWT decodedJWT=jwtVerifier.verify(token);
            String user_id = decodedJWT.getClaim("user_id").asString();
            LoggerManager.logger().info(String.format(
                    "[com.zulong.web.utils.TokenUtils]TokenUtils.getCurrUserId@get token|user_id=%s", user_id));
            return user_id;
        }catch (Exception e){
            LoggerManager.logger().info(String.format(
                    "[com.zulong.web.utils.TokenUtils]TokenUtils.getCurrUserId@get token failed"));
            return null;
        }
    }

    public static boolean isAdmin() {
        return TokenUtils.admin;
    }

    public static void setAdmin(boolean admin) {
        TokenUtils.admin = admin;
    }

    private static boolean admin;

    private static ArrayList<String> blackList = new ArrayList<>();

    private static void addToBlackList(String token){
        blackList.add(token);
    }

    public static String getToken(String user_id, String ip){
        //todo:加上ip
        String token=null;
        try {
            Date expireAt=new Date(System.currentTimeMillis()+EXPIRE_TIME);
            token = JWT.create()
                    //发行人
                    .withIssuer("auth0")
                    //存放数据
                    .withClaim("user_id",user_id)
                    //过期时间
                    .withExpiresAt(expireAt)
                    .sign(Algorithm.HMAC256(TOKEN_SECRET));
            LoggerManager.logger().info(String.format(
                    "[com.zulong.web.utils.TokenUtils]TokenUtils.sign@create token|token=%s|user_id = %s", token, user_id));
        } catch (Exception e) {

        }
        return token;
    }
    public static String sign(User user){
        String token=null;
        try {
            Date expireAt=new Date(System.currentTimeMillis()+EXPIRE_TIME);
            token = JWT.create()
                    //发行人
                    .withIssuer("auth0")
                    //存放数据
                    .withClaim("user_id",user.getUser_id())
                    //过期时间
                    .withExpiresAt(expireAt)
                    .sign(Algorithm.HMAC256(TOKEN_SECRET));
            LoggerManager.logger().info(String.format(
                    "[com.zulong.web.utils.TokenUtils]TokenUtils.sign@create token|token=%s|user_id = %s", token, user.getUser_id()));
        } catch (Exception e) {

        }
        return token;
    }

    public static String loginOut(String token){
        try {
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            JWTVerifier jwtVerifier=JWT.require(Algorithm.HMAC256(TOKEN_SECRET)).withIssuer("auth0").build();
            DecodedJWT decodedJWT=jwtVerifier.verify(token);
            String user_id = decodedJWT.getClaim("user_id").asString();
            LoggerManager.logger().info(String.format(
                    "[com.zulong.web.utils.TokenUtils]TokenUtils.loginOut@get token|user_id=%s", user_id));
            addToBlackList(token);
            return user_id;
        }catch (Exception e){
            LoggerManager.logger().info(String.format(
                    "[com.zulong.web.utils.TokenUtils]TokenUtils.loginOut@get token failed"));
            return null;
        }

    }

    /**
     * token验证
     * @param token
     * @return
     */
    public static String verify(String token) {
        if(blackList.contains(token)){
            return null;
        }
        try {
            //创建token验证器
            //用token黑名单实现token的撤销
            JWTVerifier jwtVerifier=JWT.require(Algorithm.HMAC256(TOKEN_SECRET)).withIssuer("auth0").build();
            DecodedJWT decodedJWT=jwtVerifier.verify(token);
            String user_id = decodedJWT.getClaim("user_id").asString();
            LoggerManager.logger().info(String.format(
                    "[com.zulong.web.utils.TokenUtils]TokenUtils.verify@verify token|token=%s", token));
            LoggerManager.logger().info(String.format(
                    "[com.zulong.web.utils.TokenUtils]TokenUtils.verify@verify token|user_id=%s", user_id));
            return user_id;
        }catch (Exception e){
            return null;
        }

    }
}
