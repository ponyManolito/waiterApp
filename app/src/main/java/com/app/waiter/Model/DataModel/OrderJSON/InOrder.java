package com.app.waiter.Model.DataModel.OrderJSON;

import java.util.Date;
import java.util.List;

/**
 * Created by javier.gomez on 03/06/2015.
 */
public class InOrder {
    public int idTable;

    public String description;

    public List<InOrderType> types;

    public InOrder() {}

    public InOrder(int idTable, String description, List<InOrderType> types) {
        this.idTable = idTable;
        this.description = description;
        this.types = types;
    }

    public int getIdTable() {
        return idTable;
    }

    public void setIdTable(int idTable) {
        this.idTable = idTable;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<InOrderType> getTypes() {
        return types;
    }

    public void setTypes(List<InOrderType> types) {
        this.types = types;
    }

}
