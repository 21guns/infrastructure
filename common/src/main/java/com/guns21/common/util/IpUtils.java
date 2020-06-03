package com.guns21.common.util;

import java.net.*;
import java.util.Enumeration;
import java.util.Optional;

/**
 * @author jliu
 * @date 2020/6/2
 */
public class IpUtils {

    public static Optional<String> getIpByName(String name) throws SocketException {
        NetworkInterface ni = NetworkInterface.getByName(name);
        Enumeration<InetAddress> inetAddresses = ni.getInetAddresses();
        while (inetAddresses.hasMoreElements()) {
            InetAddress ia = inetAddresses.nextElement();
            if (!ia.isLinkLocalAddress()) {
                return Optional.of(ia.getHostAddress());
            }
        }
        return Optional.empty();

    }

    private static Optional<InetAddress> getFirstNonLoopbackAddress() throws SocketException {
        Enumeration en = NetworkInterface.getNetworkInterfaces();
        while (en.hasMoreElements()) {
            NetworkInterface i = (NetworkInterface) en.nextElement();
            for (Enumeration en2 = i.getInetAddresses(); en2.hasMoreElements(); ) {
                InetAddress addr = (InetAddress) en2.nextElement();
                if (!addr.isLoopbackAddress()) {
                    return Optional.of(addr);

                }
            }
        }
        return Optional.empty();
    }
}
