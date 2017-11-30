package com.randioo.majiang_collections_server.module.fight.component.parser;

import org.springframework.stereotype.Component;

import com.randioo.majiang_collections_server.module.fight.component.cardlist.CardList;
import com.randioo.majiang_collections_server.module.fight.component.cardlist.Chi;
import com.randioo.majiang_collections_server.module.fight.component.cardlist.Gang;
import com.randioo.majiang_collections_server.module.fight.component.cardlist.Hu;
import com.randioo.majiang_collections_server.module.fight.component.cardlist.Peng;
import com.randioo.majiang_collections_server.module.fight.component.cardlist.qiaoma.Ting;
import com.randioo.randioo_server_base.template.Parser;

@Component
public class CardListPrototype implements Parser<Class<? extends CardList>, CardList> {

    @Override
    public Class<? extends CardList> parse(CardList cardList) {
        if (cardList instanceof Peng) {
            return Peng.class;
        } else if (cardList instanceof Gang) {
            return Gang.class;
        } else if (cardList instanceof Chi) {
            return Chi.class;
        } else if (cardList instanceof Hu) {
            return Hu.class;
        } else if (cardList instanceof Ting) {
            return Ting.class;
        }
        return null;
    }

}
