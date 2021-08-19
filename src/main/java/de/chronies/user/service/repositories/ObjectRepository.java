package de.chronies.user.service.repositories;

import java.util.List;
import java.util.Optional;

public interface ObjectRepository<T> {

    boolean create(T t);

/*    List<T> getAll();*/

/*
    boolean create(T t);

    Optional<T> get(long id);

    boolean update(T t, long id);

    boolean delete(long id);
*/

}
