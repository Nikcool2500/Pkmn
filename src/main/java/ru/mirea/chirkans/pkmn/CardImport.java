package ru.mirea.chirkans.pkmn;

import java.io.*;
import java.util.Arrays;
import java.util.Objects;

public class CardImport {

    private String fileName = "";
    public Card myCard;
    public Card myCardEvolvesFrom;

    public CardImport(String fileName) {
        this.fileName = fileName;
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
            String evolName = this.fileName.replace("my_card.txt", "evolves_from.txt");
            CardImport evolCardImport =  new CardImport(evolName);
            evolCardImport.FillCard();

            myCardEvolvesFrom = evolCardImport.myCard;

//            FileInputStream streamInputEvol = new FileInputStream(fileNameEvol);
//            BufferedInputStream bufferedInputEvol = new BufferedInputStream(streamInputEvol);
//            byte[] dataEvol = bufferedInputEvol.readAllBytes();
//            reportDataEvol = new String(dataEvol).split("\r\n");
//            streamInputEvol.close();
//            bufferedInputEvol.close()
        }
        else myCardEvolvesFrom = null;

        FillingCard(myCardData);
    }

    private void FillingCard(String[] parsedCard) {

        String[] parsedCardSkills = parsedCard[5].split(",");
        AttackSkill[] attSkillList = new AttackSkill[parsedCardSkills.length];
        for (int i = 0; i < parsedCardSkills.length; i++) {
            String[] attack = parsedCardSkills[i].split(" / ");
            attSkillList[i] = new AttackSkill(attack[1],
                    "",
                    attack[0],
                    Integer.parseInt(attack[2].replace("\r", "")));
        }

        String[] parsedCardStudent = parsedCard[11].split(" / ");

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
                        )
                );
    }

    public void Deserialization(String name) throws IOException, ClassNotFoundException {
        File fileName = new File(name);
        FileInputStream streamInput = new FileInputStream(fileName);
        ObjectInputStream objectStream = new ObjectInputStream(streamInput);
        Card cardSelf = (Card) objectStream.readObject();
        String evolName = "C:\\Users\\student\\IdeaProjects\\Pkmn\\src\\main\\java\\ru\\mirea\\chirkans\\pkmn\\evolves_from";
        FileInputStream streamInputEvol = new FileInputStream(evolName);
        ObjectInputStream objectStreamEvol = new ObjectInputStream(streamInputEvol);
        Card cardEvol = (Card) objectStreamEvol.readObject();
    }

}
