package ch.nomoresecrets.ngsqueeze.presentation;

import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import ch.nomoresecrets.ngsqueeze.domain.Track;
import ch.nomoresecrets.ngsqueeze.search.ISearchable;

@ManagedBean(name = "searchBean")
@RequestScoped
public class SearcherBean {

	@EJB
	private ISearchable searcher;

	private List<Track> tracks;

	public List<Track> getTracks() {
		return tracks;
	}

	public void setTracks(List<Track> tracks) {
		this.tracks = tracks;
	}

	private String keyword;

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public void getResults() {
		if (keyword != null || keyword != "") {
			tracks = searcher.searchAnyString(keyword);
		}
	}
}
