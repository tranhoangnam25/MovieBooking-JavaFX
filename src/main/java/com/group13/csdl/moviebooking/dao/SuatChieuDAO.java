package com.group13.csdl.moviebooking.dao;

import com.group13.csdl.moviebooking.model.SuatChieu;
import com.group13.csdl.moviebooking.util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SuatChieuDAO {

    // --- PHẦN DÀNH CHO KHÁCH HÀNG ---

    public List<SuatChieu> laySuatChieuTheoPhim(int idPhim) {
        List<SuatChieu> dsSuatChieu = new ArrayList<>();
        String sql = "SELECT * FROM SuatChieu WHERE idPhim = ? AND ThoiGianBatDau > NOW() ORDER BY ThoiGianBatDau ASC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idPhim);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                dsSuatChieu.add(mapResultSetToSuatChieu(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsSuatChieu;
    }

    public SuatChieu laySuatChieuTheoId(int idSuatChieu) {
        String sql = "SELECT * FROM SuatChieu WHERE idSuatChieu = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idSuatChieu);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToSuatChieu(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // --- PHẦN MỚI DÀNH CHO ADMIN ---

    // 1. Lấy tất cả suất chiếu
    public List<SuatChieu> layTatCaSuatChieu() {
        List<SuatChieu> list = new ArrayList<>();
        String sql = "SELECT * FROM SuatChieu ORDER BY ThoiGianBatDau DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                list.add(mapResultSetToSuatChieu(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 2. Thêm suất chiếu mới
    public boolean themSuatChieu(SuatChieu sc) {
        String sql = "INSERT INTO SuatChieu (GiaVe, ThoiGianBatDau, idPhongChieu, idPhim, idNguoiDung) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, sc.getGiaVe());
            stmt.setObject(2, sc.getThoiGianBatDau());
            stmt.setInt(3, sc.getIdPhongChieu());
            stmt.setInt(4, sc.getIdPhim());
            stmt.setString(5, "AD01"); // Mặc định Admin tạo

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi thêm suất chiếu: " + e.getMessage());
            return false;
        }
    }

    // 3. Xóa suất chiếu
    public boolean xoaSuatChieu(int idSuatChieu) {
        String sql = "DELETE FROM SuatChieu WHERE idSuatChieu = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idSuatChieu);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Không thể xóa suất chiếu đã có vé bán ra.");
            return false;
        }
    }

    private SuatChieu mapResultSetToSuatChieu(ResultSet rs) throws SQLException {
        SuatChieu sc = new SuatChieu();
        sc.setIdSuatChieu(rs.getInt("idSuatChieu"));
        sc.setGiaVe(rs.getInt("GiaVe"));
        sc.setThoiGianBatDau(rs.getObject("ThoiGianBatDau", LocalDateTime.class));
        sc.setIdPhongChieu(rs.getInt("idPhongChieu"));
        sc.setIdPhim(rs.getInt("idPhim"));
        sc.setIdNguoiDung(rs.getString("idNguoiDung"));
        return sc;
    }
}