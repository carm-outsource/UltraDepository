package cc.carm.plugin.ultrabackpack.api.configuration.gui;

import java.util.Map;

public interface GUIConfiguration {


	String getTitle();

	int getLines();

	Map<Integer, GUIItemDetail> getItems();

}
