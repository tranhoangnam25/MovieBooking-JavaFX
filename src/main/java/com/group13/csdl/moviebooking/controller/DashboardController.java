package com.group13.csdl.moviebooking.controller;

import com.group13.csdl.moviebooking.dao.PhimDAO;
import com.group13.csdl.moviebooking.model.Phim;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    @FXML
    private FlowPane movieContainer; // Nơi chứa danh sách phim

    @FXML
    private TextField tfTimKiem;

    @FXML
    private Label lblXinChao;

    private final PhimDAO phimDAO = new PhimDAO();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Khi màn hình vừa hiện lên, tải toàn bộ phim từ Database
        List<Phim> listPhim = phimDAO.layDanhSachPhim();
        loadMovies(listPhim);
    }

    // Hàm hiển thị danh sách phim lên giao diện
    private void loadMovies(List<Phim> listPhim) {
        movieContainer.getChildren().clear(); // Xóa danh sách cũ

        if (listPhim.isEmpty()) {
            Label lblEmpty = new Label("Không tìm thấy phim nào trong Database!");
            lblEmpty.setFont(Font.font(16));
            lblEmpty.setStyle("-fx-text-fill: #2c3e50;");
            movieContainer.getChildren().add(lblEmpty);
            return;
        }

        for (Phim phim : listPhim) {
            VBox card = createMovieCard(phim);
            movieContainer.getChildren().add(card);
        }
    }

    // Hàm tạo giao diện cho 1 bộ phim (gồm Tên, Thời lượng, Nút đặt vé)
    private VBox createMovieCard(Phim phim) {
        VBox card = new VBox();
        card.setPrefSize(200, 280);
        // Nền trắng, bo góc
        card.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 0);");
        card.setAlignment(Pos.TOP_CENTER);
        card.setPadding(new Insets(15));
        card.setSpacing(10);

        // Tên phim
        Label lblTen = new Label(phim.getTen());
        lblTen.setFont(Font.font("System", FontWeight.BOLD, 16));
        lblTen.setWrapText(true);
        lblTen.setAlignment(Pos.CENTER);
        lblTen.setPrefHeight(50);
        lblTen.setStyle("-fx-text-fill: #2c3e50;");

        // Thời lượng
        Label lblThoiLuong = new Label(phim.getThoiLuong() + " phút");
        lblThoiLuong.setStyle("-fx-text-fill: #7f8c8d;");

        // Ngôn ngữ
        Label lblNgonNgu = new Label(phim.getNgonNguChinh());
        lblNgonNgu.setStyle("-fx-background-color: #ecf0f1; -fx-text-fill: #2c3e50; -fx-padding: 3 8; -fx-background-radius: 10;");

        // Nút Đặt vé
        Button btnDatVe = new Button("ĐẶT VÉ");
        btnDatVe.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand;");
        btnDatVe.setPrefWidth(150);

        // Xử lý khi bấm Đặt vé
        btnDatVe.setOnAction(e -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/group13/csdl/moviebooking/fxml/Booking.fxml"));
                Parent root = loader.load();

                BookingController bookingController = loader.getController();
                bookingController.setPhim(phim);

                Stage stage = (Stage) btnDatVe.getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        card.getChildren().addAll(lblTen, lblThoiLuong, lblNgonNgu, btnDatVe);
        return card;
    }

    @FXML
    public void handleSearch(ActionEvent event) {
        String tuKhoa = tfTimKiem.getText();
        List<Phim> ketQua = phimDAO.timKiemPhim(tuKhoa);
        loadMovies(ketQua);
    }

    @FXML
    public void handleHistory(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/group13/csdl/moviebooking/fxml/History.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleLogout(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/group13/csdl/moviebooking/fxml/Login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}