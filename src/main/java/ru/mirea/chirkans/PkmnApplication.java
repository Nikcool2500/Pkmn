package ru.mirea.chirkans;

import com.fasterxml.jackson.databind.JsonNode;
import ru.mirea.chirkans.web.http.PkmnHttpClient;
import ru.mirea.chirkans.web.jdbc.DatabaseServiceImpl;
import ru.mirea.pkmn.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

public class PkmnApplication {
    public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException {
        CardImport myCard = new CardImport("C:\\Users\\123\\Desktop\\Pkmn\\src\\main\\resources\\my_card.txt");
        myCard.FillCard();

        CardExport cardExport = new CardExport(myCard.myCard);
        cardExport.Serialize();

        System.out.println(myCard.myCard.getSkills() + "\n");

        DatabaseServiceImpl db = new DatabaseServiceImpl();
//        db.createPokemonOwner(myCard.myCard.getPokemonOwner());
//        db.saveCardToDatabase(myCard.myCard);
        Student student = db.getStudentFromDatabase(myCard.myCard.getPokemonOwner().getFirstName());
        System.out.println(student.toString());
        Card card = db.getCardFromDatabase(myCard.myCard.getName());
        System.out.println(card.toString());


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



//        Student student = db.getStudentFromDatabase("0f3b9250-d5b3-44e5-86e7-07f2a4e70d13");
//        if (student != null) System.out.println(student.toString());
//        else System.out.println("null");
//        Card card = db.getCardFromDatabase("d8ed83a5-8aad-4a7e-a359-e358c48fd589");
//        System.out.println(card.toString());
//        card = db.getCardFromDatabase("7e9fa30e-fb71-4ef0-9c89-c25ac487f328");
//        System.out.println(card.toString());
    }
}
