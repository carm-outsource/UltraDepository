import org.junit.Test;

public class PluginNameTest {


	@Test
	public void test() {
		outputPlugin();
	}


	public static void outputPlugin() {
		log(" _    _ _ _             _____                       _ _                   ");
		log("| |  | | | |           |  __ \\                     (_) |                  ");
		log("| |  | | | |_ _ __ __ _| |  | | ___ _ __   ___  ___ _| |_ ___  _ __ _   _ ");
		log("| |  | | | __| '__/ _` | |  | |/ _ \\ '_ \\ / _ \\/ __| | __/ _ \\| '__| | | |");
		log("| |__| | | |_| | | (_| | |__| |  __/ |_) | (_) \\__ \\ | || (_) | |  | |_| |");
		log(" \\____/|_|\\__|_|  \\__,_|_____/ \\___| .__/ \\___/|___/_|\\__\\___/|_|   \\__, |");
		log("                                   | |                               __/ |");
		log("                                   |_|                              |___/ ");
	}

	private static void log(String s) {
		System.out.println(s);
	}

}
