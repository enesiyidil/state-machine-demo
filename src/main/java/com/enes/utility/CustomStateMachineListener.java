package com.enes.utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CustomStateMachineListener implements StateMachineListener<String, String> {

    private final Logger logger = LoggerFactory.getLogger(CustomStateMachineListener.class);

    @Override
    public void stateChanged(State<String, String> from, State<String, String> to) {

        System.out.printf(
                "\033[1;34m %s -> Listener Operation: stateChanged, From: %s, To: %s, Thread: %d%n \033[0m",
                LocalDateTime.now(),
                from != null ? from.getId() : "",
                to != null ? to.getId() : "",
                Thread.currentThread().getId());
        logger.info("State Changed : {} => {}", from, to);
    }

    @Override
    public void stateEntered(State<String, String> state) {

        System.out.printf(
                "\033[1;34m %s -> Listener Operation: stateEntered, State: %s, Thread: %d%n \033[0m",
                LocalDateTime.now(),
                state != null ? state.getId() : "",
                Thread.currentThread().getId());
        logger.info("State Entered : {}", state);
    }

    @Override
    public void stateExited(State<String, String> state) {

        System.out.printf(
                "\033[1;34m %s -> Listener Operation: stateExited, State: %s, Thread: %d%n \033[0m",
                LocalDateTime.now(),
                state != null ? state.getId() : "",
                Thread.currentThread().getId());
        logger.info("State Exited : {}", state);
    }

    @Override
    public void eventNotAccepted(Message<String> event) {

        System.err.printf(
                "\033[1;34m %s -> Listener Operation Error: eventNotAccepted, Event: %s, Thread: %d%n \033[0m",
                LocalDateTime.now(),
                event,
                Thread.currentThread().getId());
        logger.error("Event Not Accepted : {}", event);
    }

    @Override
    public void transition(Transition<String, String> transition) {

        String source = transition.getSource() != null ? transition.getSource().getId() : "";
        String event = transition.getTrigger() != null ? transition.getTrigger().getEvent() : "";
        String target = transition.getTarget() != null ? transition.getTarget().getId() : "";
        System.out.printf(
                "\033[1;34m %s -> Listener Operation: transition, Transition: %s -- %s --> %s, Thread: %d%n \033[0m",
                LocalDateTime.now(),
                source,
                event,
                target,
                Thread.currentThread().getId());
        logger.info("Transition : {} -- {} --> {}", source, event, target);
    }

    @Override
    public void transitionStarted(Transition<String, String> transition) {

        String source = transition.getSource() != null ? transition.getSource().getId() : "";
        String event = transition.getTrigger() != null ? transition.getTrigger().getEvent() : "";
        String target = transition.getTarget() != null ? transition.getTarget().getId() : "";
        System.out.printf(
                "\033[1;34m %s -> Listener Operation: transitionStarted, Transition: %s -- %s --> %s, Thread: %d%n \033[0m",
                LocalDateTime.now(),
                source,
                event,
                target,
                Thread.currentThread().getId());
        logger.info("Transition Started : {} -- {} --> {}", source, event, target);
    }

    @Override
    public void transitionEnded(Transition<String, String> transition) {

        String source = transition.getSource() != null ? transition.getSource().getId() : "";
        String event = transition.getTrigger() != null ? transition.getTrigger().getEvent() : "";
        String target = transition.getTarget() != null ? transition.getTarget().getId() : "";
        System.out.printf(
                "\033[1;34m %s -> Listener Operation: transitionEnded, Transition: %s -- %s --> %s, Thread: %d%n \033[0m",
                LocalDateTime.now(),
                source,
                event,
                target,
                Thread.currentThread().getId());
        logger.info("Transition Ended : {} -- {} --> {}", source, event, target);
    }

    @Override
    public void stateMachineStarted(StateMachine<String, String> stateMachine) {

        System.out.printf(
                "\033[1;34m %s -> Listener Operation: stateMachineStarted, Machine Id: %s, Machine UUID: %s, Thread: %d%n \033[0m",
                LocalDateTime.now(),
                stateMachine.getId(),
                stateMachine.getUuid(),
                Thread.currentThread().getId());
        logger.info("State Machine Started : {}", stateMachine);
    }

    @Override
    public void stateMachineStopped(StateMachine<String, String> stateMachine) {

        System.out.printf(
                "\033[1;34m %s -> Listener Operation: stateMachineStopped, Machine Id: %s, Machine UUID: %s, Thread: %d%n \033[0m",
                LocalDateTime.now(),
                stateMachine.getId(),
                stateMachine.getUuid(),
                Thread.currentThread().getId());
        logger.info("State Machine Stopped : {}", stateMachine);
    }

    @Override
    public void stateMachineError(StateMachine<String, String> stateMachine, Exception e) {

        System.err.printf(
                "\033[1;34m %s -> Listener Operation Error: stateMachineError, Machine Id: %s, Machine UUID: %s, Error: %s, Thread: %d%n \033[0m",
                LocalDateTime.now(),
                stateMachine.getId(),
                stateMachine.getUuid(),
                e.getClass().getSimpleName(),
                Thread.currentThread().getId());
        logger.error("State Machine Error : State Machine => {1}, Exception => {}", stateMachine, e);
    }

    @Override
    public void extendedStateChanged(Object o, Object o1) {

    }

    @Override
    public void stateContext(StateContext<String, String> stateContext) {

        System.out.printf(
                "\033[1;34m %s -> Listener Operation: stateContext, Current State: %s, Thread: %d%n \033[0m",
                LocalDateTime.now(),
                stateContext.getStateMachine().getState() != null ? stateContext.getStateMachine()
                        .getState().getId() : "",
                Thread.currentThread().getId());
        logger.info("StateContext : {}", stateContext);
    }
}
