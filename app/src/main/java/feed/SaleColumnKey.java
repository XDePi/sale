package feed;

import lombok.NonNull;

public enum SaleColumnKey {

    SALE_ID("sale_id"),
    DATE("date"),
    CUSTOMER_NAME("customer_name"),
    AMOUNT("amount");

    private final String name;

    SaleColumnKey(@NonNull String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
