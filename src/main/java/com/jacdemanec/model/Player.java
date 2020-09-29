package com.jacdemanec.model;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OrderColumn;

@Entity
public class Player {

	@Id
	@GeneratedValue
	private Long id;

	private String aliasString;

	private String emailString;

	private int score;

	private int classification;
	
	private int lines_score;
	
	private int level_score;

	public Player(String aliasString, String emailString, int score, int classification, int lines_score, int level_score) {
		super();
		this.aliasString = aliasString;
		this.emailString = emailString;
		this.score = score;
		this.classification = classification;
		this.lines_score = lines_score;
		this.level_score = level_score;
	}

	public Player() {
	}	
		
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAliasString() {
		return aliasString;
	}

	public void setAliasString(String aliasString) {
		this.aliasString = aliasString;
	}

	public String getEmailString() {
		return emailString;
	}

	public void setEmailString(String emailString) {
		this.emailString = emailString;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getLines_score() {
		return lines_score;
	}

	public void setLines_score(int lines_score) {
		this.lines_score = lines_score;
	}

	public int getLevel_score() {
		return level_score;
	}

	public void setLevel_score(int level_score) {
		this.level_score = level_score;
	}

	public int getClassification() {
		return classification;
	}

	public void setClassification(int classification) {
		this.classification = classification;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.id, this.aliasString, this.emailString, this.score, this.lines_score, this.level_score);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (!(obj instanceof Player)) return false;
		Player player = (Player) obj;
		return Objects.equals(this.id, player.id)
				&& Objects.equals(this.aliasString, player.aliasString)
				&& Objects.equals(this.emailString, player.emailString)
				&& Objects.equals(this.score, player.score)
				&& Objects.equals(this.lines_score, player.lines_score)
				&& Objects.equals(this.level_score, player.level_score);
 	}

	@Override
	public String toString() {
		return "Player [id=" + id + ", alias=" + aliasString + ", email=" + emailString + ", score=" + score +
				", lines=" + lines_score + ", level=" + level_score + "]";
	}

}
