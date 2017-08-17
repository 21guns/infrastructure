package com.guns21.support.domain;

import com.guns21.support.domain.result.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.util.Assert;

import java.util.Locale;

/**
 * Created by jliu on 16/9/27.
 */
public class ResultFactory {

  private MessageSource messages;

  public ResultFactory(MessageSource messages) {
    Assert.notNull(messages, "messages is null");
    this.messages = messages;
  }

  public Result fail(String code) {
    return toFailResult(getMessage(code));
  }

  public Result fail(String code, Object... objects) {
    return toFailResult(getMessage(code, objects));
  }

  private String getMessage(String code, Object... objects) {
    return messages.getMessage(code,
        objects, Locale.getDefault());
  }

  private Result toFailResult(String message) {
    String[] split = StringUtils.split(message, ":");
    if (split.length == 2) {
      return Result.fail(split[1], split[0]);
    } else {
      return Result.fail(message);
    }
  }
}
