package com.vending.machine.service;

import com.vending.machine.dto.ItemDto;
import com.vending.machine.dto.MenuDto;
import com.vending.machine.enums.MachineEvent;
import com.vending.machine.enums.MachineState;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachineEventResult;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class MenuService {
    @Autowired
    private VendingStateMachineService stateMachineService;

    private static List<ItemDto> menu = List.of(new ItemDto(1,"lays chips", 10.0), new ItemDto(2, "Goodday", 20.0), new ItemDto(3, "Parle-G", 10.0));
    public MenuDto getMenu(){
        Mono<Message<MachineEvent>> event = Mono.just(MessageBuilder.withPayload(MachineEvent.SHOW_MENU).build());
        String uuid = UUID.randomUUID().toString();
        try {
//            StateMachineEventResult<MachineState, MachineEvent> vsm = stateMachineService.getMachine(uuid).flatMapMany(sm -> sm.sendEvent(event)).blockFirst();
            StateMachine<MachineState, MachineEvent> sm = stateMachineService.getStateMachine(uuid);
            sm.sendEvent(event).subscribe();
//            sm.startReactively().block();
        } catch (Exception e) {
            log.info("unable to transition");
            throw new RuntimeException();
        }

        return new MenuDto(uuid, menu);
    }
}
