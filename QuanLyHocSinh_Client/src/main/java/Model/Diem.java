package Model;

public class Diem {
    private String maHS;
    private String tenHS; 
    private String maMH;
    private String tenMH;
    private int hocKy;

    private double diem15p;  
    private double diem1Tiet;
    private double diemGiuaKy;
    private double diemCuoiKy;    

    public Diem() {}

    public Diem(String maHS, String maMH, int hocKy, double diem15p, double diem1Tiet, double diemGiuaKy, double diemCuoiKy) {
        this.maHS = maHS;
        this.maMH = maMH;
        this.hocKy = hocKy;
        this.diem15p = diem15p;
        this.diem1Tiet = diem1Tiet;
        this.diemGiuaKy = diemGiuaKy;
        this.diemCuoiKy = diemCuoiKy;
    }

    public String getMaHS() { return maHS; }
    public void setMaHS(String maHS) { this.maHS = maHS; }

    public String getTenHS() { return tenHS; }
    public void setTenHS(String tenHS) { this.tenHS = tenHS; }

    public String getMaMH() { return maMH; }
    public void setMaMH(String maMH) { this.maMH = maMH; }

    public String getTenMH() { return tenMH; }
    public void setTenMH(String tenMH) { this.tenMH = tenMH; }

    public int getHocKy() { return hocKy; }
    public void setHocKy(int hocKy) { this.hocKy = hocKy; }

    public double getDiem15p() { return diem15p; }
    public void setDiem15p(double diem15p) { this.diem15p = diem15p; }

    public double getDiem1Tiet() { return diem1Tiet; }
    public void setDiem1Tiet(double diem1Tiet) { this.diem1Tiet = diem1Tiet; }

    public double getDiemGiuaKy() { return diemGiuaKy; }
    public void setDiemGiuaKy(double diemGiuaKy) { this.diemGiuaKy = diemGiuaKy; }

    public double getDiemCuoiKy() { return diemCuoiKy; }
    public void setDiemCuoiKy(double diemCuoiKy) { this.diemCuoiKy = diemCuoiKy; }

    // TÍNH ĐIỂM TỔNG KẾT
    public double getDiemTongKet() {
        return (diem15p + diem1Tiet + diemGiuaKy * 2 + diemCuoiKy * 3) / 7.0;
    }
}