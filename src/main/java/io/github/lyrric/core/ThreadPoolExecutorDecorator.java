package io.github.lyrric.core;

import io.github.lyrric.task.AbstractTaskMonitorDecorator;
import io.github.lyrric.task.RunnableMonitorDecorator;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;


public class ThreadPoolExecutorDecorator extends ThreadPoolExecutor {
    /** 配置中心的key */
    private final String key;
    /** 线程池名称 */
    private String name;

    /** 任务队列长度阈值 */
    private int queueWarningSize;
    /**  队列总长度 */
    private int queueTotalSize;
    /** 任务队列使用比例阈值 */
    private double queueWarningRatio = 0.75f;
    /** 任务等待时长阈值，单位毫秒 */
    private long waitTimeout;
    /** 任务执行时间阈值，单位毫秒 */
    private long execTimeout;

    /** 任务等待时间超过阈值的任务数量 */
    private final AtomicInteger waitTimeoutCount = new AtomicInteger(0);
    /** 任务执行超过阈值的任务数量 */
    private final AtomicInteger execTimeoutCount = new AtomicInteger(0);
    /** 任务执行总时长 */
    private final AtomicLong totalExecTime = new AtomicLong(0);
    /** 任务等待总时长 */
    private final AtomicLong totalWaitTime = new AtomicLong(0);


    protected ThreadPoolExecutorDecorator(String key, String name, Double queueWarningRatio, long waitTimeout, long execTimeout,
                                          int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
                                          BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
        this.key = key;
        this.name = name;
        this.queueTotalSize = workQueue.remainingCapacity();
        if (queueWarningRatio != null) {
            this.queueWarningRatio = queueWarningRatio;
        }
        this.queueWarningSize = (int) (workQueue.size() / this.queueWarningRatio);
        this.waitTimeout = waitTimeout;
        this.execTimeout = execTimeout;
        ThreadPoolExecutorMonitor.add(key, this);
    }


    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        super.afterExecute(r, t);
        //计算等待时间、执行时间
        if (r instanceof AbstractTaskMonitorDecorator) {
            long waitTime = ((AbstractTaskMonitorDecorator) r).getWaitTime();
            totalWaitTime.addAndGet(waitTime);
            if (waitTime > waitTimeout) {
                waitTimeoutCount.incrementAndGet();
            }
            long execTime = ((AbstractTaskMonitorDecorator) r).getExecTime();
            totalExecTime.addAndGet(execTime);
            if (execTime > execTimeout) {
                execTimeoutCount.incrementAndGet();
            }
        }
    }



    @Override
    public void execute(Runnable command) {
        super.execute(new RunnableMonitorDecorator(command));
    }


    public static ThreadPoolExecutorDecoratorBuilder builder(){
        return new ThreadPoolExecutorDecoratorBuilder();
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public long getWaitTimeout() {
        return waitTimeout;
    }

    public void setWaitTimeout(long waitTimeout) {
        this.waitTimeout = waitTimeout;
    }

    public long getExecTimeout() {
        return execTimeout;
    }

    public void setExecTimeout(long execTimeout) {
        this.execTimeout = execTimeout;
    }


    public Integer getWaitTimeoutCount() {
        return waitTimeoutCount.get();
    }

    public Integer getExecTimeoutCount() {
        return execTimeoutCount.get();
    }

    public Long getTotalExecTime() {
        return totalExecTime.get();
    }

    public Long getTotalWaitTime() {
        return totalWaitTime.get();
    }

    public String getKey() {
        return key;
    }

    public int getQueueTotalSize() {
        return queueTotalSize;
    }



    public double getQueueWarningRatio() {
        return queueWarningRatio;
    }

    public void setQueueWarningRatio(double queueWarningRatio) {
        if (queueWarningRatio <= 0) {
            throw new IllegalArgumentException();
        }
        this.queueWarningSize = (int) (getQueue().size()/queueWarningRatio);
        this.queueWarningRatio = queueWarningRatio;
    }
}
