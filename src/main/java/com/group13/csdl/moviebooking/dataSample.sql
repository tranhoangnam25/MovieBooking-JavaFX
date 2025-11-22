USE `QuanLyBanVeOnline`;

-- ============================================================
-- BƯỚC 1: LÀM SẠCH DỮ LIỆU (RESET TOÀN BỘ)
-- ============================================================
SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE VeXemPhim;
TRUNCATE TABLE HoaDon;
TRUNCATE TABLE LichCuThe;
TRUNCATE TABLE SuatChieu;
TRUNCATE TABLE TheLoaiPhim;
TRUNCATE TABLE Phim;
TRUNCATE TABLE Ghe;
TRUNCATE TABLE PhongChieu;
TRUNCATE TABLE NguoiDung;
TRUNCATE TABLE TheLoai;
TRUNCATE TABLE CaLamViec;
TRUNCATE TABLE PhuongThucThanhToan;

-- ============================================================
-- BƯỚC 2: CẤU HÌNH HỆ THỐNG (PHÒNG, CA, THANH TOÁN)
-- ============================================================

-- 1. Ca Làm Việc
INSERT INTO CaLamViec (NoiDung, GioBatDau, GioKetThuc) VALUES 
('CASANG', '08:00:00', '12:00:00'),
('CACHIEU', '13:00:00', '17:00:00'),
('CATOI', '18:00:00', '22:00:00'),
('CADEM', '22:00:00', '02:00:00');

-- 2. Phương Thức Thanh Toán
INSERT INTO PhuongThucThanhToan (NoiDung) VALUES 
('Tiền mặt tại quầy'), 
('Thẻ ATM/Visa/Mastercard'), 
('Ví MoMo'), 
('ZaloPay'), 
('ShopeePay'), 
('VNPAY-QR');

-- 3. Thể Loại Phim
INSERT INTO TheLoai (NoiDung) VALUES 
('Hành động'), ('Tình cảm'), ('Hoạt hình'), ('Khoa học viễn tưởng'), 
('Kinh dị'), ('Hài hước'), ('Gia đình'), ('Tâm lý'), 
('Lịch sử'), ('Chiến tranh'), ('Tài liệu'), ('Anime');

-- 4. Phòng Chiếu (Mô phỏng rạp thật)
INSERT INTO PhongChieu (SucChua, TrangThai) VALUES 
(120, 'DANGHOATDONG'),  -- Phòng 1: IMAX (Lớn nhất)
(80, 'DANGHOATDONG'),   -- Phòng 2: Standard 1
(80, 'DANGHOATDONG'),   -- Phòng 3: Standard 2
(40, 'DANGHOATDONG'),   -- Phòng 4: VIP (Ghế đôi)
(60, 'BAOTRI');         -- Phòng 5: Đang sửa chữa

-- ============================================================
-- BƯỚC 3: TẠO GHẾ NGỒI (SỐ LƯỢNG LỚN & ĐA DẠNG)
-- ============================================================
-- Kỹ thuật: Dùng CROSS JOIN để nhân bản dòng, tạo hàng loạt ghế mà không cần viết 1000 dòng lệnh INSERT

-- 3.1. Tạo ghế cho Phòng 1 (IMAX - 12 hàng x 10 cột = 120 ghế)
INSERT INTO Ghe (TrangThai, idPhongChieu, Hang, Cot)
SELECT 'TOT', 1, H.Hang, C.Cot
FROM (SELECT 'A' AS Hang UNION SELECT 'B' UNION SELECT 'C' UNION SELECT 'D' UNION SELECT 'E' UNION SELECT 'F' UNION SELECT 'G' UNION SELECT 'H' UNION SELECT 'I' UNION SELECT 'J' UNION SELECT 'K' UNION SELECT 'L') H
CROSS JOIN (SELECT 1 AS Cot UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9 UNION SELECT 10) C;

