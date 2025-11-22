package com.group13.csdl.moviebooking.model;

import java.time.LocalDate;

public class Phim {
    private int idPhim;
    private String ten;
    private LocalDate ngayPhatHanh;
    private float thoiLuong;
    private String ngonNguChinh;
    private String noiDung;
    private String idNguoiDung; // ID của Quản lý tạo phim này

    // Constructor không tham số
    public Phim() {
    }

    // Constructor đầy đủ
    public Phim(int idPhim, String ten, LocalDate ngayPhatHanh, float thoiLuong, String ngonNguChinh, String noiDung, String idNguoiDung) {
        this.idPhim = idPhim;
        this.ten = ten;
        this.ngayPhatHanh = ngayPhatHanh;
        this.thoiLuong = thoiLuong;
        this.ngonNguChinh = ngonNguChinh;
        this.noiDung = noiDung;
        this.idNguoiDung = idNguoiDung;
    }

    // Getters and Setters
    public int getIdPhim() { return idPhim; }
    public void setIdPhim(int idPhim) { this.idPhim = idPhim; }

    public String getTen() { return ten; }
    public void setTen(String ten) { this.ten = ten; }

    public LocalDate getNgayPhatHanh() { return ngayPhatHanh; }
    public void setNgayPhatHanh(LocalDate ngayPhatHanh) { this.ngayPhatHanh = ngayPhatHanh; }

    public float getThoiLuong() { return thoiLuong; }
    public void setThoiLuong(float thoiLuong) { this.thoiLuong = thoiLuong; }

    public String getNgonNguChinh() { return ngonNguChinh; }
    public void setNgonNguChinh(String ngonNguChinh) { this.ngonNguChinh = ngonNguChinh; }

    public String getNoiDung() { return noiDung; }
    public void setNoiDung(String noiDung) { this.noiDung = noiDung; }

    public String getIdNguoiDung() { return idNguoiDung; }
    public void setIdNguoiDung(String idNguoiDung) { this.idNguoiDung = idNguoiDung; }

    @Override
    public String toString() {
        return ten; // Để hiển thị tên phim trong ComboBox hoặc List
    }
}