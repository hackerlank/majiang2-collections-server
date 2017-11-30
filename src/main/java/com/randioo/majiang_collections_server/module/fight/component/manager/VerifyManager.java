package com.randioo.majiang_collections_server.module.fight.component.manager;

import com.randioo.randioo_server_base.entity.Verify;
import com.randioo.randioo_server_base.service.AbstractVerifyManager;

/**
 * 操作校验器
 * 
 * @author wcy 2017年11月29日
 *
 */
public class VerifyManager extends AbstractVerifyManager<Verify> {

    @Override
    public int getVerifyId(Verify t) {
        return t.verifyId;
    }

    @Override
    protected int getAccumlate(Verify t) {
        return t.useId;
    }

    @Override
    protected void verifyId(Verify t, int newVerifyId) {
        t.verifyId = newVerifyId;
    }

    @Override
    protected void accumlateToValue(Verify t, int accumlate) {
        t.useId = accumlate;
    }

    @Override
    public void accumlate(Verify t) {
        t.useId++;
    }

}
