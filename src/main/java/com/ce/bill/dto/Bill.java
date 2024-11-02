package com.ce.bill.dto;

import lombok.Data;

import java.util.List;

/**
 * @startuml
 *
 * class Bill {
 *     - List<Item> items
 *     - double totalAmount
 *     - String originalCurrency
 *     - String targetCurrency
 * }
 *
 * @enduml
 */

@Data
public class Bill {

    private List<Item> items;

    private double totalAmount;

    private String originalCurrency;

    private String targetCurrency;

}
