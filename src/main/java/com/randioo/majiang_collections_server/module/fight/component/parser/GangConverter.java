package com.randioo.majiang_collections_server.module.fight.component.parser;

import org.springframework.stereotype.Component;

import com.randioo.mahjong_public_server.protocol.Entity.CardListData;
import com.randioo.mahjong_public_server.protocol.Entity.CardListType;
import com.randioo.majiang_collections_server.module.fight.component.cardlist.Gang;
import com.randioo.randioo_server_base.template.Parser;

@Component
public class GangConverter implements Parser<CardListData, Gang> {

    @Override
    public CardListData parse(Gang gang) {
        CardListData.Builder gangDataBuilder = CardListData.newBuilder();
        gangDataBuilder.setCard(gang.card);
        gangDataBuilder.setTargetCard(gang.card);
        gangDataBuilder.setTargetSeat(gang.getTargetSeat());
        gangDataBuilder.setCardListType(gang.dark ? CardListType.CARD_LIST_TYPE_GANG_DARK : gang.peng == null ? CardListType.CARD_LIST_TYPE_GANG_LIGHT : CardListType.CARD_LIST_TYPE_GANG_ADD);

        return gangDataBuilder.build();
    }

}
