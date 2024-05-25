package ecommerce.model;

public class OrderDetail {
    private int orderItemSN;
    private String orderID;
    private String productID;
    private int quantity;
    private int standPrice;
    private int itemPrice;

    // get && set
    public int getOrderItemSN() {
        return orderItemSN;
    }

    public void setOrderItemSN(int orderItemSN) {
        this.orderItemSN = orderItemSN;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getStandPrice() {
        return standPrice;
    }

    public void setStandPrice(int standPrice) {
        this.standPrice = standPrice;
    }

    public int getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(int itemPrice) {
        this.itemPrice = itemPrice;
    }
}