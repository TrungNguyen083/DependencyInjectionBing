package org.example.ormframework.repository;


import org.example.ormframework.pagination.Pagination;
import org.example.ormframework.predicate.QueryPredicateExecutor;


public interface CrudRepository<T,ID> extends Repository<T, ID> , QueryPredicateExecutor<T>, Pagination<T,ID> {
}
