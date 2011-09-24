package ch.nomoresecrets.ngsqueeze.indexer;

import java.util.List;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
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
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.nomoresecrets.ngsqueeze.api.IMyIndexer;
import ch.nomoresecrets.ngsqueeze.domain.Artist;
import ch.nomoresecrets.ngsqueeze.domain.Track;

@RunWith(Arquillian.class)
public class IndexerTest {

	@Inject
	UserTransaction utx;

	@PersistenceContext(unitName = "soundrack")
	EntityManager em;

	@EJB(mappedName = "Indexerli")
	private IMyIndexer indexer;

	@Deployment
	public static Archive<?> createTestArchive() {
		return ShrinkWrap
				.create(JavaArchive.class, "test.jar")
				.addPackage(ID3v1_1.class.getPackage())
				.addPackage(AbstractLyrics3.class.getPackage())
				.addPackage(FilenameTag.class.getPackage())
				.addPackage(TagException.class.getPackage())
				.addPackage(ObjectNumberHashMap.class.getPackage())
				.addClasses(MyIndexer.class, IMyIndexer.class, Artist.class,
						Track.class, AbstractMP3Object.class)
				.addAsManifestResource(EmptyAsset.INSTANCE,
						ArchivePaths.create("beans.xml"))
				.addAsManifestResource(EmptyAsset.INSTANCE,
						ArchivePaths.create("beans.xml"))
				.addAsManifestResource("test-persistence.xml",
						ArchivePaths.create("persistence.xml"));
	}

	@Test
	public void testIndexing() throws NotSupportedException, SystemException,
			SecurityException, IllegalStateException, RollbackException,
			HeuristicMixedException, HeuristicRollbackException {
		Assert.assertNotNull(indexer);
		utx.begin();
		indexer.indexDir("/home/thomas/jeeworkspace1/newejbClient/src/test/resources/");
		em.joinTransaction();
		List<Track> tracks = em.createQuery("select t from Track t",
				Track.class).getResultList();
		System.out.println("found: " + tracks.size() + " tracks");
		Assert.assertEquals(29, tracks.size());
		utx.commit();
	}
}
