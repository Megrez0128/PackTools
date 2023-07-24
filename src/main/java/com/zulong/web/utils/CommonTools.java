package com.zulong.web.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.util.DigestUtils;

public final class CommonTools {

    /**
     * 判断当天天数是否在之前天数的7天以后
     */
    public static boolean ifAfter7Days(Date now, Date before){
        return (now.getTime()-before.getTime())>7*24*3600*1000;
    }

    /**
     * @return Description 判断一个字符串是不是十进制整形
     */
    public static boolean isDexInteger(String s) {
        for (char c : s.toCharArray()) {
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断是否是数值
     */
    public static boolean isFloat(String f){
        try{
            Float.parseFloat(f);
        }catch(Exception e){
            return false;
        }
        return true;
    }

    /**
     * 验证人民币格式,如;0.00,0.05,1.00,1,1.5
     */
    public static boolean isRmb(String s) {
        String regEx = "^[0-9]+(\\.[0-9]{1,2})?$";
        return s.matches(regEx);
    }

    /**
     * 将分转化成元
     */
    public static String convertRmb(String amount){
        int fee = Integer.parseInt(amount);
        return convertRmb(fee);

    }

    public static String convertRmb(int amount){
        int yuan = amount /100;
        int fen = amount %100;
        if(fen > 9){
            return yuan + "." + fen;
        }else{
            return yuan + ".0" + fen;
        }
    }

    /**
     * 是否空值
     */
    public static boolean isNULLOrEmpty(String s) {
        return s == null || s.trim().length() == 0;
    }

    /**
     * 取当前日期，格式自定义
     */
    public static String getCurrentTime(String dateFormat) {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        return formatter.format(currentTime);
    }

    /**
     * @return Description 获取和今天相差的日期，并格式化，days是相隔的天数，负数是往前的日期
     */
    public static String getDateByWeek(String dateFormat, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, days);
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        return formatter.format(calendar.getTime());
    }

    public static String getRemoteIp(HttpServletRequest request) {
        return getRemoteIp(request, false);
    }

    /**
     * 获得客户端IP
     */
    public static String getRemoteIp(HttpServletRequest request, boolean useFrontData) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");

        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        String[] ips = ip.split(",");
        if (ips.length > 1) {
            if(useFrontData) {
                ip = ips[0];
            } else {
                ip = ips[ips.length - 1];
            }
        }
        return ip.trim();
    }

    public static Integer convertIpToInteger(String ip) {
        String[] ips = ip.split("\\.");
        Integer x1 = new Integer(ips[0].trim());
        Integer x2 = new Integer(ips[1].trim());
        Integer x3 = new Integer(ips[2].trim());
        Integer x4 = new Integer(ips[3].trim());
        return (((0x000000ff & x1) << 24) | ((0x000000ff & x2) << 16)
                | ((0x000000ff & x3) << 8) | ((0x000000ff & x4)));
    }

    public static int bswap(int i)
    {
        int b0 = i & 0xff;
        int b1 = (i >> 8) & 0xff;
        int b2 = (i >> 16) & 0xff;
        int b3 = (i >> 24) & 0xff;
        return 0xff000000 & ((b0) << 24) | 0xff0000 & ((b1) << 16) | 0xff00 & ((b2) << 8) | 0xff & b3;
    }

    public static String getIP(int ip)
    {
        int _ip = bswap(ip);
        try
        {
            String r = ((_ip >> 24) & 0xFF) + "." + ((_ip >> 16) & 0xFF) + "." + ((_ip >> 8) & 0xFF) + "." + (_ip & 0xFF);
            return r;
        } catch (Exception e)
        {
            e.printStackTrace(System.out);
        }
        return "";
    }

    public static int getIP(String ip)
    {
        String[] ts = ip.split("\\.");
        if (ts.length == 4)
        {
            int d1 = Integer.parseInt(ts[0]);
            int d2 = Integer.parseInt(ts[1]);
            int d3 = Integer.parseInt(ts[2]);
            int d4 = Integer.parseInt(ts[3]);
            return (d4 << 24 | d3 << 16 | d2 << 8 | d1);
        }
        return 0;
    }

    /**
     * 签名字符串
     * @param s 给定字符串参数
     * @return 对给定字符串参数获得一个不可逆的签名（MD5）字符串。
     */
    public static String digest(String s) {
        //	return DigestUtils.md5Hex(s);
        return DigestUtils.md5DigestAsHex(s.getBytes());
    }

    /**
     * 将密保卡xy坐标转为字符表示
     */
    public static String getCoordinates(Integer x, Integer y) {
        return String.valueOf(((char) (65 + y))) + (x + 1);
    }

    /**
     * 编号 转为用户id
     * @param code int
     * @return int
     */
    public static int userCodeToID(int code) {
        return (code ^ 0x5ACD35A1);
    }

    /**
     * 账号id转为编号
     * @param userid int
     * @return int
     */
    public static int userIDToCode(int userid) {
        return (userid ^ 0x5ACD35A1);
    }

    public static String appendParam(String returnStr, String paramId, String paramValue) {
        if (!returnStr.equals("")) {
            if (!paramValue.equals("")) {
                returnStr = returnStr + "&" + paramId + "=" + paramValue;
            }
        } else {
            returnStr = paramId + "=" + paramValue;
        }
        return returnStr;
    }

