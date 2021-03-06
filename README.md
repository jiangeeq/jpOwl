<h2>项目源码将于2020年3月开源公布</font></h2>

## 设计架构
jpOwl客户端是java语言编写而成，要求做到API简单、高可靠性能、无论在任何场景下客户端都不能影响各业务服务的性能。旨在为各业务线提供丰富的埋点功能与数据采集。

在收集数据方面使用ThreadLocal，为每一个使用该变量的线程都提供一个变量值的副本，是Java中一种较为特殊的线程绑定机制，是每一个线程都可以独立地改变自己的副本，而不会和其它线程的副本冲突。

![](https://user-gold-cdn.xitu.io/2019/7/24/16c233271510a63f?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)
如图，执行业务逻辑的时候，就会把此次请求对应的监控存放于ThreadContext中，ThreadContext其实是一个监控树的结构。最后业务线程执行结束时，将监控对象**异步**存入一个内存队列中，jpOwl有个消费线程将队列内的数据**异步**发送到第三方存储引擎。
## 场景需求
* 监控粒度可控制作用在接口上，方法上，乃至代码块上
* 异步方式运行，即使失败也不影响业务流程
* 提供注解式与编程式的使用方式
* 日志级别可根据响应时间，失败次数，数据大小决定升降
* 可添加自定义触发器，触发特定功能
* 监控产生的日志内容可动态指定记录前缀名
* 支持内存，本地文件，mongodb，elastic search等输出源
* 支持在线修改调整日志记录级别
* 支持监控数据延迟上报或触发，便于聚合业务数据统计记录

支持以秒级，分钟级监控异常失败次数，方法接口执行次数。根据指标配置告警策略，如钉钉，邮件，短信等方式。业务可根据需要设置日志前缀与延迟上报，在内存队列或触发器进行业务指标计算公式的预计算，对个性化业务指标进行监控告警或数据记录至其他数据源，由echart等图表工具展示。

## 性能设计
序列化和通信是整个客户端包括服务端性能里面很关键的一个环节
* 异步序列化，jpOwl序列化协议protobuf序列化协议
* 异步通信，jpOwl通信是基于Netty来实现的NIO的数据传输
* 异步化IO传输的操作
* 异步数据采集，基于NIO管道记录日志
* 基于内存级别的数据缓冲，可指定缓存队列大小
* 可指定内存队列安全机制，当队列的80%已满,则会丢弃TRACT、DEBUG、INFO级别的日志

## 价值与优势
**价值**
* 减少故障发现时间
* 降低故障定位成本
* 辅助应用程序优化
* 监控数据是全量统计，jpOwl支持预计算数据
* 链路数据是采样计算

**优势**
* 实时处理：信息的价值会随时间锐减，所以收集信息需要迅速
* 全量数据：全量采集指标数据，便于深度分析故障案例
* 故障容忍：故障不影响业务正常运转、对业务透明
* 高吞吐：海量监控数据的收集，需要高吞吐能力做保证

## 业务模型监控
jpOwl主要支持以下四种监控模型：

* **Transaction**	适合记录跨越系统边界的程序访问行为,比如远程调用，数据库调用，也适合执行时间较长的业务逻辑监控，Transaction用来记录一段代码的执行时间和次数。
* **Event**	用来记录一件事发生的次数，比如记录系统异常，它和transaction相比缺少了时间的统计，开销比transaction要小。
* **Heartbeat**	表示程序内定期产生的统计信息, 如CPU利用率, 内存利用率, 连接池状态, 系统负载等。
* **Metric** 用于记录业务指标、指标可能包含对一个指标记录次数、记录平均值、记录总和，业务指标最低统计粒度为1分钟。

### 消息树
jpOwl监控系统将每次URL、Service的请求内部执行情况都封装为一个完整的消息树、消息树可能包括`Transaction`、`Event`、`Heartbeat`、`Metric`等信息。
**完整的消息树**
![](https://user-gold-cdn.xitu.io/2019/7/24/16c233f3411080ce?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)
**可视化消息树**
![](https://user-gold-cdn.xitu.io/2019/7/24/16c233f3411080ce?w=976&h=469&f=png&s=560622)
**分布式消息树【一台机器调用另外一台机器】**
![](https://user-gold-cdn.xitu.io/2019/7/24/16c234002943d50e?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)
