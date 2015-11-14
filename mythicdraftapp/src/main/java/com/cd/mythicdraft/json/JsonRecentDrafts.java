package com.cd.mythicdraft.json;

import java.io.Serializable;
import java.util.List;

public class JsonRecentDrafts implements Serializable {

	private static final long serialVersionUID = -8485990059723271817L;
	
	private List<JsonDraft> recentDrafts;
	private Integer pages;
	
	public List<JsonDraft> getRecentDrafts() {
		return recentDrafts;
	}
	public void setRecentDrafts(List<JsonDraft> recentDrafts) {
		this.recentDrafts = recentDrafts;
	}
	public Integer getPages() {
		return pages;
	}
	public void setPages(Integer pages) {
		this.pages = pages;
	}
}
