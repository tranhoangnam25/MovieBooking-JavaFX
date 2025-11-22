package com.group13.csdl.moviebooking.model;

public class VeXemPhim {
    private int idVe;
    private String trangThai; // CHUASUDUNG, DASUDUNG, DAHUY
    private int idSuatChieu;
    private int idHoaDon;
    private int idGhe;

    // Các trường bổ sung để hiển thị thông tin chi tiết (Joined Data)
    private String tenGhe; // Ví dụ: A1, B2
    private String tenPhim;
    private String thoiGianChieu;

    public VeXemPhim() {
    }

    public VeXemPhim(int idVe, String trangThai, int idSuatChieu, int idHoaDon, int idGhe) {
        this.idVe = idVe;
        this.trangThai = trangThai;
        this.idSuatChieu = idSuatChieu;
        this.idHoaDon = idHoaDon;
        this.idGhe = idGhe;
    }

    // Getters and Setters
    public int getIdVe() { return idVe; }
    public void setIdVe(int idVe) { this.idVe = idVe; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    public int getIdSuatChieu() { return idSuatChieu; }
    public void setIdSuatChieu(int idSuatChieu) { this.idSuatChieu = idSuatChieu; }

    public int getIdHoaDon() { return idHoaDon; }
    public void setIdHoaDon(int idHoaDon) { this.idHoaDon = idHoaDon; }

    public int getIdGhe() { return idGhe; }
    public void setIdGhe(int idGhe) { this.idGhe = idGhe; }

    public String getTenGhe() { return tenGhe; }
    public void setTenGhe(String tenGhe) { this.tenGhe = tenGhe; }

    public String getTenPhim() { return tenPhim; }
    public void setTenPhim(String tenPhim) { this.tenPhim = tenPhim; }

    public String getThoiGianChieu() { return thoiGianChieu; }
    public void setThoiGianChieu(String thoiGianChieu) { this.thoiGianChieu = thoiGianChieu; }
}