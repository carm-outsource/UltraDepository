package cc.carm.plugin.ultradepository.hooker;

import cc.carm.lib.githubreleases4j.GithubReleases4J;
import cc.carm.plugin.ultradepository.UltraDepository;

public class UpdateChecker {

	public static void checkUpdate(UltraDepository plugin) {
		plugin.getScheduler().runAsync(() -> {

			Integer behindVersions = GithubReleases4J.getVersionBehind(
					"CarmJos", "UltraDepository",
					plugin.getDescription().getVersion()
			);

			if (behindVersions == null) {
				plugin.error("检查更新失败，请您定期查看插件是否更新，避免安全问题。");
				plugin.error("插件下载地址&e " + GithubReleases4J.getReleasesURL("CarmJos", "UltraDepository"));
			} else {
				if (behindVersions > 0) {
					plugin.log("检查更新完成，当前已落后 " + behindVersions + " 个版本。");
					plugin.log("最新版本下载地址&e " + GithubReleases4J.getLatestReleaseURL("CarmJos", "UltraDepository"));
				} else {
					plugin.log("检查更新完成，当前已是最新版本。");
				}
			}

		});
	}


}
