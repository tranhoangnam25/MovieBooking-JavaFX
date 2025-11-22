package com.group13.csdl.moviebooking.model;

public class Ghe {
    private int idGhe;
    private String trangThai; // "TOT", "BAOTRI"
    private int idPhongChieu;
    private String hang;      // A, B, C...
    private int cot;          // 1, 2, 3...

    // Constructor không tham số
    public Ghe() {
    }

    // Constructor đầy đủ
    public Ghe(int idGhe, String trangThai, int idPhongChieu, String hang, int cot) {
        this.idGhe = idGhe;
        this.trangThai = trangThai;
        this.idPhongChieu = idPhongChieu;
        this.hang = hang;
        this.cot = cot;
    }

    // Getters and Setters
    public int getIdGhe() { return idGhe; }
    public void setIdGhe(int idGhe) { this.idGhe = idGhe; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    public int getIdPhongChieu() { return idPhongChieu; }
    public void setIdPhongChieu(int idPhongChieu) { this.idPhongChieu = idPhongChieu; }

    public String getHang() { return hang; }
    public void setHang(String hang) { this.hang = hang; }

    public int getCot() { return cot; }
    public void setCot(int cot) { this.cot = cot; }

    // Helper để hiển thị tên ghế (VD: A1, B5)
    public String getTenGhe() {
        return hang + cot;
    }
}