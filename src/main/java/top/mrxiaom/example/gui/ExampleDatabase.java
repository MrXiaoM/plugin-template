package top.mrxiaom.example.gui;

import top.mrxiaom.example.db.IDatabase;
import top.mrxiaom.example.func.AbstractPluginHolder;
import top.mrxiaom.example.func.DatabaseManager;
import top.mrxiaom.sqlhelper.*;
import top.mrxiaom.sqlhelper.conditions.Condition;
import top.mrxiaom.sqlhelper.conditions.EnumOperators;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ExampleDatabase extends AbstractPluginHolder implements IDatabase {
    public static final String TABLE_NAME = "EXAMPLE";
    public final DatabaseManager manager;
    public ExampleDatabase(DatabaseManager manager) {
        super(manager.plugin);
        this.manager = manager;
    }

    @Override
    public void reload(Connection conn) {
        SQLang.createTable(conn, TABLE_NAME, true,
                TableColumn.create(SQLValueType.ValueString.of(255), "name"),
                TableColumn.create(SQLValueType.ValueString.of(255), "example")
        );
    }

    public List<String> getExamplesByName(String name) {
        List<String> record = new ArrayList<>();
        try (Connection conn = manager.getConnection()) {
            try (PreparedStatement ps = SQLang.select(TABLE_NAME)
                    .column("name", "example")
                    .where(
                            Condition.of("name", EnumOperators.EQUALS, name)
                    )
                    .orderBy(EnumOrder.DESC, " time")
                    .limit(600)
                    .build(conn).orElse(null)) {
                if (ps == null) throw new SQLException("无法构建语句");

                ResultSet result = ps.executeQuery();

                while (result.next()) {
                    String example = result.getString("example");

                    record.add(example);
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return record;
    }

    public void putNameExample(String name, String example) {
        try (Connection conn = manager.getConnection()) {
            try (PreparedStatement ps = SQLang.insertInto(TABLE_NAME)
                    .addValues(
                            Pair.of("name", name),
                            Pair.of("example", example)
                    ).build(conn).orElse(null)) {
                if (ps == null) return;
                ps.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
