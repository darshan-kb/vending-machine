package com.vending.machine.config;

import com.vending.machine.enums.MachineEvent;
import com.vending.machine.enums.MachineState;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.persist.StateMachinePersister;

import java.util.EnumSet;

@Configuration
@EnableStateMachineFactory
@RequiredArgsConstructor
public class MachineConfig extends EnumStateMachineConfigurerAdapter<MachineState, MachineEvent> {
//    private final MachineListener machineListener;
//    private final AppreciateThat appreciateThatAction;
//    private final MakePaymentAction makePaymentAction;
//    private final WelcomeAction welcomeAction;
//    private final YouAreNotAllowedToEnter youAreNotAllowedToEnter;
    private final StateMachinePersister<MachineState, MachineEvent, String> stateMachinePersister;

    @Override
    public void configure(StateMachineConfigurationConfigurer<MachineState, MachineEvent> config) throws Exception {
        config.withConfiguration().autoStartup(true);
//                .listener(machineListener);
//        config.withPersistence();
    }

    @Override
    public void configure(StateMachineStateConfigurer<MachineState, MachineEvent> states) throws Exception {
        states.withStates()
                .initial(MachineState.IDLE)
                .states(EnumSet.allOf(MachineState.class));
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<MachineState, MachineEvent> transitions) throws Exception {
        transitions
                .withExternal()
                .source(MachineState.IDLE).target(MachineState.MENU).event(MachineEvent.SHOW_MENU)
                .and()
                .withExternal()
                .source(MachineState.MENU).target(MachineState.ITEM_SELECTED).event(MachineEvent.ITEM_SELECTED)
                .and()
                .withExternal()
                .source(MachineState.ITEM_SELECTED).target(MachineState.PAYMENT).event(MachineEvent.BUY_ITEM)
                .and()
                .withExternal()
                .source(MachineState.PAYMENT).target(MachineState.PAYMENT_PROCESSED).event(MachineEvent.AMOUNT_RECEIVED);
    }
}
