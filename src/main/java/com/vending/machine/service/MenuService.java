package com.vending.machine.service;

import com.vending.machine.dto.MenuDto;
import com.vending.machine.enums.MachineEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class MenuService {
    @Autowired
    private VendingStateMachineService stateMachineService;

    private static Map<String, Double> menu = Map.of("lays chips", 10.0, "Goodday", 20.0, "Parle-G", 10.0);
    public MenuDto getMenu(){
        Mono<Message<MachineEvent>> event = Mono.just(MessageBuilder.withPayload(MachineEvent.SHOW_MENU).build());
        String uuid = UUID.randomUUID().toString();
        try {
            stateMachineService.getMachine(uuid).map(sm -> sm.sendEvent(event));
        } catch (Exception e) {
            log.info("unable to transition");
        }

        return new MenuDto(uuid, menu);
    }
}
