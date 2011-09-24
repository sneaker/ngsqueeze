package ch.nomoresecrets.ngsqueeze.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity(name="Artist")
public class Artist implements Serializable {
	
	private static final long serialVersionUID = 1830857161475302624L;
	private long id;

	private String name;
	private List<Track> tracks;
	
	public Artist() {}
	
	public Artist(String name) {
		this.name = name;
	}
	
	@Id
	@GeneratedValue
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@OneToMany (mappedBy="artist", fetch=FetchType.EAGER)
	public List<Track> getTracks() {
		return tracks;
	}
	public void setTracks(List<Track> tracks) {
		this.tracks = tracks;
	}
}
