package Model;

public class Giaovien {
    private String maGV;
    private String hoTen;
    private String ngaysinh;
    private String sdt;
    private String maTH; // Mã tổ hợp

    public Giaovien() { }

    public Giaovien(String maGV, String hoTen, String ngaysinh, String sdt, String maTH) {
        this.maGV = maGV;
        this.hoTen = hoTen;
        this.ngaysinh = ngaysinh;
        this.sdt = sdt;
        this.maTH = maTH;
    }

    public String getMaGV() { return maGV; }
    public void setMaGV(String maGV) { this.maGV = maGV; }

    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }

    public String getNgaysinh() { return ngaysinh; }
    public void setNgaysinh(String ngaysinh) { this.ngaysinh = ngaysinh; }

    public String getSdt() { return sdt; }
    public void setSdt(String sdt) { this.sdt = sdt; }

    public String getMaTH() { return maTH; }
    public void setMaTH(String maTH) { this.maTH = maTH; }
    
    @Override
    public String toString() {
        return this.hoTen; 
    }
}