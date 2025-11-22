package com.group13.csdl.moviebooking.dao;

import com.group13.csdl.moviebooking.model.Ghe;
import com.group13.csdl.moviebooking.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GheDAO {

    // 1. Lấy danh sách toàn bộ ghế trong phòng (để vẽ sơ đồ)
    // Sắp xếp theo Hàng và Cột để hiển thị cho đẹp
    public List<Ghe> layGheTheoPhong(int idPhongChieu) {
        List<Ghe> listGhe = new ArrayList<>();
        String sql = "SELECT * FROM Ghe WHERE idPhongChieu = ? ORDER BY Hang, Cot";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idPhongChieu);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Ghe ghe = new Ghe();
                ghe.setIdGhe(rs.getInt("idGhe"));
                ghe.setTrangThai(rs.getString("TrangThai")); // TOT hoặc BAOTRI
                ghe.setIdPhongChieu(rs.getInt("idPhongChieu"));
                ghe.setHang(rs.getString("Hang"));
                ghe.setCot(rs.getInt("Cot"));

                listGhe.add(ghe);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listGhe;
    }

    // 2. Lấy danh sách ID các ghế đã được đặt vé trong suất chiếu này
    // Để chúng ta biết và tô màu đỏ (disable) trên giao diện
    public List<Integer> layGheDaDat(int idSuatChieu) {
        List<Integer> daDat = new ArrayList<>();
        // JOIN bảng VeXemPhim để tìm những ghế đã bán trong suất chiếu này
        String sql = "SELECT idGhe FROM VeXemPhim WHERE idSuatChieu = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idSuatChieu);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                daDat.add(rs.getInt("idGhe"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return daDat;
    }
}