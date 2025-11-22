package com.group13.csdl.moviebooking.dao;

import com.group13.csdl.moviebooking.util.DatabaseConnection;

import java.sql.*;
import java.util.Set;

public class HoaDonDAO {

    /**
     * Hàm xử lý giao dịch thanh toán trọn gói
     * @param idKhachHang: ID người dùng đang đăng nhập
     * @param idSuatChieu: Suất chiếu khách chọn
     * @param listIdGhe: Danh sách các ID ghế khách chọn
     * @return true nếu thành công, false nếu lỗi
     */
    public boolean thanhToan(String idKhachHang, int idSuatChieu, Set<Integer> listIdGhe) {
        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmtHoaDon = null;
        PreparedStatement stmtVe = null;
        ResultSet rs = null;

        try {
            // 1. Tắt chế độ tự động lưu (Auto Commit) để bắt đầu Transaction
            conn.setAutoCommit(false);

            // ---------------------------------------------------------
            // BƯỚC 1: TẠO HÓA ĐƠN
            // ---------------------------------------------------------
            // TrangThai: DATHANHTOAN (Viết liền, in hoa khớp với ENUM trong DB)
            String sqlHoaDon = "INSERT INTO HoaDon (NgayTao, NgayThanhToan, TrangThai, idPhuongThucThanhToan, idNguoiDung) " +
                    "VALUES (NOW(), NOW(), 'DATHANHTOAN', 1, ?)";

            stmtHoaDon = conn.prepareStatement(sqlHoaDon, Statement.RETURN_GENERATED_KEYS);
            stmtHoaDon.setString(1, idKhachHang);

            int affectedRows = stmtHoaDon.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Tạo hóa đơn thất bại.");
            }

            // Lấy ID Hóa đơn vừa sinh ra
            int idHoaDon = 0;
            rs = stmtHoaDon.getGeneratedKeys();
            if (rs.next()) {
                idHoaDon = rs.getInt(1);
            } else {
                throw new SQLException("Không lấy được ID hóa đơn.");
            }

            // ---------------------------------------------------------
            // BƯỚC 2: TẠO VÉ CHO TỪNG GHẾ
            // ---------------------------------------------------------
            // SỬA LỖI QUAN TRỌNG: Dùng 'CHUASUDUNG' thay vì 'CHUA_SU_DUNG'
            // Để khớp với script SQL mới và cấu hình ENUM của Database
            String sqlVe = "INSERT INTO VeXemPhim (TrangThai, idSuatChieu, idHoaDon, idGhe) VALUES ('CHUASUDUNG', ?, ?, ?)";
            stmtVe = conn.prepareStatement(sqlVe);

            for (Integer idGhe : listIdGhe) {
                stmtVe.setInt(1, idSuatChieu);
                stmtVe.setInt(2, idHoaDon);
                stmtVe.setInt(3, idGhe);
                stmtVe.addBatch(); // Gom lại chạy 1 lần cho nhanh
            }

            stmtVe.executeBatch();

            // ---------------------------------------------------------
            // BƯỚC 3: CHỐT GIAO DỊCH (COMMIT)
            // ---------------------------------------------------------
            conn.commit();
            System.out.println("Giao dịch thành công! Đã tạo Hóa đơn ID: " + idHoaDon);
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            // Nếu có lỗi, hoàn tác lại tất cả (Rollback)
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return false;
        } finally {
            // Đóng kết nối
            try {
                if (rs != null) rs.close();
                if (stmtHoaDon != null) stmtHoaDon.close();
                if (stmtVe != null) stmtVe.close();
                if (conn != null) conn.setAutoCommit(true); // Bật lại auto commit
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}