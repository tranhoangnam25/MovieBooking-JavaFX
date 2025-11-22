package com.group13.csdl.moviebooking.dao;

import com.group13.csdl.moviebooking.model.NguoiDung;
import com.group13.csdl.moviebooking.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class NguoiDungDAO {

    public NguoiDung dangNhap(String taiKhoan, String matKhau) {
        String sql = "SELECT * FROM NguoiDung WHERE TaiKhoan = ? AND MatKhau = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, taiKhoan);
            stmt.setString(2, matKhau);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return mapResultSetToNguoiDung(rs);
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public boolean dangKy(NguoiDung user) {
        if (kiemTraTonTai(user.getTaiKhoan(), user.getEmail(), user.getSdt())) return false;
        String sql = "INSERT INTO NguoiDung (idNguoiDung, TaiKhoan, MatKhau, HoTen, NgaySinh, SDT, Email, VaiTro) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            String id = "ND" + System.currentTimeMillis();
            stmt.setString(1, id.length() > 20 ? id.substring(0, 20) : id);
            stmt.setString(2, user.getTaiKhoan());
            stmt.setString(3, user.getMatKhau());
            stmt.setString(4, user.getHoTen());
            stmt.setObject(5, user.getNgaySinh());
            stmt.setString(6, user.getSdt());
            stmt.setString(7, user.getEmail());
            stmt.setString(8, "KHACHHANG");
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean kiemTraTonTai(String taiKhoan, String email, String sdt) {
        String sql = "SELECT count(*) FROM NguoiDung WHERE TaiKhoan = ? OR Email = ? OR SDT = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, taiKhoan);
            stmt.setString(2, email);
            stmt.setString(3, sdt);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    public List<NguoiDung> timKiemKhachHang(String tuKhoa) {
        List<NguoiDung> list = new ArrayList<>();
        String sql = "SELECT * FROM NguoiDung WHERE VaiTro = 'KHACHHANG' AND (SDT LIKE ? OR LOWER(Email) LIKE LOWER(?) OR LOWER(HoTen) LIKE LOWER(?))";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            String query = "%" + tuKhoa + "%";
            stmt.setString(1, query); stmt.setString(2, query); stmt.setString(3, query);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) list.add(mapResultSetToNguoiDung(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    // --- HÀM MỚI: CẬP NHẬT THÔNG TIN CÁ NHÂN (SĐT, EMAIL) ---
    public boolean capNhatThongTin(NguoiDung user) {
        String sql = "UPDATE NguoiDung SET SDT = ?, Email = ? WHERE idNguoiDung = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getSdt());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getIdNguoiDung());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private NguoiDung mapResultSetToNguoiDung(ResultSet rs) throws SQLException {
        NguoiDung user = new NguoiDung();
        user.setIdNguoiDung(rs.getString("idNguoiDung"));
        user.setTaiKhoan(rs.getString("TaiKhoan"));
        user.setMatKhau(rs.getString("MatKhau"));
        user.setHoTen(rs.getString("HoTen"));
        user.setNgaySinh(rs.getObject("NgaySinh", LocalDate.class));
        user.setSdt(rs.getString("SDT"));
        user.setEmail(rs.getString("Email"));
        user.setVaiTro(rs.getString("VaiTro"));
        user.setNgayBatDau(rs.getObject("NgayBatDau", LocalDate.class));
        user.setTrangThai(rs.getString("TrangThai"));
        user.setIdQuanLy(rs.getString("idQuanLy"));
        return user;
    }
}