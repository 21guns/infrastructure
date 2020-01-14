基础设施
=====
## 版本管理
### 使用GNU风格的版本号
***
[主版本号].[子版本号].[修正版本号]
* 主版本号 代码有重大修改或局部修正累积较多，代码发生比较大的变化时，主版本号+1，子版本号和修正版本号为0
* 子版本号 在原有的基础上增加了部分功能时，主版本号不变，子版本号+1, 修正版本号为0
* 修正版本号 局部修改或 bug 修正时，修正版本号+1

**修改各个模块的代码是请升级各个模块的版本号**

API 说明
=====

## MessageResult

### ofOptional() 静态方法

`MessageResult` 类增加了 4 个 `ofOptional` 方法：

1.  `public static <T> MessageResult<T> ofOptional(Optional<T>
    optionalObject, String code, String errorMessage)`
2.  `public static <T> MessageResult<T> ofOptional(Optional<T>
    optionalObject, String errorMessage)`
3.  `public static <T> MessageResult<T> ofOptional(Optional<T>
    optionalObject, Class<T> missingObjectClass)`
4.  `public static <T> MessageResult<T> ofOptional(Optional<T>
    optionalObject)`

使用示例：

``` java
public interface UserService {
    Optional<UserDTO> getUserByUID(String id);
}

public class UserController {

    private UserService userService;

    @GetMapping("/users/{uid}")
    public MessageResult<UserDTO> getUserByUID(@PathVariable String uid) {
        return MessageResult.ofOptional(userService.getUserByUID(uid));
    }
}
```

在上述示例中，如果 `userService` 没有找到对应的 `UserDTO` 对象，那么 Action 方法将返回一个失败的
`MessageResult`, 否则返回一个成功的 `MessageResult` ； 默认的 `code` 为 `404`
，默认的错误消息为 `Resource not found` ；可以使用其 他的重载方法自定义 `code`
和错误消息。

### stream() / optional() 方法

`MessageResult` 及其基类 `AbstractResult` 上增加了 `stream()` 和 `optional()`
方法。在通过 Feign 调用其他模块获得查询结果之后，如果获取的结 果，其数据是集合类型，那么可以在返回的
`MessageResult` 上调用 `stream()` 方 法，如果其实是单个对象，那么可以在返回的 `MessageResult`
上调用 `optional()` 方法。

示例：

``` java
public interface UserFeignService {
    MessageResult<UserDTO> getUserByUID(String id);
}

public interface AddressFeignService {
    MessageResult<List<AddressDTO>> getAddressByUID(String uid);
}

public class OrderController {

    private UserFeignService userService;

    private AddressFeignService addressService;

    @PostMapping("/orders")
    public MessageResult create(OrderDTO order) {
        MessageResult<UserDTO> userResult = userService.getUserByUID(order.getUID());
        userResult.optional().ifPresent(user -> {
            order.setUsername(user.getName());
            Stream<AddressDTO> addresses = addressService.getAddressByUID(order.getUID()).stream();
            Optional<AddressDTO> address = addresses.filter(AddressDTO::getEnabled).findFirst();
            address.ifPresent(addr -> {
                order.setAddress(address.get());
            })
        });
    }
}
```

对于错误的 `MessageResult`, `optional()` 方法会返回空的 `Optional` 对象， `stream()`
方法会返回空的 `Stream` 对象，并且在日志中记录这种情况下的调用， 因此，在调用 `optional()` /
`stream()` 之前无需在通过 `getSuccess()` 判断 Feign 调用是否成功。

## ResultDecoder

在提供 Feign Client Service 代理对象的时候，可以使用 `ResultDecoder` 代替 `JsonDecoder`
，而获得可以直接将服务端返回的 `MessageResult` JSON 串转换为: `Optional` / `Stream` /
`List` / `Set` 的能力。

示例：

### 使用 ResultDecoder 代替 JsonDecoder

``` java
@Configuration
public class OrderLeadsConfig {

    @Value("${com.ktjr.ddhc.config.opBaseUrl}")
    private String baseUrl;

    @Bean
    public FinancialSchemeLeadsService financialSchemeLeadsService(ObjectMapper objectMapper) {
        return Feign.builder()
            .decode404()
            .encoder(new JacksonEncoder(objectMapper))
            .decoder(new ResultDecoder(objectMapper))
            .target(SpringSessionHeaderTokenTarget.newTarget(FinancialSchemeLeadsService.class, baseUrl));
    }
}
```

### 将 Feign Client Service 接口中的方法的返回值声明为 Stream 或 Optional

``` java
@Headers("Content-Type: application/json")
public interface FinancialSchemeLeadsService {
    @RequestLine("GET /op/v1/leads/{no}")
    Optional<LeadDTO> getLeadByNo(String no);

    @RequestLine("GET /op/v1/leads/")
    Stream<LeadDTO> getAllLeads();
}
```

当然 `MessageResult<T>` 返回类型仍然是支持的。

## FeignService

像上述示例中的 `OrderLeadsConfig` 中几乎都是同样的模式代码，可以直接在 Feign Client Service 接口中使用
`@FeignService` 注解，而不必再像 `OrderLeadsConfig` 中一样地手动提供代理对象了：

``` java
@FeignService("${com.ktjr.ddhc.config.opBaseUrl}")
@Headers("Content-Type: application/json")
public interface FinancialSchemeLeadsService {
    @RequestLine("GET /op/v1/leads/{no}")
    Optional<LeadDTO> getLeadByNo(String no);

    @RequestLine("GET /op/v1/leads/")
    Stream<LeadDTO> getAllLeads();
}
```

现在可以把 `OrderLeadsConfig` 类删除了。

`@FeignService` 注解的参数为 Base URL，和 `@Value` 注解类似，支持 `${PROP_KEY}`
语法从配置文件或环境中取值。 `@FeignService` 注解自动提供的代 理对象使用的 decoder 为
`ResultDecoder` 。


## ValuableEnum 完成枚举对象的自动格式化、序列化和反序列化

1.  实现 `ValuableEnum` 接口的 `getValue()` 方法（手动或使用 `Lombok` 实现），这个方法的返回值应该是这个枚举类包裹的那个整型值
2.  确保实现了 `ValuableEnum` 的枚举类的包名匹配模式： `com.ktjr.ddhc.**.enums`
3.  `mapper.xml` 中不需要为枚举类型的字段手动配置 `typeHandler` 属性
    
    ```java
    package com.ktjr.ddhc.docking.api.enums;
    
    import com.fasterxml.jackson.annotation.JsonValue;
    import com.ktjr.ddhc.enums.ValuableEnum;
    import lombok.Getter;
    
    public enum ChannelEnum implements ValuableEnum {
        TONGDUN(1,  "同盾"),
        PENGYUAN(2, "鹏元"),
        XINYAN(3,   "新颜"),
        BAIRONG(4,  "百融");
    
        @JsonValue
        @Getter
        private Integer value;
    
        @Getter
        private String desc;
    
        ChannelEnum(Integer value, String desc) {
            this.value = value;
            this.desc = desc;
        }
    }
    ```
5.  `application.yml` 添加以下配置：
    
    ```yaml
    mybatis:
      type-handlers-package: com.guns21.mybatis.handler
    com:
      guns21:
        spring:
          mvc:
            valuable-enum-package: com.ktjr.**.enums
    ```
