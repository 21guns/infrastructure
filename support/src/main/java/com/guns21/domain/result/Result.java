package com.guns21.domain.result;

import java.util.HashMap;
import java.util.Map;

/**
 * 接口统一返回结果
 */
public class Result extends AbstractResult {

  public Result() {
  }

  public static Result getInstance() {
    return new Result();
  }

  public static Result success() {
    return success(Code.CODE_200.text, Code.CODE_200.code);
  }

  public static Result success(Object object) {
    return success(Code.CODE_200.text, Code.CODE_200.code, object);
  }

  /**
   * 输入参数是以key:value为键值对的数据
   *
   * @param objects
   * @return
   */
  public static Result success(Object... objects) {
    if (null != objects && objects.length > 0 && objects.length % 2 == 0) {
      Map map = new HashMap();
      for (int i = 0; i < objects.length; i += 2) {
        map.put(objects[i], objects[i + 1]);
      }
      return success(map);
    }
    throw new IllegalArgumentException("objects is illegal");
  }

  /**
   * 通用成功
   *
   * @param message 成功信息
   * @return 返回成功结果
   */
  public static Result success(String message) {
    return success(message, Code.CODE_200.code);
  }

  /**
   * 通用成功
   *
   * @param code    成功编码
   * @param message 成功信息
   * @return 返回成功结果
   */
  public static Result success(String message, String code) {
    return success(message, code, "");
  }

  /**
   * 通用成功
   *
   * @param code    成功编码
   * @param message 成功信息
   * @param object  对象信息
   * @return 返回成功结果
   */
  public static Result success(String message, String code, Object object) {
    return getInstance(Boolean.TRUE, message, code, object);
  }

  private static Result getInstance(Boolean aTrue, String message, String code, Object object) {
    Result result = getInstance();
    result.setSuccess(aTrue);
    result.setMessage(message);
    result.setCode(code);
    result.setData(object);
    return result;
  }

  /**
   * 通用异常
   *
   * @param message 失败信息
   * @return 返回失败信息和４０３
   */
  public static Result fail403(String message) {
    return fail(message, Code.CODE_403.code);
  }

  /**
   * 通用异常
   *
   * @param message 失败信息
   * @return 返回失败信息和500
   */
  public static Result fail(String message) {
    return fail(message, Code.CODE_500.code);
  }

  /**
   * 通用异常
   *
   * @param message 失败信息
   * @return 返回400失败
   */
  public static Result fail400(String message) {
    return fail(message, Code.CODE_400.code);
  }

  /**
   * 通用异常
   *
   * @return 返回400失败
   */
  public static Result fail400() {
    return fail(Code.CODE_400.text, Code.CODE_400.code);
  }

  public static Result fail() {
    return fail(Code.CODE_500.text, Code.CODE_500.code);
  }

  /**
   * 通用异常
   *
   * @param message 失败信息
   * @return 返回失败结果
   */
  public static Result fail(String message, String code) {
    return fail(message, code, null);
  }

  /**
   * 通用异常
   *
   * @param code    失败编码
   * @param message 失败信息
   * @param object  对象信息
   * @return 返回失败结果
   */
  public static Result fail(String message, String code, Object object) {
    return getInstance(Boolean.FALSE, message, code, object);
  }


}
