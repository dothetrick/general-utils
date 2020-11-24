package utils;

import java.net.*;
import java.util.Enumeration;

public class EnvUtils {

    /**
     * 获取服务器地址
     *
     * @return Ip地址
     */
    public static String getServerIp() throws UnknownHostException, SocketException {
        // 获取操作系统类型
        String sysType = System.getProperties().getProperty("os.name");
        String ip;
        // 如果是Windows系统，获取本地IP地址
        if (sysType.toLowerCase().startsWith("win")) {
            String localIP = null;
            localIP = InetAddress.getLocalHost().getHostAddress();
            if (localIP != null) {
                return localIP;
            }
        } else {
            // 兼容Linux
            ip = getIpByEthNum("eth0");
            if (ip != null) {
                return ip;
            }
        }
        throw new UnknownHostException("get ip error!");
    }

    /**
     * 根据网络接口获取IP地址
     *
     * @param ethNum 网络接口名，Linux下是eth0
     * @return
     */
    private static String getIpByEthNum(String ethNum) throws SocketException, UnknownHostException {
        Enumeration allNetInterfaces = NetworkInterface.getNetworkInterfaces();
        InetAddress ip;
        while (allNetInterfaces.hasMoreElements()) {
            NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
            if (ethNum.equals(netInterface.getName())) {
                Enumeration addresses = netInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    ip = (InetAddress) addresses.nextElement();
                    if (ip != null)
                        if (ip instanceof Inet4Address) {
                            return ip.getHostAddress();
                        }
                }
            }
        }
        throw new UnknownHostException("get ip error!");
    }
}
