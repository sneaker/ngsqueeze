package ch.nomoresecrets.ngsqueeze.indexer;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.nomoresecrets.ngsqueeze.domain.Artist;
import ch.nomoresecrets.ngsqueeze.domain.Track;

@RunWith(Arquillian.class)
public class TrackTest {

	@Inject UserTransaction utx;
	
	@PersistenceContext EntityManager em;
	
	@Deployment public static JavaArchive createDeployment() {
	return ShrinkWrap.create(JavaArchive.class, "test3.jar")
            .addClasses(Track.class, Artist.class)
            .addAsManifestResource(EmptyAsset.INSTANCE,ArchivePaths.create("beans.xml"))
            .addAsManifestResource("test-persistence.xml", ArchivePaths.create("persistence.xml"));
	}
	
	@Test
	public void createNewTrackAndInsertit() throws NotSupportedException, SystemException, SecurityException, IllegalStateException, RollbackException, HeuristicMixedException, HeuristicRollbackException {
		utx.begin();
		em.createQuery("delete from Track");
		
		System.out.println("Selecting (using JPQL)...");
		Track t = new Track(1, "titel", "/home/wherever");
		em.persist(t);
		utx.commit();
		
		utx.begin();
		em.joinTransaction();
		List<Track> tracks = em.createQuery("select t from Track t", Track.class).getResultList();
		System.out.println("found: " + tracks.size() + " tracks");
		Assert.assertEquals(1, tracks.size());
		utx.commit();
	}
}
