package com.boot.demo.service.check;

import com.boot.demo.model.check.Violation;
import com.boot.demo.model.rules.Rule;
import org.apache.poi.sl.usermodel.ShapeType;
import org.apache.poi.xslf.usermodel.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PresentationCheck {
	public void check(XMLSlideShow presentation, List<Rule> rules, List<Violation> violations) {
		Map<Integer, List<Rule>> ruleMap = new HashMap<>();

		for (Rule rule : rules) {
			if (rule.attribute.equals("PRESENTATION") && rule.active) {
				if (rule.minSlides > presentation.getSlides().size()) {
					violations.add(violationConstructor(List.of(rule), "В презентации слишком мало слайдов!\n", 0));
				}
				if (rule.maxSlides < presentation.getSlides().size()) {
					violations.add(violationConstructor(List.of(rule), "В презентации слишком много слайдов!\n", 0));
				}
				for (int i = 0; i < presentation.getSlides().size(); ++i) {
					if (!ruleMap.containsKey(i)) {
						ruleMap.put(i, new ArrayList<>());
					}
					ruleMap.get(i).add(rule);
				}
			}
		}

		List<XSLFSlide> slides = presentation.getSlides();
		int totalTextBoxes = 0;
		int totalPictures = 0;
		int totalCharts = 0;
		int totalTables = 0;
		int totalVideos = 0;

		for (int i = 0; i < slides.size(); ++i) {
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
					}
					if (shape instanceof XSLFDiagram) {
						charts++;
					}
				}
			}

			totalTextBoxes += textBoxes;
			totalPictures += pictures;
			totalCharts += charts;
			totalTables += tables;
			totalVideos += videos;

			List<Rule> rulesSlide = ruleMap.get(i);
			for (Rule rule : rulesSlide) {
				if (!rule.allowText && totalTextBoxes > 0) {
					violations.add(violationConstructor(List.of(rule), "На слайде присутствует текст!\n", i + 1));
				}
				if (!rule.allowImages && totalPictures > 0) {
					violations.add(violationConstructor(List.of(rule), "На слайде присутствует картинка!\n", i + 1));
				}
				if (!rule.allowCharts && totalCharts > 0) {
					violations.add(violationConstructor(List.of(rule), "На слайде присутствует диаграмма!\n", i + 1));
				}
				if (!rule.allowTables && totalTables > 0) {
					violations.add(violationConstructor(List.of(rule), "На слайде присутствует таблица!\n", i + 1));
				}
				if (!rule.allowVideo && totalVideos > 0) {
					violations.add(violationConstructor(List.of(rule), "На слайде присутствует видео!\n", i + 1));
				}

				if (totalTextBoxes + totalPictures + totalCharts + totalTables + totalVideos < rule.minSlideElements) {
					violations.add(violationConstructor(List.of(rule), "На слайде слишком мало элементов!\n", i + 1));
				}
				if (totalTextBoxes + totalPictures + totalCharts + totalTables + totalVideos > rule.maxSlideElements) {
					violations.add(violationConstructor(List.of(rule), "На слайде слишком много элементов!\n", i + 1));
				}
			}
		}
	}

	Violation violationConstructor(List<Rule> rules, String description, Integer slideNumber) {
		Violation violation =  new Violation();

		violation.setRules(rules);
		violation.setDescription(description);
		violation.setSlideNumber(slideNumber);

		return violation;
	}
}
