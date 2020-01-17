# jackson Json数据序列化
=======

* 1.使用RequestMappingHandlerMapping缓存mixin，当同一个Entity有多个mixin时会互相影响
* 2.使用HandlerMethodReturnValueHandler,每次使用新的ObjectMapper去序列化
