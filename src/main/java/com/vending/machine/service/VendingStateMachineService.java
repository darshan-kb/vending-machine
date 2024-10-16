package com.vending.machine.service;

import com.vending.machine.enums.MachineEvent;
import com.vending.machine.enums.MachineState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.statemachine.service.StateMachineService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
@Service
public class VendingStateMachineService {

    @Autowired
    private StateMachineService<MachineState, MachineEvent> stateMachineService;
    @Autowired
    private StateMachinePersister<MachineState, MachineEvent, String> stateMachinePersister;
    public Mono<StateMachine<MachineState, MachineEvent>> getMachine(String txnId) throws Exception {

        StateMachine<MachineState, MachineEvent> sm = stateMachineService.acquireStateMachine(txnId);
        stateMachinePersister.persist(sm, txnId);
        return Mono.just(sm);
    }
}
