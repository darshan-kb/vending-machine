package com.vending.machine.dto;

import com.vending.machine.enums.MachineState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TxnDetailsDto {
    private String txnId;
    private List<ItemDto> items;
}
