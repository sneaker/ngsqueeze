package ch.nomoresecrets.ngsqueeze.editor;

import java.io.IOException;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.TransactionRolledbackException;

import org.farng.mp3.TagException;

import ch.nomoresecrets.ngsqueeze.api.IEditor;
import ch.nomoresecrets.ngsqueeze.api.ISearchable;
import ch.nomoresecrets.ngsqueeze.domain.Artist;
import ch.nomoresecrets.ngsqueeze.domain.Track;

@Stateless(mappedName="editorli")
public class MyEditor implements IEditor {

	@PersistenceContext
	private EntityManager manager;

	@EJB
	private ISearchable searcher;

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void updateArtist(long id, String newartistname, boolean merge)
			throws Exception {
		updateId3Artist(id, newartistname);
		updateDbArtist(id, newartistname, merge);
	}

	private void updateDbArtist(long id, String newartistname, boolean merge)
			throws Exception {		
		System.out.println("called");
		Artist toremoveartist = manager.find(Artist.class, id);
		Artist takeoverartist = searcher.searchExactArtist(newartistname);
		if (takeoverartist != null) {
			if (merge) {
				for(Track t: toremoveartist.getTracks()) {
					t.setArtist(takeoverartist);
					manager.merge(t);
					System.out.println("the new artist is: " + t.getArtist().getName());
				}
				manager.merge(takeoverartist);
				manager.remove(toremoveartist);
				System.out.println("removed");
			} else if (takeoverartist.getId()!=toremoveartist.getId()){
				throw new TransactionRolledbackException(
						"An Artist with this name already exists");
			}
		} else {
			toremoveartist.setName(newartistname);
			manager.persist(toremoveartist);
		}
	}

	private void updateId3Artist(long id, String newartistname) throws IOException,
			TagException {
		ID3Editor id3editor = new ID3Editor();
		Artist artist = manager.find(Artist.class, id);
		for (Track t : artist.getTracks()) {
			id3editor.editTrack(t.getLocation(), t.getName(), newartistname);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void updateTrack(long id, String name) throws IOException, TagException {
		updateDbTrack(id, name);
		updateId3Track(id, name);
	}

	private void updateDbTrack(long id, String name) {
		Track track = manager.find(Track.class, id);
		track.setName(name);
		manager.persist(track);

	}

	private void updateId3Track(long id, String name) throws IOException,
			TagException {
		Track track = manager.find(Track.class, id);
		ID3Editor id3editor = new ID3Editor();
		id3editor.editTrack(track.getLocation(), name, track.getArtist().getName());
	}
}
