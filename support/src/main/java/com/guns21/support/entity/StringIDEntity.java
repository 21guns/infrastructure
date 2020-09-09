package com.guns21.support.entity;

import com.guns21.common.util.ObjectUtils;
import com.guns21.common.uuid.ID;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Setter
@Getter
@EqualsAndHashCode(callSuper = true)
public abstract class StringIDEntity extends AbstractEntity<String> {
    @Override
    public void preCreate() {
        if (!ObjectUtils.hasText(getId())) {
            setId(ID.get());
        }
        super.preCreate();
    }
}
