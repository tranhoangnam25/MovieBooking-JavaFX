package com.group13.csdl.moviebooking.model;

import java.time.LocalDateTime;

public class SuatChieu {
    private int idSuatChieu;
    private int giaVe;
    private LocalDateTime thoiGianBatDau;
    private int idPhongChieu;
    private int idPhim;
    private String idNguoiDung; // Người quản lý tạo suất chiếu này

    // Constructor không tham số
    public SuatChieu() {
    }

    // Constructor đầy đủ
    public SuatChieu(int idSuatChieu, int giaVe, LocalDateTime thoiGianBatDau, int idPhongChieu, int idPhim, String idNguoiDung) {
        this.idSuatChieu = idSuatChieu;
        this.giaVe = giaVe;
        this.thoiGianBatDau = thoiGianBatDau;
        this.idPhongChieu = idPhongChieu;
        this.idPhim = idPhim;
        this.idNguoiDung = idNguoiDung;
    }

    // Getters and Setters
    public int getIdSuatChieu() { return idSuatChieu; }
    public void setIdSuatChieu(int idSuatChieu) { this.idSuatChieu = idSuatChieu; }

    public int getGiaVe() { return giaVe; }
    public void setGiaVe(int giaVe) { this.giaVe = giaVe; }

    public LocalDateTime getThoiGianBatDau() { return thoiGianBatDau; }
    public void setThoiGianBatDau(LocalDateTime thoiGianBatDau) { this.thoiGianBatDau = thoiGianBatDau; }

    public int getIdPhongChieu() { return idPhongChieu; }
    public void setIdPhongChieu(int idPhongChieu) { this.idPhongChieu = idPhongChieu; }

    public int getIdPhim() { return idPhim; }
    public void setIdPhim(int idPhim) { this.idPhim = idPhim; }

    public String getIdNguoiDung() { return idNguoiDung; }
    public void setIdNguoiDung(String idNguoiDung) { this.idNguoiDung = idNguoiDung; }

    @Override
    public String toString() {
        // Hiển thị dạng: "18:00 - Phòng 1" (Dùng cho ComboBox sau này nếu cần)
        return thoiGianBatDau.toLocalTime() + " - P" + idPhongChieu;
    }
}