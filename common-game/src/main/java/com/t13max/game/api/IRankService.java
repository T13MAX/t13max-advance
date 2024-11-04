package com.t13max.game.api;

import com.t13max.game.api.args.*;
import com.t13max.rpc.anno.RpcInterface;

import java.util.List;

/**
 * @author t13max
 * @since 14:24 2024/10/31
 */
@RpcInterface
public interface IRankService {
    void updateRankList(UpdateRankReq args);

    List<RankViewInfo> viewRankInfo(RankInfoReq request);

    OwnRankInfo selectRankOwn(RankOwnReq request);
}
