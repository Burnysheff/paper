package com.boot.demo.service.check;

import com.boot.demo.model.check.Violation;
import com.boot.demo.model.rules.Rule;
import org.apache.poi.sl.usermodel.ShapeType;
import org.apache.poi.xslf.usermodel.*;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.IntStream;

@Service
public class PresentationCheck {
	public void check(XMLSlideShow presentation, List<Rule> rules, List<Violation> violations) {
		Map<Integer, List<Rule>> ruleMap = new HashMap<>();
		Map<Integer, List<Rule>> imageMap = new HashMap<>();

		for (Rule rule : rules) {
			if (rule.attribute.equals("PRESENTATION") && rule.active) {
				if (rule.minSlides != null) {
					if (rule.minSlides > presentation.getSlides().size()) {
						violations.add(violationConstructor(List.of(rule), "В презентации слишком мало слайдов!\n", 0));
					}
				}
				if (rule.maxSlides != null) {
					if (rule.maxSlides < presentation.getSlides().size()) {
						violations.add(violationConstructor(List.of(rule), "В презентации слишком много слайдов!\n", 0));
					}
				}
				for (int i = 0; i < presentation.getSlides().size(); ++i) {
					if (!ruleMap.containsKey(i)) {
						ruleMap.put(i, new ArrayList<>());
					}
					ruleMap.get(i).add(rule);
				}
			}
		}

		for (Rule rule : rules) {
			if (rule.attribute.equals("IMAGE") && rule.active) {
				List<Integer> slides;
				if (rule.slides != null && !rule.slides.trim().equals("")) {
					slides = parseString(rule.slides);
				} else {
					slides = IntStream.rangeClosed(0, presentation.getSlides().size() - 1).boxed().toList();
				}
				for (int i = 0; i < presentation.getSlides().size(); ++i) {
					if ((!rule.invertSlides && slides.contains(i)) || (rule.invertSlides && !slides.contains(i))) {
						if (!imageMap.containsKey(i)) {
							imageMap.put(i, new ArrayList<>());
						}
						imageMap.get(i).add(rule);
					}
				}
			}
		}

		List<XSLFSlide> slides = presentation.getSlides();

		for (int i = 0; i < slides.size(); ++i) {
			checkImage(i, slides.get(i), imageMap.get(i), violations);

			int textBoxes = 0;
			int pictures = 0;
			int charts = 0;
			int tables = 0;
			int videos = 0;

			for (XSLFShape shape : slides.get(i).getShapes()) {
				if (shape instanceof XSLFTextShape) {
					textBoxes++;
				} else if (shape instanceof XSLFPictureShape) {
					XSLFPictureShape pictureShape = (XSLFPictureShape) shape;
					if (pictureShape.isVideoFile()) {
						++videos;
					} else {
						pictures++;
					}
				} else if (shape instanceof XSLFGraphicFrame) {
					if (shape instanceof XSLFTable) {
						tables++;
					} else {
						charts++;
					}
				}
			}

			List<Rule> rulesSlide = ruleMap.get(i);
			if (rulesSlide == null) {
				continue;
			}
			for (Rule rule : rulesSlide) {
				if (!rule.allowText && textBoxes > 0) {
					violations.add(violationConstructor(List.of(rule), "На слайде присутствует текст!\n", i + 1));
				}
				if (!rule.allowImages && pictures > 0) {
					violations.add(violationConstructor(List.of(rule), "На слайде присутствует картинка!\n", i + 1));
				}
				if (!rule.allowCharts && charts > 0) {
					violations.add(violationConstructor(List.of(rule), "На слайде присутствует диаграмма!\n", i + 1));
				}
				if (!rule.allowTables && tables > 0) {
					violations.add(violationConstructor(List.of(rule), "На слайде присутствует таблица!\n", i + 1));
				}
				if (!rule.allowVideo && videos > 0) {
					violations.add(violationConstructor(List.of(rule), "На слайде присутствует видео!\n", i + 1));
				}

				if (rule.minSlideElements != null) {
					if (textBoxes + pictures + charts + tables + videos < rule.minSlideElements) {
						violations.add(violationConstructor(List.of(rule), "На слайде слишком мало элементов!\n", i + 1));
					}
				}
				if (rule.maxSlideElements != null) {
					if (textBoxes + pictures + charts + tables + videos > rule.maxSlideElements) {
						violations.add(violationConstructor(List.of(rule), "На слайде слишком много элементов!\n", i + 1));
					}
				}
			}
		}
	}

	private void checkImage(int number, XSLFSlide slide, List<Rule> rules, List<Violation> violations) {
		if (rules == null || rules.isEmpty()) {
			return;
		}

		int shapes = 0;

		List<Double> heights = new ArrayList<>();
		List<Double> widths = new ArrayList<>();

		for (XSLFShape shape : slide.getShapes()) {
			if (shape instanceof XSLFPictureShape pictureShape) {
				++shapes;

				widths.add(pictureShape.getAnchor().getWidth());
				heights.add(pictureShape.getAnchor().getHeight());
			}
		}

		System.out.println("AAAA  " + slide + "   " + shapes);

		Collections.sort(heights);
		Collections.sort(widths);

		for (Rule rule : rules) {
			if (rule.obligatory) {
				if (rule.minQuantity != null && rule.minQuantity > shapes) {
					violations.add(violationConstructor(List.of(rule), "Слишком мало картинок / видео", number + 1));
				}
				if (rule.maxQuantity != null && rule.maxQuantity < shapes) {
					violations.add(violationConstructor(List.of(rule), "Слишком много картинок / видео", number + 1));
				}
			}

			if (shapes == 0) {
				return;
			}

			if (rule.minWidth != null && rule.minWidth > widths.get(0)) {
				violations.add(violationConstructor(List.of(rule), "У картинки / видео слишком маленькая ширина", number + 1));
			}
			if (rule.maxWidth != null && rule.maxWidth < widths.get(widths.size() - 1)) {
				violations.add(violationConstructor(List.of(rule), "У картинки / видео слишком большая ширина", number + 1));
			}
			if (rule.minHeight != null && rule.minHeight > heights.get(0)) {
				violations.add(violationConstructor(List.of(rule), "У картинки / видео слишком маленькая высота", number + 1));
			}
			if (rule.maxHeight != null && rule.maxHeight < heights.get(heights.size() - 1)) {
				violations.add(violationConstructor(List.of(rule), "У картинки / видео слишком большая высота", number + 1));
			}
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
					numberEntities.add(i);
				}
			} else {
				try {
					numberEntities.add(Integer.parseInt(part.trim()));
				} catch (NumberFormatException ignored) {
				}
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
