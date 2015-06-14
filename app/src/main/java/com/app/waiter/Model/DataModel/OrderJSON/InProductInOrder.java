package com.app.waiter.Model.DataModel.OrderJSON;

/**
 * Created by javier.gomez on 03/06/2015.
 */
public class InProductInOrder {
    public int idProduct;

    public int quantity;

    public double price;

    public InProductInOrder() {}

    public InProductInOrder(int idProduct, int quantity, double price) {
        this.idProduct = idProduct;
        this.quantity = quantity;
        this.price = price;
    }

    public int getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(int idProduct) {
        this.idProduct = idProduct;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void increaseQuantity() { this.quantity++; }

    public void decreaseQuantity() { if (quantity != 0) this.quantity--; }
}
