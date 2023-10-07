package com.guns21.common.util;

import com.guns21.common.exception.UnCheckException;

import java.net.*;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

/**
 * @see https://github.com/looly/hutool/blob/v5-master/hutool-core/src/main/java/cn/hutool/core/net/NetUtil.java
 * @author jliu
 * @date 2020/6/2
 */
public class IpUtils {
    public static final Integer IPV4_LENGTH = 4;

    /**
     * 获取所有满足过滤条件的本地IP地址对象
     *
     * @param addressFilter 过滤器，null表示不过滤，获取所有地址
     * @return 过滤后的地址对象列表
     */
    public static LinkedHashSet<InetAddress> localAddressList(Function<InetAddress, Boolean> addressFilter) {
        Enumeration<NetworkInterface> networkInterfaces;
        try {
            networkInterfaces = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            throw new UnCheckException(e);
        }

        if (networkInterfaces == null) {
            throw new UnCheckException("Get network interface error!");
        }

        final LinkedHashSet<InetAddress> ipSet = new LinkedHashSet<>();

        while (networkInterfaces.hasMoreElements()) {
            final NetworkInterface networkInterface = networkInterfaces.nextElement();
            final Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
            while (inetAddresses.hasMoreElements()) {
                final InetAddress inetAddress = inetAddresses.nextElement();
                if (inetAddress != null && (null == addressFilter || addressFilter.apply(inetAddress))) {
                    ipSet.add(inetAddress);
                }
            }
        }

        return ipSet;
    }

    /**
     * 获取本机网卡IP地址，规则如下：
     *
     * <pre>
     * 1. 查找所有网卡地址，必须非回路（loopback）地址、非局域网地址（siteLocal）、IPv4地址
     * 2. 如果无满足要求的地址，调用 {@link InetAddress#getLocalHost()} 获取地址
     * </pre>
     * <p>
     * 此方法不会抛出异常，获取失败将返回{@code null}<br>
     * <p>
     * 见：https://github.com/looly/hutool/issues/428
     *
     * @return 本机网卡IP地址，获取失败返回{@code null}
     */
    public static Optional<InetAddress> getLocalhost() {
        final LinkedHashSet<InetAddress> localAddressList = localAddressList(address -> {
            // 非loopback地址，指127.*.*.*的地址
            return false == address.isLoopbackAddress()
                    // 非地区本地地址，指10.0.0.0 ~ 10.255.255.255、172.16.0.0 ~ 172.31.255.255、192.168.0.0 ~ 192.168.255.255
                    && false == address.isSiteLocalAddress()
                    // 需为IPV4地址
                    && address instanceof Inet4Address;
        });

        if (ObjectUtils.nonEmpty(localAddressList)) {
            return Optional.of(localAddressList.iterator().next());
        }

        try {
            return Optional.of(InetAddress.getLocalHost());
        } catch (UnknownHostException e) {
            // ignore
        }

        return Optional.empty();
    }

    /**
     * 获取本机网卡IP地址，这个地址为所有网卡中非回路地址的第一个<br>
     * 如果获取失败调用 {@link InetAddress#getLocalHost()}方法获取。<br>
     * 此方法不会抛出异常，获取失败将返回{@code null}<br>
     * <p>
     * 参考：http://stackoverflow.com/questions/9481865/getting-the-ip-address-of-the-current-machine-using-java
     *
     * @return 本机网卡IP地址，获取失败返回{@code null}
     */
    public static Optional<String> getIp() {
        return getLocalhost().map(InetAddress::getHostAddress);
    }

    public static Optional<String> getHostName() {
        return getLocalhost().map(InetAddress::getHostName);
    }
    /**
     * 将字符串表示的ip地址转换为long表示.
     *
     * @param ip ip地址
     * @return 以32位整数表示的ip地址
     */
    public static long ipv4ToLong(final String ip) {
        if (!RegexChkUtils.checkIP(ip)) {
            throw new IllegalArgumentException("[" + ip + "]不是有效的ip地址");
        }
        final String[] ipNums = ip.split("\\.");
        return (Long.parseLong(ipNums[0]) << 24)
                + (Long.parseLong(ipNums[1]) << 16)
                + (Long.parseLong(ipNums[2]) << 8)
                + (Long.parseLong(ipNums[3]));
    }

    public static String[] getIps() {
        Optional<String> ip = getIp();
        if (ip.isPresent()) {
            if (!RegexChkUtils.checkIP(ip.get())) {
                throw new IllegalArgumentException("[" + ip + "]不是有效的ip地址");
            }
            return ip.get().split("\\.");
        } else {
            return null;
        }
    }

    public static boolean validate(String[] ips) {
        return Objects.nonNull(ips) && ips.length == IPV4_LENGTH;
    }
}