-- 3.2. Tạo ghế cho Phòng 2 & 3 (Standard - 8 hàng x 10 cột = 80 ghế/phòng)
INSERT INTO Ghe (TrangThai, idPhongChieu, Hang, Cot)
SELECT 'TOT', P.idPhong, H.Hang, C.Cot
FROM (SELECT 2 AS idPhong UNION SELECT 3) P
CROSS JOIN (SELECT 'A' AS Hang UNION SELECT 'B' UNION SELECT 'C' UNION SELECT 'D' UNION SELECT 'E' UNION SELECT 'F' UNION SELECT 'G' UNION SELECT 'H') H
CROSS JOIN (SELECT 1 AS Cot UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9 UNION SELECT 10) C;

-- 3.3. Tạo ghế cho Phòng 4 (VIP - 5 hàng x 8 cột = 40 ghế)
INSERT INTO Ghe (TrangThai, idPhongChieu, Hang, Cot)
SELECT 'TOT', 4, H.Hang, C.Cot
FROM (SELECT 'A' AS Hang UNION SELECT 'B' UNION SELECT 'C' UNION SELECT 'D' UNION SELECT 'E') H
CROSS JOIN (SELECT 1 AS Cot UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8) C;

-- 3.4. Tạo ghế Bảo trì (Vài ghế bị hỏng ngẫu nhiên)
UPDATE Ghe SET TrangThai = 'BAOTRI' WHERE idGhe IN (5, 15, 25, 100, 101, 200);

-- ============================================================
-- BƯỚC 4: NHÂN SỰ VÀ KHÁCH HÀNG
-- ============================================================

-- Quản lý
INSERT INTO NguoiDung (idNguoiDung, TaiKhoan, MatKhau, HoTen, NgaySinh, SDT, Email, VaiTro, NgayBatDau, TrangThai) VALUES 
('AD01', 'admin', 'Admin@123', 'Trần Quản Lý', '1990-01-01', '0909999888', 'manager@cinema13.com', 'QUANLY', '2020-01-01', 'DANGLAM');

-- Nhân viên
INSERT INTO NguoiDung (idNguoiDung, TaiKhoan, MatKhau, HoTen, NgaySinh, SDT, Email, VaiTro, NgayBatDau, TrangThai, idQuanLy) VALUES 
('NV01', 'staff1', 'Staff@123', 'Nguyễn Thu Ngân', '1998-05-10', '0901111222', 'ngan.nt@cinema13.com', 'NHANVIEN', '2023-01-01', 'DANGLAM', 'AD01'),
('NV02', 'staff2', 'Staff@123', 'Lê Soát Vé', '1999-08-20', '0903333444', 've.ls@cinema13.com', 'NHANVIEN', '2023-02-15', 'DANGLAM', 'AD01'),
('NV03', 'staff3', 'Staff@123', 'Phạm Bảo Vệ', '1985-12-12', '0905555666', 'baove@cinema13.com', 'NHANVIEN', '2023-03-01', 'DANGLAM', 'AD01');

-- Khách hàng (Dữ liệu mẫu phong phú)
INSERT INTO NguoiDung (idNguoiDung, TaiKhoan, MatKhau, HoTen, NgaySinh, SDT, Email, VaiTro) VALUES 
('KH01', 'khach1', 'User@123', 'Nguyễn Văn An', '2000-01-01', '0910000001', 'an.nv@gmail.com', 'KHACHHANG'),
('KH02', 'khach2', 'User@123', 'Trần Thị Bình', '2001-02-02', '0910000002', 'binh.tt@gmail.com', 'KHACHHANG'),
('KH03', 'khach3', 'User@123', 'Lê Văn Cường', '2002-03-03', '0910000003', 'cuong.lv@gmail.com', 'KHACHHANG'),
('KH04', 'khach4', 'User@123', 'Phạm Thị Dung', '2003-04-04', '0910000004', 'dung.pt@gmail.com', 'KHACHHANG'),
('KH05', 'khach5', 'User@123', 'Hoàng Văn Em', '1995-05-05', '0910000005', 'em.hv@gmail.com', 'KHACHHANG');

-- Phân công lịch làm việc cho nhân viên
INSERT INTO LichCuThe (NgayLamViec, idCaLamViec, idNguoiDung) VALUES 
(CURDATE(), 1, 'NV01'), (CURDATE(), 2, 'NV02'), -- Hôm nay
(DATE_ADD(CURDATE(), INTERVAL 1 DAY), 1, 'NV02'), -- Ngày mai
(DATE_ADD(CURDATE(), INTERVAL 1 DAY), 3, 'NV01'); -- Ngày mai

