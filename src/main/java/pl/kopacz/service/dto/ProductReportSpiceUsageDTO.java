package pl.kopacz.service.dto;

import java.io.Serializable;

public class ProductReportSpiceUsageDTO implements Serializable {

    private Double spiceAmount;
    private String serialNumber;

    public Double getSpiceAmount() {
        return spiceAmount;
    }

    public void setSpiceAmount(Double spiceAmount) {
        this.spiceAmount = spiceAmount;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

}
