import cc.carm.lib.githubreleases4j.GithubRelease;
import cc.carm.lib.githubreleases4j.GithubReleases4J;
import org.junit.Test;

import java.util.List;

public class ReleasesTest {

	@Test
	public void onTest() {

		List<GithubRelease> releases = GithubReleases4J.listReleases("CarmJos", "UltraDepository");

		for (GithubRelease release : releases) {
			System.out.println("#" + release.getID() + " (:" + release.getTagName() + ")" + " " + release.getName());
			System.out.println("- " + release.getHTMLUrl());
		}

	}
}
