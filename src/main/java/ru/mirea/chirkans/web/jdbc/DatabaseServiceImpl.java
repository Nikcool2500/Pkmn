package ru.mirea.chirkans.web.jdbc;

import java.sql.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.postgresql.util.PSQLException;
import ru.mirea.pkmn.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class DatabaseServiceImpl implements DatabaseService {
    private HashMap<String, UUID> PokemonsUUID = new HashMap<>();

    private final Connection connection;

    private final Properties databaseProperties;

    public HashMap<String, UUID> getPokemonsUUID() {
        return PokemonsUUID;
    }

    public DatabaseServiceImpl() throws SQLException, IOException {

        // Загружаем файл database.properties

        databaseProperties = new Properties();
        databaseProperties.load(new FileInputStream("C:\\Users\\123\\Desktop\\Pkmn\\src\\main\\resources\\database.properties"));

        // Подключаемся к базе данных

        connection = DriverManager.getConnection(
                databaseProperties.getProperty("database.url"),
                databaseProperties.getProperty("database.user"),
                databaseProperties.getProperty("database.password")
        );
        System.out.println("Connection is "+(connection.isValid(0) ? "up" : "down"));
    }

    @Override
    public Card getCardFromDatabase(String cardName) throws SQLException, JsonProcessingException {
        // Реализовать получение данных о карте из БД
        Statement selectQuery = connection.createStatement();
        String query = null;
        PreparedStatement preparedStatement = null;
        if (cardName.length() != 36) {
            query = "SELECT * FROM card WHERE name = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, cardName);
        }
        else {
            query = "SELECT * FROM card WHERE id = '" + cardName + "'";
            preparedStatement = connection.prepareStatement(query);
        }
//        System.out.println(query);
        ResultSet resultOfQuery = preparedStatement.executeQuery();

        Card card = new Card();
        resultOfQuery.next();
        System.out.println("UUID: " + resultOfQuery.getString("id"));
        card.setName(resultOfQuery.getString("name"));
        card.setHp(resultOfQuery.getInt("hp"));
        card.setGameSet(resultOfQuery.getString("game_set"));

        String pokemonOwner = resultOfQuery.getString("pokemon_owner");
        card.setPokemonOwner(this.getStudentFromDatabase(pokemonOwner));
        card.setPokemonStage(PokemonStage.valueOf(resultOfQuery.getString("stage")));
        card.setRetreatCost(resultOfQuery.getString("retreat_cost"));
        String weakness = resultOfQuery.getString("weakness_type");
        card.setWeaknessType(weakness == null ? null:EnergyType.valueOf(weakness));

        String resistance = resultOfQuery.getString("resistance_type");
        card.setResistanceType(resistance == null ? null:EnergyType.valueOf(resistance));

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonSkills = resultOfQuery.getString("attack_skills");
        card.setSkills(objectMapper.readValue(jsonSkills, new TypeReference<>() {}));
        card.setPokemonType(EnergyType.valueOf(resultOfQuery.getString("pokemon_type")));
        card.setRegulationMark(resultOfQuery.getString("regulation_mark").charAt(0));
        card.setNumber(resultOfQuery.getString("card_number"));

        String evolvesFrom = resultOfQuery.getString("evolves_from");
        if (!Objects.equals(evolvesFrom, null) && evolvesFrom.length() == 36) {
            card.setEvolvesFrom(this.getCardFromDatabase(evolvesFrom));
        }

        return card;
    }

    @Override
    public Student getStudentFromDatabase(String studentName) throws SQLException {
        // Реализовать получение данных о студенте из БД
        Statement selectQuery = connection.createStatement();
        if (studentName == null) return null;
        String query = null;
        PreparedStatement preparedStatement = null;
        if (studentName.length() != 36) {
            query = "SELECT * FROM student WHERE \"firstName\" = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, studentName);
        }
        else {
            query = "SELECT * FROM student WHERE id = '" + studentName + "'";
            preparedStatement = connection.prepareStatement(query);
        }
        ResultSet resultOfQuery = preparedStatement.executeQuery();

        Student student = new Student();
        if (resultOfQuery.next()) {
            student.setSurName(resultOfQuery.getString("familyName"));
            student.setFirstName(resultOfQuery.getString("firstName"));
            student.setFatherName(resultOfQuery.getString("patronicName"));
            student.setGroup(resultOfQuery.getString("group"));
            System.out.println("UUID: " + resultOfQuery.getString("id"));
        }

        return student;
//        ResultSet resultOfQuery = selectQuery.executeQuery("SELECT * FROM card");
//        while (resultOfQuery.next()) {
//            System.out.println(resultOfQuery.getString("name"));
//        }
//        return null;
    }

    @Override
    public void saveCardToDatabase(Card card) throws SQLException, JsonProcessingException {
        // Реализовать отправку данных карты в БД

        if (card.getEvolvesFrom() != null)
            this.saveCardToDatabase(card.getEvolvesFrom());

        ObjectMapper objectMapper = new ObjectMapper();
        card.getSkills().get(0).setDescription(card.getSkills().get(0).getDescription().replace("'", ""));
        String jsonSkills = objectMapper.writeValueAsString(card.getSkills());
        Statement selectQuery = connection.createStatement();
        getUUID(card);
        String query = "INSERT INTO card (id, name, hp, evolves_from, game_set, pokemon_owner, stage, " +
                "retreat_cost, weakness_type, resistance_type, attack_skills, pokemon_type, regulation_mark, card_number) " +
                "VALUES ('" +
                PokemonsUUID.get(card.getName()) + "', '" +
                card.getName() + "', " +
                card.getHp() + ", " +
                (card.getEvolvesFrom() == null ? null : "'" + PokemonsUUID.get(card.getEvolvesFrom().getName()) + "'") + ", '" +
                card.getGameSet() + "', " +
                (card.getPokemonOwner() == null ? null : "'" + PokemonsUUID.get(card.getPokemonOwner().getFirstName()) + "'") + ", '" +
                card.getPokemonStage() + "', " +
                card.getRetreatCost() + ", '" +
                card.getWeaknessType() + "', '" +
                card.getResistanceType() + "', '" +
                jsonSkills + "', '" +
                card.getPokemonType() + "', '" +
                card.getRegulationMark() + "', " +
                card.getNumber() + ")";

//        System.out.println(query);
        selectQuery.executeUpdate(query);
        System.out.println("Card " + card.getName() + " saved");
    }

    @Override
    public void createPokemonOwner(Student owner) throws SQLException {
        // Реализовать добавление студента - владельца карты в БД
        Statement selectQuery = connection.createStatement();
        getUUID(owner);
        selectQuery.executeUpdate(
                "INSERT INTO student (id, \"familyName\", \"firstName\", \"patronicName\", \"group\") VALUES ('" +
                        PokemonsUUID.get(owner.getFirstName()) + "', '" + owner.getSurName() + "', '" + owner.getFirstName() + "', '" + owner.getFatherName() +
                        "', '" + owner.getGroup() + "')"
        );
        System.out.println("Owner created");
    }

    private void getUUID(Card card) {
        if (card.getPokemonOwner() != null && !PokemonsUUID.containsKey(card.getPokemonOwner().getFirstName()))
            PokemonsUUID.put(card.getPokemonOwner().getFirstName(), UUID.randomUUID());

        do {
            if (!PokemonsUUID.containsKey(card.getName()))
                PokemonsUUID.put(card.getName(), UUID.randomUUID());
            card = card.getEvolvesFrom();
        } while (card != null);

        System.out.println(PokemonsUUID);
    }

    private void getUUID(Student student) {
        if (!PokemonsUUID.containsKey(student.getFirstName()))
            PokemonsUUID.put(student.getFirstName(), UUID.randomUUID());

        System.out.println(PokemonsUUID);
    }

}
