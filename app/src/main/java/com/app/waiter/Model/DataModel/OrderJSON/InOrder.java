package com.app.waiter.Model.DataModel.OrderJSON;

import java.util.Date;
import java.util.List;

/**
 * Created by javier.gomez on 03/06/2015.
 */
public class InOrder {
    public int id;

    public int idTable;

    public String description;

    public List<InOrderType> types;

    Date reg_date;

    public InOrder() {}

    public InOrder(int id, int idTable, String description, List<InOrderType> types, Date reg_date) {
        setId(id);
        this.idTable = idTable;
        this.description = description;
        this.types = types;
        setReg_date(reg_date);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public Date getReg_date() {
        return reg_date;
    }
    public void setReg_date(Date reg_date) {
        this.reg_date = reg_date;
    }

    public boolean isNew() {
        return getId() == 0;
    }

}
