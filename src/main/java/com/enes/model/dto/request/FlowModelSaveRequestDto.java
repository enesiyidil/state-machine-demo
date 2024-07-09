package com.enes.model.dto.request;

import com.enes.model.entity.State;
import com.enes.model.entity.Transition;
import com.enes.model.enums.EStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FlowModelSaveRequestDto {

    private String flowName;

    private String companyId;

    private String companyDepartment;

    private String editorId;

    private List<State> states;

    private List<Transition> transitions;
}
