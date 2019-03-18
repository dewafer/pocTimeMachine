PoC TimeMachine
----------------

时光机概念验证项目。

背景
===

在Java8之前我们一般使用`new Date()`或者`System.currentTimeMillis()`来获取系统时间。
Java8之后提供了`Instant.now()`, `LocalDateTime.now()`...等方法来获取当前系统时间。

Java8更提供了`Clock`抽象类来方便我们测试。本项目演示了如何使用一套API来更改通过依赖注入的`Clock`对象，
以达到随心所欲更改系统时间，方便项目测试。


说明
===

具体Case请参考`com.dewafer.demo.pocTimeMachine.PocTimeMachineApplicationTests`。

仅当启用的Spring Profile有`timeMachine`时，相关组件才会启用，否则将会使用系统默认的`Clock`。

