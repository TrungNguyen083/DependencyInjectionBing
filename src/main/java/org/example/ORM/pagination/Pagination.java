package org.example.ORM.pagination;


import org.example.ORM.repository.Repository;

import java.util.List;

public interface Pagination<T,ID> extends Repository<T,ID> {
    List<T> getInPage(int page, int size) throws Exception;
}
