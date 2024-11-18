package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.ExecutionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ExecutionType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExecutionTypeRepository extends JpaRepository<ExecutionType, Long> {}
