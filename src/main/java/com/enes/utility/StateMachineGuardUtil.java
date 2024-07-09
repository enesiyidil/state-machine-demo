package com.enes.utility;

import com.enes.model.entity.Flow;
import com.enes.service.FlowService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.statemachine.guard.Guard;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static com.enes.constant.Regex.*;

@Service
public class StateMachineGuardUtil {

    private FlowService flowService;

    private final StringUtil stringUtil;

    ExpressionParser parser = new SpelExpressionParser();

    private final Logger logger = LoggerFactory.getLogger(StateMachineGuardUtil.class);

    public StateMachineGuardUtil(StringUtil stringUtil) {
        this.stringUtil = stringUtil;
    }

    @Autowired
    public void setFlowService(@Lazy FlowService flowService) {
        this.flowService = flowService;
    }

    public Guard<String, String> transitionGuard(String guardExpression) {

        logger.info("Transition Guard, Expression => {}", guardExpression);
        return ctx -> {
            Flow flow = flowService.getFlowByMachineId(ctx.getStateMachine().getId());
            return setTransitionCondition(flow, guardExpression);
        };
    }

    boolean setTransitionCondition(Flow flow, String guardExpression) {

        List<String> fieldPlaceHolders = stringUtil.getPlaceHolders(guardExpression,
                PATTERN_FIND_STRING_BETWEEN_AT);
        AtomicReference<String> replacedExp = new AtomicReference<>(guardExpression);
        fieldPlaceHolders.forEach(placeHold -> {
            String fieldValue = flow.getParameters().get(placeHold);
            if (fieldValue.contains("$")) {
                List<String> valuePlaceHolders = stringUtil.getPlaceHolders(fieldValue,
                        PATTERN_FIND_DOLLAR_AMOUNT);
                Double fieldDoubleValue = parser.parseExpression(
                        "T(Double).parseDouble('" + valuePlaceHolders.get(0) + "')").getValue(
                        Double.class);
                replacedExp.set(replacedExp.get().replace("@" + placeHold + "@", fieldDoubleValue + ""));
            } else {
                replacedExp.set(replacedExp.get().replace("@" + placeHold + "@", "'" + fieldValue + "'"));
            }
        });
        return Boolean.TRUE.equals(
                parser.parseExpression(replacedExp.get()).getValue(Boolean.class));
    }
}
