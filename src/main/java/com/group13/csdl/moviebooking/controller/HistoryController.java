package com.group13.csdl.moviebooking.controller;

import com.group13.csdl.moviebooking.dao.LichSuDAO;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class HistoryController implements Initializable {

    @FXML private TableView<VeXemPhim> tableLichSu;
    @FXML private TableColumn<VeXemPhim, Integer> colMaVe;
    @FXML private TableColumn<VeXemPhim, String> colTenPhim;
    @FXML private TableColumn<VeXemPhim, String> colThoiGian;
    @FXML private TableColumn<VeXemPhim, String> colGhe;
    @FXML private TableColumn<VeXemPhim, String> colTrangThai;

    private final LichSuDAO lichSuDAO = new LichSuDAO();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // 1. Cấu hình các cột (Liên kết với tên thuộc tính trong class VeXemPhim)
        colMaVe.setCellValueFactory(new PropertyValueFactory<>("idVe"));
        colTenPhim.setCellValueFactory(new PropertyValueFactory<>("tenPhim"));
        colThoiGian.setCellValueFactory(new PropertyValueFactory<>("thoiGianChieu"));
        colGhe.setCellValueFactory(new PropertyValueFactory<>("tenGhe"));
        colTrangThai.setCellValueFactory(new PropertyValueFactory<>("trangThai"));

        // 2. Tải dữ liệu
        loadData();
    }

    private void loadData() {
        // Giả lập lấy lịch sử của khách hàng KH01
        String currentUserId = "KH01";

        List<VeXemPhim> listVe = lichSuDAO.layLichSuDatVe(currentUserId);

        // Chuyển List thường thành ObservableList để JavaFX hiểu được
        ObservableList<VeXemPhim> data = FXCollections.observableArrayList(listVe);
        tableLichSu.setItems(data);
    }

    @FXML
    public void handleBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/group13/csdl/moviebooking/fxml/Dashboard.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}