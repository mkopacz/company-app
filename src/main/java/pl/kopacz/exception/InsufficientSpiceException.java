package pl.kopacz.exception;

public class InsufficientSpiceException extends RuntimeException {

    private String productName;
    private String spiceName;

    public InsufficientSpiceException(String productName, String spiceName) {
        this.productName = productName;
        this.spiceName = spiceName;
    }

    public String getProductName() {
        return productName;
    }

    public String getSpiceName() {
        return spiceName;
    }

}
