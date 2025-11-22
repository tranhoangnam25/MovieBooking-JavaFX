package com.group13.csdl.moviebooking.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class LichLamViec {
    private LocalDate ngayLam;
    private String tenCa;     // Ví dụ: CASANG, CACHIEU
    private LocalTime gioBatDau;
    private LocalTime gioKetThuc;

    public LichLamViec() {
    }

    public LichLamViec(LocalDate ngayLam, String tenCa, LocalTime gioBatDau, LocalTime gioKetThuc) {
        this.ngayLam = ngayLam;
        this.tenCa = tenCa;
        this.gioBatDau = gioBatDau;
        this.gioKetThuc = gioKetThuc;
    }

    public LocalDate getNgayLam() { return ngayLam; }
    public void setNgayLam(LocalDate ngayLam) { this.ngayLam = ngayLam; }

    public String getTenCa() { return tenCa; }
    public void setTenCa(String tenCa) { this.tenCa = tenCa; }

    public LocalTime getGioBatDau() { return gioBatDau; }
    public void setGioBatDau(LocalTime gioBatDau) { this.gioBatDau = gioBatDau; }

    public LocalTime getGioKetThuc() { return gioKetThuc; }
    public void setGioKetThuc(LocalTime gioKetThuc) { this.gioKetThuc = gioKetThuc; }

    // Helper để hiển thị giờ đẹp hơn (VD: 08:00 - 12:00)
    public String getThoiGianCa() {
        return gioBatDau + " - " + gioKetThuc;
    }
}