package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Strategy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Strategy entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StrategyRepository extends JpaRepository<Strategy, Long> {}
