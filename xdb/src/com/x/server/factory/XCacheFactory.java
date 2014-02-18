package com.x.server.factory;

import com.x.cache.memcache.ICache;

/**
 * memcache缓存实例工厂类
 * 
 * @author ericSong
 * 
 */
public class XCacheFactory {
	private static ICache cache;
	/**
	 * 获取一个memcache缓存实例
	 * 
	 * @return
	 */
	public static ICache getInstance() {
		if (cache == null) {
			cache = (ICache) CustomBeanFactory.getBean("cache");
		}
		return cache;
	}
}
