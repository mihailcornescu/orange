package com.orange.api.composite.product;

public class ProductAggregate {
    private final int productId;
    private final String name;
    private final int amount;

    public ProductAggregate() {
        productId = 0;
        name = null;
        amount = 0;
    }

    public ProductAggregate(
            int productId,
            String name,
            int amount) {

        this.productId = productId;
        this.name = name;
        this.amount = amount;
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

}
