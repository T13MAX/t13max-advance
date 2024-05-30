package com.t13max.data.dao;

import com.t13max.data.entity.AccountData;
import com.t13max.game.config.BaseConfig;
import com.t13max.game.exception.DataException;
import com.t13max.game.run.Application;
import com.t13max.util.Log;
import com.t13max.util.TextUtil;
import lombok.experimental.UtilityClass;

import java.sql.*;

/**
 * 开发阶段凑合用 后续把数据存储改为MongoDB 账号存MySQL 中间件用Redis
 *
 * @author: t13max
 * @since: 11:10 2024/5/29
 */
@UtilityClass
public class SqlLiteUtil {

    //暂时就单链接 后续补充优化
    private final static Connection conn;

    static {

        BaseConfig config = Application.config();

        String dbFile = config.getDbFile();
        if (dbFile == null) {
            throw new DataException("未配置dbFile, 无法创建连接");
        }

        String url = "jdbc:sqlite:" + dbFile;

        try {
            Class.forName("org.sqlite.JDBC");

            conn = DriverManager.getConnection(url);
        } catch (Exception e) {
            Log.data.error("数据库连接创建失败, error={}", e.getMessage());
            throw new RuntimeException(e);
        }

        checkCreate();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                SqlLiteUtil.shutdown();
            } catch (SQLException e) {
                Log.data.error("关闭数据库连接出错, error={}", e.getMessage());
            }
        }));
    }

    private static void checkCreate() {
        try {
            String sql = TextUtil.readSql("create.sql");
            Statement statement = conn.createStatement();
            boolean execute = statement.execute(sql);
            if (!execute) {
                //表已存在会返回false
                Log.common.info("checkCreate, sql执行失败");
            }
        } catch (Exception e) {
            throw new DataException("创建表失败, error=" + e.getMessage());
        }
    }

    public static AccountData selectAccount(String username) {
        String sql = "select * from accountData where username=" + username;
        AccountData accountData = null;
        try {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sql);

            accountData = createAccountData(rs);
            rs.close();
            statement.close();
        } catch (Exception e) {
            Log.data.error("数据获取失败, username={}", username);
        }
        return accountData;
    }

    public static AccountData selectAccount(long id) throws SQLException {
        String sql = "select * from accountData where id=" + id;
        Statement statement = conn.createStatement();
        AccountData accountData = null;
        try {
            ResultSet rs = statement.executeQuery(sql);
            accountData = createAccountData(rs);
            rs.close();
            statement.close();
        } catch (Exception e) {
            Log.data.error("数据获取失败, id={}", id);
        }
        return accountData;
    }

    private static AccountData createAccountData(ResultSet rs) {
        AccountData accountData = null;
        try {
            if (rs.next()) {
                accountData = new AccountData();
                accountData.setId(rs.getLong("id"));
                accountData.setUsername(rs.getString("username"));
                accountData.setPassword(rs.getString("password"));
            }
        } catch (Exception e) {
            Log.data.error("createAccountData失败, rs={}", rs);
        }
        return accountData;
    }

    public static void insertAccount(AccountData accountData) {
        String sql = "insert into accountData (id, username, password) values(" + accountData.getId() + "," + accountData.getUsername() + "," + accountData.getPassword() + ")";
        try {
            Statement stat = conn.createStatement();
            stat.executeUpdate(sql);
            stat.close();
        } catch (Exception e) {
            //理论上 他不应该这样处理 应该有个专门的异常处理 重试队列之类的
            Log.data.error("数据插入失败, accountData={}", accountData);
        }

    }

    public static void deleteAccount(long id) throws SQLException {
        String sql = "delete from accountData where id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setObject(1, id);
        ps.executeUpdate();
        ps.close();
    }

    private static void shutdown() throws SQLException {
        conn.close();
    }
}
