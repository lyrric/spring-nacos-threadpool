# 动态线程池
- 实时监测线程池的各项指标信息
- 通过nacos动态设置线程池参数
## 实时监测线程池的状态
检测线程池的如下信息:
```
    /** 线程池唯一标识 */
    private String key;
    /** 线程池名称 */
    private String name;
    /** 任务队列是否长度超过阈值 */
    private Boolean queueFullWarning ;
    /** 任务队列使用数量 */
    private Integer queueUseSize ;

    /** 任务队列使用比例阈值 */
    private Double queueWarningRatio;
    /** 任务等待时间超过阈值的任务数量 */
    private Integer waitTimeoutCount ;
    /** 任务执行超过阈值的任务数量 */
    private Integer execTimeoutCount;
    /** 任务执行总时长 */
    private Long totalExecTime;
    /** 任务平均执行总时长 */
    private Integer avgExecTime;
    /** 任务等待总时长 */
    private Long totalWaitTime ;
    /** 任务平均等待总时长 */
    private Integer avgWaitTime ;
    /** 已完成任务 */
    private Long completedTaskCount;
    /** 当前活跃的线程数量 */
    private Integer activeCount;


    /** 核心线程池数量 */
    private Integer corePoolSize;
    /** 最大线程池数量 */
    private Integer maximumPoolSize;
    /** 队列总长度 */
    private Integer queueTotalSize;
    /** 任务等待时长阈值，单位毫秒 */
    private long waitTimeout;
    /** 任务执行时间阈值，单位毫秒 */
    private long execTimeout;
```


## 动态设置线程池参数
通过nacos动态设置线程池参数，可设置的线程池参数如下：
```
        /** 线程池唯一标识 */
        private String key;
        /** 线程池名称 */
        private String name;
        /** 任务队列使用比例阈值 */
        private Double queueWarningRatio;
        /** 任务等待时长阈值，单位毫秒 */
        private long waitTimeout;
        /** 任务执行时间阈值，单位毫秒 */
        private long execTimeout;
        /** 核心线程池数量 */
        private Integer corePoolSize;
        /** 最大线程池数量 */
        private Integer maximumPoolSize;
```

## 使用方法
## 待定