module com.group13.csdl.moviebooking {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j; // Thư viện MySQL

    // Mở gói chính để JavaFX khởi chạy Main
    opens com.group13.csdl.moviebooking to javafx.fxml;

    // Mở gói Controller để JavaFX gán sự kiện giao diện
    opens com.group13.csdl.moviebooking.controller to javafx.fxml;

    // --- SỬA LỖI Ở ĐÂY ---
    // Mở gói Model để JavaFX (TableView) có thể đọc dữ liệu từ các class như VeXemPhim, Phim...
    opens com.group13.csdl.moviebooking.model to javafx.base;

    // Xuất gói chính ra ngoài
    exports com.group13.csdl.moviebooking;
}