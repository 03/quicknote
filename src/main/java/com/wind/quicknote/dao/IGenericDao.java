package com.wind.quicknote.dao;

import java.util.List;
import java.util.Map;

@SuppressWarnings("hiding")
public interface IGenericDao<T> {

	<T> List<T> search(String jpaQuery);

	<T> List<T> search(String jpaQuery, Map<String, Object> params);

	<T> List<T> executeQuery(String jpaQuery);

	<T> List<T> executeNamedQuery(String queryName);

	<T> List<T> executeNamedQuery(String queryName, Integer maxResults);

	<T> List<T> executeNamedQuery(String queryName, Map<String, Object> params);

	<T> List<T> executeNamedQuery(String queryName, Map<String, Object> params, Integer maxResults);

	<T> void create(T t);

	void delete(T t);

	<T> void merge(T t);

	void save(T t);

	void update(T t);

	T findById(Long id, Class<T> cls);
}
