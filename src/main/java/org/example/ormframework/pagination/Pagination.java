package org.example.ormframework.pagination;


import org.example.ormframework.repository.Repository;

import java.util.List;

public interface Pagination<T,ID> extends Repository<T,ID> {
    List<T> getInPage(int page, int size) throws Exception;
}
