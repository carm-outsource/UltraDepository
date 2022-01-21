package cc.carm.plugin.ultradepository.hooker;

import cc.carm.lib.githubreleases4j.GithubRelease;
import cc.carm.lib.githubreleases4j.GithubReleases4J;
import cc.carm.plugin.ultradepository.UltraDepository;

import java.util.List;

public class UpdateChecker {

	public static void checkUpdate(UltraDepository plugin) {
		plugin.getScheduler().runAsync(() -> {

			try {

				List<GithubRelease> releases = GithubReleases4J.listReleases("CarmJos", "UltraDepository");
				if (releases.isEmpty()) throw new NullPointerException(); // 无法获取更新

				String currentVersion = plugin.getDescription().getVersion();
				int i = 0;

				for (GithubRelease release : releases) {
					if (release.getTagName().equalsIgnoreCase(currentVersion)) {
						break;
					}
					i++;
				}

				if (i > 0) {
					GithubRelease latestRelease = releases.get(0);
					plugin.log("检查更新完成，当前已落后 " + i + " 个版本，最新版本为 &6&l" + latestRelease.getTagName() + " &r。");
					plugin.log("最新版本下载地址&e " + latestRelease.getHTMLUrl());
				} else {
					plugin.log("检查更新完成，当前已是最新版本。");
				}

			} catch (Exception exception) {
				plugin.error("检查更新失败，请您定期查看插件是否更新，避免安全问题。");
				plugin.error("插件下载地址&e https://github.com/CarmJos/UltraDepository/releases");
			}

		});
	}


}
