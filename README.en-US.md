Infrastructure
=====
## Version Management
### Use GNU-style Versioning
***
[Major Version].[Minor Version].[Patch Version]
* Major Version: Incremented by 1 when there are major changes to the code or a significant accumulation of local fixes; Minor and Patch versions are reset to 0.
* Minor Version: Incremented by 1 when new features are added to the existing base; Major version remains unchanged, and Patch version is reset to 0.
* Patch Version: Incremented by 1 for local modifications or bug fixes.

**Please upgrade the version number of each module when modifying the code of that specific module.**

API Specifications
=====

## MessageResult

### ofOptional() Static Method

The `MessageResult` class has added 4 `ofOptional` methods:

1.  `public static <T> MessageResult<T> ofOptional(Optional<T> optionalObject, String code, String errorMessage)`
2.  `public static <T> MessageResult<T> ofOptional(Optional<T> optionalObject, String errorMessage)`
3.  `public static <T> MessageResult<T> ofOptional(Optional<T> optionalObject, Class<T> missingObjectClass)`
4.  `public static <T> MessageResult<T> ofOptional(Optional<T> optionalObject)`

Usage Example:

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

In the above example, if `userService` cannot find the corresponding `UserDTO` object, the Action method will return a failed `MessageResult`; otherwise, it returns a successful `MessageResult`. The default `code` is `404` and the default error message is `Resource not found`. Other overloaded methods can be used to customize the `code` and error message.

### stream() / optional() Methods

`stream()` and `optional()` methods have been added to `MessageResult` and its base class `AbstractResult`. After obtaining a query result from another module via Feign, if the returned data is a collection type, you can call the `stream()` method on the returned `MessageResult`. If it is a single object, you can call the `optional()` method on the returned `MessageResult`.

Example:

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

For an error `MessageResult`, the `optional()` method returns an empty `Optional` object, and the `stream()` method returns an empty `Stream` object, and this occurrence is recorded in the logs. Therefore, there is no need to check if the Feign call was successful via `getSuccess()` before calling `optional()` / `stream()`.

## ResultDecoder

When providing a Feign Client Service proxy object, `ResultDecoder` can be used instead of `JsonDecoder` to gain the ability to directly convert the `MessageResult` JSON string returned by the server into `Optional` / `Stream` / `List` / `Set`.

Example:

### Using ResultDecoder instead of JsonDecoder

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

### Declaring method return values in Feign Client Service interfaces as Stream or Optional

``` java
@Headers("Content-Type: application/json")
public interface FinancialSchemeLeadsService {
    @RequestLine("GET /op/v1/leads/{no}")
    Optional<LeadDTO> getLeadByNo(String no);

    @RequestLine("GET /op/v1/leads/")
    Stream<LeadDTO> getAllLeads();
}
```

Of course, the `MessageResult<T>` return type is still supported.

## FeignService

Since the code in `OrderLeadsConfig` in the above example follows a nearly identical pattern, you can use the `@FeignService` annotation directly in the Feign Client Service interface instead of manually providing the proxy object as in `OrderLeadsConfig`:

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

The `OrderLeadsConfig` class can now be deleted.

The parameter for the `@FeignService` annotation is the Base URL. Similar to the `@Value` annotation, it supports the `${PROP_KEY}` syntax to retrieve values from configuration files or the environment. The proxy object automatically provided by the `@FeignService` annotation uses `ResultDecoder` as its decoder.


## ValuableEnum for Automatic Formatting, Serialization, and Deserialization of Enum Objects

1.  Implement the `getValue()` method of the `ValuableEnum` interface (manually or using `Lombok`). The return value of this method should be the integer value wrapped by this enum class.
2.  Ensure the package name of the enum class implementing `ValuableEnum` matches the pattern: `com.ktjr.ddhc.**.enums`
3.  The `typeHandler` attribute does not need to be manually configured for enum-type fields in `mapper.xml`.
    
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
5.  Add the following configuration to `application.yml`:
    
    ```yaml
    mybatis:
      type-handlers-package: com.guns21.mybatis.handler
    com:
      guns21:
        spring:
          mvc:
            valuable-enum-package: com.ktjr.**.enums
    ```
