package com.guns21.web.converter;

import com.guns21.common.util.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;

import java.util.Date;

/**
 * Created by ljun on 15/4/30.
 */
public class StringToDateConvert implements Converter<String, Date> {
  private static final Logger logger = LoggerFactory.getLogger(StringToDateConvert.class);

  @Override
  public Date convert(String source) {
    if (StringUtils.isEmpty(source)) {
      return null;
    }

    if (NumberUtils.isCreatable(source)) {
      return new Date(NumberUtils.createLong(source));
    } else {
      return DateUtils.parse(source, DateUtils.LONG_DATE_FORMAT);
    }

  }
}
