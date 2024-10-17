package com.vending.machine.service;

import com.vending.machine.dto.InvoiceDto;
import com.vending.machine.dto.ItemDto;
import com.vending.machine.dto.PaymentPayload;
import com.vending.machine.dto.TxnDetailsDto;
import com.vending.machine.enums.MachineEvent;
import com.vending.machine.enums.MachineState;
import com.vending.machine.enums.VendEnums;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.cache.spi.support.AbstractReadWriteAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@Slf4j
public class ItemService {

    @Autowired
    private VendingStateMachineService stateMachineService;
    public InvoiceDto selectItem(List<ItemDto> items, String txnId){
            Mono<Message<MachineEvent>> event = Mono.just(MessageBuilder.withPayload(MachineEvent.ITEM_SELECTED).build());
            StateMachine<MachineState, MachineEvent> sm = stateMachineService.getStateMachine(txnId);
            sm.sendEvent(event).subscribe();
            sm.getExtendedState().getVariables().put(VendEnums.TXN_DETAILS, new TxnDetailsDto(txnId,items));

        return new InvoiceDto(txnId, items);
    }

    public InvoiceDto  buyItem(String txnId){

            Mono<Message<MachineEvent>> event = Mono.just(MessageBuilder.withPayload(MachineEvent.BUY_ITEM).build());
            StateMachine<MachineState, MachineEvent> sm = stateMachineService.getStateMachine(txnId);
            sm.sendEvent(event).subscribe();
            TxnDetailsDto txnDetails = sm.getExtendedState().get(VendEnums.TXN_DETAILS,TxnDetailsDto.class);

            return new InvoiceDto(txnDetails.getTxnId(), txnDetails.getItems());

    }

    public String pay(PaymentPayload paymentPayload) {

        Mono<Message<MachineEvent>> event = Mono.just(MessageBuilder.withPayload(MachineEvent.AMOUNT_RECEIVED).build());
        StateMachine<MachineState, MachineEvent> sm = stateMachineService.getStateMachine(paymentPayload.getTxnId());
        TxnDetailsDto txnDetailsDto = sm.getExtendedState().get(VendEnums.TXN_DETAILS, TxnDetailsDto.class);
        Double total = txnDetailsDto.getItems().stream().map(i -> i.getPrice()).reduce(0.0,(a,b)->a+b);
        if(paymentPayload.getAmount() == total) {
            sm.sendEvent(event).subscribe();
            return "Order placed successfully with id : " + paymentPayload.getTxnId();
        }
        else{
            return "Please add exact amount in vending machine";
        }
    }
}
