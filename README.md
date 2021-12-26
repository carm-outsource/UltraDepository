```text
 _    _ _ _             ____             _                     _    
| |  | | | |           |  _ \           | |                   | |   
| |  | | | |_ _ __ __ _| |_) | __ _  ___| | ___ __   __ _  ___| | __
| |  | | | __| '__/ _` |  _ < / _` |/ __| |/ / '_ \ / _` |/ __| |/ /
| |__| | | |_| | | (_| | |_) | (_| | (__|   <| |_) | (_| | (__|   < 
 \____/|_|\__|_|  \__,_|____/ \__,_|\___|_|\_\ .__/ \__,_|\___|_|\_\
                                             | |                    
                                             |_|                    
```

# UltraBackpack

[![version](https://img.shields.io/github/v/release/CarmJos/UltraBackpack)](https://github.com/CarmJos/UltraBackpack/releases)
[![License](https://img.shields.io/github/license/CarmJos/UltraBackpack)](https://opensource.org/licenses/GPL-3.0)
[![workflow](https://github.com/CarmJos/UltraBackpack/actions/workflows/maven.yml/badge.svg?branch=master)](https://github.com/CarmJos/UltraBackpack/actions/workflows/maven.yml)
![CodeSize](https://img.shields.io/github/languages/code-size/CarmJos/UltraBackpack)
![](https://visitor-badge.glitch.me/badge?page_id=UltraBackpack.readme)

超级背包插件，支持设定不同物品的存储背包。

本插件基于Spigot实现，**理论上支持全版本**。

本插件由 [墨豆Mordo](https://www.zimrs.cn) 请求本人开发，经过授权后开源。

## 效果预览

## 插件依赖

- **[必须]** 插件本体基于 [Spigot-API](https://hub.spigotmc.org/stash/projects/SPIGOT)、[BukkitAPI](http://bukkit.org/) 实现。
- **[必须]** 经济部分基于 [VaultAPI](https://github.com/MilkBowl/VaultAPI) 实现。
- **[自带]** 数据部分基于 [EasySQL](https://github.com/CarmJos/EasySQL) 实现。
- **[可选]** 变量部分基于 [PlaceholderAPI](https://www.spigotmc.org/resources/6245/) 实现。

详细依赖列表可见 [Dependencies](https://github.com/CarmJos/UltraBackpack/network/dependencies) 。

## 特殊优势

## 插件指令

指令主指令为 /UltraBackpack (/ub | /backpack)

<details>
<summary>展开查看所有子指令</summary>

```text
# sell <背包ID> <物品ID> <数量>
@ 玩家指令 (UltraBackpack.Command.Sell)
- 售出对应数量的对应物品。
- 该指令受到玩家每日售出数量的限制。

# sellAll [背包ID] [物品ID]
@ 玩家指令 (UltraBackpack.Command.SellAll)
- 售出所有相关物品。
- 该指令受到玩家每日售出数量的限制。


# info <玩家> [背包ID] [物品ID] 
@ 管理指令 (UltraBackpack.admin)
- 得到玩家的相关物品信息。

# add <玩家> <背包ID> <物品ID> <数量>
@ 管理指令 (UltraBackpack.admin)
- 为玩家添加对应背包中对于物品的数量。

# remove <玩家> <背包ID> <物品ID> <数量>
@ 管理指令 (UltraBackpack.admin)
- 为玩家减少对应背包中对于物品的数量。

# sell <玩家> [背包ID] [物品ID] [数量]
@ 管理指令 (UltraBackpack.admin)
- 为玩家售出相关物品。
- 若不填写数量，则售出所有对应背包的对应物品。
- 若不填写物品，则售出对应背包内所有物品。
- 若不填写背包，则售出所有背包内所有物品。
- 该指令受到玩家每日售出数量的限制。
```

</details>

## 插件变量 ([PlaceholderAPI](https://www.spigotmc.org/resources/6245/))

变量部分基于 [PlaceholderAPI](https://www.spigotmc.org/resources/6245/) 实现，如需使用变量请安装其插件。

<details>
<summary>展开查看所有变量</summary>

```text
# %UltraBackpack_amount_<背包ID>_<物品ID>%
- 得到对应背包内对应物品的数量

# %UltraBackpack_price_<背包ID>_<物品ID>%
- 得到对应背包内对应物品的价格

# %UltraBackpack_sold_<背包ID>_<物品ID>%
- 得到对应背包内对应物品的今日售出数量

# %UltraBackpack_limit_<背包ID>_<物品ID>%
- 得到对应背包内对应物品的每日售出限制

# %UltraBackpack_remain_<背包ID>_<物品ID>%
- 得到对应背包内对应物品的剩余可售出数量
- $剩余可售出数量 = $每日售出限制 - $今日售出数量

# %UltraBackpack_capacity_<背包ID>%
- 得到对应背包的容量
```

</details>

## 插件权限

## 配置文件

### [插件配置文件](ultrabackpack-plugin/src/main/resources/config.yml) (config.yml)

详见源文件。

### [消息配置文件](ultrabackpack-plugin/src/main/resources/messages.yml) (messages.yml)

详见源文件。

### 背包类型配置文件 (backpacks/<ID>.yml)

所有 背包类均为单独的配置文件，存放于 `插件配置目录/backpacks` 下，便于管理。

文件名即背包的ID，理论上可以随便取，但强烈推荐使用纯英文，部分符号可能会影响正常读取，请避免使用。

<details>
<summary>展开查看示例背包配置</summary>

```yaml

```

</details>

## 支持与捐赠

若您觉得本插件做的不错，您可以捐赠支持我！

<img height=25% width=25% src="https://raw.githubusercontent.com/CarmJos/CarmJos/main/img/donate-code.jpg"  alt=""/>

## 开源协议

本项目源码采用 [GNU General Public License v3.0](https://opensource.org/licenses/GPL-3.0) 开源协议。

<details>
<summary>关于 GPL 协议</summary>

> GNU General Public Licence (GPL) 有可能是开源界最常用的许可模式。GPL 保证了所有开发者的权利，同时为使用者提供了足够的复制，分发，修改的权利：
>
> #### 可自由复制
> 你可以将软件复制到你的电脑，你客户的电脑，或者任何地方。复制份数没有任何限制。
> #### 可自由分发
> 在你的网站提供下载，拷贝到U盘送人，或者将源代码打印出来从窗户扔出去（环保起见，请别这样做）。
> #### 可以用来盈利
> 你可以在分发软件的时候收费，但你必须在收费前向你的客户提供该软件的 GNU GPL 许可协议，以便让他们知道，他们可以从别的渠道免费得到这份软件，以及你收费的理由。
> #### 可自由修改
> 如果你想添加或删除某个功能，没问题，如果你想在别的项目中使用部分代码，也没问题，唯一的要求是，使用了这段代码的项目也必须使用 GPL 协议。
>
> 需要注意的是，分发的时候，需要明确提供源代码和二进制文件，另外，用于某些程序的某些协议有一些问题和限制，你可以看一下 @PierreJoye 写的 Practical Guide to GPL Compliance 一文。使用 GPL 协议，你必须在源代码代码中包含相应信息，以及协议本身。
>
> *以上文字来自 [五种开源协议GPL,LGPL,BSD,MIT,Apache](https://www.oschina.net/question/54100_9455) 。*
</details>
