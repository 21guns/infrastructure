package com.guns21.mybatis.handler;

import com.guns21.common.enums.ValuableEnum;
import com.guns21.common.enums.ValuableEnumFactory;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 使用方式
 * 配置mybatis.type-handlers-package: com.xx.enums.typehandler
 * Created by Liu Xiang on 2018/7/30.
 *
 * @param <E> Enum class that implements ValuableEnum
 */
@MappedTypes(ValuableEnum.class)
public class EnumTypeHandler<E extends Enum<E> & ValuableEnum> extends BaseTypeHandler<E> {

    private Class<E> clazz;

    public EnumTypeHandler(Class<E> clazz) {
        this.clazz = clazz;
        ValuableEnumFactory.buildEnum(this.clazz);
    }

    /**
     * since 3.5.0 need no args constructor
     */
    public EnumTypeHandler() {
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, E parameter, JdbcType jdbcType) throws SQLException {
        ps.setInt(i, parameter.getValue());
    }

    @Override
    public E getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return ValuableEnumFactory.getEnum(clazz, rs.getByte(columnName));
    }

    @Override
    public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return ValuableEnumFactory.getEnum(clazz, rs.getByte(columnIndex));
    }

    @Override
    public E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return ValuableEnumFactory.getEnum(clazz, cs.getByte(columnIndex));
    }
}
