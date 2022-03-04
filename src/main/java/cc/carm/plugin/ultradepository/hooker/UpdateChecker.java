package cc.carm.plugin.ultradepository.hooker;

import cc.carm.lib.githubreleases4j.GithubReleases4J;
import cc.carm.plugin.ultradepository.Main;
import cc.carm.plugin.ultradepository.UltraDepository;

public class UpdateChecker {

	public static void checkUpdate(Main plugin) {
		plugin.getScheduler().runAsync(() -> {

			Integer behindVersions = GithubReleases4J.getVersionBehind(
					"CarmJos", "UltraDepository",
					plugin.getDescription().getVersion()
			);

			String downloadURL = GithubReleases4J.getReleasesURL("CarmJos", "UltraDepository");

			if (behindVersions == null) {
				plugin.error("检查更新失败，请您定期查看插件是否更新，避免安全问题。");
				plugin.error("下载地址 " + downloadURL);
			} else if (behindVersions == 0) {
				plugin.log("检查完成，当前已是最新版本。");
			} else if (behindVersions > 0) {
				plugin.log("发现新版本! 目前已落后 " + behindVersions + " 个版本。");
				plugin.log("最新版下载地址 " + downloadURL);
			} else {
				plugin.error("检查更新失败! 当前版本未知，请您使用原生版本以避免安全问题。");
				plugin.error("最新版下载地址 " + downloadURL);
			}

		});
	}


}
