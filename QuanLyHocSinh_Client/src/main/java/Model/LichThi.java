package Model;

public class LichThi {
    private int maLT;
    private String tenKyThi;
    private String maMH;
    private String ngayThi;     
    private String gioBatDau;  
    private String gioKetThuc;  
    private String maPhong;

    public LichThi() {}

    public LichThi(int maLT, String tenKyThi, String maMH, String ngayThi, String gioBatDau, String gioKetThuc, String maPhong) {
        this.maLT = maLT;
        this.tenKyThi = tenKyThi;
        this.maMH = maMH;
        this.ngayThi = ngayThi;
        this.gioBatDau = gioBatDau;
        this.gioKetThuc = gioKetThuc;
        this.maPhong = maPhong;
    }


    public int getMaLT() { return maLT; }
    public void setMaLT(int maLT) { this.maLT = maLT; }

    public String getTenKyThi() { return tenKyThi; }
    public void setTenKyThi(String tenKyThi) { this.tenKyThi = tenKyThi; }

    public String getMaMH() { return maMH; }
    public void setMaMH(String maMH) { this.maMH = maMH; }

    public String getNgayThi() { return ngayThi; }
    public void setNgayThi(String ngayThi) { this.ngayThi = ngayThi; }

    public String getGioBatDau() { return gioBatDau; }
    public void setGioBatDau(String gioBatDau) { this.gioBatDau = gioBatDau; }

    public String getGioKetThuc() { return gioKetThuc; }
    public void setGioKetThuc(String gioKetThuc) { this.gioKetThuc = gioKetThuc; }

    public String getMaPhong() { return maPhong; }
    public void setMaPhong(String maPhong) { this.maPhong = maPhong; }
}