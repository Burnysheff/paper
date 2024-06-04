package com.boot.demo.controller;

import com.boot.demo.dto.RuleForm;
import com.boot.demo.model.User;
import com.boot.demo.model.rules.Rule;
import com.boot.demo.service.RuleService;
import org.springframework.security.core.context.SecurityContextHolder;
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
		List<Rule> rules = ruleService.getRulesUser();
		List<Rule> common = ruleService.getRulesNotUser();

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
		if (ruleForm.slides != null && !ruleForm.slides.matches("[1234567890,-]+") && !ruleForm.slides.isEmpty()) {
			return "redirect:/rules/create-rule/text?type=code&errorSlides=true";
		}
		if (ruleForm.minQuantity != null && ruleForm.maxQuantity != null && ruleForm.minQuantity > ruleForm.maxQuantity) {
			return "redirect:/rules/create-rule/text?type=code&errorQuantity=true";
		}
		if (ruleForm.maxWords != null && ruleForm.minWords != null && ruleForm.minWords > ruleForm.maxWords) {
			return "redirect:/rules/create-rule/text?type=code&errorWords=true";
		}
		if (ruleForm.maxSentences != null && ruleForm.minSentences != null && ruleForm.minSentences > ruleForm.maxSentences) {
			return "redirect:/rules/create-rule/text?type=code&errorSent=true";
		}
		if (ruleForm.maxParagraphs != null && ruleForm.minParagraphs != null && ruleForm.minParagraphs > ruleForm.maxParagraphs) {
			return "redirect:/rules/create-rule/text?type=code&errorPar=true";
		}
		ruleForm.attribute = "TEXT";
		ruleService.createRule(ruleForm);
		return "redirect:/rules";
	}

	@PostMapping("/create-rule/submit/image")
	public String submitImage(@ModelAttribute RuleForm ruleForm) {
		if (ruleForm.slides != null && !ruleForm.slides.matches("[1234567890,-]+") && !ruleForm.slides.isEmpty()) {
			return "redirect:/rules/create-rule/text?type=image&errorSlides=true";
		}
		if (ruleForm.minQuantity != null && ruleForm.maxQuantity != null && ruleForm.minQuantity > ruleForm.maxQuantity) {
			return "redirect:/rules/create-rule/text?type=image&errorQuantityImage=true";
		}
		if (ruleForm.minHeight != null && ruleForm.maxHeight != null && ruleForm.minHeight > ruleForm.maxHeight) {
			return "redirect:/rules/create-rule/text?type=image&errorHeight=true";
		}
		if (ruleForm.minWidth!= null && ruleForm.maxWidth != null && ruleForm.minWidth > ruleForm.maxWidth) {
			return "redirect:/rules/create-rule/text?type=image&errorWidth=true";
		}
		ruleForm.attribute = "IMAGE";
		ruleService.createRule(ruleForm);
		return "redirect:/rules";
	}

	@PostMapping("/create-rule/submit/presentation")
	public String submitPresentation(@ModelAttribute RuleForm ruleForm) {
		if (ruleForm.minSlides != null && ruleForm.maxSlides != null && ruleForm.minSlides > ruleForm.maxSlides) {
			return "redirect:/rules/create-rule/text?type=presentation&Slides=true";
		}
		if (ruleForm.minSlideElements != null && ruleForm.maxSlideElements != null && ruleForm.minSlideElements > ruleForm.maxSlideElements) {
			return "redirect:/rules/create-rule/text?type=presentation&errorQuantitySlides=true";
		}
		ruleForm.attribute = "PRESENTATION";
		ruleService.createRule(ruleForm);
		return "redirect:/rules";
	}

	@PostMapping("/add/{id}")
	public String addRule(@PathVariable Long id) {
		Rule rule = ruleService.findById(id);

		Rule newRule = new Rule();

		System.out.println(rule.getId());
		RuleService.copyFields(rule, newRule);

		newRule.setOwner(((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId().toString());

		System.out.println(rule.getId());
		ruleService.saveRule(newRule);

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
		if (rule.minSlides != null && rule.maxSlides != null && rule.minSlides > rule.maxSlides) {
			return "redirect:/rules/" + id + "/edit?minSlides=true";
		}
		if (rule.minSlideElements != null && rule.maxSlideElements != null && rule.minSlideElements > rule.maxSlideElements) {
			return "redirect:/rules/" + id + "/edit?slideElements=true";
		}
		if (rule.kaggle != null && !rule.kaggle.matches("[1234567890,-]+") && !rule.kaggle.isEmpty()) {
			return "redirect:/rules/" + id + "/kaggle=true";
		}
		if (rule.maxWords != null && rule.minWords != null && rule.minWords > rule.maxWords) {
			return "redirect:/rules/" + id + "/minWords=true";
		}
		if (rule.maxSentences != null && rule.minSentences != null && rule.minSentences > rule.maxSentences) {
			return "redirect:/rules/" + id + "/minSent=true";
		}
		if (rule.maxParagraphs != null && rule.minParagraphs != null && rule.minParagraphs > rule.maxParagraphs) {
			return "redirect:/rules/" + id + "/edit?minPara=true";
		}
		if (rule.slides != null && !rule.slides.matches("[1234567890,-]+") && !rule.slides.isEmpty()) {
			return "redirect:/rules/" + id + "/edit?Slide=true";
		}
		if (rule.minQuantity != null && rule.maxQuantity != null && rule.minQuantity > rule.maxQuantity) {
			return "redirect:/rules/" + id + "/edit?minQuantity=true";
		}
		if (rule.minHeight != null && rule.maxHeight != null && rule.minHeight > rule.maxHeight) {
			return "redirect:/rules/" + id + "/edit?minHeight=true";
		}
		if (rule.minWidth!= null && rule.maxWidth != null && rule.minWidth > rule.maxWidth) {
			return "redirect:/rules/" + id + "/edit?minWidth=true";
		}


		Rule original = ruleService.findById(id);

		ruleService.edit(original, rule);
		return "redirect:/rules/" + rule.getId();
	}

	@PostMapping("/{id}/delete")
	public String deleteRule(@PathVariable Long id) {
		ruleService.delete(id);
		return "redirect:/rules";
	}
}
