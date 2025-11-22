package com.group13.csdl.moviebooking.controller;

import com.group13.csdl.moviebooking.dao.PhimDAO;
import com.group13.csdl.moviebooking.model.Phim;
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
import java.util.List;
import java.util.ResourceBundle;

public class ManageMoviesController implements Initializable {

    // Các thành phần giao diện
    @FXML private TextField tfTenPhim;
    @FXML private DatePicker dpNgayPhatHanh;
    @FXML private TextField tfThoiLuong;
    @FXML private TextField tfNgonNgu;
    @FXML private TextArea taNoiDung;
    @FXML private Label lblMessage;

    @FXML private Button btnThem;
    @FXML private Button btnSua;
    @FXML private Button btnXoa;

    // Bảng và Cột
    @FXML private TableView<Phim> tablePhim;
    @FXML private TableColumn<Phim, Integer> colID;
    @FXML private TableColumn<Phim, String> colTen;
    @FXML private TableColumn<Phim, LocalDate> colNgay;
    @FXML private TableColumn<Phim, Float> colThoiLuong;
    @FXML private TableColumn<Phim, String> colNgonNgu;

    private final PhimDAO phimDAO = new PhimDAO();

    // Biến lưu phim đang được chọn (để sửa/xóa)
    private Phim selectedPhim = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // 1. Cấu hình cột cho bảng (Map với tên thuộc tính trong Model Phim)
        colID.setCellValueFactory(new PropertyValueFactory<>("idPhim"));
        colTen.setCellValueFactory(new PropertyValueFactory<>("ten"));
        colNgay.setCellValueFactory(new PropertyValueFactory<>("ngayPhatHanh"));
        colThoiLuong.setCellValueFactory(new PropertyValueFactory<>("thoiLuong"));
        colNgonNgu.setCellValueFactory(new PropertyValueFactory<>("ngonNguChinh"));

        // 2. Tải dữ liệu lên bảng
        loadData();

        // 3. Thêm sự kiện khi chọn dòng trong bảng
        tablePhim.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectPhim(newSelection);
            }
        });
    }

    private void loadData() {
        List<Phim> list = phimDAO.layDanhSachPhim();
        ObservableList<Phim> data = FXCollections.observableArrayList(list);
        tablePhim.setItems(data);
    }

    // Hàm điền dữ liệu từ dòng chọn vào Form
    private void selectPhim(Phim phim) {
        this.selectedPhim = phim;

        tfTenPhim.setText(phim.getTen());
        dpNgayPhatHanh.setValue(phim.getNgayPhatHanh());
        tfThoiLuong.setText(String.valueOf(phim.getThoiLuong()));
        tfNgonNgu.setText(phim.getNgonNguChinh());
        taNoiDung.setText(phim.getNoiDung());

        // Bật nút Sửa/Xóa, tắt nút Thêm (để tránh thêm nhầm khi đang sửa)
        btnThem.setDisable(true);
        btnSua.setDisable(false);
        btnXoa.setDisable(false);

        lblMessage.setText("");
    }

    @FXML
    public void handleThem(ActionEvent event) {
        if (!validateInput()) return;

        try {
            Phim phimMoi = new Phim();
            phimMoi.setTen(tfTenPhim.getText());
            phimMoi.setNgayPhatHanh(dpNgayPhatHanh.getValue());
            phimMoi.setThoiLuong(Float.parseFloat(tfThoiLuong.getText()));
            phimMoi.setNgonNguChinh(tfNgonNgu.getText());
            phimMoi.setNoiDung(taNoiDung.getText());

            // Giả định Admin ID là AD01 (Trong thực tế sẽ lấy từ session đăng nhập)
            phimMoi.setIdNguoiDung("AD01");

            if (phimDAO.themPhim(phimMoi)) {
                showMessage("Thêm phim thành công!", "green");
                handleLamMoi(null); // Reset form
                loadData(); // Tải lại bảng
            } else {
                showMessage("Thêm thất bại! Kiểm tra lại dữ liệu.", "red");
            }
        } catch (NumberFormatException e) {
            showMessage("Thời lượng phải là số!", "red");
        }
    }

    @FXML
    public void handleSua(ActionEvent event) {
        if (selectedPhim == null) return;
        if (!validateInput()) return;

        try {
            // Cập nhật thông tin cho đối tượng đang chọn
            selectedPhim.setTen(tfTenPhim.getText());
            selectedPhim.setNgayPhatHanh(dpNgayPhatHanh.getValue());
            selectedPhim.setThoiLuong(Float.parseFloat(tfThoiLuong.getText()));
            selectedPhim.setNgonNguChinh(tfNgonNgu.getText());
            selectedPhim.setNoiDung(taNoiDung.getText());

            if (phimDAO.capNhatPhim(selectedPhim)) {
                showMessage("Cập nhật thành công!", "green");
                handleLamMoi(null);
                loadData();
            } else {
                showMessage("Cập nhật thất bại!", "red");
            }
        } catch (NumberFormatException e) {
            showMessage("Thời lượng phải là số!", "red");
        }
    }

    @FXML
    public void handleXoa(ActionEvent event) {
        if (selectedPhim == null) return;

        // Hỏi xác nhận trước khi xóa
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Xác nhận xóa");
        alert.setHeaderText("Bạn có chắc chắn muốn xóa phim: " + selectedPhim.getTen() + "?");
        alert.setContentText("Hành động này không thể hoàn tác.");

        if (alert.showAndWait().get() == ButtonType.OK) {
            if (phimDAO.xoaPhim(selectedPhim.getIdPhim())) {
                showMessage("Đã xóa phim.", "green");
                handleLamMoi(null);
                loadData();
            } else {
                showMessage("Không thể xóa! Phim này có thể đã có lịch chiếu/vé.", "red");
            }
        }
    }

    @FXML
    public void handleLamMoi(ActionEvent event) {
        // Xóa trắng các ô nhập liệu
        tfTenPhim.clear();
        dpNgayPhatHanh.setValue(null);
        tfThoiLuong.clear();
        tfNgonNgu.clear();
        taNoiDung.clear();
        lblMessage.setText("");

        // Reset trạng thái nút và bảng
        selectedPhim = null;
        tablePhim.getSelectionModel().clearSelection();
        btnThem.setDisable(false);
        btnSua.setDisable(true);
        btnXoa.setDisable(true);
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

    // Helper: Kiểm tra dữ liệu nhập vào
    private boolean validateInput() {
        if (tfTenPhim.getText().isEmpty() || dpNgayPhatHanh.getValue() == null ||
                tfThoiLuong.getText().isEmpty() || tfNgonNgu.getText().isEmpty()) {
            showMessage("Vui lòng điền đầy đủ thông tin bắt buộc!", "red");
            return false;
        }
        return true;
    }

    private void showMessage(String msg, String color) {
        lblMessage.setText(msg);
        if ("red".equals(color)) {
            lblMessage.setStyle("-fx-text-fill: #e74c3c;");
        } else {
            lblMessage.setStyle("-fx-text-fill: #27ae60;");
        }
    }
}