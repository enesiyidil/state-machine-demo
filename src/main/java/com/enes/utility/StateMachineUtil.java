package com.enes.utility;

import com.enes.model.entity.Flow;
import com.enes.model.entity.FlowModel;
import com.enes.model.entity.State;
import com.enes.model.entity.Transition;
import com.enes.model.enums.EChoiceType;
import com.enes.model.enums.EStateType;
import com.enes.model.enums.ETransitionType;
import com.enes.service.FlowModelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.config.StateMachineBuilder.Builder;
import org.springframework.statemachine.guard.Guard;
import org.springframework.statemachine.persist.StateMachineRuntimePersister;
import org.springframework.statemachine.service.DefaultStateMachineService;
import org.springframework.statemachine.service.StateMachineService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class StateMachineUtil {

    final HashMap<String, StateMachineService<String, String>> machineServices = new HashMap<>();

    private final StateMachineRuntimePersister<String, String, String> stateMachineRuntimePersister;

    private final CustomStateMachineListener listener;

    private final CustomStateMachineMonitor monitor;

    private final StateMachineActionUtil actionUtil;

    private final StateMachineGuardUtil guardUtil;

    private final FlowModelService flowModelService;

    private final Logger logger = LoggerFactory.getLogger(StateMachineUtil.class);

    public StateMachineUtil(StateMachineRuntimePersister<String, String, String> stateMachineRuntimePersister,
                            CustomStateMachineListener listener,
                            CustomStateMachineMonitor monitor,
                            StateMachineActionUtil actionUtil,
                            StateMachineGuardUtil guardUtil,
                            FlowModelService flowModelService) {

        this.stateMachineRuntimePersister = stateMachineRuntimePersister;
        this.listener = listener;
        this.monitor = monitor;
        this.actionUtil = actionUtil;
        this.guardUtil = guardUtil;
        this.flowModelService = flowModelService;
    }

    public StateMachine<String, String> getStateMachine(Flow flow) {

        logger.info("Get State Machine : Flow => {}", flow);
        StateMachineService<String, String> stateMachineService = getStateMachineService(flow);
        logger.info("Get State Machine Service : stateMachineService => {}", stateMachineService);
        return stateMachineService.acquireStateMachine(flow.getMachineId());
    }

    StateMachineService<String, String> getStateMachineService(Flow flow) {

        logger.info("Get State Machine Service : Flow => {}", flow);
        if (!machineServices.containsKey(flow.getFlowModelId())) {
            try {
                stateMachineServiceBuilder(flow);
            } catch (Exception e) {
                // ToDo: Exception -> machineBuilder
                logger.error("Get State Machine Service : Flow => {1}, Exception => {}", flow, e);
                throw new RuntimeException(e);
            }
        }
        return machineServices.get(flow.getFlowModelId());
    }

    void stateMachineServiceBuilder(Flow flow) throws Exception {

        logger.info("State Machine Service Builder : Flow => {}", flow);
        StateMachineFactory<String, String> machineFactory = getStateMachineFactory(flow);
        machineServices.put(flow.getFlowModelId(),
                new DefaultStateMachineService<>(machineFactory, stateMachineRuntimePersister));
    }

    StateMachineFactory<String, String> getStateMachineFactory(Flow flow) throws Exception {

        long startTime = System.nanoTime();

        Builder<String, String> machineBuilder = new Builder<>();
        FlowModel flowModel = flowModelService.getModel(flow.getFlowModelId());

        configureState(flowModel, machineBuilder);
        configureTransition(flowModel, machineBuilder);
        configureChoice(flowModel, machineBuilder);

        machineBuilder.configureConfiguration()
                .withConfiguration().listener(listener);
        machineBuilder.configureConfiguration()
                .withMonitoring().monitor(monitor);
        machineBuilder.configureConfiguration().withPersistence()
                .runtimePersister(stateMachineRuntimePersister);

        logger.info("Get State Machine Factory : Flow => {}, Builder Time => {}ms", flow, (System.nanoTime() - startTime) / 1000000);
        return machineBuilder.createFactory();
    }

    void configureState(FlowModel flowModel, Builder<String, String> machineBuilder) throws Exception {

        String initialState = Objects.requireNonNull(
                flowModel.getStates().stream()
                        .filter(state -> state.getStateType().equals(EStateType.INITIATED)).findFirst()
                        .orElse(null)
        ).getId();
        String endState = Objects.requireNonNull(
                flowModel.getStates().stream()
                        .filter(state -> state.getStateType().equals(EStateType.ENDED)).findFirst()
                        .orElse(null)
        ).getId();
        machineBuilder.configureStates()
                .withStates()
                .initial(initialState)
                .end(endState)
                .states(new HashSet<>(
                        flowModel.getStates().stream()
                                .filter(
                                        state -> !Arrays.asList(EStateType.INITIATED, EStateType.ENDED)
                                                .contains(state.getStateType()))
                                .map(State::getId)
                                .collect(Collectors.toSet())
                )).and();
    }

    void configureTransition(FlowModel flowModel, Builder<String, String> machineBuilder) {

        flowModel.getTransitions()
                .forEach(transition -> {
                    switch (transition.getTransitionType()) {
                        case DEFAULT:
                            configureTransitionUtil(flowModel, machineBuilder, transition.getName(),
                                    transition.getSource(), transition.getTarget(), transition.getEvent(),
                                    null, null);
                            break;
                        case INITIATED, ENDED:
                            configureTransitionUtil(flowModel, machineBuilder, transition.getName(),
                                    transition.getSource(), transition.getTarget(), null,
                                    null, null);
                            break;
                    }
                });
    }

    private void configureTransitionUtil(FlowModel flowModel, Builder<String, String> machineBuilder,
                                         String name, String source, String target, String event,
                                         Guard<String, String> guard, Action<String, String> action) {
        try {
            machineBuilder.configureTransitions()
                    .withExternal()
                    .name(name)
                    .source(source)
                    .target(target)
                    .event(event)
                    .and();
        } catch (Exception e) {
            // ToDo: Exception -> machineBuilder
            logger.error("Configure Transition : FlowModel => {1}, Exception => {}", flowModel, e);
            throw new RuntimeException(e);
        }
    }

    void configureChoice(FlowModel flowModel, Builder<String, String> machineBuilder) {

        flowModel.getStates().stream()
                .filter(state -> state.getStateType().equals(EStateType.CHOICE))
                .forEach(state -> {
                    try {
                        machineBuilder.configureStates().withStates().choice(state.getId());

                        Transition firstChoice = flowModel.getTransitions().stream()
                                .filter(transition -> transition.getSource().equals(state.getId()) && transition.getChoiceType().equals(EChoiceType.FIRST))
                                        .findFirst().orElseThrow();
                        Transition lastChoice = flowModel.getTransitions().stream()
                                .filter(transition -> transition.getSource().equals(state.getId()) && transition.getChoiceType().equals(EChoiceType.LAST))
                                .findFirst().orElseThrow();

                        machineBuilder.configureTransitions()
                                .withChoice()
                                .source(state.getId())
                                .first(firstChoice.getTarget(),
                                        guardUtil.transitionGuard(firstChoice.getGuardExpression()))
                                .last(lastChoice.getTarget());
                    } catch (Exception e) {
                        // ToDo: Exception -> machineBuilder
                        logger.error("Configure Transition : FlowModel => {1}, Exception => {}", flowModel, e);
                        throw new RuntimeException(e);
                    }
                });
    }

}
