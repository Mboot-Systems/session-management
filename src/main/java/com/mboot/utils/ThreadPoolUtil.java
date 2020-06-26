package com.mboot.utils;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Mboot
 * @date 2020-06-26 17:03
 */
public class ThreadPoolUtil {

	private static BlockingQueue<Runnable> bqueue = new ArrayBlockingQueue<Runnable>(100);
	private static final int SIZE_CORE_POOL = 5;
	private static final int SIZE_MAX_POOL = 10;
	private static final long ALIVE_TIME = 2000;

	private static ThreadPoolExecutor pool = new ThreadPoolExecutor(SIZE_CORE_POOL, SIZE_MAX_POOL, ALIVE_TIME,
			TimeUnit.MILLISECONDS, bqueue, new ThreadPoolExecutor.CallerRunsPolicy());

	static {

		pool.prestartAllCoreThreads();
	}

	public static ThreadPoolExecutor getPool() {
		return pool;
	}

	public static void main(String[] args) {
		System.out.println(pool.getPoolSize());
	}
}
