package cc.carm.plugin.ultradepository.configuration.depository;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DepositoryCapacity {

	int defaultCapacity;
	Map<String, Integer> permissions;

	public DepositoryCapacity(int defaultCapacity, List<String> permissionStrings) {
		this.defaultCapacity = defaultCapacity;
		Map<String, Integer> permissions = new HashMap<>();
		permissionStrings.stream()
				.filter(s -> s.contains(":"))
				.map(s -> s.split(":", 1))
				.forEach(args -> {
					try {
						permissions.put(args[0], Integer.parseInt(args[1]));
					} catch (Exception ignored) {
					}
				});
		this.permissions = permissions;
	}


	public DepositoryCapacity(int defaultCapacity, Map<String, Integer> permissions) {
		this.defaultCapacity = defaultCapacity;
		this.permissions = permissions;
	}

	public int getDefault() {
		return defaultCapacity;
	}

	public @NotNull Map<String, Integer> getPermissions() {
		return permissions;
	}

	public int getPlayerCapacity(Player player) {
		if (defaultCapacity == -1) return -1;

		int capacity = defaultCapacity;
		for (Map.Entry<String, Integer> entry : getPermissions().entrySet()) {
			if (player.hasPermission(entry.getKey())) {
				int value = entry.getValue();
				if (value == -1) {
					return -1;
				} else if (value > capacity) {
					capacity = value;
				}
			}
		}
		
		return capacity;
	}

}