-- ============================================================
-- BƯỚC 5: KHO PHIM (BLOCKBUSTERS)
-- ============================================================
INSERT INTO Phim (Ten, NgayPhatHanh, ThoiLuong, NgonNguChinh, NoiDung, idNguoiDung) VALUES 
('Mai', '2024-02-10', 131, 'Tiếng Việt', 'Phim tâm lý tình cảm sâu sắc về cuộc đời người phụ nữ tên Mai.', 'AD01'),
('Đào, Phở và Piano', '2024-02-10', 100, 'Tiếng Việt', 'Bối cảnh Hà Nội những ngày cuối năm 1946, ca ngợi tinh thần yêu nước.', 'AD01'),
('Dune: Hành Tinh Cát - Phần 2', '2024-03-01', 166, 'Tiếng Anh', 'Paul Atreides hợp tác với Chani và người Fremen để trả thù.', 'AD01'),
('Kung Fu Panda 4', '2024-03-08', 94, 'Tiếng Anh', 'Po phải tìm kiếm và huấn luyện Thần Long Đại Hiệp tiếp theo.', 'AD01'),
('Exhuma: Quật Mộ Trùng Ma', '2024-03-15', 134, 'Tiếng Hàn', 'Những sự kiện kỳ bí xảy ra khi các pháp sư khai quật một ngôi mộ cổ.', 'AD01'),
('Godzilla x Kong: Đế Chế Mới', '2024-03-29', 115, 'Tiếng Anh', 'Hai quái vật khổng lồ hợp sức chống lại một mối đe dọa mới.', 'AD01'),
('Lật Mặt 7: Một Điều Ước', '2024-04-26', 138, 'Tiếng Việt', 'Câu chuyện cảm động về người mẹ già và 5 người con.', 'AD01'),
('Hành Tinh Khỉ: Vương Quốc Mới', '2024-05-10', 145, 'Tiếng Anh', 'Thế giới nơi loài khỉ thống trị và con người sống trong bóng tối.', 'AD01');

-- Gán thể loại
INSERT INTO TheLoaiPhim (idTheLoai, idPhim) VALUES 
(2, 1), (8, 1), -- Mai: Tình cảm, Tâm lý
(9, 2), (2, 2), -- Đào Phở: Lịch sử, Tình cảm
(4, 3), (1, 3), -- Dune: Viễn tưởng, Hành động
(3, 4), (6, 4), -- Panda: Hoạt hình, Hài
(5, 5), -- Exhuma: Kinh dị
(1, 6), (4, 6), -- Godzilla: Hành động
(7, 7), (2, 7), -- Lật mặt: Gia đình
(1, 8), (4, 8); -- Khỉ: Hành động

-- ============================================================
-- BƯỚC 6: LỊCH CHIẾU (QUÁ KHỨ & TƯƠNG LAI)
-- ============================================================

-- 6.1. Suất chiếu QUÁ KHỨ (Để test vé đã dùng / hết hạn)
-- Chiếu ngày hôm qua
INSERT INTO SuatChieu (GiaVe, ThoiGianBatDau, idPhongChieu, idPhim, idNguoiDung) VALUES 
(90000, DATE_SUB(NOW(), INTERVAL 1 DAY), 1, 1, 'AD01'), -- Mai (Hôm qua)
(100000, DATE_SUB(NOW(), INTERVAL 26 HOUR), 2, 2, 'AD01'); -- Đào Phở (Hôm qua)

-- 6.2. Suất chiếu TƯƠNG LAI (Để khách hàng đặt)
-- Phim Mai (P1 - IMAX & P2 - Standard)
INSERT INTO SuatChieu (GiaVe, ThoiGianBatDau, idPhongChieu, idPhim, idNguoiDung) VALUES 
(120000, DATE_ADD(CURDATE(), INTERVAL '18:00' HOUR_MINUTE), 1, 1, 'AD01'), -- 18h hôm nay
(120000, DATE_ADD(CURDATE(), INTERVAL '21:00' HOUR_MINUTE), 1, 1, 'AD01'), -- 21h hôm nay
(100000, DATE_ADD(CURDATE(), INTERVAL '25:00' HOUR_MINUTE), 2, 1, 'AD01'); -- 10h sáng mai (24+1)

