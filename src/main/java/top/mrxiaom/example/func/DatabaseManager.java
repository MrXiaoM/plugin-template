package top.mrxiaom.example.func;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.configuration.MemoryConfiguration;
import org.jetbrains.annotations.Nullable;
import top.mrxiaom.example.ExamplePlugin;
import top.mrxiaom.example.db.IDatabase;
import top.mrxiaom.example.db.ExampleDatabase;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager extends AbstractPluginHolder {

    HikariDataSource dataSource = null;
    private final List<IDatabase> databases = new ArrayList<>();
    @Nullable
    public Connection getConnection() {
        try {
            return dataSource.getConnection();

        } catch (Throwable t) {
            t.printStackTrace();
            return null;
        }
    }
    public final ExampleDatabase example;
    public DatabaseManager(ExamplePlugin plugin) {
        super(plugin);
        databases.add(example = new ExampleDatabase(this));
        register();
    }

    @Override
    public void reloadConfig(MemoryConfiguration config) {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName("com.mysql.cj.jdbc.Driver");
        hikariConfig.setAutoCommit(true);
        hikariConfig.setMaxLifetime(120000L);
        hikariConfig.setIdleTimeout(5000L);
        hikariConfig.setConnectionTimeout(5000L);
        hikariConfig.setMinimumIdle(10);
        hikariConfig.setMaximumPoolSize(100);
        String host = config.getString("mysql.host", "localhost");
        int port = config.getInt("mysql.port", 3306);
        String user = config.getString("mysql.user", "root");
        String pass = config.getString("mysql.pass", "root");
        String database = config.getString("mysql.database", "db");
        hikariConfig.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=false&allowPublicKeyRetrieval=true&verifyServerCertificate=false&serverTimezone=UTC");
        hikariConfig.setUsername(user);
        hikariConfig.setPassword(pass);
        if (dataSource != null) dataSource.close();
        dataSource = new HikariDataSource(hikariConfig);

        info("正在连接数据库...");
        Connection conn = getConnection();
        if (conn == null) warn("无法连接到数据库!");
        else {
            for (IDatabase db : databases) db.reload(conn);
            info("数据库连接成功");
            try {
                conn.close();
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }

    @Override
    public void onDisable() {
        dataSource.close();
    }
}
