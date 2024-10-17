package com.vending.machine.listener;

import com.vending.machine.enums.MachineEvent;
import com.vending.machine.enums.MachineState;
import lombok.extern.slf4j.Slf4j;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MachineListener extends StateMachineListenerAdapter<MachineState, MachineEvent> {

    @Override
    public void stateChanged(State<MachineState, MachineEvent> from, State<MachineState, MachineEvent> to) {
        log.info("state changed from {} to {}", from, to);
    }
}
