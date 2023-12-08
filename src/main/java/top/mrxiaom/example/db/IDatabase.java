package top.mrxiaom.example.db;

import java.sql.Connection;

public interface IDatabase {
    void reload(Connection conn);
}
