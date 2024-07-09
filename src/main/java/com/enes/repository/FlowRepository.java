package com.enes.repository;

import com.enes.model.entity.Flow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FlowRepository extends JpaRepository<Flow, String> {
    Optional<Flow> findByMachineId(String machineId);
}
