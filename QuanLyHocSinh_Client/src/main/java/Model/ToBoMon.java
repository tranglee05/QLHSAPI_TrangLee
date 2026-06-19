package Model;

public class ToBoMon {
    private String maToHop;
    private String tenToHop;
    private String truongBoMon;

    public ToBoMon() { }

    public ToBoMon(String maToHop, String tenToHop, String truongBoMon) {
        this.maToHop = maToHop;
        this.tenToHop = tenToHop;
        this.truongBoMon = truongBoMon;
    }

    public String getMaToHop() { return maToHop; }
    public void setMaToHop(String maToHop) { this.maToHop = maToHop; }

    public String getTenToHop() { return tenToHop; }
    public void setTenToHop(String tenToHop) { this.tenToHop = tenToHop; }

    public String getTruongBoMon() { return truongBoMon; }
    public void setTruongBoMon(String truongBoMon) { this.truongBoMon = truongBoMon; }

    // Hàm này quyết định ComboBox hiện cái gì (Hiện Tên Tổ thay vì mã loằng ngoằng)
    @Override
    public String toString() {
        return this.tenToHop; 
    }
}