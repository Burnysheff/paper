package com.boot.demo.service;

import com.boot.demo.dto.RuleForm;
import com.boot.demo.model.User;
import com.boot.demo.model.rules.Rule;
import com.boot.demo.repo.RuleRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.sql.Date;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class RuleService {
	UserService userService;
	RuleRepository ruleRepository;

	public void createRule(RuleForm ruleForm) {
		Rule rule = new Rule();

		rule.setAuthor(((User)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getName());
		rule.setOwner(((User)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId().toString());
		rule.setAttribute(ruleForm.attribute);
		rule.setDateCreate(Date.from(Instant.now()));
		rule.setObligatory(ruleForm.obligatory);
		rule.setActive(ruleForm.active);
		rule.setName(ruleForm.name);
		rule.setDescription(ruleForm.description);
		if (ruleForm.minQuantity != null) {
			rule.setMinQuantity(ruleForm.minQuantity);
		}
		if (ruleForm.maxQuantity != null) {
			rule.setMaxQuantity(ruleForm.maxQuantity);
		}
		if (ruleForm.slides != null) {
			rule.setSlides(ruleForm.slides);
		}
		if (ruleForm.invertSlides != null) {
			rule.setInvertSlides(ruleForm.invertSlides);
		}
		if (ruleForm.prefix != null) {
			rule.setPrefix(ruleForm.prefix);
		}
		if (ruleForm.postfix != null) {
			rule.setPostfix(ruleForm.postfix);
		}
		if (ruleForm.fonts != null) {
			rule.setFont(ruleForm.fonts.toString());
		}
		if (ruleForm.fontSizes != null) {
			rule.setKaggle(ruleForm.fontSizes.toString());
		}
		if (ruleForm.prefixReverse != null) {
			rule.setInvertPrefix(ruleForm.prefixReverse);
		}
		if (ruleForm.postfixReverse != null) {
			rule.setInvertPostfix(ruleForm.postfixReverse);
		}
		if (ruleForm.fontReverse != null) {
			rule.setInvertFont(ruleForm.fontReverse);
		}
		if (ruleForm.sizeReverse != null) {
			rule.setInvertKaggle(ruleForm.sizeReverse);
		}
		if (ruleForm.hyperlinks != null) {
			rule.setHyperlinks(ruleForm.hyperlinks);
		}
		if (ruleForm.underlined != null) {
			rule.setAllowUnderlined(ruleForm.underlined);
		}
		if (ruleForm.italic != null) {
			rule.setAllowItalic(ruleForm.italic);
		}
		if (ruleForm.bold != null) {
			rule.setAllowBold(ruleForm.bold);
		}
		if (ruleForm.maxWords != null) {
			rule.setMaxWords(ruleForm.maxWords);
		}
		if (ruleForm.maxSentences != null) {
			rule.setMaxSentences(ruleForm.maxSentences);
		}
		if (ruleForm.maxParagraphs != null) {
			rule.setMaxParagraphs(ruleForm.maxParagraphs);
		}
		if (ruleForm.minWords != null) {
			rule.setMinWords(ruleForm.minWords);
		}
		if (ruleForm.minSentences != null) {
			rule.setMinSentences(ruleForm.minSentences);
		}
		if (ruleForm.minParagraphs != null) {
			rule.setMinParagraphs(ruleForm.minParagraphs);
		}

		rule.setSameBackground(ruleForm.sameBackground);
		rule.setMinSlides(ruleForm.minSlides);
		rule.setMaxSlides(ruleForm.maxSlides);
		rule.setAllowText(ruleForm.allowText);
		rule.setAllowImages(ruleForm.allowImages);
		rule.setAllowVideo(ruleForm.allowVideo);
		rule.setAllowCharts(ruleForm.allowCharts);
		rule.setAllowTables(ruleForm.allowTables);
		rule.setMinSlideElements(ruleForm.maxSlideElements);
		rule.setMaxSlideElements(ruleForm.minSlideElements);

		rule.setMinHeight(ruleForm.minHeight);
		rule.setMaxHeight(ruleForm.maxHeight);
		rule.setMinWidth(ruleForm.minWidth);
		rule.setMaxWidth(ruleForm.maxWidth);

		ruleRepository.save(rule);
	}

	public List<Rule> getRulesForUser() {
		System.out.println();
		return ruleRepository.findAllByOwnerAndActive(((User)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId().toString(), true);
	}

	public List<Rule> getRules() {
		List<Rule> rules = new ArrayList<>();
		ruleRepository.findAll().forEach(rules::add);

		return rules;
	}

	public List<Rule> getCommon() {
		return ruleRepository.findAllByOwner("common");
	}

	public RuleService(RuleRepository ruleRepository, UserService userService) {
		this.ruleRepository = ruleRepository;
		this.userService = userService;
	}

	public Rule findById(Long id) {
		return ruleRepository.findById(id).get();
	}

	public void edit(Rule rule) {
		ruleRepository.save(rule);
	}

	public void delete(Long id) {
		ruleRepository.delete(this.findById(id));
	}

	public static void copyFields(Rule source, Rule target) {
		Field[] fields = Rule.class.getDeclaredFields();
		for (Field field : fields) {
			try {
				field.setAccessible(true);
				field.set(target, field.get(source));
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}

}
