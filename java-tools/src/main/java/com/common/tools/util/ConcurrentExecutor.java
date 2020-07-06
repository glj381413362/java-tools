package com.common.tools.util;

import java.util.Collection;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.SchedulingTaskExecutor;

/**
 * 并发执行器
 *
 *  2019-02-12
 */

public class ConcurrentExecutor<T> {

    private static final Logger LOG = LoggerFactory.getLogger(ConcurrentExecutor.class);
    private static final Long DEFAULT_WAIT_MILLIS = 200L;

    private long sleepMillis;
    private Integer taskCount;
    private Collection<T> processDataList;
    private SchedulingTaskExecutor taskExecutor;
    private AtomicInteger taskCounter = new AtomicInteger(0);

    public ConcurrentExecutor(Integer taskCount, Collection<T> processDataList, SchedulingTaskExecutor taskExecutor) {
        this.taskCount = taskCount;
        this.taskExecutor = taskExecutor;
        this.processDataList = processDataList;
        this.sleepMillis = DEFAULT_WAIT_MILLIS;
    }

    public ConcurrentExecutor(long sleepMillis, Integer taskCount, Collection<T> processDataList, SchedulingTaskExecutor taskExecutor) {
        this.sleepMillis = sleepMillis;
        this.taskCount = taskCount;
        this.processDataList = processDataList;
        this.taskExecutor = taskExecutor;
    }

    public void invoke(Consumer<T> consumer) {

        if (CollectionUtils.isEmpty(processDataList)) {
            return;
        }

        // 分发任务
        for (T processData : processDataList) {
            distributeTask(consumer, processData);
        }

        // 等待所有任务执行完成
        while (taskCounter.get() > 0) {
            taskWait();
        }
    }

    private void distributeTask(Consumer<T> consumer, T processData) {

        this.taskCounter.incrementAndGet();
        while (taskCounter.get() > taskCount) {
            taskWait();
        }
        this.taskExecutor.execute(() -> {
            try {
                consumer.accept(processData);
            } finally {
                this.taskCounter.decrementAndGet();
            }
        });
    }

    private void taskWait() {
        try {
            TimeUnit.MILLISECONDS.sleep(this.sleepMillis);
        } catch (Exception ex) {
            LOG.error("task wait error:{}", ex);
        }
    }
}
