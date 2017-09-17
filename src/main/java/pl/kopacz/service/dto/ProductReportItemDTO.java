package pl.kopacz.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class ProductReportItemDTO implements Iterable<ProductReportItemDTO>,
    Comparable<ProductReportItemDTO>, Serializable {

    private LocalDate productionDate;
    private BigDecimal productionAmount;
    private List<ProductReportSpiceDTO> usedSpices = new ArrayList<>();

    public LocalDate getProductionDate() {
        return productionDate;
    }

    public void setProductionDate(LocalDate productionDate) {
        this.productionDate = productionDate;
    }

    public BigDecimal getProductionAmount() {
        return productionAmount;
    }

    public void setProductionAmount(BigDecimal productionAmount) {
        this.productionAmount = productionAmount;
    }

    public List<ProductReportSpiceDTO> getUsedSpices() {
        return usedSpices;
    }

    public void setUsedSpices(List<ProductReportSpiceDTO> usedSpices) {
        this.usedSpices = usedSpices;
    }

    public String getSpiceName(int index) {
        return usedSpices.get(index).getSpiceName();
    }

    public void sort() {
        Collections.sort(usedSpices);
        usedSpices.forEach(ProductReportSpiceDTO::sort);
    }

    @Override
    public Iterator<ProductReportItemDTO> iterator() {
        return new ProductReportItemIterator();
    }

    @Override
    public int compareTo(ProductReportItemDTO item) {
        return this.productionDate.compareTo(item.productionDate);
    }

    private class ProductReportItemIterator implements Iterator<ProductReportItemDTO> {

        private int part = 0;

        @Override
        public boolean hasNext() {
            return usedSpices.size() > part * 3;
        }

        @Override
        public ProductReportItemDTO next() {
            ProductReportItemDTO reportItem = new ProductReportItemDTO();
            reportItem.setProductionDate(productionDate);
            reportItem.setProductionAmount(productionAmount);
            reportItem.setUsedSpices(usedSpices(part++));
            return reportItem;
        }

        private List<ProductReportSpiceDTO> usedSpices(int part) {
            List<ProductReportSpiceDTO> result = new ArrayList<>();
            for (int i = part * 3; i < part * 3 + 3; i++) {
                if (i < usedSpices.size()) {
                    result.add(usedSpices.get(i));
                } else {
                    result.add(new ProductReportSpiceDTO());
                }
            }
            return result;
        }

    }

}
