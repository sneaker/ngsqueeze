package ch.nomoresecrets.ngsqueeze.domain;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity(name="Track")
public class Track implements Serializable, Comparable<Track> {
	
	private static final long serialVersionUID = 3306191759760511051L;
	private long id;
	private int nr;
	private String name;
	private Artist artist;
	private String location;
	
	public Track() {}
	
	public Track(int nr, String name, String location) {
		this.nr = nr;
		this.name = name;
		this.location = location;
	}
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	public int getNr() {
		return nr;
	}
	public void setNr(int nr) {
		this.nr = nr;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@ManyToOne (cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	@JoinColumn(name="artist")
	public Artist getArtist() {
		return artist;
	}
	public void setArtist(Artist artist) {
		this.artist = artist;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}

	public int compareTo(Track o) {
		return getName().compareTo(o.getName());
	}
}
