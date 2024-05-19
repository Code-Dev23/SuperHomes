package it.scopped.superhomes.backend.struct;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import it.scopped.superhomes.SuperHomes;
import it.scopped.superhomes.backend.objects.PlayerHomeData;
import lombok.Getter;
import org.bson.Document;

import java.util.UUID;

@Getter
public class DatabaseManager {
    private final SuperHomes plugin;

    private final MongoClient client;
    private final MongoDatabase database;
    private final MongoCollection<Document> players;

    public DatabaseManager(SuperHomes plugin) {
        this.plugin = plugin;

        this.client = MongoClients.create(plugin.getConfig().getString("mongodb.uri"));
        this.database = this.client.getDatabase(plugin.getConfig().getString("mongodb.database"));

        System.out.println(client);
        System.out.println(database);
        if (!collectionExists(plugin.getConfig().getString("mongodb.collection"))) {
            database.createCollection(plugin.getConfig().getString("mongodb.collection"));
        }
        this.players = this.database.getCollection(plugin.getConfig().getString("mongodb.collection"));
    }

    private boolean collectionExists(String collectionName) {
        for (String name : database.listCollectionNames()) {
            if (name.equals(collectionName)) {
                return true;
            }
        }
        return false;
    }

    public Document getPlayer(UUID uuid) {
        return this.players.find(Filters.eq("uuid", uuid.toString())).first();
    }

    public void replacePlayer(PlayerHomeData data, Document document) {
        this.players.replaceOne(Filters.eq("uuid", data.getUuid().toString()), document, (new ReplaceOptions())
                .upsert(true));
    }

    public void insertPlayer(Document document) {
        this.players.insertOne(document);
    }
}