package com.chat.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

import static com.chat.util.SessionUtil.getRequest;
import static com.chat.util.StringUtil.isBlank;

public class RequestUtil {
    public String getClientIp() {
        final String LOCALHOST_IPV4 = "127.0.0.1";
        final String LOCALHOST_IPV6 = "0:0:0:0:0:0:0:1";

        String ipAddress = getRequest().getHeader("X-Forwarded-For");
        if (isBlank(ipAddress) || "unknown".equalsIgnoreCase(ipAddress))
            ipAddress = getRequest().getHeader("Proxy-Client-IP");

        if (isBlank(ipAddress) || "unknown".equalsIgnoreCase(ipAddress))
            ipAddress = getRequest().getHeader("WL-Proxy-Client-IP");

        if (isBlank(ipAddress) || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = getRequest().getRemoteAddr();
            if (LOCALHOST_IPV4.equals(ipAddress) || LOCALHOST_IPV6.equals(ipAddress))
                try {
                    InetAddress inetAddress = InetAddress.getLocalHost();
                    ipAddress = inetAddress.getHostAddress();
                } catch (UnknownHostException e) {
                }
        }

        if (!isBlank(ipAddress) && ipAddress.length() > 15 && ipAddress.indexOf(",") > 0)
            ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));

        return ipAddress;
    }
}
