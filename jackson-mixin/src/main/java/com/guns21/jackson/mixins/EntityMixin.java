package com.guns21.jackson.mixins;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"gmtCreate", "gmtModified"})
public interface EntityMixin {
}
