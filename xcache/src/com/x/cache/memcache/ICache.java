package com.x.cache.memcache;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * memcache存储对象接口
 * @author ericSong
 *
 */
public interface ICache {
	/**
	 * 存入单个的对象
	 */
	boolean put(String key, Serializable value);

	/**
	 * 存入带有过期日期的对象
	 */
	boolean put(String key, Serializable value, Date expriedDate);

	/**
	 * 存入带有过期时间的对象
	 */
	boolean put(String key, Serializable value, int expirySec);
	/**
	 * 获取一个对象
	 * @param <T>
	 * @param key
	 * @return 不存在返回null
	 */
	<T extends Serializable> T get(String key);
	
	/**
	 * 通过一个KEY集合获取一个值集合
	 * @param <T>
	 * @param keys
	 * @return
	 */
	<T extends Serializable> Collection<T> getValues(Collection<String> keys);
	
	/**
	 * 通过一个KEY集合获取一个键值对的MAP
	 * @param <T>
	 * @param keys
	 * @return
	 */
	<T extends Serializable> Map<String, T> getBatch(Collection<String> keys);
	
	/**
	 * 删除一个对象
	 * @param key
	 * @return
	 */
	boolean delete(String key);
	/**
	 * 增加一个JMX的缓存服务器
	 * @param host
	 * @param port
	 */
	void addCacheServer(String host,int port);
	
	/**
	 * 减少一个JMX的缓存服务器
	 * @param host
	 * @param port
	 */
	void reduceCacheServer(String host,int port);
	/**
	 * 获取缓存服务器的信息
	 * @return
	 */
	List<String> getCacheServer();
	/**
	 * 清空缓存数据
	 */
	 void clearData();
	 /**
	  * 重新加载缓存连接
	 * @return 
	  */
	 void reloadMemcache();
}
