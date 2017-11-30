package com.randioo.majiang_collections_server.module.fight.component.parser;

import org.springframework.stereotype.Component;

import com.randioo.mahjong_public_server.protocol.Entity.CardListData;
import com.randioo.mahjong_public_server.protocol.Entity.CardListType;
import com.randioo.majiang_collections_server.module.fight.component.cardlist.Chi;
import com.randioo.randioo_server_base.template.Parser;

@Component
public class ChiConverter implements Parser<CardListData, Chi> {

    @Override
    public CardListData parse(Chi chi) {
        CardListData.Builder chiDataBuilder = CardListData.newBuilder();
        chiDataBuilder.setCard(chi.card);
        chiDataBuilder.setTargetCard(chi.targetCard);
        chiDataBuilder.setTargetSeat(chi.getTargetSeat());
        chiDataBuilder.setCardListType(CardListType.CARD_LIST_TYPE_CHI);

        return chiDataBuilder.build();
    }

}
