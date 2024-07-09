package com.enes.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Flow {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String machineId;

    private String flowModelId;

    private String initiatorId;

    private String notification;

    @Builder.Default
    private LocalDateTime createdDate = LocalDateTime.now();

    private String currentState;

    @Builder.Default
    private Boolean isFlowCompleted = false;

    private String lastEvent;

    private LocalDateTime lastEventDate;

    @ElementCollection
    private Map<String, String> eventNotifications = new HashMap<>();

    @ElementCollection
    private Map<String, String> parameters;
}
