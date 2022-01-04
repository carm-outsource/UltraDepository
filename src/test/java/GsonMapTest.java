import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.lang.reflect.Method;
import java.util.*;

public class GsonMapTest {

	private static final Gson GSON = new Gson();

	@org.junit.Test
	public void test() {
		System.out.println(this.getClass().getSimpleName());


		List<Test> tests = new ArrayList<>();
		tests.add(new Test1());
		tests.add(new Test2());
		tests.add(new Test3());
		tests.stream().map(test -> test.getClass().getSimpleName() + " : " + test.isOverride("load")).forEach(System.out::println);

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

		JsonObject dataObject = new JsonObject();
		dataObject.addProperty("date", 20201011);
		dataObject.add("depositories", GSON.toJsonTree(values));

		System.out.println(GSON.toJson(dataObject));
	}

	public interface Test {

		void load();

		default boolean isOverride(String methodName) {
			Map<Method, Method> methodMap = new HashMap<>();
			Arrays.stream(Test.class.getDeclaredMethods())
					.filter(method -> method.getName().equals(methodName))
					.forEach(method -> Arrays.stream(getClass().getDeclaredMethods())
							.filter(extend -> extend.getName().equals(methodName))
							.filter(extend -> extend.getReturnType().equals(method.getReturnType()))
							.filter(extend -> extend.getParameterTypes().length == method.getParameterTypes().length)
							.findFirst().ifPresent(extendMethod -> methodMap.put(method, extendMethod))
					);
			return !methodMap.isEmpty();
		}

	}

	public static class Test1 implements Test {


		@Override
		public void load() {
			System.out.println("test1");
		}
	}

	public static class Test2 extends Test1 {

	}

	public static class Test3 extends Test2 {

		@Override
		public void load() {
		}
	}


}
