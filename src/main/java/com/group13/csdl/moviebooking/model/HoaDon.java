package com.group13.csdl.moviebooking.model;

import java.time.LocalDateTime;

public class HoaDon {
    private int idHoaDon;
    private LocalDateTime ngayTao;
    private LocalDateTime ngayThanhToan;
    private String trangThai; // DATHANHTOAN, CHUATHANHTOAN
    private int idPhuongThucThanhToan;
    private String idNguoiDung;

    public HoaDon() {
    }

    public HoaDon(int idHoaDon, LocalDateTime ngayTao, LocalDateTime ngayThanhToan, String trangThai, int idPhuongThucThanhToan, String idNguoiDung) {
        this.idHoaDon = idHoaDon;
        this.ngayTao = ngayTao;
        this.ngayThanhToan = ngayThanhToan;
        this.trangThai = trangThai;
        this.idPhuongThucThanhToan = idPhuongThucThanhToan;
        this.idNguoiDung = idNguoiDung;
    }

    public int getIdHoaDon() { return idHoaDon; }
    public void setIdHoaDon(int idHoaDon) { this.idHoaDon = idHoaDon; }

    public LocalDateTime getNgayTao() { return ngayTao; }
    public void setNgayTao(LocalDateTime ngayTao) { this.ngayTao = ngayTao; }

    public LocalDateTime getNgayThanhToan() { return ngayThanhToan; }
    public void setNgayThanhToan(LocalDateTime ngayThanhToan) { this.ngayThanhToan = ngayThanhToan; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    public int getIdPhuongThucThanhToan() { return idPhuongThucThanhToan; }
    public void setIdPhuongThucThanhToan(int idPhuongThucThanhToan) { this.idPhuongThucThanhToan = idPhuongThucThanhToan; }

    public String getIdNguoiDung() { return idNguoiDung; }
    public void setIdNguoiDung(String idNguoiDung) { this.idNguoiDung = idNguoiDung; }
}
