package ch.nomoresecrets.ngsqueeze.search;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import ch.nomoresecrets.ngsqueeze.api.ISearchable;
import ch.nomoresecrets.ngsqueeze.domain.Artist;
import ch.nomoresecrets.ngsqueeze.domain.Track;

@Stateless(mappedName="Searcherli")
@Remote(ISearchable.class)
public class MySearcher implements ISearchable {

	@PersistenceContext
	private EntityManager manager;

	@SuppressWarnings("unchecked")
	public List<Track> search(String s) {
		List<Track> trackresults = manager
				.createQuery("FROM Track WHERE name = ?1").setParameter(1, s)
				.getResultList();
		List<Artist> artist = manager
				.createQuery("FROM Artist WHERE name = ?1").setParameter(1, s)
				.getResultList();

		for (Artist a : artist) {
			trackresults.addAll(a.getTracks());
		}
		return trackresults;
	}

	@SuppressWarnings("unchecked")
	public List<Track> searchAnyString(String s) {
		List<Artist> artists = searchArtist(s);
		List<Track> tracks;
		try {
			Query q = manager.createQuery("From Track WHERE name LIKE ?1")
					.setParameter(1, "%" + s + "%");
			q.setHint("org.hibernate.cacheable", true);
			tracks = q.getResultList();
		} catch (NoResultException e) {
			tracks = new ArrayList<Track>();
		}
		for (Artist a : artists) {
			tracks.addAll(a.getTracks());
		}
		return tracks;
	}

	public Artist searchExactArtist(String s) {
		Query artistquery = manager.createQuery(
				"FROM Artist WHERE name LIKE ?1")
				.setParameter(1, s);
		artistquery.setHint("org.hibernate.cacheable", true);
		return (Artist) artistquery.getSingleResult();
	}
	
	@SuppressWarnings("unchecked")
	public List<Artist> searchArtist(String s) {
		Query artistquery = manager.createQuery(
				"FROM Artist WHERE name LIKE ?1")
				.setParameter(1, "%" + s + "%");
		artistquery.setHint("org.hibernate.cacheable", true);
		List<Artist> artists = artistquery.getResultList();
		return artists;
	}

}
