package com.enes.repository;

import com.enes.model.entity.FlowModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlowModelRepository extends JpaRepository<FlowModel, String> {
}
