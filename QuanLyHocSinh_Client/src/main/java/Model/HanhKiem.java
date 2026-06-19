package Model;

public class HanhKiem {
    private String maHS;
    private String tenHS; 
    private String maLop;
    private int hocKy;
    private String namHoc;
    private String xepLoai;
    private String nhanXet;

    public HanhKiem() {}

    public HanhKiem(String maHS, String tenHS, int hocKy, String namHoc, String xepLoai, String nhanXet) {
        this.maHS = maHS;
        this.tenHS = tenHS;
        this.hocKy = hocKy;
        this.namHoc = namHoc;
        this.xepLoai = xepLoai;
        this.nhanXet = nhanXet;
    }

  
    public String getMaHS() { return maHS; }
    public void setMaHS(String maHS) { this.maHS = maHS; }

    public String getTenHS() { return tenHS; }
    public void setTenHS(String tenHS) { this.tenHS = tenHS; }

    public String getMaLop() { return maLop; }
    public void setMaLop(String maLop) { this.maLop = maLop; }

    public int getHocKy() { return hocKy; }
    public void setHocKy(int hocKy) { this.hocKy = hocKy; }

    public String getNamHoc() { return namHoc; }
    public void setNamHoc(String namHoc) { this.namHoc = namHoc; }

    public String getXepLoai() { return xepLoai; }
    public void setXepLoai(String xepLoai) { this.xepLoai = xepLoai; }

    public String getNhanXet() { return nhanXet; }
    public void setNhanXet(String nhanXet) { this.nhanXet = nhanXet; }
}
