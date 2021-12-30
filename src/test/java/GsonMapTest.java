import com.google.gson.Gson;
import org.junit.Test;

import java.util.*;

public class GsonMapTest {

	private static final Gson GSON = new Gson();

	@Test
	public void test() {

		Map<String, Map<String, Map<String, Integer>>> values = new LinkedHashMap<>();


		for (int u = 0; u < 3; u++) {
			Map<String, Map<String, Integer>> depositoryDataMap = new LinkedHashMap<>();
			for (int i = 0; i < 5; i++) {
				Map<String, Integer> itemDataMap = new HashMap<>();
				int amount = Math.max(0, new Random().nextInt(15));
				int sold = Math.max(0, new Random().nextInt(15));
				if (amount > 0) itemDataMap.put("amount", amount);
				if (sold > 0) itemDataMap.put("sold", sold);
				if (!itemDataMap.isEmpty()) {
					depositoryDataMap.put(UUID.randomUUID().toString().substring(0, 4), itemDataMap);
				}
			}
			if (!depositoryDataMap.isEmpty()) {
				values.put(UUID.randomUUID().toString().substring(0, 8), depositoryDataMap);
			}
		}

		System.out.println(values.size());


		String jsonValues = GSON.toJson(values);
		System.out.println(jsonValues);
	}


}
