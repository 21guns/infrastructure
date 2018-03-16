package com.guns21.jackjson.mixins;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"gmtCreate", "gmtModified"})
public interface EntityMixin {
}
