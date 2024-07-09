package com.enes.controller;

import com.enes.model.dto.request.FlowModelSaveRequestDto;
import com.enes.model.dto.response.FlowModelResponseDto;
import com.enes.service.FlowModelService;
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
@RequestMapping("/flow-models")
@RequiredArgsConstructor
@Tag(name="Flow Model Controller API", description = "Flow Model Controller")
public class FlowModelController {

    private final FlowModelService service;

    @PostMapping()
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Save flow model", description = "Save flow model by FlowModelSaveRequestDto",
            responses = {
                    @ApiResponse(responseCode = "200", description = "",
                            content = @Content(schema = @Schema(implementation = String.class)))
            })
    public ResponseEntity<String> save(@RequestBody FlowModelSaveRequestDto dto){

        return ResponseEntity.ok(service.save(dto));
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get flow model info", description = "Get flow model info by id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "",
                            content = @Content(schema = @Schema(implementation = FlowModelResponseDto.class)))
            })
    public ResponseEntity<FlowModelResponseDto> findById(@PathVariable String id) {

        return ResponseEntity.ok(service.findById(id));
    }
}
