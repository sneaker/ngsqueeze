package ch.nomoresecrets.ngsqueeze.indexer;

import java.io.File;
import java.io.IOException;

import javax.ejb.Remote;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.farng.mp3.MP3File;
import org.farng.mp3.TagException;
import org.farng.mp3.id3.AbstractID3;

import ch.nomoresecrets.ngsqueeze.api.IMyIndexer;
import ch.nomoresecrets.ngsqueeze.domain.Artist;
import ch.nomoresecrets.ngsqueeze.domain.Track;

@Stateful(mappedName="Indexerli")
@Remote(IMyIndexer.class)
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class MyIndexer implements IMyIndexer {

	@PersistenceContext(unitName = "soundrack", type = PersistenceContextType.TRANSACTION)
	private EntityManager entityManager;

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void indexDir(String dir) {
		File f = new File(dir);
		File[] filelist = f.listFiles();
		if (filelist != null) {
			for (File file : filelist) {
				if (file.isDirectory()) {
					System.out.println("found subdir: "
							+ file.getAbsolutePath());
					indexDir(file.getPath());
				} else if (file.isFile() && file.getPath().endsWith("mp3")) {
					System.out.println("found file: " + file.getAbsolutePath());
					File testfile = new File(file.getAbsolutePath());
					indexFile(testfile);
				}
			}
		}
	}

	private void indexFile(File file) {
		try {
			MP3File mpfile = new MP3File(file);
			AbstractID3 id3 = mpfile.getID3v1Tag();
			if (id3 == null) {
				id3 = mpfile.getID3v2Tag();
			}
			if (id3 == null) {
				return;
			}

			String artistname = id3.getLeadArtist();
			String tracknr = id3.getTrackNumberOnAlbum();
			String trackname = id3.getSongTitle();

			if (artistname == null || tracknr == null || trackname == null
					|| artistname == "" || tracknr == "" || trackname == "") {
				System.err.println("Something is null, skipping");
				return;
			}

			Artist artist = saveArtist(artistname);
			saveTrack(file, tracknr, trackname, artist);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TagException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void saveTrack(File file, String tracknr, String trackname,
			Artist artist) {
		if (artist == null){
			return;
		}
		Query q2 = entityManager
				.createQuery("FROM Track WHERE name = ?1 AND artist.id = ?2");
		q2.setParameter(1, trackname);
		q2.setParameter(2, artist.getId());
		Track track;
		try {
			track = (Track) q2.getSingleResult();
		} catch (NoResultException e) {
			try {
				Integer tnr = new Integer(tracknr);
				track = new Track(tnr, trackname, file.getAbsolutePath());
				track.setArtist(artist);
				entityManager.persist(track);
			} catch (NumberFormatException e1) {
				e1.printStackTrace();
			}
		}
	}

	private Artist saveArtist(String artistname) {
		System.err.println("Saving: " + artistname);
		Query artistQuery = entityManager
				.createQuery("FROM Artist WHERE name = ?1");
		artistQuery.setParameter(1, artistname);
		Artist artist = null;
		try {
			artist = (Artist) artistQuery.getSingleResult();
		} catch (NoResultException e) {
			artist = new Artist(artistname);
			entityManager.persist(artist);
		} catch (PersistenceException e) {
			e.printStackTrace();
			return null;
		}
		return artist;
	}
}
