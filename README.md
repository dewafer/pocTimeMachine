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

可使用的API有：

| 方法  | API URL                | 参数                              | 说明 
| ---- | ---------------------- | --------------------------------- | --------------------------------------
| GET  | /now                   | zoneId(可选)                       | 使用`Instant.now()`方法获取当前系统时间。
| GET  | /zone                  | 无                                 | 获取当前`Clock`的默认Zone信息。
| POST | /time-machine/reset    | 无                                 | 重置当前`Clock`。
| POST | /time-machine/at-zone  | zoneId                             | 设定当前时区
| POST | /time-machine/fixed-at | at(ISO格式当前时间），zoneId（可选）  | 将系统时间固定在指定时间


路线图
====

还没想好。



------------

MIT License

Copyright (c) 2019 dewafer

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
