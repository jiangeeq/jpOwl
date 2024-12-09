
<div align="center">
<a href="https://git.io/typing-svg"><img src="https://readme-typing-svg.demolab.com?font=Noto+Sans+S&duration=3500&pause=2000&color=21C8B8&center=%E7%9C%9F%E7%9A%84&vCenter=%E7%9C%9F%E7%9A%84&repeat=%E7%9C%9F%E7%9A%84&random=%E9%94%99%E8%AF%AF%E7%9A%84&width=435&lines=JpOwl%EF%BC%8C%E8%BD%BB%E9%87%8F%E7%BA%A7Java%E5%BA%94%E7%94%A8%E7%9B%91%E6%8E%A7%E4%B8%8E%E5%91%8A%E8%AD%A6%E6%A1%86%E6%9E%B6" alt="Typing SVG" /></a><p align="center">JpOwl是一个轻量级的Java应用监控框架,提供了完整的应用监控、日志管理和告警功能。</p>
<p align="center">目标是为各业务线提供全面的埋点功能和数据采集能力它旨在提供简洁的API和高可靠性,确保在各种业务场景下不会影响业务服务性能。</p>
</div>

<p align="center">
  <img src="https://img.shields.io/badge/License-Apache%202-4EB1BA.svg" alt="License" />
  <img src="https://img.shields.io/badge/JDK-8+-green" alt="Java Version" />
  <img src="https://img.shields.io/badge/Spring%20Boot-2.x-blue" alt="Spring Boot Version" />
  <img src="https://img.shields.io/badge/Gradle-8.5-02303A?logo=gradle" alt="Gradle" />
  <img src="https://img.shields.io/badge/Spring-6DB33F?logo=spring" alt="Spring" />
  <img src="https://img.shields.io/badge/Disruptor-3.4.4-brightgreen" alt="Disruptor" />
  <img src="https://img.shields.io/github/stars/jiangeeq/jpOwl" alt="Stars" />
  <img src="https://img.shields.io/github/forks/jiangeeq/jpOwl" alt="Forks" />
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Elasticsearch-7.x|8.x-005571?logo=elasticsearch" alt="Elasticsearch" />
  <img src="https://img.shields.io/badge/MongoDB-4.x|5.x-47A248?logo=mongodb" alt="MongoDB" />
  <img src="https://img.shields.io/badge/InfluxDB-2.x-22ADF6?logo=influxdb" alt="InfluxDB" />

  <img src="https://img.shields.io/badge/Logback-1.2-2D3B41" alt="Logback" />
  <img src="https://img.shields.io/badge/Log4j2-2.x-D22128" alt="Log4j2" />

  <img src="https://img.shields.io/badge/DingTalk-Robot-0077FF" alt="DingTalk" />
  <img src="https://img.shields.io/badge/Email-Spring--Email-EA4335?logo=gmail" alt="Email" />
  <img src="https://img.shields.io/badge/Webhook-HTTP-000000" alt="Webhook" />
</p>



## 特性
- **轻量级**: 低侵入性,对应用性能影响极小
- **高性能**: 异步处理,支持批量写入,内存级缓冲
- **可扩展**: 插件化架构,支持自定义扩展
- **多框架支持**:
    - 日志框架: Logback, Log4j2
    - 存储引擎: 文件, ES, MongoDB, InfluxDB
    - 告警通道: 钉钉, 邮件, Webhook
- **丰富的监控模型**:
    - Transaction: 记录方法调用链路和性能
    - Event: 记录业务事件
    - Metric: 记录业务指标
    - Heartbeat: 系统健康检查
- **动态配置**: 支持运行时调整采样率、日志级别等
- **告警管理**: 支持多维度告警策略配置



### 数据流转

1. 业务线程执行时创建MonitorContext
2. 通过ThreadLocal存储上下文信息
3. 异步写入监控数据到输出源
4. 触发相应的告警规则

## 场景需求
1. **监控粒度控制：** 支持在接口、方法或代码块级别进行监控。
2. **异步处理：** 确保异步操作失败不会影响业务流程。
3. **注解与编程式使用：** 提供注解式和编程式两种使用方式。
4. **动态日志级别：** 日志级别可根据响应时间、失败次数和数据大小进行调整。
5. **自定义触发器：** 支持添加自定义触发器，以激活特定功能。
6. **动态记录前缀：** 监控日志内容的记录前缀可动态指定。
7. **支持多种输出源：** 支持内存、本地文件、MongoDB、Elasticsearch 等输出源。
8. **在线调整日志级别：** 支持在线修改日志记录级别。
9. **数据延迟上报：** 支持延迟上报监控数据或触发，便于聚合业务数据统计记录。
10. **监控数据统计与告警：** 支持秒级、分钟级的监控，配置告警策略（如钉钉、邮件、短信等），支持个性化业务指标的预计算和监控告警。


## 性能设计

### 异步处理
- 使用Disruptor队列
- 批量写入机制
- 背压控制

### 采样控制
- 支持配置采样率
- 动态调整采样策略
- 支持过滤规则

### 内存管理
- 内存队列控制
- 缓冲区大小限制
- 溢出保护机制


