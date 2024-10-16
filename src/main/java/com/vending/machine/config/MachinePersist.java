package com.vending.machine.config;

import com.vending.machine.enums.MachineEvent;
import com.vending.machine.enums.MachineState;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.data.jpa.JpaPersistingStateMachineInterceptor;
import org.springframework.statemachine.data.jpa.JpaRepositoryStateMachinePersist;
import org.springframework.statemachine.data.jpa.JpaStateMachineRepository;
import org.springframework.statemachine.persist.DefaultStateMachinePersister;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.statemachine.service.DefaultStateMachineService;
import org.springframework.statemachine.service.StateMachineService;

@Configuration
public class MachinePersist {
    @Bean
    public StateMachinePersister<MachineState, MachineEvent, String> stateMachineRuntimePersister(
            JpaStateMachineRepository jpaStateMachineRepository
    ){
        JpaPersistingStateMachineInterceptor<MachineState, MachineEvent, String> interceptor =
                new JpaPersistingStateMachineInterceptor<>(jpaStateMachineRepository);

        return new DefaultStateMachinePersister<>(interceptor);
//        return new JpaPersistingStateMachineInterceptor<>(jpaStateMachineRepository);
    }

    @Bean
    public StateMachinePersist stateMachinePersist(
            JpaStateMachineRepository jpaStateMachineRepository
    ){
        return new JpaRepositoryStateMachinePersist<>(jpaStateMachineRepository);
    }

    @Bean
    public StateMachineService<MachineState, MachineEvent> stateMachineService(
            StateMachineFactory<MachineState, MachineEvent> stateMachineFactory,
            StateMachinePersist<MachineState, MachineEvent, String> stateMachinePersist
    ){
        return new DefaultStateMachineService<>(stateMachineFactory, stateMachinePersist);
    }
}
