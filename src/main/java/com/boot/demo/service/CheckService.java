package com.boot.demo.service;

import com.boot.demo.model.User;
import com.boot.demo.model.check.Check;
import com.boot.demo.model.check.Violation;
import com.boot.demo.model.rules.Rule;
import com.boot.demo.repo.CheckRepository;
import com.boot.demo.repo.ViolationRepository;
import com.boot.demo.service.check.PresentationCheck;
import com.boot.demo.service.check.TextCheck;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CheckService {
	CheckRepository checkRepository;
	ViolationRepository violationRepository;

	public CheckService(CheckRepository checkRepository, ViolationRepository violationRepository) {
		this.checkRepository = checkRepository;
		this.violationRepository = violationRepository;
	}

	public Check checkRules(File file, List<Rule> rules) throws IOException {
		if (rules.isEmpty()) {
			return null;
		}
		XMLSlideShow ppt;
		FileInputStream inputstream = new FileInputStream(file);
		ppt = new XMLSlideShow(inputstream);

		Check check = new Check();
		List<Violation> violations = new ArrayList<>();

		TextCheck textCheck = new TextCheck();
		PresentationCheck presentationCheck = new PresentationCheck();

		presentationCheck.check(ppt, rules, violations);
		textCheck.mapText(ppt, rules, violations);

		violationRepository.saveAll(violations);
		check.setViolationList(violations);
		check.setRulesNumber((long) rules.size());
		check.setStatus(violations.isEmpty());
		check.setUser((User)SecurityContextHolder.getContext().getAuthentication().getPrincipal());
		check.setDate(Date.from(Instant.now()));
		checkRepository.save(check);

		return check;
	}

	public void deleteViolationRule(Rule rule) {
		List<Violation> violations = new ArrayList<>();
		violationRepository.findAll().forEach(violations::add);

		List<Violation> vioRule = new ArrayList<>();
		for (Violation violation : violations) {
			if (violation.getRules().contains(rule)) {
				vioRule.add(violation);
			}
		}

		for (Violation violation : vioRule) {
			violations.remove(violation);
			violationRepository.delete(violation);
		}
	}

	public List<Check> getUserChecks(User user) {
		return checkRepository.getAllByUser(user);
	}

	public Check getCheckById(Long id) {
		return checkRepository.getById(id);
	}

	public Check getUserChecksLatest(User user) {
		List<Check> checks = checkRepository.getAllByUser(user);

		Instant min = Instant.MIN;
		Check latest = null;
		for (Check check : checks) {
			if (check.getDate().toInstant().isAfter(min)) {
				min = check.getDate().toInstant();
				latest = check;
			}
		}

		return latest;
	}
}
