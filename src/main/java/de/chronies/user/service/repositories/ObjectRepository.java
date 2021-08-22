package de.chronies.user.service.repositories;

import java.util.Optional;

public interface ObjectRepository<T> {

    boolean update(T t);

    boolean create(T t);

    Optional<T> get(int id);

    boolean delete(int id);

}
