package com.guns21.support.boot.config;

import com.guns21.common.exception.UnCheckException;
import com.guns21.common.util.IpUtils;
import com.guns21.common.util.ObjectUtils;
import com.guns21.support.idgen.IDGen;
import com.guns21.support.idgen.snowflake.LeadIDGen;
import com.guns21.support.idgen.snowflake.SohuIDGen;
import com.sohu.idcenter.IdWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

/**
 *  使用方式
 *  1.手动指定workerId 默认方式，不配置workerId是0
 *  2.使用ip生成workerId, 使用低16位进行运算
 *  3.使用id center 生成workerId
 */
@Configuration
@Slf4j
public class IDFactory {
    /**
     * 1
     */
    @Value("${com.guns21.id.worker:0}")
    private Long workerId;
    @Value("${com.guns21.id.data-center:0}")
    private Long dataCenterId;
    @Value("${com.guns21.id.sequence:0}")
    private Long sequence;
    @Value("${com.guns21.id.type:DEFAULT}")
    private IDType type;

    /**
     * 3
     */
    @Value("${com.guns21.id.center.url:#{null}}")
    private String url;

    private static IDGen idGen;

    public static long getId() {
        return idGen.getId();
    }

    @PostConstruct
    private void init() {
        /*
            时间差值-起始时间
         */
        Calendar calendar = Calendar.getInstance();
        calendar.set(2018, Calendar.JANUARY, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long ec = calendar.getTimeInMillis();

        log.info("id use type[{}]", type);
        switch (type) {
            case IP:
                String[] ips = IpUtils.getIps();
                if (IpUtils.validate(ips)) {
                    dataCenterId = Long.parseLong(ips[2]) % 32;
                    workerId = Long.parseLong(ips[3]) % 32;
                    log.info("ip[{}] --> workerId[{}], dataCenterId[{}]", Arrays.toString(ips), workerId, dataCenterId);
                    log.info("workerId[{}] dataCenterId[{}] sequence[{}] ec[{}]", workerId, dataCenterId, sequence, ec);
                    idGen = new SohuIDGen(workerId, dataCenterId, sequence, ec);
                } else  {
                    log.error("don't find ip");
                    throw new UnCheckException("don't find ip");
                }
                break;
            case CENTER:
                if (!ObjectUtils.hasText(url)) {
                    throw new UnCheckException("don't set url");
                }
                idGen = new LeadIDGen(url);
                break;
            default:
                log.info("workerId[{}] dataCenterId[{}] sequence[{}] ec[{}]", workerId, dataCenterId, sequence, ec);
                idGen = new SohuIDGen(workerId, dataCenterId, sequence, ec);
                break;
        }
    }

    public enum IDType {
        /**
         * 默认算法，
         */
        DEFAULT,
        /**
         * 使用ip计算workerId
         */
        IP,
        /**
         * 从id中心那workerId
         */
        CENTER

    }

//    @Bean
//    @ConditionalOnProperty(name="com.guns21.web.request.logging", havingValue="true")


    static class IdWorkThread implements Runnable {
        private final Set<Long> set;
        private final IdWorker idWorker;

        public IdWorkThread(Set<Long> set, IdWorker idWorker) {
            this.set = set;
            this.idWorker = idWorker;
        }

        public void run() {
            while (true) {
                long id = idWorker.getId();
                System.out.println(Thread.currentThread().getName()+"             real id:" + id);
                if (!set.add(id)) {
                    System.out.println("duplicate:" + id);
                }
            }
        }
    }
    public static void main(String[] args) {
//    final long idepo = System.currentTimeMillis() - 3600 * 1000L;
        IdWorker iw = new IdWorker(1, 1, 0, 0);

//        for (int i = 0; i < 10; i++) {
//            System.out.println(iw.getId());
//        }
//        long nextId = 236424704167936l;//generateId();
//        System.out.println("===" + nextId);
//        long time = iw.getIdTimestamp(nextId);
//        System.out.println(time + " -> " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(time)));
//
//
//        long st = System.currentTimeMillis();
//        final int max = 100000;
//        for (int i = 0; i < max; i++) {
//
//            System.err.println(SidWorker.nextSid());
//        }
//        long et = System.currentTimeMillis();
//        System.out.println(1000 * max / (et - st) + "/s");
//        System.err.println(ID.get());


        Set<Long> set = new HashSet<Long>();
        Calendar calendar = Calendar.getInstance();
        calendar.set(2018, Calendar.JANUARY, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long ec = calendar.getTimeInMillis();
        long idepo = System.currentTimeMillis() - 3600 * 1000L;
        final IdWorker idWorker1 = new IdWorker(0, 0,0, ec);
        final IdWorker idWorker2 = new IdWorker(1, 0,0, ec);
        Thread t1 = new Thread(new IdWorkThread(set, idWorker1));
        Thread t2 = new Thread(new IdWorkThread(set, idWorker2));
        t1.setDaemon(true);
        t2.setDaemon(true);
        t1.start();
        t2.start();
        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }
}
