package com.common.tools.util;

import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * <p>
 * 多线程工具类
 * </p>
 *
 * @author gongliangjun 2019/12/28 7:25 PM
 */
public class ThreadUtil {
	private static final Logger LOG = LoggerFactory.getLogger(ThreadUtil.class);


	/**
	 * @param dataList  需要处理的数据
	 * @param maxThread 线程数
	 * @param taskPool  线程池
	 * @param function  线程执行函数
	 * @param <T>       函数输入值
	 * @param <R>       函数返回值
	 * @return
	 */
	public static <T, R> List<R> batchHandlerDataList(final Map<String, List<T>> dataList, final int maxThread, ThreadPoolTaskExecutor taskPool,
													  final Function<List<T>, List<R>> function) {
		LOG.info("ThreadUtil config threadCount is {},threadSize is {}", maxThread);

		List<R> resDataList = new ArrayList<>();
		if (MapUtils.isEmpty(dataList)) {
			LOG.error("request data is null return null");
			return null;
		}
		if (dataList.size() == 1) {
			resDataList = function.apply(dataList.entrySet().iterator().next().getValue());
		} else {
			// 多线程分配策略
			final List<Future<String>> results = new ArrayList<>();
			final AtomicInteger taskCounter = new AtomicInteger(0);
			final CountDownLatch countDownLatch = new CountDownLatch(dataList.size());
			for (Map.Entry<String, List<T>> currentBatchProcess : dataList.entrySet()) {
				results.add(taskRunner(currentBatchProcess.getValue(), maxThread, taskPool, taskCounter, resDataList, function));
			}
			// 等待调度的线程执行结束
			for (final Future<String> result : results) {
				try {
					result.get();
				} catch (final Exception e) {
					final String errorMsg = "ThreadUtil thread happened error!" + StringUtil.exceptionString(e);
					LOG.error(errorMsg, e);
				} finally {
					countDownLatch.countDown();
				}
			}
			try {
				countDownLatch.await();
			} catch (Exception e) {
				LOG.error("阻塞等待异常，{}", StringUtil.exceptionString(e));
			}
		}
		return resDataList;
	}

	/**
	 * @param dataList   需要处理的数据
	 * @param maxThread  线程数
	 * @param taskPool   线程池
	 * @param biFunction 线程执行函数
	 * @param <T>        函数输入值
	 * @param <R>        函数返回值
	 * @return
	 */
	public static <T, U, R> List<R> batchHandlerDataList(final Map<String, List<T>> dataList, final U u, final int maxThread, ThreadPoolTaskExecutor taskPool,
														 final BiFunction<List<T>, U, List<R>> biFunction) {
		LOG.info("ThreadUtil config threadCount is {},threadSize is {}", maxThread);

		List<R> resDataList = new ArrayList<>();
		if (MapUtils.isEmpty(dataList)) {
			LOG.error("request data is null return null");
			return null;
		}
		if (dataList.size() == 1) {
			resDataList = biFunction.apply(dataList.entrySet().iterator().next().getValue(), u);
		} else {
			// 多线程分配策略
			final List<Future<String>> results = new ArrayList<>();
			final AtomicInteger taskCounter = new AtomicInteger(0);
			final CountDownLatch countDownLatch = new CountDownLatch(dataList.size());
			for (Map.Entry<String, List<T>> currentBatchProcess : dataList.entrySet()) {
				results.add(taskRunner(currentBatchProcess.getValue(), u, maxThread, taskPool, taskCounter, resDataList, biFunction));
			}
			// 等待调度的线程执行结束
			for (final Future<String> result : results) {
				try {
					result.get();
				} catch (final Exception e) {
					final String errorMsg = "ThreadUtil thread happened error!" + StringUtil.exceptionString(e);
					LOG.error(errorMsg, e);
				} finally {
					countDownLatch.countDown();
				}
			}
			try {
				countDownLatch.await();
			} catch (Exception e) {
				LOG.error("阻塞等待异常，{}", StringUtil.exceptionString(e));
			}
		}
		return resDataList;
	}

