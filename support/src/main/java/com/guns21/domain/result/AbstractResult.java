package com.guns21.domain.result;

/**
 * Created by jliu on 16/7/19.
 */
public class AbstractResult {

  protected String code;
  protected Boolean success;
  protected String message;
  protected Object data;


  public enum Code {
    CODE_200("200", "请求成功！"),
    CODE_400("400", "请求无效！"),
    CODE_500("500", "执行失败！"),
    CODE_403("403", "未授权"),

    TYPE_VIOLATION("1020", "数据类型不合法"),
    MISS_PARAMETER("1021", "丢失参数");

    protected String code;
    protected String text;

    Code(String code, String text) {
      this.code = code;
      this.text = text;
    }

    public String getCode() {
      return code;
    }

    public String getText() {
      return text;
    }
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public Boolean getSuccess() {
    return success;
  }

  public void setSuccess(Boolean success) {
    this.success = success;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public Object getData() {
    return data;
  }

  public void setData(Object data) {
    this.data = data;
  }
}
