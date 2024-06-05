package com.boot.demo.controller;

import com.boot.demo.model.User;
import com.boot.demo.model.check.Check;
import com.boot.demo.model.rules.Rule;
import com.boot.demo.service.CheckService;
import com.boot.demo.service.RuleService;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller
public class HomeController {
	RuleService ruleService;
	CheckService checkService;

	public HomeController(RuleService ruleService, CheckService checkService) {
		this.ruleService = ruleService;
		this.checkService = checkService;
	}

	@GetMapping("/home")
	public String home() {
		return "home";
	}

	@GetMapping("/user")
	public String user(Model model) {
		model.addAttribute("user", SecurityContextHolder.getContext().getAuthentication().getPrincipal());
		model.addAttribute("checks", checkService.getUserChecks((User)SecurityContextHolder.getContext().getAuthentication().getPrincipal()));

		System.out.println(checkService.getUserChecks((User)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).size());
		return "user";
	}

	@GetMapping("/check")
	public String check(Model model) {
		Check check = checkService.getUserChecksLatest((User)SecurityContextHolder.getContext().getAuthentication().getPrincipal());
		model.addAttribute("check", checkService.getUserChecksLatest((User)SecurityContextHolder.getContext().getAuthentication().getPrincipal()));
		return "violations";
	}

	@GetMapping("/check/{id}")
	public String checkId(@PathVariable Long id,  Model model) {
		model.addAttribute("check", checkService.getCheckById(id));
		return "violations";
	}

	@PostMapping("/upload")
	public String uploadFile(@RequestParam("file") MultipartFile file, Model model) throws IOException {
		if (!file.getContentType().endsWith("openxmlformats-officedocument.presentationml.presentation")) {
			return "redirect:/home?error=true";
		}

		ZipSecureFile.setMinInflateRatio(0);

		File convFile = new File( "/Users/nick/data/" + file.getName());
		file.transferTo(convFile);

		List<Rule> rules = ruleService.getRulesForUser();

		Check check = checkService.checkRules(convFile, rules);

		model.addAttribute("check", check);

		return "violations";
	}
}
