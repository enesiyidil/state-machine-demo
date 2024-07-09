package com.enes.model.entity;

import com.enes.model.enums.EStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class FlowModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String flowName;

    private String companyId;

    private String companyDepartment;

    private String editorId;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<State> states;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Transition> transitions;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private EStatus status = EStatus.ACTIVE;
}
