package ch.nomoresecrets.ngsqueeze.indexer;

import javax.ejb.EJB;
import javax.naming.NamingException;

import org.farng.mp3.TagException;
import org.farng.mp3.id3.ID3v1_1;
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

import ch.nomoresecrets.ngsqueeze.api.ISearchable;
import ch.nomoresecrets.ngsqueeze.domain.Artist;
import ch.nomoresecrets.ngsqueeze.domain.Track;
import ch.nomoresecrets.ngsqueeze.search.MySearcher;

@RunWith(Arquillian.class)
public class SearcherTest {

	@EJB(mappedName="Searcherli")
	private ISearchable searcher;

	@Deployment
	public static Archive<?> createTestArchive() {
		return ShrinkWrap
				.create(JavaArchive.class, "test2.jar")
				.addPackage(ID3v1_1.class.getPackage())
				.addPackage(TagException.class.getPackage())
				.addPackage(ObjectNumberHashMap.class.getPackage())
				.addClasses(MySearcher.class, ISearchable.class, Artist.class, Track.class, AbstractMP3Object.class)
				.addAsManifestResource(EmptyAsset.INSTANCE,
						ArchivePaths.create("beans.xml"));
	}
	
	@Test
	public void testIsDeployed() throws NamingException {
		Assert.assertNotNull(searcher);
	}
}
