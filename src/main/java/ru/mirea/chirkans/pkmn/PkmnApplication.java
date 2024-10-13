package ru.mirea.chirkans.pkmn;

import java.io.IOException;

public class PkmnApplication {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        // C:\Users\123\Desktop\Pkmn\src\main\java\ru\mirea\chirkans\pkmn\my_card.txt
        // C:\Users\student\IdeaProjects\Pkmn\src\main\java\ru\mirea\chirkans\pkmn\my_card.txt
        CardImport myCard = new CardImport("C:\\Users\\123\\Desktop\\Pkmn\\src\\main\\java\\ru\\mirea\\chirkans\\pkmn\\my_card.txt");
        myCard.FillCard();

        System.out.println(myCard.myCard.toString());
        System.out.println("----------------------------------------------");
        System.out.println(myCard.myCardEvolvesFrom.toString());
    }
}
