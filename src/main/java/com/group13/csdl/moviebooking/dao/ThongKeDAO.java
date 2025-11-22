package com.group13.csdl.moviebooking.dao;

import com.group13.csdl.moviebooking.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ThongKeDAO {

    // 1. Thống kê Doanh thu theo Phim
    // Trả về Map<Tên Phim, Tổng Tiền>
    public Map<String, Long> layDoanhThuTheoPhim() {
        Map<String, Long> data = new HashMap<>();

        // Câu lệnh SQL: Tính tổng giá vé của các vé đã bán, nhóm theo tên phim
        String sql = "SELECT p.Ten, SUM(sc.GiaVe) AS TongDoanhThu " +
                "FROM VeXemPhim v " +
                "JOIN SuatChieu sc ON v.idSuatChieu = sc.idSuatChieu " +
                "JOIN Phim p ON sc.idPhim = p.idPhim " +
                // Chỉ tính vé đã thanh toán (trạng thái CHUASUDUNG hoặc DASUDUNG)
                "WHERE v.TrangThai IN ('CHUASUDUNG', 'DASUDUNG') " +
                "GROUP BY p.idPhim, p.Ten " +
                "ORDER BY TongDoanhThu DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String tenPhim = rs.getString("Ten");
                long doanhThu = rs.getLong("TongDoanhThu");
                data.put(tenPhim, doanhThu);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    // 2. Thống kê Số lượng vé bán ra theo Ngày (7 ngày gần nhất)
    public Map<String, Integer> laySoVeBanTrong7Ngay() {
        Map<String, Integer> data = new HashMap<>();

        String sql = "SELECT DATE(h.NgayThanhToan) AS Ngay, COUNT(v.idVeXemPhim) AS SoVe " +
                "FROM HoaDon h " +
                "JOIN VeXemPhim v ON h.idHoaDon = v.idHoaDon " +
                "WHERE h.NgayThanhToan >= DATE(NOW()) - INTERVAL 7 DAY " +
                "GROUP BY DATE(h.NgayThanhToan) " +
                "ORDER BY Ngay ASC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String ngay = rs.getString("Ngay");
                int soVe = rs.getInt("SoVe");
                data.put(ngay, soVe);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    // 3. Lấy tổng doanh thu toàn hệ thống
    public long layTongDoanhThu() {
        long total = 0;
        String sql = "SELECT SUM(sc.GiaVe) FROM VeXemPhim v JOIN SuatChieu sc ON v.idSuatChieu = sc.idSuatChieu WHERE v.TrangThai IN ('CHUASUDUNG', 'DASUDUNG')";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                total = rs.getLong(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }
}