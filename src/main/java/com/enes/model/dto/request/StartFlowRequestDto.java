package com.enes.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StartFlowRequestDto {

    private String flowModelId;

    private String initiatorId;

    private String notification;

    private Map<String, String> parameters;
}
