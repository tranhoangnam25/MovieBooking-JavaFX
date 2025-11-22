package com.group13.csdl.moviebooking;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        // Tải file giao diện Đăng nhập đầu tiên
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("fxml/Login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        // Thiết lập tiêu đề cửa sổ dựa trên tên đề tài
        stage.setTitle("Hệ thống bán vé xem phim online - Nhóm 13");
        stage.setScene(scene);

        // Không cho phép thay đổi kích thước cửa sổ để giữ giao diện đẹp (tùy chọn)
        stage.setResizable(false);

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}