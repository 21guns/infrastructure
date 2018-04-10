## assembler
实现思路有两种：
* 使用package扫描方式在spring启动时扫描do和dto包，将包内的各个类映射，并生成BeanCopier。//TODO
* 在第一次使用时生产BeanCopier，并缓存供下次使用(com.guns21.data.assembler.AssemblerFactory)