-- Phim Dune 2 (P1 - IMAX)
INSERT INTO SuatChieu (GiaVe, ThoiGianBatDau, idPhongChieu, idPhim, idNguoiDung) VALUES 
(150000, DATE_ADD(CURDATE(), INTERVAL '14:00' HOUR_MINUTE), 1, 3, 'AD01'),
(180000, DATE_ADD(CURDATE(), INTERVAL '26:00' HOUR_MINUTE), 1, 3, 'AD01'); -- 2h sáng mai

-- Phim Kungfu Panda (P3 - Standard)
INSERT INTO SuatChieu (GiaVe, ThoiGianBatDau, idPhongChieu, idPhim, idNguoiDung) VALUES 
(80000, DATE_ADD(CURDATE(), INTERVAL '09:00' HOUR_MINUTE), 3, 4, 'AD01'),
(80000, DATE_ADD(CURDATE(), INTERVAL '11:00' HOUR_MINUTE), 3, 4, 'AD01'),
(90000, DATE_ADD(CURDATE(), INTERVAL '15:00' HOUR_MINUTE), 3, 4, 'AD01');

-- Phim Exhuma (P4 - VIP)
INSERT INTO SuatChieu (GiaVe, ThoiGianBatDau, idPhongChieu, idPhim, idNguoiDung) VALUES 
(200000, DATE_ADD(CURDATE(), INTERVAL '20:00' HOUR_MINUTE), 4, 5, 'AD01'), -- VIP tối nay
(200000, DATE_ADD(CURDATE(), INTERVAL '23:00' HOUR_MINUTE), 4, 5, 'AD01'); -- VIP đêm nay

-- ============================================================
-- BƯỚC 7: GIAO DỊCH & VÉ (MÔ PHỎNG DOANH THU)
-- ============================================================

-- 7.1. Hóa đơn & Vé cho suất CHIẾU RỒI (Đã sử dụng/Hết hạn)
INSERT INTO HoaDon (NgayTao, NgayThanhToan, TrangThai, idPhuongThucThanhToan, idNguoiDung) VALUES (DATE_SUB(NOW(), INTERVAL 1 DAY), DATE_SUB(NOW(), INTERVAL 1 DAY), 'DATHANHTOAN', 1, 'KH01');
-- Vé đã xem (Giả sử suất ID 1 là suất hôm qua)
INSERT INTO VeXemPhim (TrangThai, idSuatChieu, idHoaDon, idGhe) VALUES 
('DASUDUNG', 1, 1, 1), ('DASUDUNG', 1, 1, 2);

-- 7.2. Hóa đơn & Vé cho suất SẮP CHIẾU (Khách đã đặt trước)
-- Khách 2 đặt vé Mai IMAX 18h hôm nay (Suất ID 3)
INSERT INTO HoaDon (NgayTao, NgayThanhToan, TrangThai, idPhuongThucThanhToan, idNguoiDung) VALUES (NOW(), NOW(), 'DATHANHTOAN', 3, 'KH02');
-- Vé chưa dùng
INSERT INTO VeXemPhim (TrangThai, idSuatChieu, idHoaDon, idGhe) VALUES 
('CHUASUDUNG', 3, 2, 50), ('CHUASUDUNG', 3, 2, 51), ('CHUASUDUNG', 3, 2, 52); -- Ghế giữa rạp

-- Khách 3 đặt vé VIP Exhuma (Suất ID 9)
INSERT INTO HoaDon (NgayTao, NgayThanhToan, TrangThai, idPhuongThucThanhToan, idNguoiDung) VALUES (NOW(), NOW(), 'DATHANHTOAN', 4, 'KH03');
INSERT INTO VeXemPhim (TrangThai, idSuatChieu, idHoaDon, idGhe) VALUES 
('CHUASUDUNG', 9, 3, 320), ('CHUASUDUNG', 9, 3, 321); -- Ghế trong phòng 4

-- ============================================================
-- HOÀN TẤT
-- ============================================================
SET FOREIGN_KEY_CHECKS = 1;