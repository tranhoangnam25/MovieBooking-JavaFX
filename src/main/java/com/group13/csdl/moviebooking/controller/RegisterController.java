package com.group13.csdl.moviebooking.controller;

import com.group13.csdl.moviebooking.dao.NguoiDungDAO;
import com.group13.csdl.moviebooking.model.NguoiDung;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;

public class RegisterController {

    @FXML private TextField tfHoTen;
    @FXML private DatePicker dpNgaySinh;
    @FXML private TextField tfSdt;
    @FXML private TextField tfEmail;
    @FXML private TextField tfTaiKhoan;
    @FXML private PasswordField pfMatKhau;
    @FXML private Label lblMessage;

    private final NguoiDungDAO nguoiDungDAO = new NguoiDungDAO();

    @FXML
    public void handleRegister(ActionEvent event) {
        // 1. Lấy dữ liệu từ form
        String hoTen = tfHoTen.getText();
        LocalDate ngaySinh = dpNgaySinh.getValue();
        String sdt = tfSdt.getText();
        String email = tfEmail.getText();
        String taiKhoan = tfTaiKhoan.getText();
        String matKhau = pfMatKhau.getText();

        // 2. Kiểm tra dữ liệu nhập vào (Validate)
        if (hoTen.isEmpty() || sdt.isEmpty() || email.isEmpty() || taiKhoan.isEmpty() || matKhau.isEmpty() || ngaySinh == null) {
            lblMessage.setText("Vui lòng điền đầy đủ tất cả thông tin!");
            lblMessage.setStyle("-fx-text-fill: red;");
            return;
        }

        // Kiểm tra ràng buộc mật khẩu (tối thiểu 8 ký tự, 1 hoa, 1 ký tự đặc biệt, không dấu cách - source 98)
        if (!matKhau.matches("^(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$")) {
            lblMessage.setText("Mật khẩu phải > 8 ký tự, có chữ hoa và ký tự đặc biệt, không dấu cách.");
            lblMessage.setStyle("-fx-text-fill: red;");
            return;
        }

        // 3. Tạo đối tượng NguoiDung
        NguoiDung newUser = new NguoiDung();
        newUser.setHoTen(hoTen);
        newUser.setNgaySinh(ngaySinh);
        newUser.setSdt(sdt);
        newUser.setEmail(email);
        newUser.setTaiKhoan(taiKhoan);
        newUser.setMatKhau(matKhau);
        // Các trường khác sẽ được set mặc định trong DAO (Vai trò, ID, v.v.)

        // 4. Gọi DAO để lưu xuống DB
        if (nguoiDungDAO.dangKy(newUser)) {
            lblMessage.setText("Đăng ký thành công! Hãy đăng nhập.");
            lblMessage.setStyle("-fx-text-fill: green;");

            // Xóa trắng form để tránh bấm nhầm
            clearForm();
        } else {
            lblMessage.setText("Đăng ký thất bại. Tài khoản/Email/SĐT có thể đã tồn tại.");
            lblMessage.setStyle("-fx-text-fill: red;");
        }
    }

    @FXML
    public void switchToLogin(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/group13/csdl/moviebooking/fxml/Login.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void clearForm() {
        tfHoTen.clear();
        tfSdt.clear();
        tfEmail.clear();
        tfTaiKhoan.clear();
        pfMatKhau.clear();
        dpNgaySinh.setValue(null);
    }
}