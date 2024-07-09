package com.enes.model.entity;

import com.enes.model.enums.EStateType;
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
@Table(name = "my_state")
public class State {

    @Id
    private String id;

    private String name;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private EStateType stateType = EStateType.DEFAULT;
}
