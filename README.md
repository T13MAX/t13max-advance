# 卡牌对战

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
| [service-client]  | 客户端    | 写着玩 理论上不应该用Java写客户端 主要是想把协议调通 且 好看效果 |

### 备忘录
* 行为树 rpc login gateway redis 跨进程事件系统 
* 热更 使用t13max-common的打表
* 功能:  任务/成就(进度) 支付 消息同步(全量/变化 合并) 全自动打表生成类
* GC 虚拟线程
* data和proto 依赖
* game内存管理 存储 活动 道具(装备,资源) 进行中...
* 
