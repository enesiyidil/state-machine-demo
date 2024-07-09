package com.enes.service;

import com.enes.mapper.FlowModelMapper;
import com.enes.model.dto.request.FlowModelSaveRequestDto;
import com.enes.model.dto.response.FlowModelResponseDto;
import com.enes.model.entity.FlowModel;
import com.enes.repository.FlowModelRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FlowModelService {

    private final FlowModelRepository repository;

    private final FlowModelMapper mapper;

    private final Logger logger = LoggerFactory.getLogger(FlowModelService.class);

    public FlowModelService(FlowModelRepository repository,
                            FlowModelMapper mapper) {

        this.repository = repository;
        this.mapper = mapper;
    }

    public String save(FlowModelSaveRequestDto dto) {

        FlowModel flowModel = repository.save(mapper.dtoToEntity(dto));
        logger.info("FlowModel save. Model => {}", flowModel);
        return flowModel.getId();
    }

    public FlowModelResponseDto findById(String id) {

        Optional<FlowModel> optionalFlowModel = repository.findById(id);
        if (optionalFlowModel.isPresent()) {
            logger.info("FlowModel findById. Model => {}", optionalFlowModel.get());
            return mapper.entityToResponseDto(optionalFlowModel.get());
        }
        throw new RuntimeException();
    }

    public FlowModel getModel(String id) {
        return repository.findById(id).orElse(null);
    }
}
