package com.x.cache.memcache;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.MemcachedClientBuilder;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.command.TextCommandFactory;
import net.rubyeye.xmemcached.exception.MemcachedException;
import net.rubyeye.xmemcached.transcoders.CachedData;
import net.rubyeye.xmemcached.transcoders.CompressionMode;
import net.rubyeye.xmemcached.transcoders.SerializingTranscoder;
import net.rubyeye.xmemcached.transcoders.Transcoder;
import net.rubyeye.xmemcached.utils.AddrUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * XMEMCACHE�ͻ���
 * 
 * @author ericSong
 * 
 */
public class CacheClient implements ICache {
	private static Logger logger = LoggerFactory.getLogger(CacheClient.class);
	/**
	 * CACHE�Ĺ���ʱ��
	 */
	private final int CACHE_EXPRIED = 60 * 60 * 24 * 3;
	/**
	 * ȫ�ֵĵȴ�ʱ��
	 */
	private final int timeOut = 5000;
	private int countError = 10;
	private MemcachedClient client;

	private String address;
	private int port;
	private int mps;
	private static final SerializingTranscoder transcoder = new SerializingTranscoder();
	{
		transcoder.setCompressionThreshold(1024 * 128);
		transcoder.setCharset("utf-8");
	}

	public CacheClient() {
	}

	public CacheClient(MemcachedClient mc) {
		this.client = mc;
	}

	/**
	 * ��ʼ�����컺��
	 */
	private void initClient() {
		MemcachedClientBuilder builder = new XMemcachedClientBuilder(AddrUtil.getAddresses(address + ":" + port));
		builder.setCommandFactory(new TextCommandFactory());
		builder.setConnectionPoolSize(mps);// ���������
		builder.setFailureMode(true);// ����ת�Ƶ���һ����Ч�ڵ�
		builder.setTranscoder(transcoder);
		try {
			client = builder.build();
			client.setOpTimeout(timeOut);
			client.setOptimizeGet(true);
			client.setEnableHeartBeat(false);
			client.setMergeFactor(50);
			Date current = new Date();
//			for (int i = 1; i <= 5; i++) {
//				Actor actor = client.get("actor_" + i);
//				if (actor != null) {
//					System.out.println(actor.getName());
//				}
//			}
			System.out.println("���Ի�ȡ5����ɫ���ѵ�ʱ��:"
					+ (System.currentTimeMillis() - current.getTime()));
		} catch (IOException e) {
			logger.error("���컺�����ӳ���", e);
		} catch (Exception e) {
			logger.error("���컺�����ӳ���", e);
		}
	}

	public CacheClient(String address, int port, int mps) {
		this.address = address;
		this.port = port;
		this.mps = mps;
		this.initClient();
	}

	public MemcachedClient getClient() {
		return client;
	}

	/**
	 * ���¼��ػ�������
	 */
	public void reloadMemcacheConnect(boolean isForce) {
		if (countError >= 10||isForce) {
			logger.info("���¼��ػ������ӡ���");
			try {
				client.shutdown();
			} catch (IOException e) {
				logger.error("���¼��ػ��������", e);
			}
			this.initClient();
			countError = 0;
		} else {
			countError++;
		}
	}

	@Override
	public boolean delete(String key) {
		try {
			return client.delete(key);
		} catch (Exception e) {
			logger.error("ɾ�����������쳣", e);
			this.reloadMemcacheConnect(false);
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Serializable> T get(String key) {
		try {
			return (T) client.get(key, new CastTranscoder<T>());
		} catch (Exception e) {
			logger.error("��ȡ���������쳣", e);
			this.reloadMemcacheConnect(false);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Serializable> Map<String, T> getBatch(
			Collection<String> keys) {
		try {
			return client.get(keys, new CastTranscoder<T>());
		} catch (Exception e) {
			logger.error("��ȡ���������쳣", e);
			this.reloadMemcacheConnect(false);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Serializable> Collection<T> getValues(
			Collection<String> keys) {
		try {
			return client.get(keys, new CastTranscoder<T>()).values();
		} catch (Exception e) {
			logger.error("��ȡ���������쳣", e);
			this.reloadMemcacheConnect(false);
		}
		return null;
	}

	@Override
	public boolean put(String key, Serializable value) {
		try {
			return client.set(key, CACHE_EXPRIED, value);
		} catch (Exception e) {
			logger.error("�洢���������쳣", e);
			this.reloadMemcacheConnect(false);
		}
		return false;
	}

	@Override
	public boolean put(String key, Serializable value, Date expriedDate) {
		Date current = new Date();
		long el = expriedDate.getTime() - current.getTime();
		try {
			if (el > 0) {
				return client.set(key, (int) el, value);
			} else {
				return this.put(key, value);
			}
		} catch (Exception e) {
			logger.error("�洢���������쳣", e);
			this.reloadMemcacheConnect(false);
		}
		return false;
	}

	@Override
	public boolean put(String key, Serializable value, int expirySec) {
		try {
			if (expirySec == 0) {
				expirySec = CACHE_EXPRIED;
			}
			return client.set(key, expirySec, value);
		} catch (Exception e) {
			logger.error("�洢���������쳣", e);
			this.reloadMemcacheConnect(false);
		}
		return false;
	}

	@Override
	public void addCacheServer(String host, int port) {
		try {
			client.addServer(host, port);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public List<String> getCacheServer() {
		return client.getServersDescription();
	}

	@Override
	public void reduceCacheServer(String host, int port) {
		client.removeServer(host + ":" + port);
	}

	@Override
	public void clearData() {
		try {
			client.flushAll();
		} catch (TimeoutException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (MemcachedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ���TranscoderΨһ�������ǽ�Future����ķ���ֵת��
	 */
	class CastTranscoder<T> implements Transcoder<T> {

		@SuppressWarnings("unchecked")
		@Override
		public T decode(CachedData arg0) {
			return (T) transcoder.decode(arg0);
		}

		@Override
		public CachedData encode(T arg0) {
			return transcoder.encode(arg0);
		}

		@Override
		public boolean isPackZeros() {
			return transcoder.isPackZeros();
		}

		@Override
		public boolean isPrimitiveAsString() {
			return transcoder.isPrimitiveAsString();
		}

		@Override
		public void setCompressionThreshold(int arg0) {
			transcoder.setCompressionThreshold(arg0);

		}

		@Override
		public void setPackZeros(boolean arg0) {
			transcoder.setPackZeros(arg0);

		}

		@Override
		public void setPrimitiveAsString(boolean arg0) {
			transcoder.setPrimitiveAsString(arg0);

		}

		@Override
		public void setCompressionMode(CompressionMode arg0) {
			transcoder.setCompressionMode(arg0);

		}

	}

	@Override
	public void reloadMemcache() {
		this.reloadMemcacheConnect(true);

	}
}
