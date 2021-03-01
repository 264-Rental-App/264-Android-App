package edu.rentals.frontend;

import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.List;

public class InvoiceList {
    @SerializedName("invoices")
    private List invoiceList;

    public InvoiceList(List invoiceList) {
        this.invoiceList = invoiceList;
    }

    public List getInvoiceList() {
        return invoiceList;
    }
}
