package com.group13.csdl.moviebooking.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    // Tên DB dựa theo file SQL bạn cung cấp [cite: 204]
    private static final String DB_URL = "jdbc:mysql://localhost:3307/QuanLyBanVeOnline?useSSL=false&allowPublicKeyRetrieval=true&characterEncoding=UTF-8";
    // Bạn hãy thay đổi user/password phù hợp với máy cá nhân của bạn
    private static final String USER = "root";
    private static final String PASS = "1";

    public static Connection getConnection() {
        Connection conn = null;
        try {
            // Đảm bảo bạn đã thêm thư viện mysql-connector-java vào dự án
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println("Kết nối cơ sở dữ liệu thành công!");
        } catch (ClassNotFoundException e) {
            System.err.println("Không tìm thấy Driver MySQL! Hãy kiểm tra lại thư viện.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Kết nối thất bại! Kiểm tra lại URL, User hoặc Password.");
            e.printStackTrace();
        }
        return conn;
    }

    // Hàm main để test nhanh kết nối
    public static void main(String[] args) {
        getConnection();
    }
}