	/**
	 * @param dataList   需要处理的数据
	 * @param maxThread  线程数
	 * @param threadSize 每个线程处理数据数
	 * @param taskPool   线程池
	 * @param function   线程执行函数
	 * @param <T>        函数输入值
	 * @param <R>        函数返回值
	 * @return
	 */
	public static <T, R> List<R> runnerOperation(final List<T> dataList, final int maxThread, int threadSize, ThreadPoolTaskExecutor taskPool,
												 final Function<List<T>, List<R>> function) {
		LOG.info("ThreadUtil config threadCount is {},threadSize is {}", maxThread, threadSize);

		List<List<T>> splitList = ListUtil.splitList(dataList, threadSize);
		List<R> resDataList = new ArrayList<>();
		if (splitList == null) {
			LOG.error("request data is null return null");
			return null;
		}
		if (splitList.size() == 1) {
			resDataList = function.apply(dataList);
		} else {
			// 多线程分配策略
			final List<Future<String>> results = new ArrayList<>();
			final AtomicInteger taskCounter = new AtomicInteger(0);
			final CountDownLatch countDownLatch = new CountDownLatch(splitList.size());
			for (List<T> currentBatchProcess : splitList) {
				results.add(taskRunner(currentBatchProcess, maxThread, taskPool, taskCounter, resDataList, function));
			}
			// 等待调度的线程执行结束
			for (final Future<String> result : results) {
				try {
					result.get();
				} catch (final Exception e) {
					final String errorMsg = "ThreadUtil thread happened error!" + StringUtil.exceptionString(e);
					LOG.error(errorMsg, e);
				} finally {
					countDownLatch.countDown();
				}
			}
			try {
				countDownLatch.await();
			} catch (Exception e) {
				LOG.error("阻塞等待异常，{}", StringUtil.exceptionString(e));
			}
		}
		return resDataList;
	}

	/**
	 * @param preProcessDatas
	 * @param taskTotal
	 * @param taskCounter
	 * @param resDataList
	 * @return
	 */
	public static <T, U, R> Future<String> taskRunner(final List<T> preProcessDatas, final U u,
													  final int taskTotal, ThreadPoolTaskExecutor taskPool,
													  final AtomicInteger taskCounter,
													  final List<R> resDataList, final BiFunction<List<T>, U, List<R>> function) {

		final BaseAwareCallable<String> taskRunner = new BaseAwareCallable<String>() {
			@Override
			protected String performActualWork() {
				taskCounter.incrementAndGet();
				try {
					LOG.info("ThreadUtil start new thread,taskRunner process ({}) records.", preProcessDatas.size());
					resDataList.addAll(function.apply(preProcessDatas, u));
				} catch (final Exception e) {
					final String errorMsg = "ThreadUtil taskRunner erroroccured:" + StringUtil.exceptionString(e);
					LOG.error(errorMsg, e);
				} finally {
					taskCounter.decrementAndGet();
				}
				return "success";
			}
		};
		while (true) {
			if (taskCounter.get() < taskTotal) {
				return taskPool.submit(taskRunner);
			}
		}
	}

	/**
	 * @param preProcessDatas
	 * @param taskTotal
	 * @param taskCounter
	 * @param resDataList
	 * @return
	 */
	public static <T, R> Future<String> taskRunner(final List<T> preProcessDatas,
												   final int taskTotal, ThreadPoolTaskExecutor taskPool,
												   final AtomicInteger taskCounter,
												   final List<R> resDataList, final Function<List<T>, List<R>> function) {

		final BaseAwareCallable<String> taskRunner = new BaseAwareCallable<String>() {
			@Override
			protected String performActualWork() {
				taskCounter.incrementAndGet();
				try {
					LOG.info("ThreadUtil start new thread,taskRunner process ({}) records.", preProcessDatas.size());
					resDataList.addAll(function.apply(preProcessDatas));
				} catch (final Exception e) {
					final String errorMsg = "ThreadUtil taskRunner erroroccured:" + StringUtil.exceptionString(e);
					LOG.error(errorMsg, e);
				} finally {
					taskCounter.decrementAndGet();
				}
				return "success";
			}
		};
		while (true) {
			if (taskCounter.get() < taskTotal) {
				return taskPool.submit(taskRunner);
			}
		}
	}
}