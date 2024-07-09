package com.enes.service;

import com.enes.model.dto.request.SendEventRequestDto;
import com.enes.model.dto.request.StartFlowRequestDto;
import com.enes.model.entity.Flow;
import com.enes.repository.FlowRepository;
import com.enes.utility.StateMachineUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

@Service
public class FlowService {

    private final FlowRepository repository;

    private final StateMachineUtil stateMachineUtil;

    private final Logger logger = LoggerFactory.getLogger(FlowModelService.class);

    public FlowService(FlowRepository repository,
                       StateMachineUtil stateMachineUtil) {
        this.repository = repository;
        this.stateMachineUtil = stateMachineUtil;
    }

    public Flow startFlow(StartFlowRequestDto dto) {

        Flow flow = Flow.builder()
                .flowModelId(dto.getFlowModelId())
                .initiatorId(dto.getInitiatorId())
                .notification(dto.getNotification())
                .machineId(UUID.randomUUID().toString())
                .parameters(dto.getParameters())
                .build();
        logger.info("Start Flow => {}", dto);

        repository.save(flow);
        StateMachine<String, String> stateMachine = stateMachineUtil.getStateMachine(flow);
        stateMachine.startReactively().block();
        flow.setCurrentState(stateMachine.getState().getId());
        flow.setIsFlowCompleted(stateMachine.isComplete());
        return repository.save(flow);
    }

    public Flow sendEvent(SendEventRequestDto dto) {

        Flow flow = findById(dto.getFlowId());
        logger.info("Send Event => {}", dto);

        StateMachine<String, String> stateMachine = stateMachineUtil.getStateMachine(flow);
        stateMachine.startReactively().block();
        stateMachine.sendEvent(Mono.just(MessageBuilder.withPayload(dto.getEvent()).build())).blockLast();
        if (!flow.getCurrentState().equals(stateMachine.getState().getId())) {
            flow.setCurrentState(stateMachine.getState().getId());
            flow.setLastEvent(dto.getEvent());
            flow.setLastEventDate(LocalDateTime.now());
            if (dto.getNotification() != null) {
                if (flow.getEventNotifications() == null) {
                    flow.setEventNotifications(new HashMap<>());
                }
                flow.getEventNotifications()
                        .put(flow.getCurrentState() + "." + dto.getEvent(), dto.getNotification());
            }
        }
        flow.setIsFlowCompleted(stateMachine.isComplete());
        return repository.save(flow);
    }

    public Flow getFlow(String id) {

        logger.info("Get Flow => {}", id);
        return findById(id);
    }

    private Flow findById(String flowId) {

        Optional<Flow> optionalFlow = repository.findById(flowId);
        if (optionalFlow.isPresent()) {
            return optionalFlow.get();
        }
        // ToDo: Exception -> Flow Not Found
        logger.error("Flow Not Found : Flow ID => {1}, Exception => {}", flowId);
        throw new RuntimeException();
    }

    public Flow getFlowByMachineId(String machineId) {

        Optional<Flow> optionalFlow = repository.findByMachineId(machineId);
        if (optionalFlow.isPresent()) {
            return optionalFlow.get();
        }
        // ToDo: Exception -> Flow Not Found
        logger.error("Flow Not Found : Flow Machine ID => {1}, Exception => {}", machineId);
        throw new RuntimeException();
    }
}
