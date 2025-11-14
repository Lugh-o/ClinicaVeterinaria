package dao;

import java.util.ArrayList;

public interface IDAO<T> {
    ArrayList<T> getAll();
    T getById(int id);
    T create(T t);
    T update(int id, T t);
    void delete(int id);
}
