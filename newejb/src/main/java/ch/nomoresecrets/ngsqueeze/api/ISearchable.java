package ch.nomoresecrets.ngsqueeze.api;

import java.util.List;

import ch.nomoresecrets.ngsqueeze.domain.Artist;
import ch.nomoresecrets.ngsqueeze.domain.Track;

public interface ISearchable {

		public List<Track> search(String s);
		
		public List<Track> searchAnyString(String s);
		
		public Artist searchExactArtist(String s);
}
