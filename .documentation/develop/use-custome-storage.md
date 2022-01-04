# 开发 - 自定义存储源

在某些情况下，插件提供的几种存储方式并不能满足您的需求，此时您可以选择在您自己的插件中自定义本插件的存储源。

## 1. 修改 plugin.yml

您需要在您自己的插件中声明依赖了本插件，即在 `plugin.yml` 中添加以下内容：

```yaml
softdepend:
  - UltraDepository 
```

添加后，Bukkit会让您的插件在本插件之后加载，此时您就可以应用您的存储源。

## 2. 依赖本插件

请依据 [开发指南](../README.md) 中的依赖介绍部分完成对本插件的依赖。

## 3. 实现 DataStorage

您需要在您的插件中实现 DataStorage 类，并实现其中的功能，他看起来像是这样的：

```java

import cc.carm.plugin.ultradepository.data.UserData;
import cc.carm.plugin.ultradepository.storage.DataStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;

import java.util.UUID;

public class CustomStorage implements DataStorage {

	@Override
	public boolean initialize() {
		//初始化存储，在这里可进行连接数据库、创建表等操作。

		return true; //返回true代表初始化成功，若失败则插件将不再加载
	}

	@Override
	public void shutdown() {
		// 插件卸载时触发，一般用于释放连接池。
	}

	@Override
	public @Nullable UserData loadData(@NotNull UUID uuid) throws Exception {
		// 加载玩家数据部分
		// 若抛出错误，则视为加载出错，会采用临时玩家数据的形式保证插件继续运行，同时在后台提示检查。
		// 返回空则代表暂无该玩家数据，会自动视作新数据
		return null;
	}

	@Override
	public void saveUserData(@NotNull UserData data) throws Exception {
		// 保存玩家数据部分
		// 若抛出错误，则视为保存出错，将在后台提示检查。
	}

}
```

您也可以 [点击这里](../../src/main/java/cc/carm/plugin/ultradepository/storage/impl) 参考本插件提供的其他已实现的存储方式，并在此基础上开发您的自定义存储。

> 若您需要JSON格式存储，可以直接继承 [`JSONStorage`](../../src/main/java/cc/carm/plugin/ultradepository/storage/impl/JSONStorage.java) ，并重写相关方法。

## 4. 应用您的存储

您需要在插件加载(`onLoad()`)时，应用您的自定义存储。

```java

import cc.carm.plugin.ultradepository.storage.DataStorage;
import cc.carm.plugin.ultradepository.storage.StorageMethod;
import cc.carm.plugin.ultradepository.storage.impl.CustomStorage;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.function.Supplier;

public class YourPlugin extends JavaPlugin {

	@Override
	public void onLoad() {

		// 应用您的存储方式
		StorageMethod.CUSTOM.setStorageSupplier(new Supplier<DataStorage>() {
			@Override
			public DataStorage get() {
				return new CustomStorage();
			}
		});

		// 简化后大概长这样(lamda)
		StorageMethod.CUSTOM.setStorageSupplier(CustomStorage::new);
	}
}
```

## 5. 修改本插件的 config.yml

您需要修改本插件的 `config.yml` 中的 `storage.method` 为 **CUSTOM** 。

修改完成后，在插件下次启动时就将应用您实现的 **DataStorage** 从而完成自定义存储源。