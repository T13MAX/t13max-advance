package com.t13max.match.service;

import com.t13max.game.api.IMatchService;
import com.t13max.game.api.args.JoinMatchReq;
import com.t13max.game.api.args.JoinMatchResp;
import com.t13max.game.api.args.StopMatchReq;
import com.t13max.game.api.args.StopMatchResp;
import com.t13max.rpc.anno.RpcImpl;

/**
 * @author t13max
 * @since 16:20 2024/11/4
 */
@RpcImpl
public class MatchServiceImpl implements IMatchService {

    @Override
    public JoinMatchResp joinMatch(JoinMatchReq joinMatchReq) {
        return null;
    }

    @Override
    public StopMatchResp stopMatch(StopMatchReq stopMatchReq) {
        return null;
    }
}
