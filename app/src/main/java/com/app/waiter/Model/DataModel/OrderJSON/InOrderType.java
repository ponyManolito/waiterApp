package com.app.waiter.Model.DataModel.OrderJSON;

import java.util.List;

/**
 * Created by javier.gomez on 03/06/2015.
 */
public class InOrderType {
    public String type;

    public String status;

    public List<InProductInOrder> products;

    public InOrderType() {}

    public InOrderType(String type, String status, List<InProductInOrder> products) {
        this.type = type;
        this.status = status;
        this.products = products;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<InProductInOrder> getProducts() {
        return products;
    }

    public void setProducts(List<InProductInOrder> products) {
        this.products = products;
    }
}
