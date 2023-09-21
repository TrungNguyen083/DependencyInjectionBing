package org.example.ORM.repository;


import org.example.ORM.pagination.Pagination;
import org.example.ORM.predicate.QueryPredicateExecutor;


public interface CrudRepository<T,ID> extends Repository<T, ID> , QueryPredicateExecutor<T>, Pagination<T,ID> {
}
