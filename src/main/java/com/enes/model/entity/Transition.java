package com.enes.model.entity;

import com.enes.model.enums.EChoiceType;
import com.enes.model.enums.ETransitionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "my_transition")
public class Transition {

    @Id
    private String id;

    private String name;

    private String source;

    private String target;

    private String event;

    private String actionExpression;

    private String guardExpression;

    private EChoiceType choiceType;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private ETransitionType transitionType = ETransitionType.DEFAULT;
}
