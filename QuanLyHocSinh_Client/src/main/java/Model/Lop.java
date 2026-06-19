package Model;

public class Lop {
    private String maLop;
    private String tenLop;
    private String nienKhoa;
    private String maGVCN;

    public Lop() { }

    public Lop(String maLop, String tenLop, String nienKhoa, String maGVCN) {
        this.maLop = maLop;
        this.tenLop = tenLop;
        this.nienKhoa = nienKhoa;
        this.maGVCN = maGVCN;
    }

    public String getMaLop() { return maLop; }
    public void setMaLop(String maLop) { this.maLop = maLop; }

    public String getTenLop() { return tenLop; }
    public void setTenLop(String tenLop) { this.tenLop = tenLop; }

    public String getNienKhoa() { return nienKhoa; }
    public void setNienKhoa(String nienKhoa) { this.nienKhoa = nienKhoa; }

    public String getMaGVCN() { return maGVCN; }
    public void setMaGVCN(String maGVCN) { this.maGVCN = maGVCN; }
}