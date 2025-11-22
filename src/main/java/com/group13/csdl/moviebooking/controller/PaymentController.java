package com.group13.csdl.moviebooking.controller;

import com.group13.csdl.moviebooking.dao.HoaDonDAO;
import com.group13.csdl.moviebooking.model.Phim;
import com.group13.csdl.moviebooking.model.SuatChieu;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class PaymentController {

    @FXML private Label lblTenPhim;
    @FXML private Label lblSuatChieu;
    @FXML private Label lblPhong;
    @FXML private Label lblGhe;
    @FXML private Label lblTongTien;
    @FXML private Label lblMessage;

    private Phim phim;
    private SuatChieu suatChieu;
    private Set<Integer> seatIds;
    private long tongTien;

    private final HoaDonDAO hoaDonDAO = new HoaDonDAO();

    // Nhận dữ liệu từ màn hình Chọn ghế
    public void setDuLieu(Phim phim, SuatChieu suatChieu, Set<Integer> seatIds, List<String> seatNames, long tongTien) {
        this.phim = phim;
        this.suatChieu = suatChieu;
        this.seatIds = seatIds;
        this.tongTien = tongTien;

        // Hiển thị thông tin lên hóa đơn
        lblTenPhim.setText(phim.getTen());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm - dd/MM/yyyy");
        lblSuatChieu.setText(suatChieu.getThoiGianBatDau().format(formatter));

        lblPhong.setText("Phòng " + suatChieu.getIdPhongChieu());
        lblGhe.setText(String.join(", ", seatNames));

        NumberFormat vnCurrency = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        lblTongTien.setText(vnCurrency.format(tongTien));
    }

    @FXML
    public void handlePay(ActionEvent event) {
        // Giả lập ID khách hàng đang mua vé (Lấy từ dữ liệu mẫu KH01)
        String idKhachHang = "KH01";

        // Gọi DAO để lưu Hóa đơn và Vé vào DB
        if (hoaDonDAO.thanhToan(idKhachHang, suatChieu.getIdSuatChieu(), seatIds)) {
            // Thông báo thành công
            showAlert(Alert.AlertType.INFORMATION, "Thành công", "Bạn đã đặt vé thành công! Cảm ơn bạn.");

            // Quay về trang chủ
            goHome(event);
        } else {
            lblMessage.setText("Thanh toán thất bại! Có lỗi hệ thống.");
            lblMessage.setVisible(true);
        }
    }

    @FXML
    public void handleBack(ActionEvent event) {
        try {
            // Quay lại màn hình Chọn ghế
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/group13/csdl/moviebooking/fxml/SeatSelection.fxml"));
            Parent root = loader.load();

            SeatSelectionController controller = loader.getController();
            controller.setDuLieu(phim, suatChieu); // Truyền lại dữ liệu để khách chọn lại

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void goHome(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/group13/csdl/moviebooking/fxml/Dashboard.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}