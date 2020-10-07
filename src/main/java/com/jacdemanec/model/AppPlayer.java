package com.jacdemanec.model;

import com.jacdemanec.controller.PlayerNotFoundException;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class AppPlayer {

	@Id
	@GeneratedValue
	private Long id;

	private String aliasString;

	@Column(name="gpgs_id")
	private String gpgsId;

	private int score;

	private int classification;
	
	private int lines_score;
	
	private int level_score;

	public AppPlayer(String aliasString, String gpgsId, int score, int classification, int lines_score, int level_score) {
		this.aliasString = aliasString;
		this.gpgsId = gpgsId;
		this.score = score;
		this.classification = classification;
		this.lines_score = lines_score;
		this.level_score = level_score;
	}

	public AppPlayer() {
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

	public String getGpgsId() {
		return gpgsId;
	}

	public void setGpgsId(String gpgsId) {
		this.gpgsId = gpgsId;
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
		return Objects.hash(this.id, this.aliasString, this.gpgsId, this.score, this.lines_score, this.level_score);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (!(obj instanceof AppPlayer)) return false;
		AppPlayer appPlayer = (AppPlayer) obj;
		return Objects.equals(this.id, appPlayer.id)
				&& Objects.equals(this.aliasString, appPlayer.aliasString)
				&& Objects.equals(this.gpgsId, appPlayer.gpgsId)
				&& Objects.equals(this.score, appPlayer.score)
				&& Objects.equals(this.lines_score, appPlayer.lines_score)
				&& Objects.equals(this.level_score, appPlayer.level_score);
 	}

	@Override
	public String toString() {
		return "Player [id=" + id + ", alias=" + aliasString + ", gpgsId=" + gpgsId + ", score=" + score +
				", lines=" + lines_score + ", level=" + level_score + "]";
	}

}
