package com.group13.csdl.moviebooking.dao;

import com.group13.csdl.moviebooking.model.LichLamViec;
import com.group13.csdl.moviebooking.util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class LichLamViecDAO {

    // Lấy lịch làm việc của nhân viên dựa trên ID
    public List<LichLamViec> layLichLamViec(String idNhanVien) {
        List<LichLamViec> list = new ArrayList<>();

        // Join bảng LichCuThe với CaLamViec để lấy chi tiết giờ giấc
        String sql = "SELECT l.NgayLamViec, c.NoiDung, c.GioBatDau, c.GioKetThuc " +
                "FROM LichCuThe l " +
                "JOIN CaLamViec c ON l.idCaLamViec = c.idCaLamViec " +
                "WHERE l.idNguoiDung = ? " +
                "ORDER BY l.NgayLamViec DESC"; // Ngày mới nhất lên đầu

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, idNhanVien);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                LichLamViec lich = new LichLamViec();
                lich.setNgayLam(rs.getObject("NgayLamViec", LocalDate.class));
                lich.setTenCa(rs.getString("NoiDung"));
                lich.setGioBatDau(rs.getObject("GioBatDau", LocalTime.class));
                lich.setGioKetThuc(rs.getObject("GioKetThuc", LocalTime.class));

                list.add(lich);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}