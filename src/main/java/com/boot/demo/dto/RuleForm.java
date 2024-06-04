package com.boot.demo.dto;

import javax.persistence.Column;
import java.util.List;

public class RuleForm {
	public String attribute;
	public boolean sameBackground;
	public int minSlides;
	public int maxSlides;
	public boolean allowText;
	public boolean allowImages;
	public boolean allowVideo;
	public boolean allowCharts;
	public boolean allowTables;
	public int maxSlideElements;
	public int minSlideElements;
	public boolean obligatory;
	public boolean active;
	public String name;
	public String description;
	public Integer minQuantity;
	public Integer maxQuantity;
	public String slides;
	public Boolean invertSlides;
	public String prefix;
	public String postfix;
	public List<String> fonts;
	public String fontSizes;
	public Boolean prefixReverse;
	public Boolean postfixReverse;
	public Boolean fontReverse;
	public Boolean sizeReverse;
	public Boolean hyperlinks;
	public Boolean underlined;
	public Boolean italic;
	public Boolean bold;
	public Integer maxWords;
	public Integer maxSentences;
	public Integer maxParagraphs;
	public Integer minWords;
	public Integer minSentences;
	public Integer minParagraphs;

	public int minHeight;
	public int minWidth;
	public int maxHeight;
	public int maxWidth;

	public void setObligatory(boolean obligatory) {
		this.obligatory = obligatory;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public void setMinHeight(int minHeight) {
		this.minHeight = minHeight;
	}

	public void setMinWidth(int minWidth) {
		this.minWidth = minWidth;
	}

	public void setMaxHeight(int maxHeight) {
		this.maxHeight = maxHeight;
	}

	public void setMaxWidth(int maxWidth) {
		this.maxWidth = maxWidth;
	}

	public boolean isObligatory() {
		return obligatory;
	}

	public boolean isActive() {
		return active;
	}

	public int getMinHeight() {
		return minHeight;
	}

	public int getMinWidth() {
		return minWidth;
	}

	public int getMaxHeight() {
		return maxHeight;
	}

	public int getMaxWidth() {
		return maxWidth;
	}

	// Getters and Setters
	public void setObligatory(Boolean obligatory) {
		this.obligatory = obligatory;
	}

	public void setActive(Boolean active) {
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

	public void setInvertSlides(Boolean invertSlides) {
		this.invertSlides = invertSlides;
	}

	public Boolean getObligatory() {
		return obligatory;
	}

	public Boolean getActive() {
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

	public Boolean getInvertSlides() {
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

	public List<String> getFonts() {
		return fonts;
	}

	public void setFonts(List<String> fonts) {
		this.fonts = fonts;
	}

	public String getFontSizes() {
		return fontSizes;
	}

	public void setFontSizes(String fontSizes) {
		this.fontSizes = fontSizes;
	}

	public Boolean getPrefixReverse() {
		return prefixReverse;
	}

	public void setPrefixReverse(Boolean prefixReverse) {
		this.prefixReverse = prefixReverse;
	}

	public Boolean getPostfixReverse() {
		return postfixReverse;
	}

	public void setPostfixReverse(Boolean postfixReverse) {
		this.postfixReverse = postfixReverse;
	}

	public Boolean getFontReverse() {
		return fontReverse;
	}

	public void setFontReverse(Boolean fontReverse) {
		this.fontReverse = fontReverse;
	}

	public Boolean getSizeReverse() {
		return sizeReverse;
	}

	public void setSizeReverse(Boolean sizeReverse) {
		this.sizeReverse = sizeReverse;
	}

	public Boolean getHyperlinks() {
		return hyperlinks;
	}

	public void setHyperlinks(Boolean hyperlinks) {
		this.hyperlinks = hyperlinks;
	}

	public Boolean getUnderlined() {
		return underlined;
	}

	public void setUnderlined(Boolean underlined) {
		this.underlined = underlined;
	}

	public Boolean getItalic() {
		return italic;
	}

	public void setItalic(Boolean italic) {
		this.italic = italic;
	}

	public Boolean getBold() {
		return bold;
	}

	public void setBold(Boolean bold) {
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

	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}

	public void setSameBackground(boolean sameBackground) {
		this.sameBackground = sameBackground;
	}

	public void setMinSlides(int minSlides) {
		this.minSlides = minSlides;
	}

	public void setMaxSlides(int maxSlides) {
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

	public void setMaxSlideElements(int maxSlideElements) {
		this.maxSlideElements = maxSlideElements;
	}

	public void setMinSlideElements(int minSlideElements) {
		this.minSlideElements = minSlideElements;
	}

	public String getAttribute() {
		return attribute;
	}

	public boolean isSameBackground() {
		return sameBackground;
	}

	public int getMinSlides() {
		return minSlides;
	}

	public int getMaxSlides() {
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

	public int getMaxSlideElements() {
		return maxSlideElements;
	}

	public int getMinSlideElements() {
		return minSlideElements;
	}
}