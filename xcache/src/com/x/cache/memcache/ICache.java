package com.x.cache.memcache;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * memcache�洢����ӿ�
 * @author ericSong
 *
 */
public interface ICache {
	/**
	 * ���뵥���Ķ���
	 */
	boolean put(String key, Serializable value);

	/**
	 * ������й������ڵĶ���
	 */
	boolean put(String key, Serializable value, Date expriedDate);

	/**
	 * ������й���ʱ��Ķ���
	 */
	boolean put(String key, Serializable value, int expirySec);
	/**
	 * ��ȡһ������
	 * @param <T>
	 * @param key
	 * @return �����ڷ���null
	 */
	<T extends Serializable> T get(String key);
	
	/**
	 * ͨ��һ��KEY���ϻ�ȡһ��ֵ����
	 * @param <T>
	 * @param keys
	 * @return
	 */
	<T extends Serializable> Collection<T> getValues(Collection<String> keys);
	
	/**
	 * ͨ��һ��KEY���ϻ�ȡһ����ֵ�Ե�MAP
	 * @param <T>
	 * @param keys
	 * @return
	 */
	<T extends Serializable> Map<String, T> getBatch(Collection<String> keys);
	
	/**
	 * ɾ��һ������
	 * @param key
	 * @return
	 */
	boolean delete(String key);
	/**
	 * ����һ��JMX�Ļ��������
	 * @param host
	 * @param port
	 */
	void addCacheServer(String host,int port);
	
	/**
	 * ����һ��JMX�Ļ��������
	 * @param host
	 * @param port
	 */
	void reduceCacheServer(String host,int port);
	/**
	 * ��ȡ�������������Ϣ
	 * @return
	 */
	List<String> getCacheServer();
	/**
	 * ��ջ�������
	 */
	 void clearData();
	 /**
	  * ���¼��ػ�������
	 * @return 
	  */
	 void reloadMemcache();
}
