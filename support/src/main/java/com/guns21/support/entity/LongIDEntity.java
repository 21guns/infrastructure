package com.guns21.support.entity;

import com.guns21.support.boot.config.IDFactory;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Objects;

@SuperBuilder
@Setter
@Getter
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public abstract class LongIDEntity extends AbstractEntity<Long> {
    @Override
    public void preCreate() {
        if (Objects.isNull(getId())) {
            setId(IDFactory.getId());
        }
        super.preCreate();
    }
}
