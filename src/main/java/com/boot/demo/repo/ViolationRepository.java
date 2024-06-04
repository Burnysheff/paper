package com.boot.demo.repo;

import com.boot.demo.model.check.Violation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ViolationRepository extends CrudRepository<Violation, Long> {
}
