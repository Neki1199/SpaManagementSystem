package com.sms.DAO;

import javafx.scene.control.ListView;
import java.sql.SQLException;
import java.util.List;

public interface DAO<T> {

    T get(int id) throws SQLException;
    List<T> getAll() throws SQLException;
    int insert(T obj) throws SQLException;
    void update(T obj) throws SQLException;
    int save(T obj);
    int delete(int id) throws SQLException;
    void search(ListView<String> list, String toSearch) throws SQLException;
}
