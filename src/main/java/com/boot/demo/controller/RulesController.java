package com.boot.demo.controller;

import com.boot.demo.dto.RuleForm;
import com.boot.demo.model.rules.Rule;
import com.boot.demo.service.RuleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/rules")
public class RulesController {
	RuleService ruleService;

	public RulesController(RuleService ruleService) {
		this.ruleService = ruleService;
	}

	@GetMapping("")
	public String home(Model model) {
		List<Rule> rules = ruleService.getRules();
		List<Rule> common = ruleService.getCommon();

		model.addAttribute("userRules", rules);
		model.addAttribute("publicRules", common);

		return "rules";
	}

	@GetMapping("/create-rule")
	public String index(Model model) {
		model.addAttribute("type", "presentation");
		return "redirect:/rules/create-rule/presentation";
	}

	@GetMapping("/create-rule/presentation")
	public String presentation(Model model) {
		model.addAttribute("ruleForm", new RuleForm());
		model.addAttribute("type", "presentation");
		return "create-rule";
	}

	@GetMapping("/create-rule/image")
	public String image(Model model) {
		model.addAttribute("ruleForm", new RuleForm());
		model.addAttribute("type", "image");
		return "create-rule";
	}

	@GetMapping("/create-rule/text")
	public String text(Model model) {
		model.addAttribute("ruleForm", new RuleForm());
		model.addAttribute("type", "code");
		return "create-rule";
	}

	@PostMapping("/create-rule/submit/text")
	public String submit(@ModelAttribute RuleForm ruleForm) {
		if (!ruleForm.fontSizes.matches("[1234567890,-]+") && !ruleForm.fontSizes.isEmpty()) {
			return "redirect:/rules/create-rule/text?type=code&errorFonts=true";
		}
		if (!ruleForm.slides.matches("[1234567890,-]+") && !ruleForm.slides.isEmpty()) {
			return "redirect:/rules/create-rule/text?type=code&errorSlides=true";
		}
		ruleForm.attribute = "TEXT";
		ruleService.createRule(ruleForm);
		return "redirect:/rules";
	}

	@PostMapping("/create-rule/submit/image")
	public String submitImage(@ModelAttribute RuleForm ruleForm) {
		if (!ruleForm.slides.matches("[1234567890,-]+") && !ruleForm.slides.isEmpty()) {
			return "redirect:/rules/create-rule/text?type=image&errorSlides=true";
		}
		ruleForm.attribute = "IMAGE";
		ruleService.createRule(ruleForm);
		return "redirect:/rules";
	}

	@PostMapping("/create-rule/submit/presentation")
	public String submitPresentation(@ModelAttribute RuleForm ruleForm) {
		ruleForm.attribute = "PRESENTATION";
		ruleService.createRule(ruleForm);
		return "redirect:/rules";
	}

	@GetMapping("/{id}")
	public String viewRule(@PathVariable Long id, Model model) {
		Rule rule = ruleService.findById(id);
		model.addAttribute("rule", rule);
		return "view-rule";
	}

	@GetMapping("/{id}/edit")
	public String editRuleForm(@PathVariable Long id, Model model) {
		Rule rule = ruleService.findById(id);

		Rule newRule = new Rule();
		RuleService.copyFields(rule, newRule);

		model.addAttribute("rule", newRule);

		return "edit-rule";
	}

	@PostMapping("/{id}/edit")
	public String editRule(@PathVariable Long id, @ModelAttribute Rule rule) {
		Rule original = ruleService.findById(id);

		rule.setAuthor(original.getAuthor());
		rule.setOwner(original.getOwner());
		rule.setAttribute(original.getAttribute());
		rule.setDateCreate(original.getDateCreate());

		ruleService.edit(rule);
		return "redirect:/rules/" + rule.getId();
	}

	@PostMapping("/{id}/delete")
	public String deleteRule(@PathVariable Long id) {
		ruleService.delete(id);
		return "redirect:/rules";
	}
}
