package com.boot.demo.dto;

import javax.persistence.Column;
import java.util.List;

public class RuleForm {
	public String attribute;
	public boolean sameBackground;
	public Integer minSlides;
	public Integer maxSlides;
	public boolean allowText;
	public boolean allowImages;
	public boolean allowVideo;
	public boolean allowCharts;
	public boolean allowTables;
	public Integer maxSlideElements;
	public Integer minSlideElements;
	public boolean obligatory;
	public boolean active;
	public String name;
	public String description;
	public Integer minQuantity;
	public Integer maxQuantity;
	public String slides;
	public boolean invertSlides;
	public String prefix;
	public String postfix;
	public String fonts;
	public String fontSizes;
	public boolean prefixReverse;
	public boolean postfixReverse;
	public boolean fontReverse;
	public boolean sizeReverse;
	public boolean hyperlinks;
	public boolean underlined;
	public boolean italic;
	public boolean bold;
	public Integer maxWords;
	public Integer maxSentences;
	public Integer maxParagraphs;
	public Integer minWords;
	public Integer minSentences;
	public Integer minParagraphs;

	public Integer minHeight;
	public Integer minWidth;
	public Integer maxHeight;
	public Integer maxWidth;


	public void setMinHeight(Integer minHeight) {
		this.minHeight = minHeight;
	}

	public void setMinWidth(Integer minWidth) {
		this.minWidth = minWidth;
	}

	public void setMaxHeight(Integer maxHeight) {
		this.maxHeight = maxHeight;
	}

	public void setMaxWidth(Integer maxWidth) {
		this.maxWidth = maxWidth;
	}

	public boolean isObligatory() {
		return obligatory;
	}

	public boolean isActive() {
		return active;
	}

	public Integer getMinHeight() {
		return minHeight;
	}

	public Integer getMinWidth() {
		return minWidth;
	}

	public Integer getMaxHeight() {
		return maxHeight;
	}

	public Integer getMaxWidth() {
		return maxWidth;
	}

	// Getters and Setters
	public void setObligatory(boolean obligatory) {
		this.obligatory = obligatory;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setMinQuantity(Integer minQuantity) {
		this.minQuantity = minQuantity;
	}

	public void setMaxQuantity(Integer maxQuantity) {
		this.maxQuantity = maxQuantity;
	}

	public void setSlides(String slides) {
		this.slides = slides;
	}

	public void setInvertSlides(boolean invertSlides) {
		this.invertSlides = invertSlides;
	}

	public boolean getObligatory() {
		return obligatory;
	}

	public boolean getActive() {
		return active;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public Integer getMinQuantity() {
		return minQuantity;
	}

	public Integer getMaxQuantity() {
		return maxQuantity;
	}

	public String getSlides() {
		return slides;
	}

	public boolean getInvertSlides() {
		return invertSlides;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getPostfix() {
		return postfix;
	}

	public void setPostfix(String postfix) {
		this.postfix = postfix;
	}

	public String getFonts() {
		return fonts;
	}

	public void setFonts(String fonts) {
		this.fonts = fonts;
	}

	public String getFontSizes() {
		return fontSizes;
	}

	public void setFontSizes(String fontSizes) {
		this.fontSizes = fontSizes;
	}

	public boolean getPrefixReverse() {
		return prefixReverse;
	}

	public void setPrefixReverse(boolean prefixReverse) {
		this.prefixReverse = prefixReverse;
	}

	public boolean getPostfixReverse() {
		return postfixReverse;
	}

	public void setPostfixReverse(boolean postfixReverse) {
		this.postfixReverse = postfixReverse;
	}

	public boolean getFontReverse() {
		return fontReverse;
	}

	public void setFontReverse(boolean fontReverse) {
		this.fontReverse = fontReverse;
	}

	public boolean getSizeReverse() {
		return sizeReverse;
	}

	public void setSizeReverse(boolean sizeReverse) {
		this.sizeReverse = sizeReverse;
	}

	public boolean getHyperlinks() {
		return hyperlinks;
	}

	public void setHyperlinks(boolean hyperlinks) {
		this.hyperlinks = hyperlinks;
	}

	public boolean getUnderlined() {
		return underlined;
	}

	public void setUnderlined(boolean underlined) {
		this.underlined = underlined;
	}

	public boolean getItalic() {
		return italic;
	}

	public void setItalic(boolean italic) {
		this.italic = italic;
	}

	public boolean getBold() {
		return bold;
	}

	public void setBold(boolean bold) {
		this.bold = bold;
	}

	public Integer getMaxWords() {
		return maxWords;
	}

	public void setMaxWords(Integer maxWords) {
		this.maxWords = maxWords;
	}

	public Integer getMaxSentences() {
		return maxSentences;
	}

	public void setMaxSentences(Integer maxSentences) {
		this.maxSentences = maxSentences;
	}

	public Integer getMaxParagraphs() {
		return maxParagraphs;
	}

	public void setMaxParagraphs(Integer maxParagraphs) {
		this.maxParagraphs = maxParagraphs;
	}

	public Integer getMinWords() {
		return minWords;
	}

	public void setMinWords(Integer minWords) {
		this.minWords = minWords;
	}

	public Integer getMinSentences() {
		return minSentences;
	}

	public void setMinSentences(Integer minSentences) {
		this.minSentences = minSentences;
	}

	public Integer getMinParagraphs() {
		return minParagraphs;
	}

	public void setMinParagraphs(Integer minParagraphs) {
		this.minParagraphs = minParagraphs;
	}

	public void setSameBackground(boolean sameBackground) {
		this.sameBackground = sameBackground;
	}

	public void setMinSlides(Integer minSlides) {
		this.minSlides = minSlides;
	}

	public void setMaxSlides(Integer maxSlides) {
		this.maxSlides = maxSlides;
	}

	public void setAllowText(boolean allowText) {
		this.allowText = allowText;
	}

	public void setAllowImages(boolean allowImages) {
		this.allowImages = allowImages;
	}

	public void setAllowVideo(boolean allowVideo) {
		this.allowVideo = allowVideo;
	}

	public void setAllowCharts(boolean allowCharts) {
		this.allowCharts = allowCharts;
	}

	public void setAllowTables(boolean allowTables) {
		this.allowTables = allowTables;
	}

	public void setMaxSlideElements(Integer maxSlideElements) {
		this.maxSlideElements = maxSlideElements;
	}

	public void setMinSlideElements(Integer minSlideElements) {
		this.minSlideElements = minSlideElements;
	}

	public boolean isSameBackground() {
		return sameBackground;
	}

	public Integer getMinSlides() {
		return minSlides;
	}

	public Integer getMaxSlides() {
		return maxSlides;
	}

	public boolean isAllowText() {
		return allowText;
	}

	public boolean isAllowImages() {
		return allowImages;
	}

	public boolean isAllowVideo() {
		return allowVideo;
	}

	public boolean isAllowCharts() {
		return allowCharts;
	}

	public boolean isAllowTables() {
		return allowTables;
	}

	public Integer getMaxSlideElements() {
		return maxSlideElements;
	}

	public Integer getMinSlideElements() {
		return minSlideElements;
	}
}