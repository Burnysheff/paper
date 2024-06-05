package com.boot.demo.model.rules;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;
import java.time.Instant;
import java.util.Date;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "rules")
@Entity
public class Rule {
	@javax.persistence.Id
	@GeneratedValue
	@Column(name = "rule_id")
	private Long Id;

	@Column(name = "attribute")
	public String attribute;

	@Column(name = "obligatory")
	public boolean obligatory;
	@Column(name = "active")
	public boolean active;

	@Column(name = "name")
	public String name;
	@Column(name = "date_create")
	public Date dateCreate;
	@Column(name = "author")
	public String author;
	@Column(name = "owner")
	public String owner;
	@Column(name = "description")
	public String description;

	// Presentation overall
	@Column(name = "sameBackground")
	public boolean sameBackground;
	@Column(name = "minSlides")
	public Integer minSlides;
	@Column(name = "maxSlides")
	public Integer maxSlides;
	@Column(name = "allowText")
	public boolean allowText;
	@Column(name = "allowImages")
	public boolean allowImages;
	@Column(name = "allowVideo")
	public boolean allowVideo;
	@Column(name = "allowCharts")
	public boolean allowCharts;
	@Column(name = "allowTables")
	public boolean allowTables;
	@Column(name = "maxSlideElements")
	public Integer maxSlideElements;
	@Column(name = "minSlideElements")
	public Integer minSlideElements;


	// Common for elements
	@Column(name = "slides")
	public String slides;
	@Column(name = "invert_slides")
	public boolean invertSlides;

	@Column(name = "minQuantity")
	public Integer minQuantity;
	@Column(name = "maxQuantity")
	public Integer maxQuantity;

	// Text fields
	@Column(name = "prefix")
	public String prefix;
	@Column(name = "postfix")
	public String postfix;
	@Column(name = "fonts")
	public String font;
	@Column(name = "kaggle")
	public String kaggle;
	@Column(name = "invert_prefix")
	public boolean invertPrefix;
	@Column(name = "invert_postfix")
	public boolean invertPostfix;
	@Column(name = "invert_kaggle")
	public boolean invertKaggle;
	@Column(name = "invert_font")
	public boolean invertFont;
	@Column(name = "hyperlinks")
	public boolean hyperlinks;
	@Column(name = "maxWords")
	public Integer maxWords;
	@Column(name = "maxSentences")
	public Integer maxSentences;
	@Column(name = "maxParagraphs")
	public Integer maxParagraphs;
	@Column(name = "minWords")
	public Integer minWords;
	@Column(name = "minSentences")
	public Integer minSentences;
	@Column(name = "minParagraphs")
	public Integer minParagraphs;
	@Column(name = "allowBold")
	public boolean allowBold;
	@Column(name = "allowItalic")
	public boolean allowItalic;
	@Column(name = "allowUnderlined")
	public boolean allowUnderlined;

	@Column(name = "min_height")
	public Integer minHeight;
	@Column(name = "min_width")
	public Integer minWidth;
	@Column(name = "height")
	public Integer maxHeight;
	@Column(name = "width")
	public Integer maxWidth;

	public Rule(String name, String desc, String author, boolean active) {
		this.name = name;
		this.dateCreate = Date.from(Instant.now());
		this.description = desc;
		this.author = author;
		this.active = active;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Rule rule = (Rule) o;

		boolean a = obligatory == rule.obligatory &&
				active == rule.active &&
				sameBackground == rule.sameBackground &&
				allowText == rule.allowText &&
				allowImages == rule.allowImages &&
				allowVideo == rule.allowVideo &&
				allowCharts == rule.allowCharts &&
				allowTables == rule.allowTables &&
				invertSlides == rule.invertSlides &&
				invertPrefix == rule.invertPrefix &&
				invertPostfix == rule.invertPostfix &&
				invertKaggle == rule.invertKaggle &&
				invertFont == rule.invertFont &&
				hyperlinks == rule.hyperlinks &&
				allowBold == rule.allowBold &&
				allowItalic == rule.allowItalic &&
				allowUnderlined == rule.allowUnderlined;
		boolean b = Objects.equals(attribute, rule.attribute) &&
				Objects.equals(name, rule.name) &&
				Objects.equals(description, rule.description) &&
				Objects.equals(minSlides, rule.minSlides) &&
				Objects.equals(maxSlides, rule.maxSlides) &&
				Objects.equals(maxSlideElements, rule.maxSlideElements) &&
				Objects.equals(minSlideElements, rule.minSlideElements) &&
				Objects.equals(slides, rule.slides) &&
				Objects.equals(minQuantity, rule.minQuantity) &&
				Objects.equals(maxQuantity, rule.maxQuantity) &&
				Objects.equals(prefix, rule.prefix) &&
				Objects.equals(postfix, rule.postfix) &&
				Objects.equals(font, rule.font) &&
				Objects.equals(kaggle, rule.kaggle) &&
				Objects.equals(maxWords, rule.maxWords) &&
				Objects.equals(maxSentences, rule.maxSentences) &&
				Objects.equals(maxParagraphs, rule.maxParagraphs) &&
				Objects.equals(minWords, rule.minWords) &&
				Objects.equals(minSentences, rule.minSentences) &&
				Objects.equals(minParagraphs, rule.minParagraphs) &&
				Objects.equals(minHeight, rule.minHeight) &&
				Objects.equals(minWidth, rule.minWidth) &&
				Objects.equals(maxHeight, rule.maxHeight) &&
				Objects.equals(maxWidth, rule.maxWidth);

		return a && b;
	}

	@Override
	public int hashCode() {
		return Objects.hash(attribute, obligatory, active, author, owner, description, sameBackground, minSlides, maxSlides, allowText, allowImages, allowVideo, allowCharts, allowTables, maxSlideElements, minSlideElements, slides, invertSlides, minQuantity, maxQuantity, prefix, postfix, font, kaggle, invertPrefix, invertPostfix, invertKaggle, invertFont, hyperlinks, maxWords, maxSentences, maxParagraphs, minWords, minSentences, minParagraphs, allowBold, allowItalic, allowUnderlined, minHeight, minWidth, maxHeight, maxWidth);
	}
}
