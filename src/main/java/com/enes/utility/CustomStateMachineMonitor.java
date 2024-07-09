package com.enes.utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.monitor.AbstractStateMachineMonitor;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.function.Function;

@Service
public class CustomStateMachineMonitor extends AbstractStateMachineMonitor<String, String> {

    private final Logger logger = LoggerFactory.getLogger(CustomStateMachineMonitor.class);

    @Override
    public void transition(StateMachine<String, String> stateMachine, Transition<String, String> transition, long duration) {

        String source = transition.getSource() != null ? transition.getSource().getId() : "";
        String event = transition.getTrigger() != null ? transition.getTrigger().getEvent() : "";
        String target = transition.getTarget() != null ? transition.getTarget().getId() : "";
        System.out.printf(
                "\033[46m %s -> Monitor Operation: transition, State Machine ID: %s, Transition: %s -- %s --> %s, Duration: %d%n \033[0m",
                LocalDateTime.now(),
                stateMachine.getId(),
                source,
                event,
                target,
                duration);
        logger.info("Transition : {} -- {} --> {} Duration => {}", source, event, target, duration);
    }

    @Override
    public void action(StateMachine<String, String> stateMachine, Function<StateContext<String, String>, Mono<Void>> action, long duration) {
        System.out.printf(
                "\033[46m %s -> Monitor Operation: action, State Machine ID: %s, Action: %s, Duration: %d%n \033[0m",
                LocalDateTime.now(),
                stateMachine.getId(),
                action.toString(),
                duration);
        logger.info("Action : State Machine => {}, Action => {}, Duration => {}", stateMachine, action, duration);
    }
}
