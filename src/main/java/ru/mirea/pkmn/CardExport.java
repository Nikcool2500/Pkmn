package ru.mirea.pkmn;

import java.io.*;

public class CardExport implements Serializable {
    private Card card = new Card();
    @Serial
    private static final long serialVersionUID = 1L;

    public CardExport(Card card) {
        this.card = card;
    }

    public void Serialize() throws IOException {
        File myFile = new File("C:\\Users\\123\\Desktop\\Pkmn\\src\\main\\resources\\Cloyster.crd");
        FileOutputStream fos = new FileOutputStream(myFile);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(card);
    }

}
