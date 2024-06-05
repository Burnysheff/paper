package com.boot.demo.service.check;

import com.boot.demo.model.check.Violation;
import com.boot.demo.model.rules.Rule;
import org.apache.poi.xslf.usermodel.*;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.IntStream;

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
				List<Integer> slides;
				if (rule.slides != null && !rule.slides.trim().equals("")) {
					slides = parseString(rule.slides);
				} else {
					slides = IntStream.rangeClosed(0, presentation.getSlides().size() - 1).boxed().toList();
				}
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
				if (rule.minQuantity != null && rule.minQuantity > shapesMap.get(slide).size()) {
					violations.add(violationConstructor(List.of(rule), "На слайде слишком маленькое количество текстовых элементов", slide + 1));
				}
				if (rule.maxQuantity != null && rule.maxQuantity < shapesMap.get(slide).size()) {
					violations.add(violationConstructor(List.of(rule), "На слайде слишком большое количество текстовых элементов", slide + 1));
				}
			}
			for (Rule rule : ruleMap.get(slide)) {
				if (rule.obligatory) {
					checkObligatory(rule, shapesMap.get(slide), slide, violations);
				} else {
					allowed.add(rule);
				}
			}
			checkAllowed(allowed, textRunMap.get(slide), shapesMap.get(slide), slide, violations);
		}
	}

	void checkAllowed(List<Rule> rules, List<XSLFTextRun> textRuns, List<XSLFTextShape> shapesMap, Integer slideNumber, List<Violation> violations) {
		if (rules == null || rules.isEmpty()) {
			return;
		}

		for (XSLFTextShape shape : shapesMap) {
			boolean flagIsAllowed = false;
			StringBuilder description = new StringBuilder();

			for (Rule rule : rules) {
				if (rule.prefix != null) {
					if ((!rule.invertPrefix && !shape.getText().startsWith(rule.prefix)) || (rule.invertPrefix && shape.getText().startsWith(rule.prefix))) {
						description.append("Нарушает правила из-за неверного префикса.\n");
						continue;
					}
				}
				if (rule.postfix != null) {
					if ((!rule.invertPostfix && !shape.getText().startsWith(rule.postfix)) || (rule.invertPostfix && shape.getText().startsWith(rule.postfix))) {
						description.append("Нарушает правила из-за неверного постфикса.\n");
						continue;
					}
				}
				if (rule.maxWords != null) {
					if (shape.getText().split(" ").length > rule.maxWords) {
						description.append("Нарушает правила из-за количества слов.\n");
						break;
					}
				}
				if (rule.minWords != null) {
					if (shape.getText().split(" ").length < rule.minWords) {
						description.append("Нарушает правила из-за количества слов.\n");
						break;
					}
				}
				if (rule.maxSentences != null) {
					if (shape.getText().split("[.!?]]").length > rule.maxSentences) {
						description.append("Нарушает правила из-за количества предложений.\n");
						break;
					}
				}
				if (rule.minSentences != null) {
					if (shape.getText().split("[.!?]]").length < rule.minSentences) {
						description.append("Нарушает правила из-за количества слов.\n");
						break;
					}
				}
				if (rule.maxParagraphs != null) {
					if (shape.getTextParagraphs().size() > rule.maxParagraphs) {
						description.append("Нарушает правила из-за количества абзацев.\n");
						break;
					}
				}
				if (rule.minParagraphs != null) {
					if (shape.getTextParagraphs().size() < rule.minParagraphs) {
						description.append("Нарушает правила из-за количества абзацев.\n");
						break;
					}
				}
				flagIsAllowed = true;
			}

			if (!flagIsAllowed) {
				String errorText = shape.getText().length() > 25 ? shape.getText().substring(0, 23) + "..." : shape.getText();

				violations.add(violationConstructor(rules, "Элемент: \"" + errorText + "\"\n" + description, slideNumber));
				return;
			}
		}

		for (XSLFTextRun textRun : textRuns) {
			boolean flagIsAllowed = false;
			StringBuilder description = new StringBuilder();

			for (Rule rule : rules) {
				if (rule.font != null && !rule.font.trim().equals("")) {
					List<String> fonts = Arrays.stream(rule.font.split(",")).map(String::trim).map(String::toLowerCase).toList();
					if ((rule.invertFont && fonts.contains(textRun.getFontFamily())) || (!rule.invertFont && !fonts.contains(textRun.getFontFamily()))) {
						description.append("Нарушает правила из-за неверного шрифта.\n");
						continue;
					}
				}
				if (rule.kaggle != null && !rule.kaggle.trim().equals("")) {
					List<Integer> kaggles = parseString(rule.kaggle);
					if ((rule.invertKaggle && kaggles.contains(textRun.getFontSize().intValue())) || (!rule.invertKaggle && !kaggles.contains(textRun.getFontSize().intValue()))) {
						description.append("Нарушает правила из-за неверного кеггля.\n");
						continue;
					}
				}
				if (!rule.hyperlinks && textRun.getHyperlink() != null) {
					description.append("Нарушает правила из-за наличия гиперссылки.\n");
					continue;
				}
				if (!rule.allowBold && textRun.isBold()) {
					description.append("Нарушает правила из-за жирного шрифта.\n");
					continue;
				}
				if (!rule.allowItalic && textRun.isItalic()) {
					description.append("Нарушает правила из-за курсива.\n");
					continue;
				}
				if (!rule.allowUnderlined && textRun.isUnderlined()) {
					description.append("Нарушает правила из-за подчеркивания.\n");
					continue;
				}

				flagIsAllowed = true;
				break;
			}

			if (!flagIsAllowed) {
				violations.add(violationConstructor(rules, "Элемент: \"" + textRun.getRawText() + "\"\n" + description, slideNumber));
			}
		}
	}

	void checkObligatory(Rule rule, List<XSLFTextShape> shapesMap, Integer slideNumber, List<Violation> violations) {
		StringBuilder description = new StringBuilder();

		boolean accepted = false;
		boolean runAccepted = true;

		for (XSLFTextShape shape : shapesMap) {
			if (accepted && runAccepted) {
				break;
			}

			accepted = false;

			String errorText = shape.getText().length() > 25 ? shape.getText().substring(0, 23) + "..." : shape.getText();
			if (rule.prefix != null) {
				if ((!rule.invertPrefix && !shape.getText().startsWith(rule.prefix)) || (rule.invertPrefix && shape.getText().startsWith(rule.prefix))) {
					description.append("Элемент: \"" + errorText + "\" не подходит из-за неверного префикса.\n");
					continue;
				}
			}
			if (rule.postfix != null) {
				if ((!rule.invertPostfix && !shape.getText().startsWith(rule.postfix)) || (rule.invertPostfix && shape.getText().startsWith(rule.postfix))) {
					description.append("Нарушает правила из-за неверного постфикса.\n");
					continue;
				}
			}

			if (rule.maxWords != null) {
				if (shape.getText().split(" ").length > rule.maxWords) {
					description.append("Нарушает правила из-за количества слов.\n");
					break;
				}
			}
			if (rule.minWords != null) {
				if (shape.getText().split(" ").length < rule.minWords) {
					description.append("Нарушает правила из-за количества слов.\n");
					break;
				}
			}
			if (rule.maxSentences != null) {
				if (shape.getText().split("[.!?]]").length > rule.maxSentences) {
					description.append("Нарушает правила из-за количества предложений.\n");
					break;
				}
			}
			if (rule.minSentences != null) {
				if (shape.getText().split("[.!?]]").length < rule.minSentences) {
					description.append("Нарушает правила из-за количества предложений.\n");
					break;
				}
			}
			if (rule.maxParagraphs != null) {
				if (shape.getTextParagraphs().size() > rule.maxParagraphs) {
					description.append("Нарушает правила из-за количества абзацев.\n");
					break;
				}
			}
			if (rule.minParagraphs != null) {
				if (shape.getTextParagraphs().size() < rule.minParagraphs) {
					description.append("Нарушает правила из-за количества абзацев.\n");
					break;
				}
			}

			accepted = true;
			runAccepted = true;

			description = new StringBuilder();

			for (XSLFTextParagraph paragraph : shape.getTextParagraphs()) {
				if (!runAccepted) {
					break;
				}
				for (XSLFTextRun textRun : paragraph.getTextRuns()) {
					String errorTextRun = textRun.getRawText().length() > 25 ? textRun.getRawText().substring(0, 23) + "..." : textRun.getRawText();
					if (rule.font != null && !rule.font.trim().equals("")) {
						List<String> fonts = Arrays.stream(rule.font.split(",")).map(String::trim).map(String::toLowerCase).toList();
						if ((rule.invertFont && fonts.contains(textRun.getFontFamily().toLowerCase())) || (!rule.invertFont && !fonts.contains(textRun.getFontFamily().toLowerCase()))) {
							description.append("Элемент: \"" + errorTextRun + "\" не подходит из-за неверного шрифта.\n");
							runAccepted = false;
							break;
						}
					}

					if (rule.kaggle != null && !rule.kaggle.trim().equals("")) {
						List<Integer> kaggles = parseString(rule.kaggle);
						try {
							if ((rule.invertKaggle && kaggles.contains(textRun.getFontSize().intValue())) || (!rule.invertKaggle && !kaggles.contains(textRun.getFontSize().intValue()))) {
								description.append("Элемент: \"" + errorTextRun + "\" не подходит из-за неверного кеггля.\n");
								runAccepted = false;
								break;
							}
						} catch (RuntimeException ignored) {}
					}
					if (!rule.hyperlinks && textRun.getHyperlink() != null) {
						description.append("Элемент: \"").append(errorTextRun).append("\" не подходит из-за наличия гиперссылки.\n");
						runAccepted = false;
						break;
					}
					if (!rule.allowBold && textRun.isBold()) {
						description.append("Элемент: \"" + errorTextRun + "\" не подходит из-за жирного шрифта.\n");
						runAccepted = false;
						break;
					}
					if (!rule.allowItalic && textRun.isItalic()) {
						description.append("Элемент: \"" + errorTextRun + "\" не подходит из-за курсива.\n");
						runAccepted = false;
						break;
					}
					if (!rule.allowUnderlined && textRun.isUnderlined()) {
						description.append("Элемент: \"" + errorTextRun + "\" не подходит из-за подчеркивания.\n");
						runAccepted = false;
						break;
					}
				}
			}
		}

		if (accepted && !runAccepted) {
			violations.add(violationConstructor(List.of(rule), "Внутри подходящей обязательной формы текста обнаружен неподходящий стиль: \n" + description, slideNumber));
		}
		if (!accepted) {
			violations.add(violationConstructor(List.of(rule), "Нет подходящей обязательной формы: \n" + description, slideNumber));
		}
	}



	List<Integer> parseString(String slidesString) {
		List<Integer> numberEntities = new ArrayList<>();

		String[] parts = slidesString.split(",");

		for (String part : parts) {
			String[] smallers;
			if (part.contains("-")) {
				smallers = part.split("-");
				for (int i = Integer.parseInt(smallers[0].trim()); i <= Integer.parseInt(smallers[1].trim()); ++i) {
					numberEntities.add(i - 1);
				}
			} else {
				try {
					numberEntities.add(Integer.parseInt(part.trim()) - 1);
				} catch (RuntimeException ignored) {
				}
			}
		}

		return numberEntities;
	}

	Violation violationConstructor(List<Rule> rules, String description, Integer slideNumber) {
		Violation violation =  new Violation();

		violation.setRules(rules);
		violation.setDescription(description);
		violation.setSlideNumber(slideNumber + 1);

		return violation;
	}
}
