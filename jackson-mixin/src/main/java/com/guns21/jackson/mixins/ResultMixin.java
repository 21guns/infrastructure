package com.guns21.jackson.mixins;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by jliu on 16/7/13.
 */
@JsonIgnoreProperties({"pageNum", "pageSize", "totals"})
public interface ResultMixin {
}
