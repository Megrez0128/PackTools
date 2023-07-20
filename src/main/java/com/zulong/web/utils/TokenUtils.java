package com.zulong.web.utils;

import java.util.Date;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.zulong.web.dao.UserDao;
import com.zulong.web.dao.daoimpl.UserDaoImpl;
import com.zulong.web.entity.User;
import com.zulong.web.log.LoggerManager;
import com.zulong.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


public class TokenUtils {//token到期时间10小时

    private static final long EXPIRE_TIME = 10 * 60 * 60 * 1000;
    //密钥盐
    private static final String TOKEN_SECRET = "ljdyaishijin**3nkjnj??";

    public static String getCurr_user_id() {
        return TokenUtils.curr_user_id;
    }

    private static String curr_user_id;

    public static boolean isAdmin() {
        return TokenUtils.admin;
    }

    public static void setAdmin(boolean admin) {
        TokenUtils.admin = admin;
    }

    private static boolean admin;


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
//
//
//    /**
//     * token验证
//     * @param token
//     * @return
//     */
    public static String verify(String token) {

        try {
            //创建token验证器

            JWTVerifier jwtVerifier=JWT.require(Algorithm.HMAC256(TOKEN_SECRET)).withIssuer("auth0").build();
            DecodedJWT decodedJWT=jwtVerifier.verify(token);
            String user_id = decodedJWT.getClaim("user_id").asString();
            LoggerManager.logger().info(String.format(
                    "[com.zulong.web.utils.TokenUtils]TokenUtils.verify@verify token|token=%s", token));
            LoggerManager.logger().info(String.format(
                    "[com.zulong.web.utils.TokenUtils]TokenUtils.verify@verify token|user_id=%s", user_id));
            curr_user_id = user_id;
            //抛出错误即为验证不通过
            return curr_user_id;
        }catch (Exception e){
            return null;
        }

    }
}
