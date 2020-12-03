package de.scope.scopeone.reporting.sec.module.xbrlrepository.repository;


public interface Dao<T> {

  void save(T t);

  T load(String id);

  int delete(String id);

  int update(T t);

}
