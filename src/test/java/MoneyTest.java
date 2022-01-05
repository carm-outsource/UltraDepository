import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MoneyTest {

	@Test
	public void test() {
		System.out.println(get(1.2, 100));
		System.out.println(get(0.55, 10));
		System.out.println(get(0.21, 5));
	}


	public double get(double price, int amount) {
		BigDecimal money = BigDecimal.valueOf(price * amount).setScale(2, RoundingMode.DOWN);
		return money.doubleValue();
	}
}
