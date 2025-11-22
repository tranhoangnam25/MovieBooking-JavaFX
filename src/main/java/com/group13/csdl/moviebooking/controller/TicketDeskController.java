package com.group13.csdl.moviebooking.controller;

import com.group13.csdl.moviebooking.dao.LichSuDAO;
import com.group13.csdl.moviebooking.dao.NguoiDungDAO;
import com.group13.csdl.moviebooking.dao.VeDAO; // Import mới
import com.group13.csdl.moviebooking.model.NguoiDung;
import com.group13.csdl.moviebooking.model.VeXemPhim;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class TicketDeskController implements Initializable {

    // UI Components
    @FXML private TextField tfTimKiem;
    @FXML private Label lblMessage;
    @FXML private Label lblHoTen;
    @FXML private Label lblSDT;
    @FXML private Label lblEmail;
    @FXML private Button btnInVe;

    // Table View
    @FXML private TableView<VeXemPhim> tableVe;
    @FXML private TableColumn<VeXemPhim, Integer> colMaVe;
    @FXML private TableColumn<VeXemPhim, String> colTenPhim;
    @FXML private TableColumn<VeXemPhim, String> colThoiGian;
    @FXML private TableColumn<VeXemPhim, String> colGhe;
    @FXML private TableColumn<VeXemPhim, String> colTrangThai;

    private final NguoiDungDAO nguoiDungDAO = new NguoiDungDAO();
    private final LichSuDAO lichSuDAO = new LichSuDAO();
    private final VeDAO veDAO = new VeDAO(); // Khởi tạo DAO xử lý vé

    private NguoiDung khachHangHienTai = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Cấu hình cột cho bảng
        colMaVe.setCellValueFactory(new PropertyValueFactory<>("idVe"));
        colTenPhim.setCellValueFactory(new PropertyValueFactory<>("tenPhim"));
        colThoiGian.setCellValueFactory(new PropertyValueFactory<>("thoiGianChieu"));
        colGhe.setCellValueFactory(new PropertyValueFactory<>("tenGhe"));
        colTrangThai.setCellValueFactory(new PropertyValueFactory<>("trangThai"));

        // Sự kiện khi chọn dòng trong bảng
        tableVe.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                btnInVe.setDisable(false);
            } else {
                btnInVe.setDisable(true);
            }
        });
    }

    @FXML
    public void handleSearch(ActionEvent event) {
        String tuKhoa = tfTimKiem.getText();
        if (tuKhoa.isEmpty()) {
            lblMessage.setText("Vui lòng nhập thông tin!");
            return;
        }

        // 1. Tìm khách hàng
        List<NguoiDung> results = nguoiDungDAO.timKiemKhachHang(tuKhoa);

        if (results.isEmpty()) {
            lblMessage.setText("Không tìm thấy khách hàng nào.");
            lblMessage.setStyle("-fx-text-fill: #e74c3c;");
            resetInfo();
        } else {
            // Lấy người đầu tiên tìm thấy
            khachHangHienTai = results.get(0);

            // 2. Hiển thị thông tin khách
            lblHoTen.setText(khachHangHienTai.getHoTen());
            lblSDT.setText(khachHangHienTai.getSdt());
            lblEmail.setText(khachHangHienTai.getEmail());
            lblMessage.setText("Đã tìm thấy khách hàng.");
            lblMessage.setStyle("-fx-text-fill: #27ae60;");

            // 3. Tải lịch sử vé của khách
            loadTicketHistory(khachHangHienTai.getIdNguoiDung());
        }
    }

    private void loadTicketHistory(String idKhachHang) {
        List<VeXemPhim> listVe = lichSuDAO.layLichSuDatVe(idKhachHang);
        ObservableList<VeXemPhim> data = FXCollections.observableArrayList(listVe);
        tableVe.setItems(data);
    }

    @FXML
    public void handlePrint(ActionEvent event) {
        VeXemPhim veChon = tableVe.getSelectionModel().getSelectedItem();
        if (veChon != null) {
            // Kiểm tra nếu vé đã dùng rồi thì cảnh báo (tùy chọn)
            if ("DASUDUNG".equals(veChon.getTrangThai())) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Cảnh báo");
                alert.setHeaderText("Vé này đã được in/sử dụng rồi!");
                alert.showAndWait();
                return;
            }

            // 1. Cập nhật trạng thái vé trong Database
            if (veDAO.capNhatTrangThai(veChon.getIdVe(), "DASUDUNG")) {

                // 2. Thông báo in thành công
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Hệ thống in vé");
                alert.setHeaderText("In vé thành công!");
                alert.setContentText("Mã vé: " + veChon.getIdVe() + "\nPhim: " + veChon.getTenPhim() + "\nGhế: " + veChon.getTenGhe() + "\n\nTrạng thái vé đã chuyển sang 'ĐÃ SỬ DỤNG'.");
                alert.showAndWait();

                // 3. Tải lại bảng để cập nhật trạng thái mới ngay lập tức
                loadTicketHistory(khachHangHienTai.getIdNguoiDung());

            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Lỗi");
                alert.setHeaderText("Không thể cập nhật trạng thái vé!");
                alert.showAndWait();
            }
        }
    }

    private void resetInfo() {
        lblHoTen.setText("---");
        lblSDT.setText("---");
        lblEmail.setText("---");
        khachHangHienTai = null;
        tableVe.getItems().clear();
        btnInVe.setDisable(true);
    }

    @FXML
    public void handleBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/group13/csdl/moviebooking/fxml/StaffDashboard.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}