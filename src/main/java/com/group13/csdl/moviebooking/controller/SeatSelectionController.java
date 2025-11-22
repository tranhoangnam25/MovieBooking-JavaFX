package com.group13.csdl.moviebooking.controller;

import com.group13.csdl.moviebooking.dao.GheDAO;
import com.group13.csdl.moviebooking.model.Ghe;
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
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class SeatSelectionController {

    @FXML private Label lblThongTinSuat;
    @FXML private GridPane gridGhe;
    @FXML private Label lblGheChon;
    @FXML private Label lblTongTien;
    @FXML private Button btnTiepTuc;

    private final GheDAO gheDAO = new GheDAO();

    private SuatChieu suatChieu;
    private Phim phim;
    private long tongTienHienTai = 0;

    // Danh sách chứa các ghế đang được khách chọn (lưu ID ghế)
    private final Set<Integer> selectedSeatIds = new HashSet<>();
    // Danh sách chứa tên các ghế đang chọn (để hiển thị label)
    private final List<String> selectedSeatNames = new ArrayList<>();

    // Hàm nhận dữ liệu từ màn hình trước
    public void setDuLieu(Phim phim, SuatChieu suatChieu) {
        this.phim = phim;
        this.suatChieu = suatChieu;

        // Hiển thị thông tin cơ bản lên Header
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm - dd/MM/yyyy");
        lblThongTinSuat.setText(phim.getTen() + " | " +
                suatChieu.getThoiGianBatDau().format(timeFormatter) +
                " | Phòng " + suatChieu.getIdPhongChieu());

        // Bắt đầu vẽ ghế
        loadGhe();
    }

    private void loadGhe() {
        gridGhe.getChildren().clear();

        // 1. Lấy tất cả ghế trong phòng
        List<Ghe> listGhe = gheDAO.layGheTheoPhong(suatChieu.getIdPhongChieu());

        // 2. Lấy danh sách ghế đã có người đặt (để disable)
        List<Integer> gheDaDat = gheDAO.layGheDaDat(suatChieu.getIdSuatChieu());

        // 3. Vẽ ghế lên GridPane
        for (Ghe ghe : listGhe) {
            Button btnGhe = new Button(ghe.getTenGhe());
            btnGhe.setPrefSize(45, 40); // Kích thước ghế

            // Tính toán vị trí hàng/cột trong GridPane
            // Hàng 'A' -> index 0, 'B' -> index 1 ...
            int rowIndex = ghe.getHang().charAt(0) - 'A';
            int colIndex = ghe.getCot() - 1; // Cột 1 -> index 0

            // Xử lý trạng thái ghế
            if ("BAOTRI".equals(ghe.getTrangThai())) {
                // Ghế bảo trì: Màu xám, không bấm được
                btnGhe.setStyle("-fx-background-color: #95a5a6; -fx-text-fill: white;");
                btnGhe.setDisable(true);
            } else if (gheDaDat.contains(ghe.getIdGhe())) {
                // Ghế đã bán: Màu đỏ, không bấm được
                btnGhe.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");
                btnGhe.setDisable(true);
            } else {
                // Ghế trống: Màu trắng viền xám
                btnGhe.setStyle("-fx-background-color: white; -fx-border-color: #bdc3c7; -fx-cursor: hand;");

                // Sự kiện khi bấm chọn ghế
                btnGhe.setOnAction(e -> toggleGhe(btnGhe, ghe));
            }

            gridGhe.add(btnGhe, colIndex, rowIndex);
        }
    }

    // Xử lý khi bấm vào một ghế trống
    private void toggleGhe(Button btn, Ghe ghe) {
        if (selectedSeatIds.contains(ghe.getIdGhe())) {
            // Nếu đang chọn -> Bỏ chọn (Trở về màu trắng)
            selectedSeatIds.remove(ghe.getIdGhe());
            selectedSeatNames.remove(ghe.getTenGhe());
            btn.setStyle("-fx-background-color: white; -fx-border-color: #bdc3c7; -fx-cursor: hand;");
        } else {
            // Nếu chưa chọn -> Chọn (Chuyển màu xanh lá)
            selectedSeatIds.add(ghe.getIdGhe());
            selectedSeatNames.add(ghe.getTenGhe());
            btn.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-cursor: hand;");
        }

        updateUI();
    }

    private void updateUI() {
        // 1. Cập nhật label danh sách ghế
        if (selectedSeatNames.isEmpty()) {
            lblGheChon.setText("Chưa chọn ghế nào");
            btnTiepTuc.setDisable(true); // Không cho tiếp tục nếu chưa chọn
        } else {
            // Sắp xếp tên ghế cho đẹp (A1, A2, B5...)
            Collections.sort(selectedSeatNames);
            lblGheChon.setText(String.join(", ", selectedSeatNames));
            btnTiepTuc.setDisable(false);
        }

        // 2. Cập nhật tổng tiền
        tongTienHienTai = (long) selectedSeatIds.size() * suatChieu.getGiaVe();
        NumberFormat vnCurrency = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        lblTongTien.setText(vnCurrency.format(tongTienHienTai));
    }

    @FXML
    public void handleConfirm(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/group13/csdl/moviebooking/fxml/Payment.fxml"));
            Parent root = loader.load();

            // Lấy Controller màn hình Thanh toán để truyền dữ liệu sang
            PaymentController paymentController = loader.getController();

            // Truyền toàn bộ thông tin vé
            paymentController.setDuLieu(phim, suatChieu, selectedSeatIds, selectedSeatNames, tongTienHienTai);

            // Chuyển cảnh
            Stage stage = (Stage) btnTiepTuc.getScene().getWindow();
            stage.setScene(new Scene(root));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/group13/csdl/moviebooking/fxml/Booking.fxml"));
            Parent root = loader.load();

            BookingController bookingController = loader.getController();
            bookingController.setPhim(phim); // Truyền lại phim cũ

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}