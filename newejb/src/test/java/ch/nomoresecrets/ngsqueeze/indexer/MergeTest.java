package ch.nomoresecrets.ngsqueeze.indexer;

import java.io.IOException;
import java.util.List;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.farng.mp3.TagException;
import org.farng.mp3.filename.FilenameTag;
import org.farng.mp3.id3.ID3v1_1;
import org.farng.mp3.lyrics3.AbstractLyrics3;
import org.farng.mp3.object.AbstractMP3Object;
import org.farng.mp3.object.ObjectNumberHashMap;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.nomoresecrets.ngsqueeze.api.IEditor;
import ch.nomoresecrets.ngsqueeze.api.IMyIndexer;
import ch.nomoresecrets.ngsqueeze.api.ISearchable;
import ch.nomoresecrets.ngsqueeze.domain.Artist;
import ch.nomoresecrets.ngsqueeze.domain.Track;
import ch.nomoresecrets.ngsqueeze.editor.ID3Editor;
import ch.nomoresecrets.ngsqueeze.editor.MyEditor;
import ch.nomoresecrets.ngsqueeze.search.MySearcher;

@RunWith(Arquillian.class)
public class MergeTest {

	@EJB(mappedName = "editorli")
	private IEditor editor;

	@Inject
	private UserTransaction utx;

	@PersistenceContext
	private EntityManager em;

	@Deployment
	public static Archive<?> createTestArchive() {
		return ShrinkWrap
				.create(JavaArchive.class, "test4.jar")
				.addPackage(ID3v1_1.class.getPackage())
				.addPackage(AbstractLyrics3.class.getPackage())
				.addPackage(FilenameTag.class.getPackage())
				.addPackage(TagException.class.getPackage())
				.addPackage(ObjectNumberHashMap.class.getPackage())
				.addClasses(MyIndexer.class, IMyIndexer.class, Artist.class,
						Track.class, AbstractMP3Object.class, MyEditor.class,
						IEditor.class, ISearchable.class, MySearcher.class,
						ID3Editor.class)
				.addAsManifestResource(EmptyAsset.INSTANCE,
						ArchivePaths.create("beans.xml"))
				.addAsManifestResource(EmptyAsset.INSTANCE,
						ArchivePaths.create("beans.xml"))
				.addAsManifestResource("test-persistence.xml",
						ArchivePaths.create("persistence.xml"));
	}

	private Artist a1;
	private Artist a2;

	@Before
	public void setUp() throws Exception, SystemException {
		System.out.println("called setup");

		a1 = new Artist("Gustav Genial");
		a2 = new Artist("Gustav Genitiv");

		Track t1 = new Track(
				0,
				"first",
				"/home/thomas/jeeworkspace1/newejbClient/src/test/resources/8 Mile Soundtrack/01 - Eminem - 8 Mile - Lose Yourself.mp3");
		Track t2 = new Track(
				1,
				"second",
				"/home/thomas/jeeworkspace1/newejbClient/src/test/resources/8 Mile Soundtrack/15 - Gang Starr - 8 Mile - Battle.mp3");
		t1.setArtist(a1);
		t2.setArtist(a2);

		utx.begin();
		em.persist(t1);
		em.persist(t2);
		utx.commit();
	}

	@After
	public void cleanUp() throws SecurityException, IllegalStateException,
			RollbackException, HeuristicMixedException,
			HeuristicRollbackException, SystemException, Exception {
		utx.begin();
		Query q1 = em.createNativeQuery("DELETE FROM Track");
		q1.executeUpdate();
		Query q2 = em.createNativeQuery("DELETE FROM Artist");
		q2.executeUpdate();
		utx.commit();
		utx.begin();
		List<Track> tracks = em.createQuery("select t from Track t",
				Track.class).getResultList();
		Assert.assertEquals(0, tracks.size());
		List<Artist> artists = em.createQuery("select a from Artist a",
				Artist.class).getResultList();
		Assert.assertEquals(0, artists.size());
		utx.commit();
	}

	@Test
	public void testMerge() throws IOException, TagException, Exception {
		editor.updateArtist(a1.getId(), a2.getName(), true);
		a1 = em.find(Artist.class, a1.getId());
		a2 = em.find(Artist.class, a2.getId());
		Assert.assertNull(a1);
		Assert.assertEquals(2, a2.getTracks().size());
	}
}
