package pl.kopacz.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProductReportSpiceDTO implements Comparable<ProductReportSpiceDTO>, Serializable {

    private String spiceName;
    private BigDecimal recipeAmount;
    private BigDecimal usedAmount;
    private List<ProductReportSpiceUsageDTO> spiceUsages = new ArrayList<>();

    public String getSpiceName() {
        return spiceName;
    }

    public void setSpiceName(String spiceName) {
        this.spiceName = spiceName;
    }

    public BigDecimal getRecipeAmount() {
        return recipeAmount;
    }

    public void setRecipeAmount(BigDecimal recipeAmount) {
        this.recipeAmount = recipeAmount;
    }

    public BigDecimal getUsedAmount() {
        return usedAmount;
    }

    public void setUsedAmount(BigDecimal usedAmount) {
        this.usedAmount = usedAmount;
    }

    public List<ProductReportSpiceUsageDTO> getSpiceUsages() {
        return spiceUsages;
    }

    public void setSpiceUsages(List<ProductReportSpiceUsageDTO> spiceUsages) {
        this.spiceUsages = spiceUsages;
        this.usedAmount = spiceUsages.stream()
            .map(ProductReportSpiceUsageDTO::getSpiceAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void sort() {
        Collections.sort(spiceUsages);
    }

    @Override
    public int compareTo(ProductReportSpiceDTO spice) {
        return this.spiceName.compareTo(spice.spiceName);
    }

}
