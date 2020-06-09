package se.magnus.api.core.product;

public class Product {
    private int productId;
    private String name;
    private int amount;
    private String serviceAddress;

    public Product() {
        productId = 0;
        name = null;
        amount = 0;
        serviceAddress = null;
    }

    public Product(int productId, String name, int amount, String serviceAddress) {
        this.productId = productId;
        this.name = name;
        this.amount = amount;
        this.serviceAddress = serviceAddress;
    }

    public int getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public int getAmount() {
        return amount;
    }

    public String getServiceAddress() {
        return serviceAddress;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setServiceAddress(String serviceAddress) {
        this.serviceAddress = serviceAddress;
    }
}
