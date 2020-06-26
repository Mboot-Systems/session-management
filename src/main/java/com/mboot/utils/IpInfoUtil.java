package com.mboot.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Mboot
 * @date 2020-06-26 17:03
 */
@Slf4j
@Component
public class IpInfoUtil {

	public String getIpAddr(HttpServletRequest request) {

		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
			if ("127.0.0.1".equals(ip)) {
				InetAddress inet = null;
				try {
					inet = InetAddress.getLocalHost();
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
				ip = inet.getHostAddress();
			}
		}
		if (ip != null && ip.length() > 15) {
			if (ip.indexOf(",") > 0) {
				ip = ip.substring(0, ip.indexOf(","));
			}
		}
		if ("0:0:0:0:0:0:0:1".equals(ip)) {
			ip = "127.0.0.1";
		}
		return ip;
	}

}
