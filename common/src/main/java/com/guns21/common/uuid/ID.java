package com.guns21.common.uuid;

import com.sohu.idcenter.IdWorker;

import java.util.UUID;

public class ID {


  /**
   * 生成一个主键值
   *
   * @return 一个32位字符的十六进制大写主键
   */
  public static synchronized String get() {
    return UUID.randomUUID().toString().replace("-", "");
  }

  /**
   * 生成一个流水号
   *
   * @return 14位数字流水号
   */
  public static long generateId() {
    return SidWorker.nextSid();
  }

  public static void main(String[] args) {
    final long idepo = System.currentTimeMillis() - 3600 * 1000L;
    IdWorker iw = new IdWorker(1, 1, 0, 0);
//
//        for (int i = 0; i < 10; i++) {
//            System.out.println(iw.getId());
//        }
//        long nextId = 236424704167936l;//generateId();
//        System.out.println("==="+nextId);
//        long time = iw.getIdTimestamp(nextId);
//        System.out.println(time+" -> "+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(time)));


    long st = System.currentTimeMillis();
    final int max = 100000;
    for (int i = 0; i < max; i++) {

      System.err.println(SidWorker.nextSid());
    }
    long et = System.currentTimeMillis();
    System.out.println(1000 * max / (et - st) + "/s");

  }
}
