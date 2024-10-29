package ru.mirea.chirkans;

import com.fasterxml.jackson.databind.JsonNode;
import ru.mirea.chirkans.web.http.PkmnHttpClient;
import ru.mirea.pkmn.AttackSkill;
import ru.mirea.pkmn.CardExport;
import ru.mirea.pkmn.CardImport;

import java.io.IOException;
import java.util.*;

public class PkmnApplication {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        CardImport myCard = new CardImport("C:\\Users\\123\\Desktop\\Pkmn\\src\\main\\resources\\my_card.txt");
        myCard.FillCard();

        System.out.println(myCard.myCard.getSkills());

        CardExport cardExport = new CardExport(myCard.myCard);
        cardExport.Serialize();

//        System.out.println(myCard.myCard.toString());
//
//        PkmnHttpClient pkmnHttpClient = new PkmnHttpClient();
//        JsonNode card2 = pkmnHttpClient.getPokemonCard(myCard.myCard.getEvolvesFrom().getName(), myCard.myCard.getEvolvesFrom().getNumber());
//        System.out.println(card2.toPrettyString());
//        if (myCard.myCard.getEvolvesFrom().getName() != null) {
//            JsonNode card2 = pkmnHttpClient.getPokemonCard(myCard.myCard.getEvolvesFrom().getName(), myCard.myCard.getEvolvesFrom().getNumber());
//
//            System.out.println(card2.toPrettyString());
//
//            System.out.println(card2.findValues("attacks")
//                    .stream()
//                    .map(JsonNode::toPrettyString)
//                    .collect(Collectors.toSet()));
//        }

//        CardImport desCard = new CardImport();
//        desCard.Deserialization("Cloyster");
//
//        System.out.println(desCard.myCard.toString());
    }
}
