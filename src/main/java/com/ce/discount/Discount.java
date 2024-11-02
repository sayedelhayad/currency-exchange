package com.ce.discount;

public interface Discount {

    double calculate(DiscountData discountData);

    default void apply(final DiscountData discountData) {
        double totalAmount = discountData.getBill().getTotalAmount();
        double discount = calculate(discountData);
        discountData.getBill().setTotalAmount(totalAmount - discount);
    }
}
