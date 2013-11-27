package com.wind.quicknote.dao;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

@SuppressWarnings({"unchecked", "hiding"})
@Repository("genericDao")
public class GenericDao<T> implements IGenericDao<T> {
	
	@Resource
	private SessionFactory sessionFactory;
	
	@Override
	public void save(T t) {
		getSession().save(t);
	}
	
	@Override
	public void delete(T t) {
		getSession().delete(t);
	}
	
	@Override
	public void update(T t) {
		getSession().update(t);
	}

	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}
	
	@Override
	public T findById(Long id, Class<T> cls) {
		//T t = (T) getSession().load(cls, id);
		T t = (T) getSession().get(cls, id);
		return t;
	}	
	
	@Override
	public <T> List<T> search(String jpaQuery, Map<String, Object> params) {
		return search(jpaQuery, params, null);
	}
	
	@Override
	public <T> List<T> search(String jpaQuery) {
		return search(jpaQuery, null, null);
	}
	
	private <T> List<T> search(String jpaQuery, Map<String, Object> params, Integer maxResults) {
		Query query = getSession().createQuery(jpaQuery);
		fillParameters(query, params);
		
		if (maxResults != null && maxResults > 0){
			query.setMaxResults(maxResults);
		}
		return query.list();
	}
	
	private void fillParameters(Query query, Map<String, Object> params){
		if (params != null && params.size() > 0) {
			for (Entry<String, Object> param : params.entrySet()){
				query.setParameter(param.getKey(), param.getValue());
			}
		}
	}
	
	@Override
	public <T> List<T> executeNamedQuery(String queryName) {
		return executeNamedQuery(queryName, (Integer) null);
	}

	@Override
	public <T> List<T> executeNamedQuery(String queryName, Integer maxResults) {
		return executeNamedQuery(queryName, null, maxResults);
	}

	@Override
	public <T> List<T> executeNamedQuery(String queryName, Map<String, Object> params) {
		return executeNamedQuery(queryName, params, null);
	}

	@Override
	public <T> List<T> executeNamedQuery(String queryName, Map<String, Object> params, Integer maxResults) {
		Assert.notNull(queryName, "queryName is required");		
		Query query = getSession().getNamedQuery(queryName);
		return executeQuery(query, params, maxResults);
	}

	private <T> List<T> executeQuery(Query query, Map<String, Object> params, Integer maxResults) {
		fillParameters(query, params);

		if (maxResults != null && maxResults > 0){
			query.setMaxResults(maxResults);
		}

		List<T> ret = (List<T>) query.list();
		return ret;
	}

	@Override
	public <T> List<T> executeQuery(String jpaQuery) {
		Query query = getSession().createQuery(jpaQuery);
		return query.list();
	}
	
	@Override
	public <T> void create(T t) {
		getSession().persist(t);
	}

	@Override
	public <T> void merge(T t) {
		getSession().merge(t);
	}

	
}
