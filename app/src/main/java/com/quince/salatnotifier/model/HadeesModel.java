package com.quince.salatnotifier.model;

import java.io.Serializable;

public class HadeesModel implements Serializable {
    String arabic, urdu, english, reference;

    public String getArabic() {
        return arabic;
    }

    public void setArabic(String arabic) {
        this.arabic = arabic;
    }

    public String getUrdu() {
        return urdu;
    }

    public void setUrdu(String urdu) {
        this.urdu = urdu;
    }

    public String getEnglish() {
        return english;
    }

    public void setEnglish(String english) {
        this.english = english;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }
}
