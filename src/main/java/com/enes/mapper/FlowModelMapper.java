package com.enes.mapper;

import com.enes.model.dto.request.FlowModelSaveRequestDto;
import com.enes.model.dto.response.FlowModelResponseDto;
import com.enes.model.entity.FlowModel;
import org.springframework.stereotype.Service;

@Service
public class FlowModelMapper {

    public FlowModel dtoToEntity(FlowModelSaveRequestDto dto) {

        return FlowModel.builder()
                .flowName(dto.getFlowName())
                .companyId(dto.getCompanyId())
                .companyDepartment(dto.getCompanyDepartment())
                .editorId(dto.getEditorId())
                .states(dto.getStates())
                .transitions(dto.getTransitions())
                .build();
    }

    public FlowModelResponseDto entityToResponseDto(FlowModel flowModel) {

        return FlowModelResponseDto.builder()
                .flowName(flowModel.getFlowName())
                .companyId(flowModel.getCompanyId())
                .companyDepartment(flowModel.getCompanyDepartment())
                .editorId(flowModel.getEditorId())
                .states(flowModel.getStates())
                .transitions(flowModel.getTransitions())
                .createdAt(flowModel.getCreatedAt())
                .updatedAt(flowModel.getUpdatedAt())
                .status(flowModel.getStatus())
                .build();
    }
}
