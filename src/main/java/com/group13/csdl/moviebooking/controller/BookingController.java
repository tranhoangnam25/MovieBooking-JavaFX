package com.group13.csdl.moviebooking.controller;

import com.group13.csdl.moviebooking.dao.SuatChieuDAO;
import com.group13.csdl.moviebooking.model.Phim;
import com.group13.csdl.moviebooking.model.SuatChieu;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class BookingController {

    @FXML private Label lblTenPhim;
    @FXML private Label lblThoiLuong;
    @FXML private Label lblNgonNgu;
    @FXML private Text txtNoiDung;
    @FXML private FlowPane fpSuatChieu;

    private final SuatChieuDAO suatChieuDAO = new SuatChieuDAO();
    private Phim phimHienTai;

    // Hàm này quan trọng: Được gọi từ Dashboard để nhận dữ liệu Phim
    public void setPhim(Phim phim) {
        this.phimHienTai = phim;

        // 1. Hiển thị thông tin chi tiết phim lên giao diện
        lblTenPhim.setText(phim.getTen());
        lblThoiLuong.setText(phim.getThoiLuong() + " phút");
        lblNgonNgu.setText(phim.getNgonNguChinh());
        txtNoiDung.setText(phim.getNoiDung());

        // 2. Tải các suất chiếu của phim này
        loadSuatChieu();
    }

    private void loadSuatChieu() {
        fpSuatChieu.getChildren().clear(); // Xóa dữ liệu cũ

        // Gọi DAO lấy danh sách suất chiếu theo ID phim
        List<SuatChieu> listSuatChieu = suatChieuDAO.laySuatChieuTheoPhim(phimHienTai.getIdPhim());

        if (listSuatChieu.isEmpty()) {
            Label lblTrong = new Label("Hiện chưa có lịch chiếu cho phim này.");
            lblTrong.setStyle("-fx-text-fill: #7f8c8d; -fx-font-size: 14px;");
            fpSuatChieu.getChildren().add(lblTrong);
            return;
        }

        // Định dạng ngày giờ cho đẹp (VD: 19:00 - 20/11)
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM");

        for (SuatChieu sc : listSuatChieu) {
            // Tạo nút bấm cho từng suất chiếu
            Button btn = new Button();

            // Nội dung nút: Giờ + Ngày + Phòng
            String text = sc.getThoiGianBatDau().format(timeFormatter) + "\n" +
                    sc.getThoiGianBatDau().format(dateFormatter) + "\n" +
                    "Phòng " + sc.getIdPhongChieu();

            btn.setText(text);
            btn.setPrefSize(100, 80);
            // Style nút: Trắng, viền xám, khi di chuột vào đổi hình bàn tay
            btn.setStyle("-fx-background-color: white; -fx-border-color: #bdc3c7; -fx-border-radius: 5; -fx-background-radius: 5; -fx-cursor: hand; -fx-text-alignment: center;");

            // Xử lý khi bấm vào suất chiếu
            btn.setOnAction(e -> handleChonSuatChieu(sc));

            fpSuatChieu.getChildren().add(btn);
        }
    }

    private void handleChonSuatChieu(SuatChieu sc) {
        System.out.println("Khách chọn suất chiếu: " + sc.getIdSuatChieu());

        try {
            // Tải màn hình chọn ghế
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/group13/csdl/moviebooking/fxml/SeatSelection.fxml"));
            Parent root = loader.load();

            // Lấy Controller màn hình chọn ghế để truyền dữ liệu sang
            SeatSelectionController seatController = loader.getController();
            seatController.setDuLieu(phimHienTai, sc); // <--- Truyền Phim và Suất chiếu

            // Chuyển cảnh
            Stage stage = (Stage) fpSuatChieu.getScene().getWindow();
            stage.setScene(new Scene(root));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleBack(ActionEvent event) {
        try {
            // Quay lại màn hình Dashboard
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/group13/csdl/moviebooking/fxml/Dashboard.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}