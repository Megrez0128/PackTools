//package com.zulong.web.saml;
//
////import net.shibboleth.utilities.java.support.component.ComponentInitializationException;
//import net.shibboleth.utilities.java.support.httpclient.HttpClientBuilder;
//import net.shibboleth.utilities.java.support.resolver.CriteriaSet;
//import net.shibboleth.utilities.java.support.resolver.Criterion;
//import net.shibboleth.utilities.java.support.resolver.ResolverException;
//
//import net.shibboleth.utilities.java.support.security.impl.RandomIdentifierGenerationStrategy;
//import org.opensaml.core.config.InitializationException;
//import org.opensaml.core.config.InitializationService;
//import org.opensaml.core.criterion.EntityIdCriterion;
//import org.opensaml.core.xml.XMLObject;
//import org.opensaml.core.xml.XMLObjectBuilderFactory;
//import org.opensaml.core.xml.config.XMLObjectProviderRegistrySupport;
//import org.opensaml.core.xml.io.Marshaller;
//import org.opensaml.core.xml.io.MarshallingException;
//import org.opensaml.core.xml.schema.XSString;
//import org.opensaml.messaging.context.InOutOperationContext;
//import org.opensaml.messaging.context.MessageContext;
//import org.opensaml.messaging.handler.MessageHandler;
//import org.opensaml.messaging.handler.MessageHandlerException;
//import org.opensaml.messaging.handler.impl.BasicMessageHandlerChain;
//import org.opensaml.messaging.pipeline.httpclient.BasicHttpClientMessagePipeline;
//import org.opensaml.messaging.pipeline.httpclient.HttpClientMessagePipeline;
//import org.opensaml.profile.context.ProfileRequestContext;
//import org.opensaml.saml.common.SAMLObject;
//import org.opensaml.saml.common.SignableSAMLObject;
//import org.opensaml.saml.common.binding.security.impl.MessageLifetimeSecurityHandler;
//import org.opensaml.saml.common.binding.security.impl.ReceivedEndpointSecurityHandler;
//import org.opensaml.saml.common.binding.security.impl.SAMLOutboundProtocolMessageSigningHandler;
//import org.opensaml.saml.common.messaging.context.SAMLEndpointContext;
//import org.opensaml.saml.common.messaging.context.SAMLMessageInfoContext;
//import org.opensaml.saml.common.messaging.context.SAMLPeerEntityContext;
//import org.opensaml.saml.common.xml.SAMLConstants;
//import org.opensaml.saml.saml2.binding.decoding.impl.HTTPPostDecoder;
//import org.opensaml.saml.saml2.binding.decoding.impl.HttpClientResponseSOAP11Decoder;
//import org.opensaml.saml.saml2.binding.encoding.impl.HTTPRedirectDeflateEncoder;
//import org.opensaml.saml.saml2.binding.encoding.impl.HttpClientRequestSOAP11Encoder;
//import org.opensaml.saml.saml2.core.*;
//import org.opensaml.saml.saml2.encryption.Decrypter;
//import org.opensaml.saml.saml2.metadata.Endpoint;
//import org.opensaml.saml.saml2.metadata.SingleSignOnService;
//import org.opensaml.saml.security.impl.SAMLSignatureProfileValidator;
//import org.opensaml.security.credential.Credential;
//import org.opensaml.security.credential.impl.KeyStoreCredentialResolver;
//import org.opensaml.security.x509.BasicX509Credential;
//import org.opensaml.soap.client.http.AbstractPipelineHttpSOAPClient;
//import org.opensaml.xmlsec.SignatureSigningParameters;
//import org.opensaml.xmlsec.config.JavaCryptoValidationInitializer;
//import org.opensaml.xmlsec.context.SecurityParametersContext;
//import org.opensaml.xmlsec.encryption.support.DecryptionException;
//import org.opensaml.xmlsec.encryption.support.InlineEncryptedKeyResolver;
//import org.opensaml.xmlsec.keyinfo.impl.StaticKeyInfoCredentialResolver;
//import org.opensaml.xmlsec.signature.support.SignatureConstants;
//import org.opensaml.xmlsec.signature.support.SignatureValidator;
//import org.opensaml.xmlsec.signature.support.SignatureException;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import javax.servlet.http.Cookie;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.xml.namespace.QName;
//import javax.xml.transform.OutputKeys;
//import javax.xml.transform.Transformer;
//import javax.xml.transform.TransformerException;
//import javax.xml.transform.TransformerFactory;
//import javax.xml.transform.dom.DOMSource;
//import javax.xml.transform.stream.StreamResult;
//import java.io.*;
//import java.net.URLEncoder;
//import java.security.*;
//import java.security.cert.CertificateException;
//import java.security.cert.CertificateFactory;
//import java.security.cert.X509Certificate;
//import java.security.spec.InvalidKeySpecException;
//import java.security.spec.PKCS8EncodedKeySpec;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import static com.fasterxml.jackson.databind.type.LogicalType.DateTime;
//
//public abstract class SAMLBaseFunction {
//
//    protected Logger logger = LoggerFactory.getLogger(this.getClass());
//    protected int authInfoExpiredTime = 2 * 60;
//    protected int userInfoExpiredTime = 30 * 60;
//
//    /**
//     * 根据requestID设置登录成功后最终跳转的URL
//     */
//    public abstract void setGoToURLToSessionByRequestID(HttpServletRequest request, String requestID, String goToURL)throws Exception;
//
//    /**
//     * 根据requestID获取登录成功后的跳转URL
//     */
//    public abstract String getGoToURLFromSessionByRequestID(HttpServletRequest request, String requestID)throws Exception;
//
//    /**
//     * 根据requestID删除登录成功后的跳转URL
//     */
//    public abstract boolean delGoToURLFromSessionByRequestID(HttpServletRequest request, String requestID)throws Exception;
//    /**
//     * 根据requestID设置用户信息
//     */
//    public abstract void setUserInfoInSessionByRequestID(HttpServletRequest request, String requestID, UserInfo userInfo) throws Exception;
//
//    /**
//     * 根据requestID获取用户信息
//     * @return
//     */
//    public abstract UserInfo getUserInfoFromSessionByRequestID(HttpServletRequest request, String requestID)throws Exception;
//
//    /**
//     * 根据requestID删除用户信息
//     * @param requestID
//     * @return
//     * @throws Exception
//     */
//    public abstract boolean delUserInfoFromSessionByRequestID(HttpServletRequest request, String requestID)throws Exception;
//
//    private static final String COOKIE_REQUEST_ID = "requestID";
//    private static final String FRIENDLY_NAME_USERNAME = "uid";
//    private static final String FRIENDLY_NAME_MAIL = "eduPersonPrincipalName";
//
//    private RandomIdentifierGenerationStrategy secureRandomIdGenerator;
//    private String SPEntityID;
//    //IDP地址：用于在登录失效后生成登录请求和重定向
//    private String IDPSSODestination;
//    //SP地址： 也就是SAML断言返回的地址
//    private String AssertionConsumerEndpoint;
//    private Credential credential;
//    private Credential idpCredential;
//    public String logoutURL;
//    public String errorPageURL;
//
//    /**
//     * 使用keystore文件加载证书信息
//     * @param SPEntityID
//     * @param IDPSSODestination
//     * @param AssertionConsumerEndpoint
//     * @param KEY_STORE_PASSWORD
//     * @param KEY_STORE_ENTRY_PASSWORD
//     * @param KEY_STORE_PATH
//     * @param KEY_ENTRY_ID
//     * @throws InitializationException
//     */
//    public boolean init(String SPEntityID, String IDPSSODestination, String AssertionConsumerEndpoint,
//                     String KEY_STORE_PASSWORD, String KEY_STORE_ENTRY_PASSWORD, String KEY_STORE_PATH,
//                     String KEY_ENTRY_ID, Integer authInfoExpiredTime, Integer userInfoExpiredTime, String logoutURL,
//                        String errorPageURL) throws Exception {
//        secureRandomIdGenerator = new RandomIdentifierGenerationStrategy();
//        this.SPEntityID = SPEntityID;
//        this.IDPSSODestination = IDPSSODestination;
//        this.AssertionConsumerEndpoint = AssertionConsumerEndpoint;
//        credential = getCredentialByKeyStoreFile(KEY_STORE_PASSWORD, KEY_STORE_ENTRY_PASSWORD, KEY_STORE_PATH, KEY_ENTRY_ID);
//        init();
//        if(authInfoExpiredTime != null){
//            this.authInfoExpiredTime = authInfoExpiredTime;
//        }
//        if(userInfoExpiredTime != null){
//            this.userInfoExpiredTime = userInfoExpiredTime;
//        }
//        this.logoutURL = logoutURL;
//        this.errorPageURL = errorPageURL;
//        return true;
//    }
//
//    /**
//     * 使用base64字符串加载证书信息
//     * @param SPEntityID
//     * @param IDPSSODestination
//     * @param AssertionConsumerEndpoint
//     * @param SPCertificate
//     * @param SPPrivateKey
//     * @param IDPCertificate
//     * @throws Exception
//     */
//    public boolean init(String SPEntityID, String IDPSSODestination, String AssertionConsumerEndpoint,
//                        String SPCertificate, String SPPrivateKey, String IDPCertificate, Integer authInfoExpiredTime,
//                        Integer userInfoExpiredTime, String logoutURL, String errorPageURL) throws Exception {
//        secureRandomIdGenerator = new RandomIdentifierGenerationStrategy();
//        this.SPEntityID = SPEntityID;
//        this.IDPSSODestination = IDPSSODestination;
//        this.AssertionConsumerEndpoint = AssertionConsumerEndpoint;
//        credential = getCredential(SPCertificate, SPPrivateKey);
//        idpCredential = getCredential(IDPCertificate, null);
//        init();
//        if(authInfoExpiredTime != null){
//            this.authInfoExpiredTime = authInfoExpiredTime;
//        }
//        if(userInfoExpiredTime != null){
//            this.userInfoExpiredTime = userInfoExpiredTime;
//        }
//        this.logoutURL = logoutURL;
//        this.errorPageURL = errorPageURL;
//        return true;
//    }
//
//    public void init() throws InitializationException {
//        JavaCryptoValidationInitializer javaCryptoValidationInitializer = new JavaCryptoValidationInitializer();
//        try {
//            javaCryptoValidationInitializer.init();
//        } catch (InitializationException e) {
//            logger.error("init:init exception", e);
//            throw e;
//        }
//
//        for (Provider jceProvider : Security.getProviders()) {
//            logger.info(jceProvider.getInfo());
//        }
//
//        try {
//            logger.info("Initializing");
//            InitializationService.initialize();
//        } catch (InitializationException e) {
//            logger.error("init:initialize exception", e);
//            throw e;
//        }
//    }
//
//    private KeyStore readKeystoreFromFile(String filePath, String keyStorePassword) {
//        try {
//            KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
//            InputStream inputStream = new FileInputStream(filePath);
//            keystore.load(inputStream, keyStorePassword.toCharArray());
//            inputStream.close();
//            return keystore;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    private Credential getCredentialByKeyStoreFile(String KEY_ENTRY_ID, String KEY_STORE_ENTRY_PASSWORD, String KEY_STORE_PATH,
//                                                          String KEY_STORE_PASSWORD){
//        try {
//            Map<String, String> passwordMap = new HashMap<>();
//            passwordMap.put(KEY_ENTRY_ID, KEY_STORE_ENTRY_PASSWORD);
//
//            KeyStore keystore = readKeystoreFromFile(KEY_STORE_PATH, KEY_STORE_PASSWORD);
//            Criterion criterion = new EntityIdCriterion(KEY_ENTRY_ID);
//            CriteriaSet criteriaSet = new CriteriaSet();
//            criteriaSet.add(criterion);
//
//            KeyStoreCredentialResolver resolver = new KeyStoreCredentialResolver(keystore, passwordMap);
//            return resolver.resolveSingle(criteriaSet);
//        } catch (ResolverException e) {
//            logger.error("getCredentialByKeyStoreFile:exception,KEY_ENTRY_ID={},KEY_STORE_ENTRY_PASSWORD={},KEY_STORE_PATH={},KEY_STORE_PASSWORD={}",
//                    KEY_ENTRY_ID, KEY_STORE_ENTRY_PASSWORD, KEY_STORE_PATH, KEY_STORE_PASSWORD, e);
//            return null;
//        }
//    }
//
//    private BasicX509Credential getCredential(String certificate, String privateKey) throws Exception {
//        if(privateKey == null){
//            return getCredential(Base64Util.decode(certificate), null);
//        }else{
//            return getCredential(Base64Util.decode(certificate), Base64Util.decode(privateKey));
//        }
//    }
//
//    private PrivateKey generatePrivateKey(byte[] privateKey) throws InvalidKeySpecException, NoSuchAlgorithmException, UnsupportedEncodingException {
//        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(privateKey);
//        KeyFactory factory = KeyFactory.getInstance("RSA");
//        return factory.generatePrivate(spec);
//    }
//
//    private BasicX509Credential getCredential(byte[] certificate, byte[] privateKey) throws CertificateException, IOException, InvalidKeySpecException, NoSuchAlgorithmException {
//        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
//        InputStream inputStream = new ByteArrayInputStream(certificate);
//        X509Certificate x509Certificate = (X509Certificate) certificateFactory.generateCertificate(inputStream);
//        inputStream.close();
//        BasicX509Credential credential;
//        if(privateKey != null){
//            credential = new BasicX509Credential(x509Certificate, generatePrivateKey(privateKey));
//        }else{
//            credential = new BasicX509Credential(x509Certificate);
//        }
//        return credential;
//    }
//
//    private static <T> T buildSAMLObject(final Class<T> clazz) throws NoSuchFieldException, IllegalAccessException {
//        XMLObjectBuilderFactory builderFactory = XMLObjectProviderRegistrySupport.getBuilderFactory();
//        QName defaultElementName = (QName) clazz.getDeclaredField("DEFAULT_ELEMENT_NAME").get(null);
//        T object = (T) builderFactory.getBuilder(defaultElementName).buildObject(defaultElementName);
//        return object;
//    }
//
//    private static String getSAMLObjectString(final XMLObject object) throws MarshallingException, TransformerException {
//        if (!(object instanceof SignableSAMLObject) || !((SignableSAMLObject) object).isSigned() || object.getDOM() == null) {
//            Marshaller out = XMLObjectProviderRegistrySupport.getMarshallerFactory().getMarshaller(object);
//            out.marshall(object);
//        }
//
//        Transformer transformer = TransformerFactory.newInstance().newTransformer();
//        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
//        StreamResult result = new StreamResult(new StringWriter());
//        DOMSource source = new DOMSource(object.getDOM());
//
//        transformer.transform(source, result);
//        return result.getWriter().toString();
//    }
//
//    private String generateSecureRandomId() {
//        return secureRandomIdGenerator.generateIdentifier();
//    }
//
//    private AuthnRequest buildAuthnRequest(String requestID) throws NoSuchFieldException, IllegalAccessException {
//        AuthnRequest authnRequest = buildSAMLObject(AuthnRequest.class);
//        //请求时间：该对象创建的时间，以判断其时效性
//        authnRequest.setIssueInstant(new DateTime());
//        //目标URL：目标地址，IDP地址
//        authnRequest.setDestination(IDPSSODestination);
//        //传输SAML断言所需要的绑定：也就是用何种协议使用Artifact来取回真正的认证信息，这里希望以POST返回讯息
//        authnRequest.setProtocolBinding(SAMLConstants.SAML2_POST_BINDING_URI);
//        //SP地址： 也就是SAML断言返回的地址
//        authnRequest.setAssertionConsumerServiceURL(AssertionConsumerEndpoint);
//        //请求的ID：为当前请求设置ID，一般为随机数j
//        authnRequest.setID(requestID);
//        //Issuer： 发行人信息，也就是SP的ID，一般是SP的URL
//        authnRequest.setIssuer(buildIssuer());
//        //NameID：IDP对于用户身份的标识；NameID policy是SP关于NameID是如何创建的说明
//        authnRequest.setNameIDPolicy(buildNameIdPolicy());
//        // 请求认证上下文（requested Authentication Context）:
//        // SP对于认证的要求，包含SP希望IDP如何验证用户，也就是IDP要依据什么来验证用户身份。
//        authnRequest.setRequestedAuthnContext(buildRequestedAuthnContext());
//        return authnRequest;
//    }
//
//    private Issuer buildIssuer() throws NoSuchFieldException, IllegalAccessException {
//        Issuer issuer = buildSAMLObject(Issuer.class);
//        issuer.setValue(SPEntityID);
//        return issuer;
//    }
//
//    private Endpoint getIPDEndpoint() throws NoSuchFieldException, IllegalAccessException {
//        SingleSignOnService endpoint = buildSAMLObject(SingleSignOnService.class);
//        endpoint.setBinding(SAMLConstants.SAML2_REDIRECT_BINDING_URI);
//        endpoint.setLocation(IDPSSODestination);
//        return endpoint;
//    }
//
//    private RequestedAuthnContext buildRequestedAuthnContext() throws NoSuchFieldException, IllegalAccessException {
//        RequestedAuthnContext requestedAuthnContext = buildSAMLObject(RequestedAuthnContext.class);
//        requestedAuthnContext.setComparison(AuthnContextComparisonTypeEnumeration.MINIMUM);
//        AuthnContextClassRef passwordAuthnContextClassRef = buildSAMLObject(AuthnContextClassRef.class);
//        passwordAuthnContextClassRef.setAuthnContextClassRef(AuthnContext.PASSWORD_AUTHN_CTX);
//        requestedAuthnContext.getAuthnContextClassRefs().add(passwordAuthnContextClassRef);
//        return requestedAuthnContext;
//    }
//
//    private NameIDPolicy buildNameIdPolicy() throws NoSuchFieldException, IllegalAccessException {
//        NameIDPolicy nameIDPolicy = buildSAMLObject(NameIDPolicy.class);
//        nameIDPolicy.setAllowCreate(true);
//        nameIDPolicy.setFormat(NameIDType.TRANSIENT);
//        return nameIDPolicy;
//    }
//
//    public void redirectToIDPLoginURL(HttpServletRequest request, HttpServletResponse response, String goToURL){
//        String requestID = generateSecureRandomId();
//        redirectToIDPLoginURL(request, response, requestID, goToURL);
//    }
//
//    /**
//     * 登录信息过期时调用此方法将浏览器重定向至IDP进行身份检查
//     */
//    public void redirectToIDPLoginURL(HttpServletRequest request, HttpServletResponse response, String requestID, String goToURL){
//        try {
//            setGoToURLToSessionByRequestID(request, requestID, goToURL);
//            //设置cookie
//            Cookie cookie = new Cookie(COOKIE_REQUEST_ID, requestID);
//            cookie.setPath("/");
//            response.addCookie(cookie);
//            //生成IDP请求
//            AuthnRequest authnRequest = buildAuthnRequest(requestID);
//            //创建主环境上下文
//            MessageContext context = new MessageContext();
//            context.setMessage(authnRequest);
//            SAMLPeerEntityContext peerEntityContext = context.getSubcontext(SAMLPeerEntityContext.class, true);
//            SAMLEndpointContext endpointContext = peerEntityContext.getSubcontext(SAMLEndpointContext.class, true);
//            endpointContext.setEndpoint(getIPDEndpoint());
//            //数据签名，可选
//            if(credential != null){
//                SignatureSigningParameters signatureSigningParameters = new SignatureSigningParameters();
//                signatureSigningParameters.setSigningCredential(credential);
//                signatureSigningParameters.setSignatureAlgorithm(SignatureConstants.ALGO_ID_SIGNATURE_RSA_SHA256);
//                context.getSubcontext(SecurityParametersContext.class, true).setSignatureSigningParameters(signatureSigningParameters);
//            }
//            HTTPRedirectDeflateEncoder encoder = new HTTPRedirectDeflateEncoder();
//            encoder.setMessageContext(context);
//            encoder.setHttpServletResponse(response);
//            encoder.initialize();
//            encoder.encode();
//        }catch (Exception e){
//            logger.error("redirectUserForAuthentication:exception", e);
//        }
//    }
//
//    private Artifact buildArtifactFromRequest(final HttpServletRequest req) throws NoSuchFieldException, IllegalAccessException {
//        Artifact artifact = buildSAMLObject(Artifact.class);
//        artifact.setArtifact(req.getParameter("SAMLart"));
//        return artifact;
//    }
//
//    private ArtifactResolve buildArtifactResolve(final Artifact artifact) throws NoSuchFieldException, IllegalAccessException {
//        ArtifactResolve artifactResolve = buildSAMLObject(ArtifactResolve.class);
//        Issuer issuer = buildSAMLObject(Issuer.class);
//        issuer.setValue(SPEntityID);
//        artifactResolve.setIssuer(issuer);
//        artifactResolve.setIssueInstant(new DateTime());
//        artifactResolve.setID(generateSecureRandomId());
//        artifactResolve.setDestination(AssertionConsumerEndpoint);
//        artifactResolve.setArtifact(artifact);
//        return artifactResolve;
//    }
//
//    /**
//     * 去IDP获取用户信息
//     * @param artifactResolve
//     * @return
//     */
//    private ArtifactResponse sendAndReceiveArtifactResolve(final ArtifactResolve artifactResolve) throws Exception {
//        MessageContext<ArtifactResolve> contextout = new MessageContext<>();
//        contextout.setMessage(artifactResolve);
//        //数据签名，可选
//        if(credential != null){
//            SignatureSigningParameters signatureSigningParameters = new SignatureSigningParameters();
//            signatureSigningParameters.setSignatureAlgorithm(SignatureConstants.ALGO_ID_SIGNATURE_RSA_SHA256);
//            signatureSigningParameters.setSigningCredential(credential);
//            signatureSigningParameters.setSignatureCanonicalizationAlgorithm(SignatureConstants.ALGO_ID_C14N_EXCL_OMIT_COMMENTS);
//            SecurityParametersContext securityParametersContext = contextout.getSubcontext(SecurityParametersContext.class, true);
//            securityParametersContext.setSignatureSigningParameters(signatureSigningParameters);
//        }
//
//        InOutOperationContext<ArtifactResponse, ArtifactResolve> context = new ProfileRequestContext<>();
//        context.setOutboundMessageContext(contextout);
//        AbstractPipelineHttpSOAPClient<SAMLObject, SAMLObject> soapClient = new AbstractPipelineHttpSOAPClient() {
//            protected HttpClientMessagePipeline newPipeline() {
//                HttpClientRequestSOAP11Encoder encoder = new HttpClientRequestSOAP11Encoder();
//                HttpClientResponseSOAP11Decoder decoder = new HttpClientResponseSOAP11Decoder();
//                BasicHttpClientMessagePipeline pipeline = new BasicHttpClientMessagePipeline(
//                        encoder,
//                        decoder
//                );
//
//                pipeline.setOutboundPayloadHandler(new SAMLOutboundProtocolMessageSigningHandler());
//                return pipeline;
//            }};
//
//        HttpClientBuilder clientBuilder = new HttpClientBuilder();
//        soapClient.setHttpClient(clientBuilder.buildClient());
//        soapClient.send(AssertionConsumerEndpoint, context);
//        return context.getInboundMessageContext().getMessage();
//    }
//
//    private void validateDestinationAndLifetime(ArtifactResponse artifactResponse, HttpServletRequest request) throws ComponentInitializationException, MessageHandlerException {
//        MessageContext context = new MessageContext<ArtifactResponse>();
//        context.setMessage(artifactResponse);
//
//        SAMLMessageInfoContext messageInfoContext = context.getSubcontext(SAMLMessageInfoContext.class, true);
//        messageInfoContext.setMessageIssueInstant(artifactResponse.getIssueInstant());
//
//        MessageLifetimeSecurityHandler lifetimeSecurityHandler = new MessageLifetimeSecurityHandler();
//        lifetimeSecurityHandler.setClockSkew(1000);
//        lifetimeSecurityHandler.setMessageLifetime(2000);
//        lifetimeSecurityHandler.setRequiredRule(true);
//
//        ReceivedEndpointSecurityHandler receivedEndpointSecurityHandler = new ReceivedEndpointSecurityHandler();
//        receivedEndpointSecurityHandler.setHttpServletRequest(request);
//        List handlers = new ArrayList<MessageHandler>();
//        handlers.add(lifetimeSecurityHandler);
//        handlers.add(receivedEndpointSecurityHandler);
//
//        BasicMessageHandlerChain<ArtifactResponse> handlerChain = new BasicMessageHandlerChain<ArtifactResponse>();
//        handlerChain.setHandlers(handlers);
//        handlerChain.initialize();
//        handlerChain.doInvoke(context);
//    }
//
//    private EncryptedAssertion getEncryptedAssertion(ArtifactResponse artifactResponse) {
//        Response response = (Response)artifactResponse.getMessage();
//        return response.getEncryptedAssertions().get(0);
//    }
//
//    private Assertion decryptAssertion(EncryptedAssertion encryptedAssertion) throws DecryptionException {
//        StaticKeyInfoCredentialResolver keyInfoCredentialResolver = new StaticKeyInfoCredentialResolver(credential);
//        Decrypter decrypter = new Decrypter(null, keyInfoCredentialResolver, new InlineEncryptedKeyResolver());
//        decrypter.setRootInNewDocument(true);
//        return decrypter.decrypt(encryptedAssertion);
//    }
//
//    private void verifyAssertionSignature(Assertion assertion) throws SignatureException {
//        if (!assertion.isSigned()) {
//            throw new RuntimeException("The SAML Assertion was not signed");
//        }
//
//        SAMLSignatureProfileValidator profileValidator = new SAMLSignatureProfileValidator();
//        profileValidator.validate(assertion.getSignature());
//        SignatureValidator.validate(assertion.getSignature(), credential);
//    }
//
//    private void logAssertionAttributes(Assertion assertion) {
//        for (Attribute attribute : assertion.getAttributeStatements().get(0).getAttributes()) {
//            logger.info("Attribute name: " + attribute.getName());
//            for (XMLObject attributeValue : attribute.getAttributeValues()) {
//                logger.info("Attribute value: " + ((XSString) attributeValue).getValue());
//            }
//        }
//    }
//
//    private void logAuthenticationInstant(Assertion assertion) {
//        logger.info("Authentication instant: " + assertion.getAuthnStatements().get(0).getAuthnInstant());
//    }
//
//    private void logAuthenticationMethod(Assertion assertion) {
//        logger.info("Authentication method: " + assertion.getAuthnStatements().get(0)
//                .getAuthnContext().getAuthnContextClassRef().getAuthnContextClassRef());
//    }
//
//    /**
//     * 从IDP获取用户信息
//     * HTTP Artifact Binding使用此方法获取断言
//     * @param req
//     * @throws Exception
//     */
//    public Assertion getAssertionByArtifact(final HttpServletRequest req) throws Exception {
//        //获得断言信息的引用
//        Artifact artifact = buildArtifactFromRequest(req);
//        ArtifactResolve artifactResolve = buildArtifactResolve(artifact);
//        //获取真正的断言
//        ArtifactResponse artifactResponse = sendAndReceiveArtifactResolve(artifactResolve);
//        validateDestinationAndLifetime(artifactResponse, req);
//        EncryptedAssertion encryptedAssertion = getEncryptedAssertion(artifactResponse);
//        return decryptAssertion(encryptedAssertion);
//    }
//
//    /**
//     * 从IDP获取用户信息，并返回requestID和用户信息
//     * HTTP post Binding使用此方法获取断言
//     * @return
//     */
//    public Pair<String, UserInfo> getAndSetUserInfoByIDPResponse(final HttpServletRequest request){
//        try {
//            String responseMessage = request.getParameter("SAMLResponse");
//            if(responseMessage == null || responseMessage.length() == 0){
//                logger.error("getAssertionByRequest:SAMLResponse is null");
//                return null;
//            }
//
//            HTTPPostDecoder httpPostDecoder = new HTTPPostDecoder();
//            httpPostDecoder.setHttpServletRequest(request);
//            httpPostDecoder.initialize();
//            httpPostDecoder.decode();
//            final MessageContext<SAMLObject> messageContext = httpPostDecoder.getMessageContext();
//            Response response = (Response) messageContext.getMessage();
//            List<Assertion> assertionList = response.getAssertions();
//            if(assertionList == null || assertionList.isEmpty()){
//                logger.error("getAssertionByRequest:no assertion,responseMessage={}", responseMessage);
//            }
//            Assertion assertion = assertionList.get(0);
//            try {
//                SignatureValidator.validate(assertion.getSignature(), idpCredential);
//            }catch (SignatureException e){
//                logger.error("getAssertionByRequest:validate failed,responseMessage={}", responseMessage, e);
//                return null;
//            }
//            //根据request设置用户信息
//            String requestID = response.getInResponseTo();
//            return getAndSetUserInfo(request, assertion, requestID);
//        }catch (Exception e){
//            logger.error("getAssertionByRequest:exception", e);
//            return null;
//        }
//    }
//
//    private Pair<String, UserInfo> getAndSetUserInfo(HttpServletRequest request, Assertion assertion, String requestID) throws Exception {
//        Map<String, List<String>> attrMap = getAttrValByAssertion(assertion);
//        String userName = attrMap.get(FRIENDLY_NAME_USERNAME).get(0);
//        String mail = attrMap.get(FRIENDLY_NAME_MAIL).get(0);
//        UserInfo userInfo = new UserInfo(userName, mail);
//        setUserInfoInSessionByRequestID(request, requestID, userInfo);
//        return new Pair<>(requestID, userInfo);
//    }
//
//    public Map<String, List<String>> getAttrValByAssertion(Assertion assertion){
//        Map<String, List<String>> attrMap = new HashMap<>();
//        List<AttributeStatement> attributeStatementList = assertion.getAttributeStatements();
//        for(AttributeStatement attributeStatement : attributeStatementList){
//            for(Attribute attribute : attributeStatement.getAttributes()){
//                String name = attribute.getFriendlyName();
//                List<String> attrValList = new ArrayList<>();
//                attrMap.put(name, attrValList);
//
//                for(XMLObject xmlObject : attribute.getAttributeValues()){
//                    XSString val = (XSString)xmlObject;
//                    attrValList.add(val.getValue());
//                }
//            }
//        }
//        return attrMap;
//    }
//
//    public UserInfo getUserInfoByCookie(HttpServletRequest request) throws Exception {
//        Cookie cookie = getCookie(request, COOKIE_REQUEST_ID);
//        if(cookie == null){
//            return null;
//        }
//        return getUserInfoFromSessionByRequestID(request, cookie.getValue());
//    }
//
//    public Cookie getCookie(HttpServletRequest request, String cookieName){
//        Cookie[] cookies = request.getCookies();
//        if(cookies == null)return null;
//        for(Cookie cookie : cookies){
//            if(cookie.getName().equals(cookieName)){
//                return cookie;
//            }
//        }
//        return null;
//    }
//
//    public boolean delInfoByCookie(HttpServletRequest request) throws Exception {
//        Cookie cookie = getCookie(request, COOKIE_REQUEST_ID);
//        if(cookie == null){
//            return true;
//        }
//        if(!delGoToURLFromSessionByRequestID(request, cookie.getValue())){
//            return false;
//        }
//        if(!delUserInfoFromSessionByRequestID(request, cookie.getValue())){
//            return false;
//        }
//        return true;
//    }
//
//    public static class UserInfo{
//        private String userName;
//        private String mail;
//
//        public UserInfo() {
//        }
//
//        public UserInfo(String userName, String mail) {
//            this.userName = userName;
//            this.mail = mail;
//        }
//
//        public String getUserName() {
//            return userName;
//        }
//
//        public void setUserName(String userName) {
//            this.userName = userName;
//        }
//
//        public String getMail() {
//            return mail;
//        }
//
//        public void setMail(String mail) {
//            this.mail = mail;
//        }
//    }
//
//    public void redirectToErrorPage(String message, String source, HttpServletResponse response) throws IOException {
//        String url = appendToURL(errorPageURL, "message", message);
//        url = appendToURL(url, "source", source);
//        response.sendRedirect(url);
//    }
//
//    private static String appendToURL(String url, String name, String value) throws UnsupportedEncodingException {
//        if(!url.contains("?")){
//            url = url + "?";
//        }else{
//            url = url + "&";
//        }
//        url = url + name + "=" + URLEncoder.encode(value, "UTF-8").replaceAll("\\+", "%20");;
//        return url;
//    }
//
//    public static void main(String[] args) throws Exception {
////        Credential credential1 = getCredentialByKeyStoreFile("ssmp", "123s56..", "C:\\Users\\MSI-NB\\Desktop\\祖龙证书\\ssmp\\ca.keystore", "123s56..");
//
////        String certificateS = "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";
////        String privateKey = "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";
////        Credential credential1 = getCredential(certificateS, privateKey);
//
//        //获取用户信息
////        String subject = assertion.getSubject().getNameID().getValue();
////        String issuer = assertion.getIssuer().getValue();
////        String audience = assertion.getConditions().getAudienceRestrictions().get(0).getAudiences().get(0).getAudienceURI();
////        String statusCode = responseObj.getStatus().getStatusCode().getValue();
//    }
//}
