package de.chronies.user.service.repositories;

public interface ObjectRepository<T> {

    boolean update(T t);

    boolean create(T t);

/*    List<T> getAll();*/

/*
    boolean create(T t);

    Optional<T> get(long id);

    boolean update(T t, long id);

    boolean delete(long id);
*/

}
