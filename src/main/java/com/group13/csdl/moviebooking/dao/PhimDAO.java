package com.group13.csdl.moviebooking.dao;

import com.group13.csdl.moviebooking.model.Phim;
import com.group13.csdl.moviebooking.util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PhimDAO {

    // --- LẤY DỮ LIỆU (READ) ---

    public List<Phim> layDanhSachPhim() {
        List<Phim> listPhim = new ArrayList<>();
        String sql = "SELECT * FROM Phim";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                listPhim.add(mapResultSetToPhim(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listPhim;
    }

    public List<Phim> timKiemPhim(String tuKhoa) {
        List<Phim> listPhim = new ArrayList<>();
        // Dùng LOWER để tìm kiếm không phân biệt hoa thường
        String sql = "SELECT * FROM Phim WHERE LOWER(Ten) LIKE LOWER(?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + tuKhoa + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                listPhim.add(mapResultSetToPhim(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listPhim;
    }

    // --- CÁC HÀM MỚI CHO ADMIN (CREATE, UPDATE, DELETE) ---

    // 1. Thêm phim mới
    public boolean themPhim(Phim phim) {
        String sql = "INSERT INTO Phim (Ten, NgayPhatHanh, ThoiLuong, NgonNguChinh, NoiDung, idNguoiDung) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, phim.getTen());
            stmt.setObject(2, phim.getNgayPhatHanh());
            stmt.setFloat(3, phim.getThoiLuong());
            stmt.setString(4, phim.getNgonNguChinh());
            stmt.setString(5, phim.getNoiDung());
            stmt.setString(6, phim.getIdNguoiDung()); // ID của Admin tạo phim

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 2. Cập nhật thông tin phim
    public boolean capNhatPhim(Phim phim) {
        String sql = "UPDATE Phim SET Ten=?, NgayPhatHanh=?, ThoiLuong=?, NgonNguChinh=?, NoiDung=? WHERE idPhim=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, phim.getTen());
            stmt.setObject(2, phim.getNgayPhatHanh());
            stmt.setFloat(3, phim.getThoiLuong());
            stmt.setString(4, phim.getNgonNguChinh());
            stmt.setString(5, phim.getNoiDung());
            stmt.setInt(6, phim.getIdPhim());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 3. Xóa phim
    public boolean xoaPhim(int idPhim) {
        // Lưu ý: Nếu phim đã có Suất chiếu hoặc Vé thì SQL sẽ chặn xóa (do khóa ngoại)
        // Cần xóa suất chiếu trước (hoặc xử lý cascade trong DB)
        String sql = "DELETE FROM Phim WHERE idPhim = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idPhim);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            // In ra lỗi để biết nếu bị chặn bởi khóa ngoại
            System.err.println("Không thể xóa phim ID " + idPhim + ". Có thể phim đang có lịch chiếu.");
            e.printStackTrace();
            return false;
        }
    }

    // Helper
    private Phim mapResultSetToPhim(ResultSet rs) throws SQLException {
        Phim phim = new Phim();
        phim.setIdPhim(rs.getInt("idPhim"));
        phim.setTen(rs.getString("Ten"));
        phim.setNgayPhatHanh(rs.getObject("NgayPhatHanh", LocalDate.class));
        phim.setThoiLuong(rs.getFloat("ThoiLuong"));
        phim.setNgonNguChinh(rs.getString("NgonNguChinh"));
        phim.setNoiDung(rs.getString("NoiDung"));
        phim.setIdNguoiDung(rs.getString("idNguoiDung"));
        return phim;
    }
}