package cc.carm.plugin.ultradepository.configuration;


import cc.carm.lib.easyplugin.configuration.language.EasyMessageList;
import cc.carm.lib.easyplugin.configuration.language.MessagesRoot;
import cc.carm.lib.easyplugin.configuration.language.MessagesSection;

public class PluginMessages extends MessagesRoot {

    @MessagesSection("help")
    public static class Usages {

        public static final EasyMessageList CONSOLE = new EasyMessageList(
                "&6&l超级仓库 &f后台指令帮助",
                "&8#&f info &6<玩家> &e[仓库ID] &e[物品ID]",
                "&8-&7 得到玩家的相关物品信息。",
                "&8#&f add &6<玩家> &6<仓库ID> &6<物品ID> &6<数量>",
                "&8-&7 为玩家添加对应仓库中对于物品的数量。",
                "&8#&f remove &6<玩家> &6<仓库ID> &6<物品ID> &e[数量]",
                "&8-&7 为玩家减少对应仓库中对于物品的数量。",
                "&8-&7 若不填写数量，则清空对应仓库的对应物品。",
                "&8#&f sell &6<玩家> &e[仓库ID] &e[物品ID] &e[数量]",
                "&8-&7 为玩家售出相关物品。",
                "&8-&7 若不填写数量，则售出所有对应仓库的对应物品。",
                "&8-&7 若不填写物品，则售出对应仓库内所有物品。",
                "&8-&7 若不填写仓库，则售出所有仓库内所有物品。",
                "&8-&7 该指令受到玩家每日售出数量的限制。"
        );

        public static final EasyMessageList PLAYER = new EasyMessageList(
                "&6&l超级仓库 &f玩家指令帮助",
                "&8#&f open &e[仓库ID]",
                "&8-&7 打开对应仓库的界面。",
                "&8#&f sell &6<仓库ID> &6<物品ID> &6<数量>",
                "&8-&7 售出对应数量的对应物品。",
                "&8-&7 该指令受到玩家每日售出数量的限制。",
                "&8#&f sellAll &e[仓库ID] &e[物品ID]",
                "&8-&7 该指令受到玩家每日售出数量的限制。"
        );

    }

    public static final EasyMessageList ITEM_SOLD = new EasyMessageList(
            new String[]{"&f您出售了 &r%(item)&7x%(amount) &f，共赚取 &6%(money) &f元。"},
            new String[]{"%(item)", "%(amount)", "%(money)"}
    );

    public static final EasyMessageList ITEM_SOLD_LIMIT = new EasyMessageList(
            new String[]{"&f该物品今日剩余可出售额度为 &a%(amount)&8/%(limit) &f个。"},
            new String[]{"%(amount)", "%(limit)"}
    );

    public static final EasyMessageList ITEM_PICKUP = new EasyMessageList(
            new String[]{"&f您拾取了 &r%(item)&7x%(amount) &f，已自动放入到您的仓库中。"},
            new String[]{"%(item)", "%(amount)"}
    );

    public static final EasyMessageList ITEM_COLLECT = new EasyMessageList(
            new String[]{"&f您收集了 &r%(item)&7x%(amount) &f，已自动放入到您的 &6%(depository) &f中。"},
            new String[]{"%(item)", "%(amount)", "%(depository)"}
    );


    public static final EasyMessageList NO_SPACE = new EasyMessageList(
            "&f您仓库内没有足够的空间取出物品！"
    );

    public static final EasyMessageList NO_ECONOMY = new EasyMessageList(
            "&f本服务器暂未启用出售功能。"
    );

    public static final EasyMessageList NO_DEPOSITORY = new EasyMessageList(
            "&f不存在该仓库，请检查仓库ID是否正确。"
    );

    public static final EasyMessageList NO_ITEM = new EasyMessageList(
            "&f仓库中不存在该物品，请检查物品ID是否正确。"
    );

    public static final EasyMessageList NO_ENOUGH_ITEM = new EasyMessageList(
            "&f仓库中不存在足够的物品。"
    );

    public static final EasyMessageList WRONG_NUMBER = new EasyMessageList(
            "&f数目输入错误，请输入正确的数字！"
    );

}
