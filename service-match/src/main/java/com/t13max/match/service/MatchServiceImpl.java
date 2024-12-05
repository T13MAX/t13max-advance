package com.t13max.match.service;

import com.t13max.game.api.IMatchService;
import com.t13max.game.api.args.JoinMatchReq;
import com.t13max.game.api.args.JoinMatchResp;
import com.t13max.game.api.args.StopMatchReq;
import com.t13max.game.api.args.StopMatchResp;
import com.t13max.game.msg.ErrorCode;
import com.t13max.match.manager.MatchManager;
import com.t13max.match.player.BarDartMatchPlayer;
import com.t13max.rpc.anno.RpcImpl;

/**
 * @author t13max
 * @since 16:20 2024/11/4
 */
@RpcImpl(group = "Global")
public class MatchServiceImpl implements IMatchService {

    @Override
    public JoinMatchResp joinMatch(JoinMatchReq joinMatchReq) {
        //joinMatchReq
        ErrorCode errorCode = MatchManager.inst().joinMatch(new BarDartMatchPlayer());
        //errorCode
        return new JoinMatchResp();
    }

    @Override
    public StopMatchResp stopMatch(StopMatchReq stopMatchReq) {
        ErrorCode errorCode = MatchManager.inst().stopMatch(stopMatchReq.getUuid());
        //errorCode
        return new StopMatchResp();
    }
}
