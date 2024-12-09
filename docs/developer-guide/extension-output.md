src/main/java/com/youpeng/jpowl/output/
├── batch/          // 批处理相关
├── cache/          // 缓存管理
├── config/         // 配置管理
├── connection/     // 连接管理
├── core/           // 核心接口
├── elasticsearch/  // ES实现
├── event/          // 事件系统
├── exception/      // 异常定义
├── file/          // 文件输出
├── health/        // 健康检查
├── init/          // 初始化
├── limiter/       // 限流
├── load/          // 负载检测
├── manager/       // 管理器
├── memory/        // 内存输出
├── metrics/       // 指标收集
├── model/         // 数据模型
├── status/        // 状态管理
└── util/          // 工具类

 2. 核心类及其用途
core/ 包
OutputSource: 核心接口，定义输出源的基本行为
OutputSourceType: 枚举类，定义支持的输出源类型
AbstractOutputSource: 抽象基类，提供通用实现
OutputSourceRegistry: 注册和管理输出源实现
config/ 包
OutputSourceConfig: 配置基类
OutputSourceConfigManager: 配置管理器
properties/*: 具体配置类
manager/ 包
OutputSourceManager: 总体管理器
OutputSourceFactory: 输出源工厂
OutputSourceLifecycleManager: 生命周期管理
3. 主要调用关系
   初始化流程
1. OutputSourceFactory 创建输出源实例
   OutputSourceInitializer 进行初始化
   OutputSourceConfigManager 加载配置
   OutputSourceConnectionManager 建立连接
   OutputSourceLifecycleManager 管理生命周期
   数据写入流程
   OutputSourceManager 接收写入请求
2. OutputSourceRateLimiter 进行限流检查
   BatchProcessor 进行批处理
   具体的 OutputSource 实现进行写入
   MetricsCollector 收集指标
   OutputSourceEventListener 发布事件
   监控流程
   LoadManager 启动负载监控
   LoadDetector 检测负载状态
   HealthChecker 进行健康检查
   OutputSourceStatusManager 更新状态
   OutputSourceEventListener 处理事件
   异常处理流程
   OutputSourceException 定义异常
   OutputSourceEventListener 发布错误事件
3. LoggingEventHandler 记录日志
   OutputSourceStatusManager 更新状态
4. 扩展点
   新增输出源类型：实现 OutputSource 接口
   自定义配置：继承 OutputSourceConfig
   事件处理：实现 OutputSourceEventHandler
   负载检测：实现 LoadDetector
5. 关键依赖关系
   OutputSourceManager → OutputSource 实现类
   BatchProcessor → OutputSource
   LoadManager → LoadDetector
   MetricsCollector → OutputSourceMetrics
   OutputSourceEventListener → OutputSourceEventHandler
   这个项目采用了模块化设计，各个组件之间通过接口解耦，便于扩展和维护。事件系统作为backbone，确保了组件间的松耦合通信。


1. 核心调用链路
   graph TD
   Client[客户端] --> OutputSourceManager
   OutputSourceManager --> OutputSourceFactory
   OutputSourceFactory --> |创建| OutputSource
   OutputSourceManager --> |写入| BatchProcessor
   BatchProcessor --> |批量写入| OutputSource
   OutputSource --> |更新| MetricsCollector
   OutputSource --> |发布事件| OutputSourceEventListener
2. 初始化调用链路
   graph TD
   Client[客户端] --> OutputSourceFactory
   OutputSourceFactory --> OutputSourceInitializer
   OutputSourceInitializer --> OutputSourceConfigManager
   OutputSourceInitializer --> OutputSourceConnectionManager
   OutputSourceInitializer --> OutputSourceStatusManager
   OutputSourceStatusManager --> OutputSourceEventListener
3. 监控调用链路
   graph TD
   LoadManager --> LoadDetector
   LoadManager --> OutputSourceEventListener
   HealthChecker --> MetricsCollector
   HealthChecker --> OutputSourceStatusManager
   OutputSourceStatusManager --> OutputSourceEventListener
4. 具体场景调用示例
   写入数据场景:
   客户端调用 OutputSourceManager.write()
   经过以下步骤:
   OutputSourceManager
   → OutputSourceRateLimiter.tryAcquire()  // 限流检查
   → BatchProcessor.add()                   // 加入批处理
   → OutputSource.write()                   // 实际写入
   → MetricsCollector.recordWrite()         // 记录指标
   → OutputSourceEventListener.publishEvent() // 发布事件
   负载检测场景:
   LoadManager
   → LoadDetector.detect()                  // 检测负载
   → OutputSourceStatusManager.updateStatus() // 更新状态
   → OutputSourceEventListener.publishEvent() // 发布事件
   → OutputSourceRateLimiter.adjust()        // 调整限流
   健康检查场景:
   HealthChecker
   → MetricsCollector.getMetrics()          // 获取指标
   → OutputSourceStatusManager.getStatus()   // 获取状态
   → OutputSourceEventListener.publishEvent() // 发布事件
   异常处理场景:
   OutputSource
   → OutputSourceException                   // 抛出异常
   → OutputSourceStatusManager.updateStatus() // 更新状态
   → OutputSourceEventListener.publishEvent() // 发布事件
   → LoggingEventHandler.handleEvent()       // 记录日志
5. 组件间的依赖关系
   OutputSourceManager
   依赖: OutputSourceFactory, BatchProcessor, OutputSourceRateLimiter
   被依赖: 客户端代码
   BatchProcessor
   依赖: OutputSource
   被依赖: OutputSourceManager
   OutputSourceEventListener
   依赖: OutputSourceEventHandler
   被依赖: 所有需要发布事件的组件
   MetricsCollector
   依赖: 无
   被依赖: OutputSource, HealthChecker
   LoadManager
   依赖: LoadDetector, OutputSourceEventListener
   被依赖: 系统监控模块
6. 配置加载顺序
   OutputSourceConfigManager 加载配置
2. OutputSourceFactory 使用配置创建实例
   OutputSourceInitializer 进行初始化
   OutputSourceConnectionManager 建立连接
   OutputSourceLifecycleManager 管理生命周期


核心功能：
多种输出源支持 (Elasticsearch, InfluxDB, File)
异步写入
批量处理
优雅关闭
可靠性保障：
异常处理
重试机制
状态监控
健康检查
性能优化：
批量写入
异步处理
限流控制
缓存管理
可观测性：
指标收集
性能监控
日志记录
事件通知
扩展性：
工厂模式
注册机制
配置管理
生命周期管理


这样的设计提供了：
统一的负载检测接口
针对不同数据源的具体实现
3. 多维度的负载指标
基于实际负载的限流调整
可扩展的检测机制
对于Redis和MySQL，我们可以实现类似的负载检测器：
Redis可以通过INFO命令获取负载信息
MySQL可以通过SHOW STATUS和SHOW PROCESSLIST获取负载信息
这些新增的类提供了：
异常处理机制
类型安全的异常
携带输出源类型信息
更好的异常追踪
事件处理机制
标准的事件模型
灵活的事件处理
异步事件通知
事件监听机制
支持多个监听器
线程安全的实现
自动指标更新
主要优点：
更好的异常处理
事件驱动架构
解耦的设计
可扩展的监听机制
事件处理
标准的日志记录
事件分类处理
异常信息记录
2. 配置管理
集中配置管理
配置验证
动态配置更新
3. 状态管理
状态生命周期
状态变更事件
错误追踪
主要优点：
更好的可观测性
集中的配置管理
完整的状态追踪
标准的事件处理
// 配置动态限流器
DynamicRateLimiter.configure(OutputSourceType.ELASTICSEARCH,
DynamicRateLimiter.LimiterConfig.builder()
.initialQps(1000)
.minQps(100)
.maxQps(5000)
.checkIntervalMs(5000)
.successRateThreshold(0.95)
.latencyThreshold(100)
.adjustmentStep(100)
.build()
);

// 在写入数据时使用限流器
if (DynamicRateLimiter.tryAcquire(OutputSourceType.ELASTICSEARCH)) {
// 执行写入操作
} else {
// 处理限流情况
}