package com.group13.csdl.moviebooking.dao;

import com.group13.csdl.moviebooking.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class VeDAO {

    // Cập nhật trạng thái của 1 vé cụ thể (Dùng cho chức năng In vé)
    public boolean capNhatTrangThai(int idVe, String trangThaiMoi) {
        String sql = "UPDATE VeXemPhim SET TrangThai = ? WHERE idVeXemPhim = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, trangThaiMoi);
            stmt.setInt(2, idVe);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // --- HÀM MỚI: TỰ ĐỘNG QUÉT VÀ CẬP NHẬT VÉ HẾT HẠN ---
    public void capNhatVeHetHan() {
        // Logic: Cập nhật thành 'HETHAN' cho những vé đang là 'CHUASUDUNG'
        // Mà có (Giờ chiếu + Thời lượng phim) < Giờ hiện tại
        String sql = "UPDATE VeXemPhim v " +
                "JOIN SuatChieu s ON v.idSuatChieu = s.idSuatChieu " +
                "JOIN Phim p ON s.idPhim = p.idPhim " +
                "SET v.TrangThai = 'HETHAN' " +
                "WHERE v.TrangThai = 'CHUASUDUNG' " +
                "AND DATE_ADD(s.ThoiGianBatDau, INTERVAL p.ThoiLuong MINUTE) < NOW()";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Hệ thống: Đã tự động cập nhật " + rows + " vé hết hạn.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}