## 价值与优势
**价值**
* **减少故障发现时间：** 实时获取和分析监控数据，缩短故障发现时间。
* **降低故障定位成本：** 提供全面的监控信息，帮助快速定位问题。
* **辅助应用程序优化：** 通过数据分析提供优化建议。
* **全量统计与预计算：** 支持全量数据统计，支持预计算以提升分析效率。

**优势**
* **实时处理：** 迅速处理信息，确保数据的时效性。
* **全量数据：** 支持全量采集和深度分析故障案例。
* **故障容忍：** 故障不会影响业务的正常运行，业务操作对监控透明。
* **高吞吐：** 高效处理大量监控数据，保证系统稳定性和性能。

## 监控模型

| 监控类型 | 适用场景 |
|---------|---------|
| **Transaction监控** | • 远程调用(RPC, HTTP等)<br>• 数据库操作<br>• 关键业务流程<br>• 长耗时操作 |
| **Event监控** | • 业务事件记录<br>• 异常事件统计<br>• 操作审计<br>• 状态变更 |
| **Metric监控** | • 业务指标统计<br>• 性能指标采集<br>• QPS/TPS监控<br>• 资源使用率 |
| **Heartbeat监控** | • 系统健康检查<br>• 服务可用性监控<br>• 资源使用监控<br>• 连接池状态 |


### 消息树
jpOwl监控系统将每次URL、Service的请求内部执行情况都封装为一个完整的消息树、消息树可能包括`Transaction`、`Event`、`Heartbeat`、`Metric`等信息。



### 使用示例
```xml
<dependency>
    <groupId>com.youpeng.jpowl</groupId>
    <artifactId>jpowl-spring-boot-starter</artifactId>
    <version>${jpowl.version}</version>
</dependency>
```
### Spring Boot配置

```yaml
jpowl:
  enabled: true
  sampling-rate: 100
  output:
    type: FILE
    path: logs/monitor.log
  alert:
    enabled: true
    type: dingtalk
    webhook: https://oapi.dingtalk.com/robot/send?access_token=xxx
```
```java
// 1. 注解方式
@JpOwlMonitor(
        tags = {"order", "payment"},
        logParams = true,
        threshold = @Threshold(duration = "1s")
)
public OrderResult processOrder(OrderRequest request) {
    // 业务逻辑
}

// 2. 编程方式
public void businessMethod() {
    MonitorContext context = null;
    try {
        context = MonitorManager.startMonitor("business-operation");
        // 业务逻辑
        context.addTag("business", "order");
        context.addMetric("amount", 100.00);
    } finally {
        MonitorManager.endMonitor(context);
    }
}
```

## 日志级别动态调整框架的应用场景
#### 性能问题排查场景
```angular2html
@JpOwlMonitor(
    logLevel = "INFO",
    threshold = @Threshold(
        responseTime = "1s",     // 响应时间超过1秒
        escalateLevel = "DEBUG"  // 自动升级到DEBUG级别
    )
)
public OrderResult processOrder(OrderRequest request) {
    // 订单处理逻辑
}
```
当订单处理时间异常时，自动提升日志级别
* 帮助定位性能瓶颈
* 收集更详细的执行信息
#### 异常监控场景
```angular2html
@JpOwlMonitor(
errorThreshold = @ErrorThreshold(
count = 5,              // 5次错误
timeWindow = "1m",      // 1分钟内
escalateLevel = "DEBUG" // 升级到DEBUG
)
)
public void paymentProcess() {
// 支付处理逻辑
}
```
- 短时间内出现多次异常时提升日志级别
-  及时发现系统异常
-  收集更多错误上下文信息
#### 数据量监控场景

```java
@JpOwlMonitor(
dataThreshold = @DataThreshold(
size = "1MB",           // 数据量超过1MB(只针对参数或返回值)
escalateLevel = "DEBUG" // 升级到DEBUG
)
)
public void batchDataProcess(List<Data> dataList) {
   // 批量数据处理
   }
```
- 处理大量数据时自动调整日志级别
-  监控数据处理性能
-  发现潜在的内存问题
#### 资源使用监控场景

```angular2html
@JpOwlMonitor(
resourceThreshold = @ResourceThreshold(
cpuUsage = "80%",       // CPU使用率超过80%
memoryUsage = "75%",    // 内存使用率超过75%
escalateLevel = "DEBUG" // 升级到DEBUG
)
)
public void resourceIntensiveTask() {
// 资源密集型任务
}
```
- 监控系统资源使用情况
-   及时发现资源瓶颈
-  优化资源使用
#### 业务监控场景

```angular2html
@JpOwlMonitor(
businessThreshold = @BusinessThreshold(
metric = "orderAmount",
threshold = "10000",     // 订单金额超过10000
escalateLevel = "DEBUG"  // 升级到DEBUG
)
)
public void processLargeOrder(Order order) {
// 大额订单处理
}
```
- 监控特定业务指标
-   重要业务流程追踪
-  业务异常快速定位

## 相关文档
```
jpowl-docs/
├── README.md                # 项目总览
├── quick-start.md          # 快速开始指南
├── user-guide/             # 用户指南
│   ├── configuration.md    # 配置说明
│   ├── monitoring.md       # 监控使用
│   └── alerts.md          # 告警配置
├── developer-guide/        # 开发者指南
│   ├── architecture.md     # 架构设计
```
