package dto.tm;

import javafx.scene.control.Button;

public class ItemTm {
    private String id;
    private String desc;
    private double unitPrice;
    private int qty;
    private Button delete;

    public ItemTm(String id, String desc, double unitPrice, int qty, Button delete) {
        this.id = id;
        this.desc = desc;
        this.unitPrice = unitPrice;
        this.qty = qty;
        this.delete = delete;
    }

    @Override
    public String toString() {
        return "ItemTm{" +
                "id='" + id + '\'' +
                ", desc='" + desc + '\'' +
                ", unitPrice=" + unitPrice +
                ", qty=" + qty +
                ", delete=" + delete +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public Button getDelete() {
        return delete;
    }

    public void setDelete(Button delete) {
        this.delete = delete;
    }
}
