package com.guns21.web.method.resolver;

import com.guns21.support.exception.IllegalInputArgumentException;
import com.guns21.web.bind.annotation.RequireUuid;
import com.guns21.web.constant.SpringConstant;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.annotation.ValueConstants;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.annotation.AbstractNamedValueMethodArgumentResolver;
import org.springframework.web.multipart.support.MultipartResolutionDelegate;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @see org.springframework.web.method.annotation.RequestParamMethodArgumentResolver
 */
public class RequireUuidMethodArgumentResolver extends AbstractNamedValueMethodArgumentResolver {

  private Pattern pattern = Pattern.compile(SpringConstant.UUID_REGEX_32);

  public RequireUuidMethodArgumentResolver() {
  }

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    if (!parameter.hasParameterAnnotation(RequireUuid.class)) {
      return false;
    }
    return true;
  }

  @Override
  protected NamedValueInfo createNamedValueInfo(MethodParameter parameter) {
    RequireUuid ann = parameter.getParameterAnnotation(RequireUuid.class);
    return (ann != null ? new RequireUuidNamedValueInfo(ann) : new RequireUuidNamedValueInfo());
  }

  @Override
  protected Object resolveName(String name, MethodParameter parameter, NativeWebRequest request) throws Exception {
    Class<?> parameterType = parameter.getParameterType();
    if (!parameterType.isAssignableFrom(String.class)) {
      throw new IllegalArgumentException("just support String for RequireUuid ");
    }
    HttpServletRequest servletRequest = request.getNativeRequest(HttpServletRequest.class);

    Object mpArg = MultipartResolutionDelegate.resolveMultipartArgument(name, parameter, servletRequest);
    if (mpArg != MultipartResolutionDelegate.UNRESOLVABLE) {
      return mpArg;
    }

    String value = null;
    String[] paramValues = request.getParameterValues(name);
    if (paramValues != null) {
      if (paramValues.length != 1) {
        throw new IllegalArgumentException("just support one params for  RequireUuid");
      } else {
        Matcher matcher = pattern.matcher(paramValues[0]);
        if (matcher.matches()) {
          return paramValues[0];
        } else if ("(null)".equals(paramValues[0])) {//ios 端设置空值是的值
          return null;
        } else {
          throw new IllegalInputArgumentException("0000001", "输入数据不合法");
        }
      }

    }

    return value;
  }

  private static class RequireUuidNamedValueInfo extends NamedValueInfo {

    public RequireUuidNamedValueInfo() {
      super("", false, ValueConstants.DEFAULT_NONE);
    }

    public RequireUuidNamedValueInfo(RequireUuid annotation) {
      super(annotation.name(), annotation.required(), annotation.defaultValue());
    }
  }
}
