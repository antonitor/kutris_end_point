package com.jacdemanec.model;

import com.jacdemanec.controller.PlayerNotFoundException;

import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.*;

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

	@Basic
	private LocalDateTime lastAliasUpdate;

	public AppPlayer(String aliasString, String gpgsId, int score, int classification, int lines_score, int level_score, LocalDateTime lastAliasUpdate) {
		this.aliasString = aliasString;
		this.gpgsId = gpgsId;
		this.score = score;
		this.classification = classification;
		this.lines_score = lines_score;
		this.level_score = level_score;
		this.lastAliasUpdate = lastAliasUpdate;
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

	public LocalDateTime getLastAliasUpdate() {
		return lastAliasUpdate;
	}

	public void setLastAliasUpdate(LocalDateTime lastAliasUpdate) {
		this.lastAliasUpdate = lastAliasUpdate;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, aliasString, gpgsId, score, classification, lines_score, level_score, lastAliasUpdate);
	}


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		AppPlayer appPlayer = (AppPlayer) o;
		return score == appPlayer.score &&
				classification == appPlayer.classification &&
				lines_score == appPlayer.lines_score &&
				level_score == appPlayer.level_score &&
				Objects.equals(id, appPlayer.id) &&
				Objects.equals(aliasString, appPlayer.aliasString) &&
				Objects.equals(gpgsId, appPlayer.gpgsId) &&
				Objects.equals(lastAliasUpdate, appPlayer.lastAliasUpdate);
	}

	@Override
	public String toString() {
		return "{" +
				"id=" + id +
				", aliasString='" + aliasString + '\'' +
				", gpgsId='" + gpgsId + '\'' +
				", score=" + score +
				", lines_score=" + lines_score +
				", level_score=" + level_score +
				'}';
	}
}
