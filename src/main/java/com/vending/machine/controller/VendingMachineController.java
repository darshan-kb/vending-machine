package com.vending.machine.controller;

import com.vending.machine.dto.MenuDto;
import com.vending.machine.enums.MachineEvent;
import com.vending.machine.enums.MachineState;
import com.vending.machine.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.statemachine.service.StateMachineService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
public class VendingMachineController {

    @Autowired
    private MenuService menuService;

    @GetMapping("/menu")
    public MenuDto getMenu() throws Exception{
        return menuService.getMenu();
    }
}
