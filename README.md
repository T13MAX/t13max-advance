# 卡牌对战玩法实现 包含

### 模块

| 模块                | 名称     | 描述                                      |  
|:------------------|:-------|:----------------------------------------|
| [common-game]     | 一些基础东东 |                                         |
| [common-template] | 读表模块   |                                         |
| [common-data]     | 持久化    |                                         |
| [common-proto]    | pb     |                                         |
| [service-battle]  | 卡牌战斗模块 |                                         |
| [service-game]    | 游戏主模块  |                                         |
| [service-gate]    | 网关     | 转发消息到game/battle                        |
| [service-login]   | 登录     |                                         |
| [service-match]   | 匹配     | 匹配服 用于实力相近的玩家进行匹配战斗 支持多玩法匹配 多实例 主从      |
| [service-match]   | 排行榜    | 基于Redis实现排行榜功能                          |
| [service-social]  | 社交服    | 包含好友 黑名单 聊天等功能                          |
| [service-client]  | 客户端    | 写着玩 主要用于调试协议 看效果 目前基于Swing实现 后续改用libgdx |

### 备忘录

* login gateway 
* 功能:  任务/成就(进度) 支付 消息同步(全量/变化 合并)
* game内存管理 存储 活动 道具(装备,资源) 奖励
*
* social
* 
