package com.boot.demo.model.check;

import com.boot.demo.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "checker")
@Entity
public class Check {
	@javax.persistence.Id
	@GeneratedValue
	@Column(name = "checker_id")
	private Long Id;

	@OneToMany
	@JoinColumn(name = "violations_id")
	List<Violation> violationList;

	@ManyToOne
	@JoinColumn(name = "owner_id")
	User user;

	@Column(name = "rules_number")
	Long rulesNumber;

	@Column(name = "status")
	boolean status;

	@Column(name = "date")
	Date date;
}
