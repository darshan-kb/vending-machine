package com.vending.machine.controller;

import com.vending.machine.dto.*;
import com.vending.machine.enums.MachineEvent;
import com.vending.machine.enums.MachineState;
import com.vending.machine.enums.VendEnums;
import com.vending.machine.service.ItemService;
import com.vending.machine.service.MenuService;
import com.vending.machine.service.VendingStateMachineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.statemachine.service.StateMachineService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@RestController
public class VendingMachineController {

    @Autowired
    private MenuService menuService;
    @Autowired
    private ItemService itemService;

    @Autowired
    private VendingStateMachineService stateMachineService;

    @GetMapping("/menu")
    public MenuDto getMenu() throws Exception{
        return menuService.getMenu();
    }

    @PostMapping("/select/{txnId}")
    public InvoiceDto selectItem(@RequestBody List<ItemDto> items, @PathVariable String txnId){
        return itemService.selectItem(items, txnId);
    }

    @GetMapping("/buy/{txnId}")
    public InvoiceDto buyItem(@PathVariable String txnId){
        return itemService.buyItem(txnId);
    }
    @PostMapping("/pay")
    public String pay(@RequestBody PaymentPayload paymentPayload){
        return itemService.pay(paymentPayload);
    }

    @GetMapping("/state/{txnId}")
    public Mono<MachineState> getState(@PathVariable String txnId) throws Exception {
        return stateMachineService.getMachine(txnId).map(sm -> sm.getState().getId());
    }

    @GetMapping("/getExtendedState/{txnId}")
    public TxnDetailsDto getExtendedState(@PathVariable String txnId){
        return stateMachineService.getStateMachine(txnId).getExtendedState().get(VendEnums.TXN_DETAILS, TxnDetailsDto.class);
    }
}
