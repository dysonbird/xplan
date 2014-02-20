package com.x.manage;


import java.math.BigInteger;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.type.NullableType;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;


/**
 * 
 * @author 
 * Class name:EntityManage
 * Description:用于进行数据库的持久化操作，即把实体对象保存到数据库中
 */
public class EntityManage extends HibernateDaoSupport{
	private static final Log log = LogFactory.getLog(EntityManage.class);
	
	/**
	 * 
	 * Function name:save
	 * Description: 保存对象到数据库
	 * @param transientInstance：需要保存的对象
	 */
	public void save(Object transientInstance) {
		log.debug("saving tbActor instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}
	
	/**
	 * 
	 * Function name:delete
	 * Description: 删除数据库中的记录
	 * @param persistentInstance：所要删除的对象实例
	 */
	public void delete(Object persistentInstance) {
		log.debug("deleting tbActor instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}
	
	/**
	 * 
	 * Function name:update
	 * Description: 更新数据库的对象
	 * @param instance：所要更新的对象实例
	 */
	public void update(Object instance) {
		log.debug("save the actor card");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
	
	/**
	 * 
	 * Function name:findById
	 * Description: 通过编号获取实体对象实例
	 * @param entity：实体对象的class
	 * @param id：对象编号
	 * @return：实体对象实例
	 */
	public Object findById(Class entity,long id) {
		log.debug("finding instance with id: " + id);
		try {
			Object rs = getHibernateTemplate().get(entity, id);
			return rs;
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}
	
	/**
	 * 
	 * Function name:findById
	 * Description: 通过编号获取实体对象实例
	 * @param entity：实体对象的class
	 * @param id：对象编号
	 * @return：实体对象实例
	 */
	public Object findById(Class entity,String id) {
		log.debug("finding instance with id: " + id);
		try {
			Object rs = getHibernateTemplate().get(entity, id);
			return rs;
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}
	
	/**
	 * 
	 * Function name:findById
	 * Description: 通过编号获取实体对象实例
	 * @param entity：实体对象的class
	 * @param id：对象编号
	 * @return：实体对象实例
	 */
	public Object findById(Class entity,int id) {
		log.debug("finding instance with id: " + id);
		try {
			Object rs = getHibernateTemplate().get(entity, id);
			return rs;
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}
	
	/**
	 * 
	 * Function name:findAll
	 * Description: 查找指定表的所有数据
	 * @param tablename：所要查找的表的名字，表名在tbEntityDAO中有定义
	 * @return：表中所有数据的集合
	 */
	public List findAll(String tablename) {
		log.debug("finding all from "+tablename);
		try {
			String queryString = "from "+tablename;
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
	
	/**
	 * 
	 * Function name:findByProperty
	 * Description: 通过属性查找符合条件的数据
	 * @param tableName：数据表名，在tbEntityDAO中定义
	 * @param propertyName：属性名
	 * @param value：属性值
	 * @return:返回符合条件的数据列表
	 */
	public List findByProperty(String tableName,String propertyName, Object value) {
		log.debug("finding instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from "+tableName+" as model where model."
					+ propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}
	
	/**
	 * 
	 * Function name:findByProperties
	 * Description: 通过多个属性查询符合条件的数据
	 * @param tableName：数据表名，在tbEntityDAO中定义
	 * @param propertyName：属性名数组
	 * @param value：属性值数组
	 * @return：返回符合条件的数据列表
	 */
	public List findByProperties(String tableName,String[] propertyName, Object[] value) {
		log.debug("finding instance with properties: "
				+ propertyName.toString() + ", value: " + value.toString());
		try {
			String queryString = "from "+tableName+" as model ";
			for(int i=0;i<propertyName.length;i++){
				if(i==0){
					queryString += " where model."+propertyName[i]+"=? ";
				}else{
					queryString += " and model."+propertyName[i]+"=? ";
				}
			}
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}
	
	/**
	 * 
	 * Function name:findByHql
	 * Description: 根据hql查询数据库，获取相应数据
	 * @param hql：hql查询语句
	 * @return：符合查询条件的数据集合
	 */
	public List findByHql(String hql){
		log.debug("finding instance with hql: "+hql);
		try {
			return getHibernateTemplate().find(hql);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}
	
	/**
	 * 
	 * Function name:findByHql
	 * Description:通过hql查找数据，带分页功能 
	 * @param hql:hql语句
	 * @param pageNum：页码（1开始）
	 * @param pageSize：页个数
	 * @return：返回数据列表
	 */
	public List findByHql(final String hql,int pageNum,final short pageSize){
		log.debug("finding instance with hql: "+hql);
		try {
			// pageIndex 当前页，pageSize页显示大小
			final int items = (pageNum - 1) * pageSize;
			List list = getHibernateTemplate().executeFind(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createQuery(hql);
						
						query.setFirstResult(items);// 定义从第几条开始查询
						query.setMaxResults(pageSize);// 定义返回的记录数

						List list = query.list();
						return list;
					}
				}
			);
			return list;
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}
	
	/**
	 * 
	 * Function name:getCountByHql
	 * Description:通过一个hql语句获得数据总量 
	 * @param hql：hql语句
	 * @return：返回数据总量
	 */
	public int getCountByHql(String hql){
		try{
			String countHql = "select count(*) "+hql;
			int count = ((Long)getHibernateTemplate().find(countHql).get(0)).intValue();
//			int count = ((Long)getHibernateTemplate().iterate(countHql).next()).intValue();
			return count;
		}catch(RuntimeException ex){
			log.error("find by getCountByHql", ex);
			throw ex;
		}
	}
	
	/**
	 * 
	 * Function name:findByHqlWithCount
	 * Description: 通过hql查找数据，带分页功能，并且返回总条目数
	 * @param hql：查询语句
	 * @param pageNum：页码（1开始）
	 * @param pageSize：页个数
	 * @return：返回数据列表
	 */
	public List findByHqlWithCount(final String hql,int pageNum,final short pageSize){
		log.debug("finding instance with hql: "+hql);
		try {
			// pageIndex 当前页，pageSize页显示大小
			final int items = (pageNum - 1) * pageSize;
			String countHql = "select count(*) "+hql;
			int count = ((Long)getHibernateTemplate().find(countHql).get(0)).intValue();
			List list = getHibernateTemplate().executeFind(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createQuery(hql);
						
						query.setFirstResult(items);// 定义从第几条开始查询
						query.setMaxResults(pageSize);// 定义返回的记录数

						List list = query.list();
						return list;
					}
				}
			);
			list.add(0,count);
			return list;
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}
	
	/**
	 * 
	 * Function name:findBySql
	 * Description: 通过sql查找数据
	 * @param sql：sql字串
	 * @return:数据列表
	 */
	public List findBySql(String sql){
		log.debug("finding instance with hql: "+sql);
		Session session = this.getSession();
		try {
			
			List rs = session.createSQLQuery(sql).list();
			return rs;
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}finally{
			this.releaseSession(session);
		}
	}
	
	/**
	 * 
	 * Function name:findStringDataBySql
	 * Description:通过sql查询数据，并且转化成为String[]的列表输出 
	 * @param sql：sql语句
	 * @return：返回结果
	 */
	public List<String[]> findStringDataBySql(String sql){
		log.debug("finding instance with hql: "+sql);
		Session session = this.getSession();
		try {
			List<String[]> result = new ArrayList<String[]>();
//			List<Object[]> rs = (List<Object[]>)findBySql(sql);
			List rs = findBySql(sql);
			if(rs!=null){
				if(rs.size()>0){
					if(rs.get(0) instanceof Object[]){
						for(Object[] objs:(List<Object[]>)rs){
							String[] data = new String[objs.length];
							for(int i=0;i<data.length;i++){
								Object obj = objs[i];
								if(obj!=null){
									data[i]=obj.toString();
								}else{
									data[i]=null;
								}
							}
							result.add(data);
						}
					}else if(rs.get(0) instanceof BigInteger){
						for(BigInteger obj:(List<BigInteger>)rs){
							String[] data = new String[1];
							if(obj!=null){
								data[0]=obj.toString();
							}else{
								data[0]=null;
							}
							result.add(data);
						}
					}else{
						for(Object obj:(List<Object>)rs){
							String[] data = new String[1];
							if(obj!=null){
								data[0]=obj.toString();
							}else{
								data[0]=null;
							}
							result.add(data);
						}
					}
				}
				return result;
			}else{
				return null;
			}
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}finally{
			this.releaseSession(session);
		}
	}
	
	/**
	 * Function name:findBySql
	 * Description: 通过sql查找数据，并且可以指定输出字段类型
	 * @param sql：sql语句
	 * @param fieldnames：字段名列表
	 * @param fieldtypes：字段指向的Hibernate类型，用Hibernate.获取，例如Hibernate.STRING
	 * @return
	 */
	public List findBySql(String sql,String[] fieldnames,NullableType[] fieldtypes){
		log.debug("finding instance with hql: "+sql);
		Session session = this.getSession();
		try {
			SQLQuery query = session.createSQLQuery(sql);
			for(int i=0;i<fieldnames.length;i++){
				if(fieldtypes[i] == null){
					query.addScalar(fieldnames[i]);
				}else{
					query.addScalar(fieldnames[i], fieldtypes[i]);
				}
			}
			List rs = query.list();
			return rs;
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}finally{
			this.releaseSession(session);
		}
	}
	
	/**
	 * 
	 * Function name:findBySql
	 * Description: 通过sql查找数据
	 * @param sql：sql字串
	 * @param clasz:要查询的类
	 * @return:数据列表
	 */
	public List findBySql(String sql,Class clasz){
		log.debug("finding instance with sql: "+sql);
		Session session = this.getSession();
		try {
			List rs = session.createSQLQuery(sql).addEntity(clasz).list();
			return rs;
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}finally{
			this.releaseSession(session);
		}
	}
	
	public List findBySql(final String sql,final Class clazz,final int pageNum,final int pageSize){
		log.debug("finding instance with hql: "+sql);
		try {
			// pageIndex 当前页，pageSize页显示大小
			final int items = (pageNum - 1) * pageSize;
			List list = getHibernateTemplate().executeFind(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						SQLQuery query = session.createSQLQuery(sql);
						
						query.setFirstResult(items);// 定义从第几条开始查询
						query.setMaxResults(pageSize);// 定义返回的记录数
						query.addEntity(clazz);

						return query.list();
					}
				}
			);
			return list;
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}
	
	/**
	 * 
	 * Function name:updateBySql
	 * Description: 根据sql进行更新
	 * @param sql：sql语句
	 * @return：返回结果
	 */
	public boolean updateBySql(String sql){
		log.debug("update by sql: "+sql);
		Session session = this.getSession();
		try {
			int rs = session.createSQLQuery(sql).executeUpdate();
			return true;
		} catch (Exception re) {
			log.error("update by sql error ", re);
			return false;
		}finally{
			this.releaseSession(session);
		}
	}
	
	/**
	 * 
	 * Function name:findByHql
	 * Description: 根据hql查询数据库，获取相应数据
	 * @param hql: hql查询语句
	 * @param values: hql中的？号填充值
	 * @return：符合查询条件的数据集合
	 */
	public List findByHql(String hql,Object[] values){
		log.debug("finding instance with hql: "+hql);
		try {
			return getHibernateTemplate().find(hql,values);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}
	
	/**
	 * 
	 * Function name:insertRecords
	 * Description: 批量插入数据
	 * @param records：数据集合
	 * @return: true成功 false失败
	 */
	public boolean insertRecords(List records){
		//Session session = getSessionFactory().openSession();
		Session session = this.getSession();
		Transaction tx = session.beginTransaction();
		try {
			for (int i = 0; i < records.size() && i < 10000; i++) {
				Object record = records.get(i);
				session.save(record);
				if (i % 20 == 0) { //20, same as the JDBC batch size 
					//flush a batch of inserts and release memory: 
					session.flush(); // 往数据库插入记录
					session.clear(); // 清理缓存
				}
			}
			tx.commit();
			return true;
		} catch (Exception e) {
			logger.error("批量插入数据出错了:"+e.getMessage());
			tx.rollback();
			return false;
		} finally{
			//session.close();
			this.releaseSession(session);
		}
	}
	
	/**
	 * 
	 * Function name:insertRecords
	 * Description: 批量更新数据
	 * @param records：数据集合
	 * @return: true成功 false失败
	 */
	public boolean updateRecords(List records){
		//Session session = getSessionFactory().openSession();
		Session session = this.getSession();
		Transaction tx = session.beginTransaction();
		try {
			for (int i = 0; i < records.size() && i < 10000; i++) {
				Object record = records.get(i);
				session.update(record);
				if (i % 20 == 0) { //20, same as the JDBC batch size 
					//flush a batch of inserts and release memory: 
					session.flush();
					session.clear();
				}
			}
			tx.commit();
			return true;
		} catch (Exception e) {
			logger.error("批量更新数据出错了:"+e.getMessage());
			tx.rollback();
			return false;
		} finally{
			//session.close();
			this.releaseSession(session);
		}
	}
	
	
	
	
	/**
	 * 
	 * Function name:insertRecords
	 * Description: 批量删除数据
	 * @param records：数据集合
	 * @return: true成功 false失败
	 */
	public boolean deleteRecords(List records){
		//Session session = getSessionFactory().openSession();
		Session session = this.getSession();
		Transaction tx = session.beginTransaction();
		try {
			for (int i = 0; i < records.size() && i < 10000; i++) {
				Object record = records.get(i);
				session.delete(record);
				if (i % 20 == 0) { //20, same as the JDBC batch size 
					//flush a batch of inserts and release memory: 
					session.flush();
					session.clear();
				}
			}
			tx.commit();
			return true;
		} catch (Exception e) {
			logger.error("批量删除数据出错了:"+e.getMessage());
			tx.rollback();
			return false;
		} finally{
			//session.close();
			this.releaseSession(session);
		}
	}
	
	/**
	 * 
	 * Function name:handleRecords
	 * Description: 批量处理数据库操作
	 * @param records：操作列表
	 * @return：返回结果
	 */
	public boolean handleRecords(List<DbOper> records){
		//Session session = getSessionFactory().openSession();
		Session session = this.getSession();
		Transaction tx = session.beginTransaction();
		int i = 0;
		try {
			for (; i < records.size() && i < 10000; i++) {
				DbOper record = records.get(i);
				if(record.operType==DbOper.OPER_TYPE_INSERT){
					//session.save(record.operObj);//高效
					session.merge(record.operObj);//安全
				}else if(record.operType==DbOper.OPER_TYPE_UPDATE){
					//session.update(record.operObj);
					session.merge(record.operObj);
				}else{
						session.delete(record.operObj);
					}
					//session.delete(record.operObj);
				}
				if (i!=0 && i % 20 == 0) { //20, same as the JDBC batch size 
					//flush a batch of inserts and release memory: 
					session.flush();
					session.clear();
			}
			tx.commit();
			return true;
		} catch (Exception e) {
			tx.rollback();
			logger.error("批量处理数据出错了:i="+i,e);
			for (int j=0; j < records.size(); j++) {
				DbOper record = records.get(j);
				if(record==null){
					continue;
				}
				String opertypeDes = "未知";
				if(record.operType==DbOper.OPER_TYPE_INSERT){
					opertypeDes="添加";
				}else if(record.operType==DbOper.OPER_TYPE_UPDATE){
					opertypeDes="更新";
				}else if(record.operType==DbOper.OPER_TYPE_DELETE){
					opertypeDes="删除";
				}
				long id = 0;
				String objtype = "未知";
				logger.error(j+":opertype="+opertypeDes+";objtype="+objtype+";id="+id);
			}
			return false;
		} finally{
			//session.close();
			this.releaseSession(session);
		}
	}
	
	/**
	 * Function name:insertOperLogMultiTable
	 * Description: 批量插入玩家操作日志(分表)
	 * @param records:日志记录列表
	 */
	public void insertOperLogMultiTable(List records){
		Session session = getSessionFactory().openSession();
		try {
			Transaction tx = session.beginTransaction();
			for (int i = 0; i < records.size() && i < 10000; i++) {
				Object record = records.get(i);
				session.save(record);
				if (i % 100 == 0) { //20, same as the JDBC batch size 
					//flush a batch of inserts and release memory: 
					session.flush();
					session.clear();
				}
			}
			tx.commit();
		} catch (Exception e) {
			log.error("批量插入玩家操作日志出错了:"+e.getMessage());
		}finally{
			session.close();
		}
	}
}
