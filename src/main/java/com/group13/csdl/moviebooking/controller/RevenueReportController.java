package com.group13.csdl.moviebooking.controller;

import com.group13.csdl.moviebooking.dao.ThongKeDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

public class RevenueReportController implements Initializable {

    @FXML private Label lblTongDoanhThu;
    @FXML private BarChart<String, Number> chartDoanhThu;

    private final ThongKeDAO thongKeDAO = new ThongKeDAO();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadData();
    }

    private void loadData() {
        // 1. Hiển thị Tổng doanh thu
        long totalRevenue = thongKeDAO.layTongDoanhThu();
        NumberFormat vnCurrency = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        lblTongDoanhThu.setText(vnCurrency.format(totalRevenue));

        // 2. Vẽ biểu đồ doanh thu theo phim
        Map<String, Long> revenueByMovie = thongKeDAO.layDoanhThuTheoPhim();

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Doanh thu (VNĐ)");

        for (Map.Entry<String, Long> entry : revenueByMovie.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        chartDoanhThu.getData().clear();
        chartDoanhThu.getData().add(series);
    }

    @FXML
    public void handleBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/group13/csdl/moviebooking/fxml/AdminDashboard.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}