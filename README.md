# 傻呆呆的游戏

### 模块

| 模块                | 名称     | 描述                                   |  
|:------------------|:-------|:-------------------------------------|
| [common-ai]       | AI     | 状态机 行为树的一些实现                         |
| [common-game]     | 一些基础东东 |                                      |
| [common-data]     | 持久化    |                                      |
| [common-proto]    | pb     |                                      |
| [common-template] | 读表模块   |                                      |
| [common-util]     | 工具类    |                                      |
| [service-battle]  | 卡牌战斗模块 |                                      |
| [service-game]    | 游戏主模块  |                                      |
| [service-scene]   | 场景模块   | 搁置(考虑新弄一个项目写场景 这个项目就是卡牌)             |
| [service-gate]    | 网关     | 转发消息到game/scene/battle (后续添加)        |
| [service-login]   | 登录     | (后续添加)                               |
| [service-client]  | 客户端    | 写着玩 理论上不应该用Java写客户端 主要是想把协议调通 且 好看效果 |

### 备忘录

* 行为树 rpc login gateway redis 跨进程事件系统 包依赖层级优化
* 功能:  任务/成就(进度) 支付 消息同步(全量/变化 合并) 全自动打表生成类
* GC 虚拟线程
*
* game内存管理 存储 活动 道具(装备,资源) 进行中...
