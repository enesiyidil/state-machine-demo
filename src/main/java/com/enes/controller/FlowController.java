package com.enes.controller;

import com.enes.model.dto.request.SendEventRequestDto;
import com.enes.model.dto.request.StartFlowRequestDto;
import com.enes.model.entity.Flow;
import com.enes.service.FlowService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/flows")
@RequiredArgsConstructor
@Tag(name="Flow Controller API", description = "Flow Controller")
public class FlowController {

    private final FlowService service;

    @PostMapping("/start-flow")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Start flow", description = "Start flow by StartFlowRequestDto",
            responses = {
                    @ApiResponse(responseCode = "200", description = "",
                            content = @Content(schema = @Schema(implementation = Flow.class)))
            })
    public ResponseEntity<Flow> startFlow(@RequestBody StartFlowRequestDto dto) throws Exception {

        return ResponseEntity.ok(service.startFlow(dto));
    }

    @PostMapping("/send-event")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Send event", description = "Send event by SendEventRequestDto",
            responses = {
                    @ApiResponse(responseCode = "200", description = "",
                            content = @Content(schema = @Schema(implementation = Flow.class)))
            })
    public ResponseEntity<Flow> sendEvent(@RequestBody SendEventRequestDto dto) throws Exception {

        return ResponseEntity.ok(service.sendEvent(dto));
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get flow info", description = "Get flow info by id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "",
                            content = @Content(schema = @Schema(implementation = Flow.class)))
            })
    public ResponseEntity<Flow> getFlow(@PathVariable("id") String id) {

        return ResponseEntity.ok(service.getFlow(id));
    }
}
