package com.boot.demo.repo;

import com.boot.demo.model.User;
import com.boot.demo.model.rules.Rule;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RuleRepository extends CrudRepository<Rule, Long> {
	List<Rule> findAllByOwnerAndActive(String owner, boolean active);
	List<Rule> findAllByOwner(String owner);
}
