package com.t13max.game.api;

import com.t13max.game.api.args.*;
import com.t13max.game.msg.ErrorCode;
import com.t13max.rpc.anno.RpcInterface;

import java.util.List;

/**
 * 匹配rpc接口
 *
 * @author t13max
 * @since 14:24 2024/10/31
 */
@RpcInterface
public interface IMatchService {

    //加入匹配
    JoinMatchResp joinMatch(JoinMatchReq joinMatchReq);

    //取消匹配
    StopMatchResp stopMatch(StopMatchReq stopMatchReq);

}
