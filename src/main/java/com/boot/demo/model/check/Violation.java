package com.boot.demo.model.check;

import com.boot.demo.model.rules.Rule;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "violation")
@Entity
public class Violation {
	@javax.persistence.Id
	@GeneratedValue
	@Column(name = "violation_id")
	private Long Id;

	@JoinColumn(name = "rules_id", columnDefinition="VARCHAR(2048)")
	@ManyToMany
	List<Rule> rules;

	@Column(name = "description", length = 8182)
	public String description;

	@Column(name = "slideNumber")
	public Integer slideNumber;
}
