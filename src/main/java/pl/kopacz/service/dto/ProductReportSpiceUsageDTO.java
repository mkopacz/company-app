package pl.kopacz.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class ProductReportSpiceUsageDTO implements Comparable<ProductReportSpiceUsageDTO>, Serializable {

    private BigDecimal spiceAmount;
    private String serialNumber;

    public BigDecimal getSpiceAmount() {
        return spiceAmount;
    }

    public void setSpiceAmount(BigDecimal spiceAmount) {
        this.spiceAmount = spiceAmount;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    @Override
    public int compareTo(ProductReportSpiceUsageDTO usage) {
        return this.serialNumber.compareTo(usage.serialNumber);
    }

}
