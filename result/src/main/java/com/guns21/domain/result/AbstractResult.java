package com.guns21.domain.result;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Created by jliu on 16/7/19.
 */
@Slf4j
@Data
public class AbstractResult<T> {

    protected String code;
    protected Boolean success;
    protected String message;
    //entity,list,page,message
    protected String type;
    protected T data;

    public enum Code {

        TYPE_VIOLATION("1020", "数据类型不合法"),
        MISS_PARAMETER("1021", "丢失参数");

        protected String code;
        protected String text;

        Code(String code, String text)
        {
            this.code = code;
            this.text = text;
        }

        public String getCode() {
            return code;
        }

        public String getText() {
            return text;
        }
    }

    /**
     * Usage Example:
     *
     *   MessageResult<List<CustomerDTO>> result = service.getCustomer();
     *   Stream<CustomerDTO> stream = result.stream();
     *
     *   result.<CustomerDTO>stream().map( ... );
     *
     *   result.stream(CustomerDTO.class).map( ... );
     * @param <E> Type of elements of in the returned stream.
     * @return If returned data type is list, return it as a stream.
     */
    public <E> Stream<E> stream() {
        if (success) {
            if (data instanceof Collection) {
                return ((Collection) data).stream();
            } else {
                return Stream.of((E) data);
            }
        } else {
            log.warn("Provide failed message result as empty stream, code: {}, message: {}, data: {}", code, message, data);
        }
        return Stream.empty();
    }

    public <E> Stream<E> stream(Class<E> clazz) {
        return stream();
    }

    public Optional<T> optional() {
        if (success) {
            return Optional.ofNullable(data);
        } else {
            log.warn("Provide failed message result as empty optional, code: {}, message: {}, data: {}", code, message, data);
            return Optional.empty();
        }
    }
}
