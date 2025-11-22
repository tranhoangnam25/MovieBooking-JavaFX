package com.group13.csdl.moviebooking.controller;

import com.group13.csdl.moviebooking.dao.LichLamViecDAO;
import com.group13.csdl.moviebooking.dao.NguoiDungDAO;
import com.group13.csdl.moviebooking.model.LichLamViec;
import com.group13.csdl.moviebooking.model.NguoiDung;
import javafx.beans.property.SimpleStringProperty;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class StaffProfileController implements Initializable {

    @FXML private Label lblHoTen;
    @FXML private Label lblChucVu;
    @FXML private Label lblNgayBatDau;
    @FXML private TextField tfSDT;
    @FXML private TextField tfEmail;
    @FXML private Label lblMessage;

    @FXML private TableView<LichLamViec> tableLich;
    @FXML private TableColumn<LichLamViec, String> colNgay;
    @FXML private TableColumn<LichLamViec, String> colCa;
    @FXML private TableColumn<LichLamViec, String> colGio;

    private final NguoiDungDAO nguoiDungDAO = new NguoiDungDAO();
    private final LichLamViecDAO lichLamViecDAO = new LichLamViecDAO();

    private NguoiDung currentUser;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Cấu hình cột bảng Lịch làm việc
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        colNgay.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getNgayLam().format(dateFormatter)));

        colCa.setCellValueFactory(new PropertyValueFactory<>("tenCa"));
        colGio.setCellValueFactory(new PropertyValueFactory<>("thoiGianCa"));

        // Tải dữ liệu
        loadUserData();
    }

    private void loadUserData() {
        // --- GIẢ LẬP NGƯỜI DÙNG ĐANG ĐĂNG NHẬP ---
        // Trong thực tế, ta sẽ lấy từ Session hoặc truyền từ LoginController sang.
        // Ở đây để test nhanh, ta lấy lại thông tin của "staff1"
        currentUser = nguoiDungDAO.dangNhap("staff1", "Staff@123");

        if (currentUser != null) {
            // 1. Điền thông tin cá nhân
            lblHoTen.setText(currentUser.getHoTen());
            lblChucVu.setText(currentUser.getVaiTro());
            if (currentUser.getNgayBatDau() != null) {
                lblNgayBatDau.setText(currentUser.getNgayBatDau().toString());
            }
            tfSDT.setText(currentUser.getSdt());
            tfEmail.setText(currentUser.getEmail());

            // 2. Tải lịch làm việc
            loadSchedule(currentUser.getIdNguoiDung());
        }
    }

    private void loadSchedule(String staffId) {
        List<LichLamViec> list = lichLamViecDAO.layLichLamViec(staffId);
        ObservableList<LichLamViec> data = FXCollections.observableArrayList(list);
        tableLich.setItems(data);
    }

    @FXML
    public void handleUpdate(ActionEvent event) {
        if (currentUser == null) return;

        String sdtMoi = tfSDT.getText();
        String emailMoi = tfEmail.getText();

        if (sdtMoi.isEmpty() || emailMoi.isEmpty()) {
            lblMessage.setText("Vui lòng không để trống thông tin.");
            lblMessage.setStyle("-fx-text-fill: red;");
            return;
        }

        // Cập nhật object tạm
        currentUser.setSdt(sdtMoi);
        currentUser.setEmail(emailMoi);

        // Gọi DAO lưu xuống DB
        if (nguoiDungDAO.capNhatThongTin(currentUser)) {
            lblMessage.setText("Cập nhật thành công!");
            lblMessage.setStyle("-fx-text-fill: green;");
        } else {
            lblMessage.setText("Cập nhật thất bại. Có lỗi xảy ra.");
            lblMessage.setStyle("-fx-text-fill: red;");
        }
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