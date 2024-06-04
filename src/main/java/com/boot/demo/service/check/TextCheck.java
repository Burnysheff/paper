package com.boot.demo.service.check;

import com.boot.demo.model.check.Violation;
import com.boot.demo.model.rules.Rule;
import org.apache.poi.xslf.usermodel.*;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TextCheck {
	public void mapText(XMLSlideShow presentation, List<Rule> rules, List<Violation> violations) {
		Map<Integer, List<Rule>> ruleMap = new HashMap<>();
		Map<Integer, List<XSLFTextRun>> textRunMap = new HashMap<>();
		Map<Integer, List<XSLFTextShape>> shapesMap = new HashMap<>();

		for (int i = 0; i < presentation.getSlides().size(); ++i) {
			for (XSLFShape shape : presentation.getSlides().get(i).getShapes()) {
				if (shape instanceof XSLFTextShape textShape) {
					if (!shapesMap.containsKey(i)) {
						shapesMap.put(i, new ArrayList<>());
					}
					shapesMap.get(i).add(textShape);
					for (XSLFTextParagraph paragraph : textShape.getTextParagraphs()) {
						for (XSLFTextRun textRun : paragraph.getTextRuns()) {
							if (textRun.getRawText() != null) {
								if (!textRunMap.containsKey(i)) {
									textRunMap.put(i, new ArrayList<>());
								}
								textRunMap.get(i).add(textRun);
							}
						}
					}
				}
			}
		}

		for (Rule rule : rules) {
			if (rule.attribute.equals("TEXT") && rule.active) {
				List<Integer> slides = parseString(rule.slides);
				for (int i = 0; i < presentation.getSlides().size(); ++i) {
					if ((!rule.invertSlides && slides.contains(i)) || (rule.invertSlides && !slides.contains(i))) {
						if (!ruleMap.containsKey(i)) {
							ruleMap.put(i, new ArrayList<>());
						}
						ruleMap.get(i).add(rule);
					}
				}
			}
		}

		checkText(textRunMap, shapesMap, ruleMap, violations);
	}

	void checkText(Map<Integer, List<XSLFTextRun>> textRunMap, Map<Integer, List<XSLFTextShape>> shapesMap,  Map<Integer, List<Rule>> ruleMap, List<Violation> violations) {
		for (Integer slide : ruleMap.keySet()) {
			List<Rule> allowed = new ArrayList<>();
			for (Rule rule : ruleMap.get(slide)) {
				if (rule.minQuantity != 0 && rule.minQuantity > shapesMap.get(slide).size()) {
					violations.add(violationConstructor(List.of(rule), "На слайде слишком маленькое количество текстовых элементов", slide + 1));
				}
				if (rule.maxQuantity != 0 && rule.maxQuantity < shapesMap.get(slide).size()) {
					violations.add(violationConstructor(List.of(rule), "На слайде слишком большое количество текстовых элементов", slide + 1));
				}
			}
			for (Rule rule : ruleMap.get(slide)) {
				if (rule.obligatory) {
					checkObligatory(rule, textRunMap.get(slide), shapesMap.get(slide), slide, violations);
				} else {
					allowed.add(rule);
				}
			}
			checkAllowed(allowed, textRunMap.get(slide), shapesMap.get(slide), slide, violations);
		}
	}

	void checkAllowed(List<Rule> rules, List<XSLFTextRun> textRuns, List<XSLFTextShape> shapesMap, Integer slideNumber, List<Violation> violations) {
		if (rules.isEmpty()) {
			return;
		}
		for (XSLFTextRun textRun : textRuns) {
			boolean flagIsAllowed = false;
			StringBuilder description = new StringBuilder();
			for (Rule rule : rules) {
				List<Integer> kaggles = parseString(rule.kaggle);
				List<String> fonts = Arrays.stream(rule.font.split(",")).map(String::trim).map(String::toLowerCase).toList();

				if ((rule.invertPrefix && textRun.getRawText().startsWith(rule.prefix)) || (rule.invertPrefix && textRun.getRawText().startsWith(rule.prefix))) {
					description.append("Не подходит из-за неверного префикса.\n");
					continue;
				}
				if ((rule.invertPostfix && textRun.getRawText().startsWith(rule.postfix)) || (rule.invertPostfix && textRun.getRawText().startsWith(rule.postfix))) {
					description.append("Не подходит из-за неверного постфикса.\n");
					continue;
				}
				if ((rule.invertFont && fonts.contains(textRun.getFontFamily())) || (!rule.invertFont && !fonts.contains(textRun.getFontFamily()))) {
					description.append("Не подходит из-за неверного шрифта.\n");
					continue;
				}
				if ((rule.invertKaggle && kaggles.contains(textRun.getFontSize().intValue())) || (!rule.invertKaggle && !kaggles.contains(textRun.getFontSize().intValue()))) {
					description.append("Не подходит из-за неверного кеггля.\n");
					continue;
				}
				if (!rule.hyperlinks && textRun.getHyperlink() != null) {
					description.append("Не подходит из-за неверного наличия гиперссылки.\n");
					continue;
				}
				if (!rule.allowBold && textRun.isBold()) {
					description.append("Не подходит из-за жирного шрифта.\n");
					continue;
				}
				if (!rule.allowItalic && textRun.isItalic()) {
					description.append("Не подходит из-за курсива.\n");
					continue;
				}
				if (!rule.allowUnderlined && textRun.isUnderlined()) {
					description.append("Не подходит из-за подчеркивания.\n");
					continue;
				}

				for (XSLFTextShape shape : shapesMap) {
					for (XSLFTextParagraph paragraph : shape.getTextParagraphs()) {
						if (paragraph.getTextRuns().contains(textRun)) {
							if (shape.getText().split(" ").length > rule.maxWords || shape.getText().split(" ").length < rule.minWords) {
								description.append("Не подходит из-за количества слов.\n");
								break;
							}
							if (shape.getText().split("[.!?]]").length > rule.maxSentences || shape.getText().split("[.!?]]").length < rule.minSentences) {
								description.append("Не подходит из-за количества предложений.\n");
								break;
							}
							if (shape.getTextParagraphs().size() > rule.maxParagraphs || shape.getTextParagraphs().size() < rule.minParagraphs) {
								description.append("Не подходит из-за количества абзацев.\n");
								break;
							}

							flagIsAllowed = true;
							break;
						}
					}
				}
			}

			if (!flagIsAllowed) {
				violations.add(violationConstructor(rules, "Элемент: \"" + textRun.getRawText() + "\" не подходит из-за:\n" + description, slideNumber + 1));
			}
		}
	}

	void checkObligatory(Rule rule, List<XSLFTextRun> textRuns, List<XSLFTextShape> shapesMap, Integer slideNumber, List<Violation> violations) {
		int accepted = 0;

		List<Integer> kaggles = parseString(rule.kaggle);
		List<String> fonts = Arrays.stream(rule.font.split(",")).map(String::trim).map(String::toLowerCase).toList();

		StringBuilder description = new StringBuilder();

		for (XSLFTextRun textRun : textRuns) {
			if ((rule.invertPrefix && textRun.getRawText().startsWith(rule.prefix)) || (rule.invertPrefix && textRun.getRawText().startsWith(rule.prefix))) {
				description.append("Элемент: \"" + textRun.getRawText() + "\" не подходит из-за неверного префикса.\n");
				continue;
			}
			if ((rule.invertPostfix && textRun.getRawText().startsWith(rule.postfix)) || (rule.invertPostfix && textRun.getRawText().startsWith(rule.postfix))) {
				description.append("Элемент: \"" + textRun.getRawText() + "\" не подходит из-за неверного постфикса.\n");
				continue;
			}
			if ((rule.invertFont && fonts.contains(textRun.getFontFamily())) || (!rule.invertFont && !fonts.contains(textRun.getFontFamily()))) {
				description.append("Элемент: \"" + textRun.getRawText() + "\" не подходит из-за неверного шрифта.\n");
				continue;
			}
			if ((rule.invertKaggle && kaggles.contains(textRun.getFontSize().intValue())) || (!rule.invertKaggle && !kaggles.contains(textRun.getFontSize().intValue()))) {
				description.append("Элемент: \"" + textRun.getRawText() + "\" не подходит из-за неверного кеггля.\n");
				continue;
			}
			if (!rule.hyperlinks && textRun.getHyperlink() != null) {
				description.append("Элемент: \"" + textRun.getRawText() + "\" не подходит из-за наличия гиперссылки.\n");
				continue;
			}
			if (!rule.allowBold && textRun.isBold()) {
				description.append("Элемент: \"" + textRun.getRawText() + "\" не подходит из-за жирного шрифта.\n");
				continue;
			}
			if (!rule.allowItalic && textRun.isItalic()) {
				description.append("Элемент: \"" + textRun.getRawText() + "\" не подходит из-за курсива.\n");
				continue;
			}
			if (!rule.allowUnderlined && textRun.isUnderlined()) {
				description.append("Элемент: \"" + textRun.getRawText() + "\" не подходит из-за подчеркивания.\n");
				continue;
			}

			boolean flagFoundRun = false;
			for (XSLFTextShape shape : shapesMap) {
				for (XSLFTextParagraph paragraph : shape.getTextParagraphs()) {
					if (paragraph.getTextRuns().contains(textRun)) {
						flagFoundRun = true;
						if (shape.getText().split(" ").length > rule.maxWords || shape.getText().split(" ").length < rule.minWords) {
							description.append("Элемент: \"" + shape.getText() + "\" не подходит из-за количества слов.\n");
							break;
						}
						if (shape.getText().split("[.!?]]").length > rule.maxSentences || shape.getText().split("[.!?]]").length < rule.minSentences) {
							description.append("Элемент: \"" + shape.getText() + "\" не подходит из-за количества предложений.\n");
							break;
						}
						if (shape.getTextParagraphs().size() > rule.maxParagraphs || shape.getTextParagraphs().size() < rule.minParagraphs) {
							description.append("Элемент: \"" + shape.getText() + "\" не подходит из-за количества абзацев.\n");
							break;
						}
						++accepted;
						break;
					}
				}
				if (flagFoundRun) {
					break;
				}
			}
		}

		if (accepted == 0) {
			violations.add(violationConstructor(List.of(rule), "Нет обязательного текстового элемента на слайде!\n" + description, slideNumber + 1));
		}
	}

	List<Integer> parseString(String slidesString) {
		List<Integer> numberEntities = new ArrayList<>();

		String[] parts = slidesString.split(",");

		for (String part : parts) {
			String[] smallers;
			if (part.contains("-")) {
				smallers = part.split("-");
				for (int i = Integer.parseInt(smallers[0]); i <= Integer.parseInt(smallers[1]); ++i) {
					numberEntities.add(i);
				}
			} else {
				numberEntities.add(Integer.parseInt(part));
			}
		}

		return numberEntities;
	}

	Violation violationConstructor(List<Rule> rules, String description, Integer slideNumber) {
		Violation violation =  new Violation();

		violation.setRules(rules);
		violation.setDescription(description);
		violation.setSlideNumber(slideNumber);

		return violation;
	}
}
