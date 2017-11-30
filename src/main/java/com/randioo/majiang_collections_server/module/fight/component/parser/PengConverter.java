package com.randioo.majiang_collections_server.module.fight.component.parser;

import org.springframework.stereotype.Component;

import com.randioo.mahjong_public_server.protocol.Entity.CardListData;
import com.randioo.mahjong_public_server.protocol.Entity.CardListType;
import com.randioo.majiang_collections_server.module.fight.component.cardlist.Peng;
import com.randioo.randioo_server_base.template.Parser;

@Component
public class PengConverter implements Parser<CardListData, Peng> {

    @Override
    public CardListData parse(Peng peng) {
        CardListData.Builder pengDataBuilder = CardListData.newBuilder();
        pengDataBuilder.setCardListType(CardListType.CARD_LIST_TYPE_PENG);
        pengDataBuilder.setTargetSeat(peng.getTargetSeat());
        pengDataBuilder.setCard(peng.card);
        pengDataBuilder.setTargetCard(peng.card);

        return pengDataBuilder.build();
    }

}
