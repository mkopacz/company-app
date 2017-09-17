package pl.kopacz.service.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class ProductReportDTO implements Iterable<ProductReportDTO>, Serializable {

    private String productName;
    private List<ProductReportItemDTO> reportItems = new ArrayList<>();

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public List<ProductReportItemDTO> getReportItems() {
        return reportItems;
    }

    public void setReportItems(List<ProductReportItemDTO> reportItems) {
        this.reportItems = reportItems;
    }

    public void addReportItem(ProductReportItemDTO reportItem) {
        this.reportItems.add(reportItem);
    }

    public String getSpiceName(int index) {
        String spiceName = null;
        if (reportItems.size() > 0) {
            ProductReportItemDTO reportItem = reportItems.get(0);
            spiceName = reportItem.getSpiceName(index);
        }
        return spiceName;
    }

    public void sort() {
        Collections.sort(reportItems);
        reportItems.forEach(ProductReportItemDTO::sort);
    }

    @Override
    public Iterator<ProductReportDTO> iterator() {
        return new ProductReportIterator();
    }

    private class ProductReportIterator implements Iterator<ProductReportDTO> {

        private List<Iterator<ProductReportItemDTO>> iterators;

        private ProductReportIterator() {
            this.iterators = reportItems.stream()
                .map(ProductReportItemDTO::iterator)
                .collect(toList());
        }

        @Override
        public boolean hasNext() {
            return iterators.stream()
                .allMatch(Iterator::hasNext);
        }

        @Override
        public ProductReportDTO next() {
            ProductReportDTO report = new ProductReportDTO();
            report.setProductName(productName);
            report.setReportItems(reportItems());
            return report;
        }

        private List<ProductReportItemDTO> reportItems() {
            return iterators.stream()
                .map(Iterator::next)
                .collect(toList());
        }

    }

}
