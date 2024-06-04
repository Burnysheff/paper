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

		rule.setAttribute(ruleForm.attribute);
		rule.setAuthor(((User)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getName());
		rule.setOwner(((User)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId().toString());
		rule.setDateCreate(Date.from(Instant.now()));
		rule.setObligatory(ruleForm.obligatory);
		rule.setActive(ruleForm.active);
		rule.setName(ruleForm.name);
		rule.setDescription(ruleForm.description);
		rule.setMinQuantity(ruleForm.minQuantity);
		rule.setMaxQuantity(ruleForm.maxQuantity);
		rule.setSlides(ruleForm.slides);
		rule.setInvertSlides(ruleForm.invertSlides);
		rule.setPrefix(ruleForm.prefix);
		rule.setPostfix(ruleForm.postfix);
		rule.setFont(ruleForm.fonts);
		rule.setKaggle(ruleForm.fontSizes);
		rule.setInvertPrefix(ruleForm.prefixReverse);
		rule.setInvertPostfix(ruleForm.postfixReverse);
		rule.setInvertFont(ruleForm.fontReverse);
		rule.setInvertKaggle(ruleForm.sizeReverse);
		rule.setHyperlinks(ruleForm.hyperlinks);
		rule.setAllowUnderlined(ruleForm.underlined);
		rule.setAllowItalic(ruleForm.italic);
		rule.setAllowBold(ruleForm.bold);
		rule.setMaxWords(ruleForm.maxWords);
		rule.setMaxSentences(ruleForm.maxSentences);
		rule.setMaxParagraphs(ruleForm.maxParagraphs);
		rule.setMinWords(ruleForm.minWords);
		rule.setMinSentences(ruleForm.minSentences);
		rule.setMinParagraphs(ruleForm.minParagraphs);
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

	public void edit(Rule original, Rule rule) {
		// String поля
		if (rule.getAttribute() == null && original.getAttribute() != null) {
			rule.setAttribute(original.getAttribute());
		}
		if (rule.getName() == null && original.getName() != null) {
			rule.setName(original.getName());
		}
		if (rule.getAuthor() == null && original.getAuthor() != null) {
			rule.setAuthor(original.getAuthor());
		}
		if (rule.getOwner() == null && original.getOwner() != null) {
			rule.setOwner(original.getOwner());
		}
		if (rule.getDescription() == null && original.getDescription() != null) {
			rule.setDescription(original.getDescription());
		}
		if (rule.getSlides() == null && original.getSlides() != null) {
			rule.setSlides(original.getSlides());
		}
		if (rule.getPrefix() == null && original.getPrefix() != null) {
			rule.setPrefix(original.getPrefix());
		}
		if (rule.getPostfix() == null && original.getPostfix() != null) {
			rule.setPostfix(original.getPostfix());
		}
		if (rule.getFont() == null && original.getFont() != null) {
			rule.setFont(original.getFont());
		}
		if (rule.getKaggle() == null && original.getKaggle() != null) {
			rule.setKaggle(original.getKaggle());
		}

// Integer поля
		if (rule.getMinSlides() == null && original.getMinSlides() != null) {
			rule.setMinSlides(original.getMinSlides());
		}
		if (rule.getMaxSlides() == null && original.getMaxSlides() != null) {
			rule.setMaxSlides(original.getMaxSlides());
		}
		if (rule.getMaxSlideElements() == null && original.getMaxSlideElements() != null) {
			rule.setMaxSlideElements(original.getMaxSlideElements());
		}
		if (rule.getMinSlideElements() == null && original.getMinSlideElements() != null) {
			rule.setMinSlideElements(original.getMinSlideElements());
		}
		if (rule.getMinQuantity() == null && original.getMinQuantity() != null) {
			rule.setMinQuantity(original.getMinQuantity());
		}
		if (rule.getMaxQuantity() == null && original.getMaxQuantity() != null) {
			rule.setMaxQuantity(original.getMaxQuantity());
		}
		if (rule.getMaxWords() == null && original.getMaxWords() != null) {
			rule.setMaxWords(original.getMaxWords());
		}
		if (rule.getMaxSentences() == null && original.getMaxSentences() != null) {
			rule.setMaxSentences(original.getMaxSentences());
		}
		if (rule.getMaxParagraphs() == null && original.getMaxParagraphs() != null) {
			rule.setMaxParagraphs(original.getMaxParagraphs());
		}
		if (rule.getMinWords() == null && original.getMinWords() != null) {
			rule.setMinWords(original.getMinWords());
		}
		if (rule.getMinSentences() == null && original.getMinSentences() != null) {
			rule.setMinSentences(original.getMinSentences());
		}
		if (rule.getMinParagraphs() == null && original.getMinParagraphs() != null) {
			rule.setMinParagraphs(original.getMinParagraphs());
		}
		if (rule.getMinHeight() == null && original.getMinHeight() != null) {
			rule.setMinHeight(original.getMinHeight());
		}
		if (rule.getMinWidth() == null && original.getMinWidth() != null) {
			rule.setMinWidth(original.getMinWidth());
		}
		if (rule.getMaxHeight() == null && original.getMaxHeight() != null) {
			rule.setMaxHeight(original.getMaxHeight());
		}
		if (rule.getMaxWidth() == null && original.getMaxWidth() != null) {
			rule.setMaxWidth(original.getMaxWidth());
		}


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
