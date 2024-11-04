package com.t13max.rank.rpc;

import com.t13max.game.api.IRankService;
import com.t13max.game.api.args.*;
import com.t13max.game.api.entity.OwnRankInfo;
import com.t13max.game.api.entity.RankInfo;
import com.t13max.game.api.entity.RankViewInfo;
import com.t13max.rank.manager.RankManager;
import com.t13max.rpc.anno.RpcImpl;

import java.util.List;
import java.util.Map;


/**
 * RPC实现
 *
 * @author t13max
 * @since 15:00 2024/10/30
 */
@RpcImpl
public class RankServiceImpl implements IRankService {

    @Override
    public void updateRankList(UpdateRankReq args) {
        Map<String, Map<Integer, List<RankInfo>>> rankDataMap = args.getRankDataMap();
        RankManager.inst().updateRankList(rankDataMap);
    }

    @Override
    public List<RankViewInfo> viewRankInfo(RankInfoReq request) {
        return RankManager.inst().viewRankInfo(request);
    }

    @Override
    public OwnRankInfo selectRankOwn(RankOwnReq request){
        return RankManager.inst().selectRankOwn(request);
    }
}
