package com.group13.csdl.moviebooking.dao;

import com.group13.csdl.moviebooking.model.VeXemPhim;
import com.group13.csdl.moviebooking.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class LichSuDAO {

    /**
     * Lấy danh sách vé đã đặt của một khách hàng cụ thể
     * Kết nối bảng: Ve -> HoaDon -> SuatChieu -> Phim, và Ve -> Ghe
     */
    public List<VeXemPhim> layLichSuDatVe(String idNguoiDung) {
        List<VeXemPhim> listVe = new ArrayList<>();

        // SỬA LỖI Ở ĐÂY: Đổi 'v.idVe' thành 'v.idVeXemPhim' cho khớp với Database
        String sql = "SELECT v.idVeXemPhim, v.TrangThai, p.Ten AS TenPhim, " +
                "s.ThoiGianBatDau, g.Hang, g.Cot " +
                "FROM VeXemPhim v " +
                "JOIN HoaDon h ON v.idHoaDon = h.idHoaDon " +
                "JOIN SuatChieu s ON v.idSuatChieu = s.idSuatChieu " +
                "JOIN Phim p ON s.idPhim = p.idPhim " +
                "JOIN Ghe g ON v.idGhe = g.idGhe " +
                "WHERE h.idNguoiDung = ? " +
                "ORDER BY h.NgayThanhToan DESC"; // Mới nhất lên đầu

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, idNguoiDung);
            ResultSet rs = stmt.executeQuery();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");

            while (rs.next()) {
                VeXemPhim ve = new VeXemPhim();

                // SỬA LỖI Ở ĐÂY: Lấy cột 'idVeXemPhim' từ kết quả SQL
                ve.setIdVe(rs.getInt("idVeXemPhim"));

                ve.setTrangThai(rs.getString("TrangThai"));

                // Gán các thông tin hiển thị bổ sung
                ve.setTenPhim(rs.getString("TenPhim"));

                // Ghép Hàng và Cột thành tên ghế (ví dụ: A5)
                String tenGhe = rs.getString("Hang") + rs.getInt("Cot");
                ve.setTenGhe(tenGhe);

                // Format thời gian chiếu
                LocalDateTime time = rs.getObject("ThoiGianBatDau", LocalDateTime.class);
                ve.setThoiGianChieu(time.format(formatter));

                listVe.add(ve);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listVe;
    }
}