package com.boot.demo.repo;

import com.boot.demo.model.User;
import com.boot.demo.model.check.Check;
import com.boot.demo.model.rules.Rule;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CheckRepository extends CrudRepository<Check, Long> {
	List<Check> getAllByUser(User user);
	Check getById(Long id);
}
