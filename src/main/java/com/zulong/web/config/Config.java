//package com.zulong.web.config;
//import com.zulong.web.saml.ServletSessionSAMLFunction;
//import org.apache.commons.configuration2.HierarchicalConfiguration;
//import java.util.List;
//// 引入相关的包
//import org.apache.commons.configuration2.HierarchicalConfiguration;
//import org.apache.commons.configuration2.XMLConfiguration;
//import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
//import org.apache.commons.configuration2.builder.fluent.Parameters;
//import org.apache.commons.configuration2.ex.ConfigurationException;
//import org.apache.commons.configuration2.tree.ImmutableNode;
//import org.springframework.context.annotation.Configuration;
//
//import javax.annotation.PostConstruct;
//import java.util.ArrayList;
//import java.util.List;
//
//@Configuration
//public class Config {
//    // 定义一个samlFunction属性，它是一个ServletSessionSAMLFunction对象的实例
//    public static ServletSessionSAMLFunction samlFunction;
//    // 定义一个config属性，它是一个XMLConfiguration对象的实例
//    public static XMLConfiguration config;
//    @PostConstruct
//    // 定义一个静态的init()方法，用于初始化XMLConfiguration对象，并从saml.xml文件中读取配置信息
//    public static void init() {
//        // 创建一个FileBasedConfigurationBuilder对象
//        Parameters params = new Parameters();
//        FileBasedConfigurationBuilder<XMLConfiguration> builder =
//                new FileBasedConfigurationBuilder<XMLConfiguration>(XMLConfiguration.class)
//                        .configure(params.xml()
//                                .setFileName("saml.xml"));
//        try {
//            // 获取一个XMLConfiguration对象
//            config = builder.getConfiguration();
//        } catch (ConfigurationException cex) {
//            // 处理异常
//            cex.printStackTrace();
//        }
//        try {
//            List<HierarchicalConfiguration<ImmutableNode>> nodeList = config.configurationsAt("saml");
//            if(nodeList != null && !nodeList.isEmpty()){
//                HierarchicalConfiguration node = nodeList.get(0);
//                String SPEntityID = node.getString("SPEntityID");
//                String IDPSSODestination = node.getString("IDPSSODestination");
//                String AssertionConsumerEndpoint = node.getString("AssertionConsumerEndpoint");
//                String SPCertificate = node.getString("SPCertificate");
//                String SPPrivateKey = node.getString("SPPrivateKey");
//                String IDPCertificate = node.getString("IDPCertificate");
//                String SPRedisSign = node.getString("SPRedisSign", "ssmp");
//                Integer authInfoExpiredTime = node.getInteger("authInfoExpiredTime", null);
//                Integer userInfoExpiredTime = node.getInteger("userInfoExpiredTime", null);
//                String logoutURL = node.getString("logoutURL");
//                String errorPageURL = node.getString("errorPageURL");
//                // 使用上述变量作为参数，创建一个samlFunction对象
//                samlFunction = new ServletSessionSAMLFunction(SPEntityID, IDPSSODestination,
//                        AssertionConsumerEndpoint, SPCertificate,
//                        SPPrivateKey, IDPCertificate, SPRedisSign,
//                        authInfoExpiredTime, userInfoExpiredTime,
//                        logoutURL, errorPageURL);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static void setSAML() {
//        // 调用init()方法，确保XMLConfiguration对象已经初始化
//        init();
//        try {
//            List<HierarchicalConfiguration<ImmutableNode>> nodeList = config.configurationsAt("saml");
//            if(nodeList != null && !nodeList.isEmpty()){
//                HierarchicalConfiguration node = nodeList.get(0);
//                String SPEntityID = node.getString("SPEntityID");
//                String IDPSSODestination = node.getString("IDPSSODestination");
//                String AssertionConsumerEndpoint = node.getString("AssertionConsumerEndpoint");
//                String SPCertificate = node.getString("SPCertificate");
//                String SPPrivateKey = node.getString("SPPrivateKey");
//                String IDPCertificate = node.getString("IDPCertificate");
//                String SPRedisSign = node.getString("SPRedisSign", "ssmp");
//                Integer authInfoExpiredTime = node.getInteger("authInfoExpiredTime", null);
//                Integer userInfoExpiredTime = node.getInteger("userInfoExpiredTime", null);
//                String logoutURL = node.getString("logoutURL");
//                String errorPageURL = node.getString("errorPageURL");
//                // 使用上述变量作为参数，创建一个samlFunction对象
//                samlFunction = new ServletSessionSAMLFunction(SPEntityID, IDPSSODestination,
//                        AssertionConsumerEndpoint, SPCertificate,
//                        SPPrivateKey, IDPCertificate, SPRedisSign,
//                        authInfoExpiredTime, userInfoExpiredTime,
//                        logoutURL, errorPageURL);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}
