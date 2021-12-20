package cc.carm.plugin.ultrabackpack.api.data;

import cc.carm.lib.easysql.api.SQLManager;

import java.sql.SQLException;

public enum DatabaseTables {

	USER_DATA("ub_data", new String[]{
			"`uuid` VARCHAR(36) NOT NULL PRIMARY KEY", // 用户的UUID
			"`backpack` VARCHAR(32) NOT NULL",// 背包组名
			"`type` VARCHAR(32) NOT NULL",// 背包内具体物品类型
			"`amount` INT(11) NOT NULL DEFAULT 0", // 该物品的数量
			"`sold` INT(11) NOT NULL DEFAULT 0", // 一周卖出次数
			"`day` DATE NOT NULL", // 记录卖出数量的所在天
			"PRIMARY KEY `data`(`uuid`,`backpack`,`type`)" // 联合主键索引
	}),
	;

	String name;
	String[] columns;

	DatabaseTables(String name, String[] columns) {
		this.name = name;
		this.columns = columns;
	}


	public static void createTables(SQLManager sqlManager) throws SQLException {
		for (DatabaseTables value : DatabaseTables.values()) {
			sqlManager.createTable(value.getName())
					.setColumns(value.getColumns())
					.build().execute();
		}
	}

	public String getName() {
		return name;
	}

	public String[] getColumns() {
		return columns;
	}

}