    /**
     * 提供精确的小数位四舍五入处理。
     * @param v 需要四舍五入的数字
     * @param scale 小数点后保留几位
     * @return 四舍五入后的结果
     */

    public static double round(double v, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }

        BigDecimal b = new BigDecimal(Double.toString(v));
        BigDecimal one = new BigDecimal("1");
        return b.divide(one, scale, RoundingMode.HALF_UP).doubleValue();
    }

    public static long longId(HttpSession session, String id) {
        Object obj = session.getAttribute(id);
        if (obj == null) {
            return 0L;
        }
        return Long.parseLong(obj.toString());
    }

    // 判断所在运行环境是否为windows系统环境
    public static boolean isWinEnv() {
        Properties pros = System.getProperties();
        String osName = pros.getProperty("os.name");
        // 默认情况下为Linux系统
        return osName != null && osName.toLowerCase().startsWith("win");
    }

    public static String getWebInfIndex() {
        URL url = CommonTools.class.getProtectionDomain().getCodeSource().getLocation();
        String webPath = url.toString();
        int index = webPath.indexOf("WEB-INF");
        if (index == -1) {
            return null;
        }

        webPath = webPath.substring(0, index);
        webPath = webPath + "WEB-INF/";
        if (webPath.startsWith("zip")) {//当class文件在war中时，此时返回zip:D:/...这样的路径
            webPath = webPath.substring(4);
        } else if (webPath.startsWith("file")) {//当class文件在class文件中时，此时返回file:/D:/...这样的路径
            webPath = webPath.substring(6);
        } else if (webPath.startsWith("jar")) {//当class文件在jar文件里面时，此时返回jar:file:/D:/...这样的路径
            webPath = webPath.substring(10);
        }
        //linux 需要加入根目录标识
        if (!CommonTools.isWinEnv()) {
            webPath = "/" + webPath;
        }
        return webPath;
    }

    /**
     * 从列表中随机选择元素
     * @param list 源列表
     * @param needCount 选择数量
     * @param cutSource 是否将选择的元素从源序列中剔除，以一个独立列表返回
     */
    public static <T> List<T> selectFromList(List<T> list, int needCount, boolean cutSource){
        if (list == null || list.isEmpty()) {
            return list;
        }
        if(list.size() <= needCount) {
            if (cutSource) {
                List<T> resultList = new ArrayList<>(list);
                list.clear();
                return resultList;
            }
            else {
                return list;
            }
        }

        int amount = list.size();
        for(int i = 0; i < amount; i++) {
            Random r = new Random();
            int randomNum1 = r.nextInt(amount);
            int randomNum2 = r.nextInt(amount);
            swap(list, randomNum1, randomNum2);
        }

        if (cutSource) {
            List<T> resultList = new ArrayList<>(list.subList(0, needCount));
            if (needCount > 0) {
                list.subList(0, needCount).clear();
            }
            return resultList;
        }
        else {
            return list.subList(0, needCount);
        }
    }

    /**
     * 按权重随机抽取元素，并加入已有元素集合中
     * @param currentItems 已有元素集合
     * @param itemAndPower 元素权重表
     * @param selectNum 选择数量
     */
    public static <T> void randomlySelectByPower(Set<T> currentItems, Map<T, Integer> itemAndPower, int selectNum) {
        Iterator<T> iterator = itemAndPower.keySet().iterator();
        while (iterator.hasNext()) {
            T item = iterator.next();
            if (itemAndPower.get(item) <= 0) {
                iterator.remove();
            }
        }

        if (itemAndPower.size() <= selectNum) {
            currentItems.addAll(itemAndPower.keySet());
            return;
        }

        int sumPower = 0;
        for (int power : itemAndPower.values()) {
            sumPower += power;
        }

        Random random = new Random();
        int destination = currentItems.size() + selectNum;
        while (!itemAndPower.isEmpty() && currentItems.size() < destination) {
            int sum = 0;
            int index = random.nextInt(sumPower) + 1;
            for (Map.Entry<T, Integer> entry : itemAndPower.entrySet()) {
                sum += entry.getValue();
                if (sum >= index) {
                    currentItems.add(entry.getKey());
                    sumPower -= entry.getValue();
                    itemAndPower.remove(entry.getKey());
                    break;
                }
            }
        }
    }

    public static <T> void swap(List<T> list, int i, int j) {
        if(i != j) {
            T tmp = list.get(i);
            list.set(i, list.get(j));
            list.set(j, tmp);
        }
    }

    public static void main(String[] args) throws Exception {
//		List<Integer> list = new ArrayList<Integer>();
//		list.add(0);
//		list.add(1);
//		list.add(2);
//		list.add(3);
//		list.add(4);
//		list.add(5);
//		list.add(6);
//		list.add(7);
//		list.add(8);
//		list.add(9);
//		list.add(10);
//		System.out.println(Arrays.toString(selectFromList(list, 5).toArray()));

//        System.out.print(JsonUtil.TransToJson(RandomUtil.getRandomNumberWithoutRedundant(3, 0, 50)));
    }
}
