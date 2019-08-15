### MCJointBan:基于GitHub的Minecraft联合封禁插件

本插件是BungeeCord/Bukkit两用插件，插件使用[ConfigureCore](https://github.com/lintx/ConfigureCore-for-Minecraft-plugins)作为前置插件，如果您将它使用在BungeeCord中，您可能还需要SQLite前置（比如[SQLite for BungeeCord](https://www.spigotmc.org/resources/sqlite-for-bungeecord.57191/)）。

#### 配置文件
```yaml
gitHubConfig:
  username: MCJointBan    #封禁列表仓库所在的GitHub用户名
  repository: BanList     #封禁列表的GitHub仓库名
checkConfig:
  onOpen: true            #插件启动时是否检查封禁列表更新，推荐开启
  onTimer: 3600           #插件定时检查封禁列表更新，单位为秒，0为关闭，推荐的数值为600-3600（10分钟-1小时）
#玩家因为被联合封禁而无法进入服务器或被踢出服务器时显示的文字，`{server}`表示提交封禁记录的服务器，`{reason}`为原因，你可以修改它，比如加入一个申诉渠道
banMessage: §c你在 §6{server} §r§c服务器因为 §6{reason} §c被封禁，本服已§6联合封禁§c。
```

#### 插件原理
插件将检查特定的GitHub仓库中的文件，如果发现新记录则将新记录添加到本地的SQLite数据库中，玩家登录时将用UUID和数据库中做比对，如果存在记录且该记录没有被禁用则拒绝玩家登录并给出原因。
默认的封禁仓库是[这里](https://github.com/MCJointBan/BanList)

#### 命令
`/jointban help`显示帮助

`/jointban reload`重新加载配置

`/jointban check`手动检查封禁列表更新

`/jointban forced [all/server]`强制检查所有/特定服务器提交的封禁列表（忽略历史数据）

`/jointban player <player_uuid> <enable/disable>`启用/禁用特定玩家的封禁记录

`/jointban server <server> <enable/disable>`启用/禁用特定服务器提交的封禁列表（禁用后依然会更新该服务器封禁列表，但是该服务器提交的封禁列表内的玩家均做放行处理）

`/jointban serverlist [page]`显示参与的服务器列表

`<>`内的表示必选参数，`[]`内的表示可选参数，参数中以`/`分隔的表示只能从中选择一项

其中，`server`是对应的服务器名（`serverlist`子命令返回的结果中，括号里面的部分），`page`是页码（数字，大于0），`player_uuid`是玩家的UUID