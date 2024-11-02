package com.ce.bill.dto;

import lombok.Data;

/**
 * @startuml
 *
 * class Item {
 *     - String title
 *     - String category
 *     - double totalPrice
 * }
 *
 * @enduml
 */
@Data
public class Item {

    private String title;
    private String category;
    private double totalPrice;

}
