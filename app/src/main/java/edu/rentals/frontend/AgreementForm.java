package edu.rentals.frontend;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AgreementForm {
    @SerializedName("forms")
    private List formList;

    public AgreementForm(List formList) {
        this.formList = formList;
    }

    public List getFormListList() {
        return formList;
    }
}


