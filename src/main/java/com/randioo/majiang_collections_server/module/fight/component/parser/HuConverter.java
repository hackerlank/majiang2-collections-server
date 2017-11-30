package com.randioo.majiang_collections_server.module.fight.component.parser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.randioo.mahjong_public_server.protocol.Entity.CardListData;
import com.randioo.mahjong_public_server.protocol.Entity.RoundCardsData;
import com.randioo.majiang_collections_server.cache.local.GameCache;
import com.randioo.majiang_collections_server.module.fight.component.cardlist.CardList;
import com.randioo.majiang_collections_server.module.fight.component.cardlist.Hu;
import com.randioo.randioo_server_base.template.Parser;

@Component
public class HuConverter implements Parser<RoundCardsData, Hu> {

    @Autowired
    private CardListPrototype cardListPrototype;

    @Override
    public RoundCardsData parse(Hu hu) {
        RoundCardsData.Builder huDataBuilder = RoundCardsData.newBuilder();
        huDataBuilder.setTargetSeat(hu.getTargetSeat());
        huDataBuilder.setHuCard(hu.card);
        huDataBuilder.setTouchCard(hu.isMine ? hu.card : 0);
        huDataBuilder.addAllHandCards(hu.handCards);
        for (CardList cardList : hu.showCardList) {
            Class<? extends CardList> clazz = cardListPrototype.parse(cardList);
            CardListData cardListData = (CardListData) GameCache.getParseCardListToProtoFunctionMap()
                    .get(clazz)
                    .apply(cardList);

            huDataBuilder.addCardListData(cardListData);
        }
        return huDataBuilder.build();
    }

}
