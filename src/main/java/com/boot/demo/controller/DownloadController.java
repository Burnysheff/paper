package com.boot.demo.controller;

import com.boot.demo.model.check.Check;
import com.boot.demo.model.check.Violation;
import com.boot.demo.model.rules.Rule;
import com.boot.demo.service.CheckService;
import com.boot.demo.service.RuleService;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class DownloadController {
	RuleService ruleService;
	CheckService checkService;

	public DownloadController(RuleService ruleService, CheckService checkService) {
		this.ruleService = ruleService;
		this.checkService = checkService;
	}

	@PostMapping(value = "/{id}/download-pdf")
	public void downloadPdf(HttpServletResponse response, @PathVariable Long id) throws IOException {
		List<Rule> rules = ruleService.getRulesForUser();
		Check check = checkService.getCheckById(id);

		List<Rule> textRules = new ArrayList<>();
		List<Rule> presRules = new ArrayList<>();
		List<Rule> imageRules = new ArrayList<>();

		for (Rule rule : rules) {
			switch (rule.attribute) {
				case "TEXT" -> textRules.add(rule);
				case "PRESENTATION" -> presRules.add(rule);
				case "IMAGE" -> imageRules.add(rule);
			}
		}

		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		PdfWriter writer = new PdfWriter(baos);

		PdfDocument pdfDoc = new PdfDocument(writer);
		Document document = new Document(pdfDoc);

		document.add(new Paragraph("Список Правил и Нарушений"));

		Table table = new Table(1);
		// TEXT
		if (textRules.size() > 0) {
			document.add(new Paragraph("Текстовые правила:"));
			table = new Table(textRules.size());

			table.addHeaderCell("ID");
			table.addHeaderCell("Атрибут");
			table.addHeaderCell("Обязательный");
			table.addHeaderCell("Активный");
			table.addHeaderCell("Название");
			table.addHeaderCell("Дата создания");
			table.addHeaderCell("Автор");
			table.addHeaderCell("Владелец");
			table.addHeaderCell("Описание");
			table.addHeaderCell("Слайды");
			table.addHeaderCell("Инверсия слайдов");
			table.addHeaderCell("Минимальное количество");
			table.addHeaderCell("Максимальное количество");
			table.addHeaderCell("Префикс");
			table.addHeaderCell("Постфикс");
			table.addHeaderCell("Шрифт");
			table.addHeaderCell("Каггл");
			table.addHeaderCell("Инверсия префикса");
			table.addHeaderCell("Инверсия постфикса");
			table.addHeaderCell("Инверсия каггл");
			table.addHeaderCell("Инверсия шрифта");
			table.addHeaderCell("Гиперссылки");
			table.addHeaderCell("Максимум слов");
			table.addHeaderCell("Максимум предложений");
			table.addHeaderCell("Максимум абзацев");
			table.addHeaderCell("Минимум слов");
			table.addHeaderCell("Минимум предложений");
			table.addHeaderCell("Минимум абзацев");
			table.addHeaderCell("Разрешить жирный шрифт");
			table.addHeaderCell("Разрешить курсив");
			table.addHeaderCell("Разрешить подчеркивание");

			for (Rule rule : textRules) {
				table.addCell(Optional.ofNullable(rule.getId()).map(Object::toString).orElse(""));
				table.addCell(Optional.ofNullable(rule.getAttribute()).orElse(""));
				table.addCell(Boolean.toString(Optional.ofNullable(rule.isObligatory()).orElse(false)));
				table.addCell(Boolean.toString(Optional.ofNullable(rule.isActive()).orElse(false)));
				table.addCell(Optional.ofNullable(rule.getName()).orElse(""));
				table.addCell(Optional.ofNullable(rule.getDateCreate()).map(Object::toString).orElse(""));
				table.addCell(Optional.ofNullable(rule.getAuthor()).orElse(""));
				table.addCell(Optional.ofNullable(rule.getOwner()).orElse(""));
				table.addCell(Optional.ofNullable(rule.getDescription()).orElse(""));
				table.addCell(Optional.ofNullable(rule.getSlides()).orElse(""));
				table.addCell(Boolean.toString(Optional.ofNullable(rule.isInvertSlides()).orElse(false)));
				table.addCell(Optional.ofNullable(rule.getMinQuantity()).map(Object::toString).orElse(""));
				table.addCell(Optional.ofNullable(rule.getMaxQuantity()).map(Object::toString).orElse(""));
				table.addCell(Optional.ofNullable(rule.getPrefix()).orElse(""));
				table.addCell(Optional.ofNullable(rule.getPostfix()).orElse(""));
				table.addCell(Optional.ofNullable(rule.getFont()).orElse(""));
				table.addCell(Optional.ofNullable(rule.getKaggle()).orElse(""));
				table.addCell(Boolean.toString(Optional.ofNullable(rule.isInvertPrefix()).orElse(false)));
				table.addCell(Boolean.toString(Optional.ofNullable(rule.isInvertPostfix()).orElse(false)));
				table.addCell(Boolean.toString(Optional.ofNullable(rule.isInvertKaggle()).orElse(false)));
				table.addCell(Boolean.toString(Optional.ofNullable(rule.isInvertFont()).orElse(false)));
				table.addCell(Boolean.toString(Optional.ofNullable(rule.isHyperlinks()).orElse(false)));
				table.addCell(Optional.ofNullable(rule.getMaxWords()).map(Object::toString).orElse(""));
				table.addCell(Optional.ofNullable(rule.getMaxSentences()).map(Object::toString).orElse(""));
				table.addCell(Optional.ofNullable(rule.getMaxParagraphs()).map(Object::toString).orElse(""));
				table.addCell(Optional.ofNullable(rule.getMinWords()).map(Object::toString).orElse(""));
				table.addCell(Optional.ofNullable(rule.getMinSentences()).map(Object::toString).orElse(""));
				table.addCell(Optional.ofNullable(rule.getMinParagraphs()).map(Object::toString).orElse(""));
				table.addCell(Boolean.toString(Optional.ofNullable(rule.isAllowBold()).orElse(false)));
				table.addCell(Boolean.toString(Optional.ofNullable(rule.isAllowItalic()).orElse(false)));
				table.addCell(Boolean.toString(Optional.ofNullable(rule.isAllowUnderlined()).orElse(false)));
			}
			document.add(table);
		}

		// IMAGE
		if (imageRules.size() > 0) {
			document.add(new Paragraph("Правила для картинок / видео"));
			Table tableImage = new Table(imageRules.size());

			tableImage.addCell("ID");
			tableImage.addCell("Атрибут");
			tableImage.addCell("Обязательный");
			tableImage.addCell("Активный");
			tableImage.addCell("Название");
			tableImage.addCell("Дата создания");
			tableImage.addCell("Автор");
			tableImage.addCell("Владелец");
			tableImage.addCell("Описание");
			tableImage.addCell("Слайды");
			tableImage.addCell("Инверсия слайдов");
			tableImage.addCell("Минимальное количество");
			tableImage.addCell("Максимальное количество");
			tableImage.addCell("Минимальная высота");
			tableImage.addCell("Максимальная высота");
			tableImage.addCell("Минимальная ширина");
			tableImage.addCell("Максимальная ширина");

			for (Rule rule : imageRules) {
				tableImage.addCell(rule.getId().toString());
				tableImage.addCell(rule.getAttribute());
				tableImage.addCell(Boolean.toString(rule.isObligatory()));
				tableImage.addCell(Boolean.toString(rule.isActive()));
				tableImage.addCell(rule.getName());
				tableImage.addCell(rule.getDateCreate().toString());
				tableImage.addCell(rule.getAuthor());
				tableImage.addCell(rule.getOwner());
				tableImage.addCell(rule.getDescription());
				tableImage.addCell(rule.getSlides());
				tableImage.addCell(Boolean.toString(rule.isInvertSlides()));
				tableImage.addCell(rule.getMinHeight().toString());
				tableImage.addCell(rule.getMaxHeight().toString());
				tableImage.addCell(rule.getMinWidth().toString());
				tableImage.addCell(rule.getMaxWidth().toString());
			}
			document.add(tableImage);
		}

		// PRES
		if (presRules.size() > 0) {
			document.add(new Paragraph("Правила для картинок / видео"));
			Table tablePres = new Table(presRules.size());

			tablePres.addCell("ID");
			tablePres.addCell("Атрибут");
			tablePres.addCell("Обязательный");
			tablePres.addCell("Активный");
			tablePres.addCell("Название");
			tablePres.addCell("Дата создания");
			tablePres.addCell("Автор");
			tablePres.addCell("Владелец");
			tablePres.addCell("Описание");
			tablePres.addCell("Минимальное количество слайдов");
			tablePres.addCell("Максимальное количество слайдов");
			tablePres.addCell("Разрешить текст");
			tablePres.addCell("Разрешить картинки");
			tablePres.addCell("Разрешить видео");
			tablePres.addCell("Разрешить таблицы");
			tablePres.addCell("Разрешить диаграммы и другие графические элементы");
			tablePres.addCell("Максимальное количество элементов на слайде");
			tablePres.addCell("Минимальное количество элементов на слайде");
			for (Rule rule : presRules) {
				tablePres.addCell(rule.getId().toString());
				tablePres.addCell(rule.getAttribute());
				tablePres.addCell(Boolean.toString(rule.isObligatory()));
				tablePres.addCell(Boolean.toString(rule.isActive()));
				tablePres.addCell(rule.getName());
				tablePres.addCell(rule.getDateCreate().toString());
				tablePres.addCell(rule.getAuthor());
				tablePres.addCell(rule.getOwner());
				tablePres.addCell(rule.getDescription());
				tablePres.addCell(rule.getMinSlides().toString());
				tablePres.addCell(rule.getMaxSlides().toString());
				tablePres.addCell(Boolean.toString(rule.isAllowText()));
				tablePres.addCell(Boolean.toString(rule.isAllowImages()));
				tablePres.addCell(Boolean.toString(rule.isAllowVideo()));
				tablePres.addCell(Boolean.toString(rule.isAllowTables()));
				tablePres.addCell(Boolean.toString(rule.isAllowCharts()));
				tablePres.addCell(rule.getMaxSlideElements().toString());
				tablePres.addCell(rule.getMinSlideElements().toString());
			}
			document.add(tablePres);
		}


		// Добавляем таблицу нарушений
		document.add(new Paragraph("Нарушения:"));

		table = new Table(3);
		table.addCell("ID");
		table.addCell("ID Правил");
		table.addCell("Описание");
		table.addCell("Номер слайда");
		for (Violation violation : check.getViolationList()) {
			table.addCell(violation.getId().toString());
			table.addCell(violation.getRules().toString());
			table.addCell(violation.getDescription());
			table.addCell(String.valueOf((violation.getSlideNumber() + 1)));
		}
		document.add(table);

		// Закрываем документ
		document.close();

		response.setHeader("Content-Disposition", "attachment; filename=\"example.pdf\"");
		response.setContentType("application/pdf");

		// Записываем буфер в выходной поток
		response.getOutputStream().write(baos.toByteArray());
		response.getOutputStream().flush();

	}
}
