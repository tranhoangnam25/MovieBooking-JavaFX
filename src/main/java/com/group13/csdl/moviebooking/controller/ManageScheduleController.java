package com.group13.csdl.moviebooking.controller;

import com.group13.csdl.moviebooking.dao.PhimDAO;
import com.group13.csdl.moviebooking.dao.SuatChieuDAO;
import com.group13.csdl.moviebooking.model.Phim;
import com.group13.csdl.moviebooking.model.SuatChieu;
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
import javafx.util.StringConverter;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.ResourceBundle;

public class ManageScheduleController implements Initializable {

    @FXML private ComboBox<Phim> cbPhim;
    @FXML private ComboBox<Integer> cbPhong;
    @FXML private TextField tfGiaVe;
    @FXML private DatePicker dpNgayChieu;
    @FXML private TextField tfGioChieu;
    @FXML private Label lblMessage;

    @FXML private TableView<SuatChieu> tableSuatChieu;
    @FXML private TableColumn<SuatChieu, Integer> colID;
    @FXML private TableColumn<SuatChieu, String> colPhim;
    @FXML private TableColumn<SuatChieu, Integer> colPhong;
    @FXML private TableColumn<SuatChieu, String> colThoiGian;
    @FXML private TableColumn<SuatChieu, Integer> colGiaVe;

    @FXML private Button btnXoa;

    private final SuatChieuDAO suatChieuDAO = new SuatChieuDAO();
    private final PhimDAO phimDAO = new PhimDAO();
    private SuatChieu selectedSuatChieu = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colID.setCellValueFactory(new PropertyValueFactory<>("idSuatChieu"));
        colPhong.setCellValueFactory(new PropertyValueFactory<>("idPhongChieu"));
        colGiaVe.setCellValueFactory(new PropertyValueFactory<>("giaVe"));

        colPhim.setCellValueFactory(cellData -> new SimpleStringProperty("Phim ID: " + cellData.getValue().getIdPhim()));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");
        colThoiGian.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getThoiGianBatDau().format(formatter)));

        loadComboBoxData();
        loadTableData();

        tableSuatChieu.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                selectedSuatChieu = newVal;
                btnXoa.setDisable(false);
            }
        });
    }

    private void loadComboBoxData() {
        List<Phim> listPhim = phimDAO.layDanhSachPhim();
        cbPhim.setItems(FXCollections.observableArrayList(listPhim));

        cbPhim.setConverter(new StringConverter<Phim>() {
            @Override
            public String toString(Phim object) {
                return object == null ? "" : object.getTen();
            }
            @Override
            public Phim fromString(String string) {
                return cbPhim.getItems().stream().filter(p -> p.getTen().equals(string)).findFirst().orElse(null);
            }
        });

        ObservableList<Integer> listPhong = FXCollections.observableArrayList();
        for (int i = 1; i <= 30; i++) listPhong.add(i); // Hỗ trợ tới 30 phòng theo dữ liệu mẫu mới
        cbPhong.setItems(listPhong);
    }

    private void loadTableData() {
        List<SuatChieu> list = suatChieuDAO.layTatCaSuatChieu();
        tableSuatChieu.setItems(FXCollections.observableArrayList(list));
    }

    @FXML
    public void handleThem(ActionEvent event) {
        if (cbPhim.getValue() == null || cbPhong.getValue() == null ||
                tfGiaVe.getText().isEmpty() || dpNgayChieu.getValue() == null || tfGioChieu.getText().isEmpty()) {
            showMessage("Vui lòng nhập đủ thông tin!", "red");
            return;
        }

        try {
            int idPhim = cbPhim.getValue().getIdPhim();
            int idPhong = cbPhong.getValue();
            int giaVe = Integer.parseInt(tfGiaVe.getText());
            LocalDate date = dpNgayChieu.getValue();

            LocalTime time = LocalTime.parse(tfGioChieu.getText(), DateTimeFormatter.ofPattern("HH:mm"));
            LocalDateTime startDateTime = LocalDateTime.of(date, time);

            SuatChieu sc = new SuatChieu();
            sc.setIdPhim(idPhim);
            sc.setIdPhongChieu(idPhong);
            sc.setGiaVe(giaVe);
            sc.setThoiGianBatDau(startDateTime);

            if (suatChieuDAO.themSuatChieu(sc)) {
                showMessage("Thêm suất chiếu thành công!", "green");
                handleLamMoi(null);
                loadTableData();
            } else {
                showMessage("Thêm thất bại! Có thể trùng lịch chiếu tại phòng này.", "red");
            }

        } catch (NumberFormatException e) {
            showMessage("Giá vé phải là số nguyên!", "red");
        } catch (DateTimeParseException e) {
            showMessage("Giờ chiếu không hợp lệ (Định dạng HH:mm, ví dụ 19:30)", "red");
        }
    }

    @FXML
    public void handleXoa(ActionEvent event) {
        if (selectedSuatChieu == null) return;

        if (suatChieuDAO.xoaSuatChieu(selectedSuatChieu.getIdSuatChieu())) {
            showMessage("Đã xóa suất chiếu.", "green");
            handleLamMoi(null);
            loadTableData();
        } else {
            showMessage("Không thể xóa! Suất chiếu này đã có vé bán ra.", "red");
        }
    }

    @FXML
    public void handleLamMoi(ActionEvent event) {
        cbPhim.setValue(null);
        cbPhong.setValue(null);
        tfGiaVe.clear();
        dpNgayChieu.setValue(null);
        tfGioChieu.clear();
        lblMessage.setText("");
        selectedSuatChieu = null;
        btnXoa.setDisable(true);
        tableSuatChieu.getSelectionModel().clearSelection();
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

    private void showMessage(String msg, String color) {
        lblMessage.setText(msg);
        if ("red".equals(color)) lblMessage.setStyle("-fx-text-fill: #e74c3c;");
        else lblMessage.setStyle("-fx-text-fill: #27ae60;");
    }
}