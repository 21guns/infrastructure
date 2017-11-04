package com.guns21.jackjson.mixins;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"createTime", "updateTime"})
public interface EntityMixin {
}
