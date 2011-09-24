package ch.nomoresecrets.ngsqueeze.presentation;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import ch.nomoresecrets.ngsqueeze.editor.IEditor;

@ManagedBean(name = "editbean")
@RequestScoped
public class EditingBean {

	@EJB
	private IEditor editor;

	private Long trackid;
	private String track;
	private Long artistid;
	private String artist;
	private boolean merge;

	public String getTrack() {
		return track;
	}

	public void setTrack(String track) {
		this.track = track;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public Long getArtistid() {
		return artistid;
	}

	public void setArtistid(Long artistid) {
		this.artistid = artistid;
	}

	public Long getTrackid() {
		return trackid;
	}

	public void setTrackid(Long trackid) {
		this.trackid = trackid;
	}

	public boolean isMerge() {
		return merge;
	}

	public void setMerge(boolean merge) {
		this.merge = merge;
	}

	public String save() throws Exception {
		if (trackid == null || artistid == null) {
			return "failure";
		}
		if (track != null || artist != "") {
			System.out.println("editing track");
			editor.updateTrack(getTrackid(), track);
		}
		if (artist != null || artist != "") {
			System.out.println("editing artist");
			editor.updateArtist(artistid, artist, merge);
		}
		return "success";
	}
}
