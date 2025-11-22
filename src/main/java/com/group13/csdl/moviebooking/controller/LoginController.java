package com.group13.csdl.moviebooking.controller;

import com.group13.csdl.moviebooking.dao.NguoiDungDAO;
import com.group13.csdl.moviebooking.dao.VeDAO; // <--- Import VeDAO
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

public class LoginController {

    @FXML private TextField tfTaiKhoan;
    @FXML private PasswordField pfMatKhau;
    @FXML private Label lblMessage;

    private final NguoiDungDAO nguoiDungDAO = new NguoiDungDAO();
    private final VeDAO veDAO = new VeDAO(); // <--- Khởi tạo VeDAO

    @FXML
    public void handleLogin(ActionEvent event) {
        String taiKhoan = tfTaiKhoan.getText();
        String matKhau = pfMatKhau.getText();


//        String taiKhoan = "khach1";
//        String matKhau = "User@123";

//        String taiKhoan = "admin";
//        String matKhau = "Admin@123";


//        String taiKhoan = "staff1";
//        String matKhau = "Staff@123";

        if (taiKhoan.isEmpty() || matKhau.isEmpty()) {
            lblMessage.setText("Vui lòng nhập đầy đủ thông tin!");
            lblMessage.setVisible(true);
            return;
        }

        NguoiDung user = nguoiDungDAO.dangNhap(taiKhoan, matKhau);

        if (user != null) {
            // --- BƯỚC MỚI: QUÉT VÉ HẾT HẠN NGAY KHI ĐĂNG NHẬP ---
            veDAO.capNhatVeHetHan();
            // ----------------------------------------------------

            lblMessage.setText("Đăng nhập thành công!");
            lblMessage.setStyle("-fx-text-fill: green;");
            lblMessage.setVisible(true);

            System.out.println("User: " + user.getHoTen() + " - Role: " + user.getVaiTro());

            try {
                String fxmlPath = "";
                String role = user.getVaiTro();

                if ("QUANLY".equalsIgnoreCase(role)) {
                    fxmlPath = "/com/group13/csdl/moviebooking/fxml/AdminDashboard.fxml";
                } else if ("NHANVIEN".equalsIgnoreCase(role)) {
                    fxmlPath = "/com/group13/csdl/moviebooking/fxml/StaffDashboard.fxml";
                } else {
                    fxmlPath = "/com/group13/csdl/moviebooking/fxml/Dashboard.fxml";
                }

                FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
                Parent root = loader.load();

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);

                // Nếu bạn đang dùng giao diện cũ (Light mode) thì bỏ dòng này đi
                // Nếu dùng giao diện mới (Dark mode) thì giữ lại
                // scene.getStylesheets().add(getClass().getResource("/com/group13/csdl/moviebooking/css/style.css").toExternalForm());

                stage.setScene(scene);
                stage.centerOnScreen();
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
                lblMessage.setText("Lỗi tải màn hình: " + e.getMessage());
                lblMessage.setStyle("-fx-text-fill: red;");
            }

        } else {
            lblMessage.setText("Sai tài khoản hoặc mật khẩu!");
            lblMessage.setStyle("-fx-text-fill: red;");
            lblMessage.setVisible(true);
        }
    }

    @FXML
    public void switchToRegister(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/group13/csdl/moviebooking/fxml/Register.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}