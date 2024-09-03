# 卡牌对战玩法实现 包含

### 模块

| 模块                | 名称     | 描述                                   |  
|:------------------|:-------|:-------------------------------------|
| [common-game]     | 一些基础东东 |                                      |
| [common-data]     | 持久化    |                                      |
| [common-proto]    | pb     |                                      |
| [common-template] | 读表模块   |                                      |
| [service-battle]  | 卡牌战斗模块 |                                      |
| [service-game]    | 游戏主模块  |                                      |
| [service-gate]    | 网关     | 转发消息到game/scene/battle (后续添加)        |
| [service-login]   | 登录     | (后续添加)                               |
| [service-match]   | 匹配     | 匹配服 用于实力相近的玩家进行匹配战斗 支持多玩法匹配 多实例 主从   |
| [service-match]   | 排行榜    | 基于Redis实现排行榜功能                       |
| [service-client]  | 客户端    | 写着玩 理论上不应该用Java写客户端 主要是想把协议调通 且 好看效果 |

### 备忘录

* login gateway match
* 功能:  任务/成就(进度) 支付 消息同步(全量/变化 合并) 全自动打表生成类
* game内存管理 存储 活动 道具(装备,资源) 进行中...
* GC 虚拟线程
* SQLite优化
* match
* rank
