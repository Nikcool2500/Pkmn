package ru.mirea.pkmn;

import com.fasterxml.jackson.databind.JsonNode;
import ru.mirea.chirkans.web.http.PkmnHttpClient;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class CardImport {

    private String fileName = "";
    public Card myCard;
    public Card myCardEvolvesFrom;

    public CardImport(String fileName) {
        this.fileName = fileName;
    }

    public CardImport() {
    }

    public void FillCard() throws IOException {
        File fileName = new File(this.fileName);
        FileInputStream streamInput = new FileInputStream(fileName);
        BufferedInputStream bufferedInput = new BufferedInputStream(streamInput);
        byte[] data = bufferedInput.readAllBytes();
        String[] myCardData = new String(data).split("\r\n");
        streamInput.close();
        bufferedInput.close();

        if (!Objects.equals(myCardData[4], "-") && !Objects.equals(myCardData[4], "None")) {
            String evolName = myCardData[4];
            CardImport evolCardImport =  new CardImport(evolName);
            evolCardImport.FillCard();

            myCardEvolvesFrom = evolCardImport.myCard;
        }
        else myCardEvolvesFrom = null;

        FillingCard(myCardData);
    }

    private void FillingCard(String[] parsedCard) throws IOException {

        String[] parsedCardSkills = parsedCard[5].split(",");
        AttackSkill[] attSkillList = new AttackSkill[parsedCardSkills.length];
        for (int i = 0; i < parsedCardSkills.length; i++) {
            String[] attack = parsedCardSkills[i].split("/");
            attSkillList[i] = new AttackSkill(attack[1],
                    "",
                    attack[0],
                    Integer.parseInt(attack[2].replace("\r", "")));
        }

        String[] parsedCardStudent = parsedCard[11].split("/");

        myCard = new Card(PokemonStage.valueOf(parsedCard[0]),
                parsedCard[1],
                Integer.parseInt(parsedCard[2]),
                EnergyType.valueOf(parsedCard[3]),
                myCardEvolvesFrom,
                Arrays.asList(attSkillList),
                EnergyType.valueOf(parsedCard[6]),
                EnergyType.valueOf(parsedCard[7]),
                parsedCard[8],
                parsedCard[9],
                parsedCard[10].toCharArray()[0],
                new Student(parsedCardStudent[0],
                        parsedCardStudent[1],
                        parsedCardStudent[2],
                        parsedCardStudent[3]
                        ),
                parsedCard[12]
                );

        SkillsWithDescription();



    }

    private void SkillsWithDescription() throws IOException {

        PkmnHttpClient pkmnHttpClient = new PkmnHttpClient();

        JsonNode card1 = pkmnHttpClient.getPokemonCard(myCard.getName(), myCard.getNumber());

        System.out.println(card1.toPrettyString());

        List<AttackSkill> desSkills = new ArrayList<>();

        for (JsonNode atk: card1.get("data")
                .get(0)
                .get("attacks")) {
            for (AttackSkill myCardAtk: myCard.getSkills()) {
                if (Objects.equals(atk.get("name").asText(), myCardAtk.getName())) {
                    myCardAtk.setDescription(atk.get("text").asText());
                    desSkills.add(myCardAtk);
                    break;
                }
            }
        }
        myCard.setSkills(desSkills);
    }

    public void Deserialization(String name) throws IOException, ClassNotFoundException {
        File fileName = new File("C:\\Users\\123\\Desktop\\Pkmn\\src\\main\\resources\\" + name + ".crd");
        FileInputStream streamInput = new FileInputStream(fileName);
        ObjectInputStream objectStream = new ObjectInputStream(streamInput);
        myCard = (Card) objectStream.readObject();
    }

}
