package com.vending.machine.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuDto {
    private String txnId;
    private Map<String, Double> menu;
}
