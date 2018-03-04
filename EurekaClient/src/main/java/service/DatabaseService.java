package service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static util.Constants.*;

public class DatabaseService {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseService.class);

    public String getMax() {
        Document max = getDocumentList(DATABASE_URL).stream().max(Comparator.comparing(o -> o.getInteger(PROPERTY_NUMBER)))
                .orElse(null);
        return convertToJson(max).toString();
    }

    public String getMin() {
        Document max = getDocumentList(DATABASE_URL).stream().min(Comparator.comparing(d -> d.getInteger(PROPERTY_NUMBER)))
                .orElse(null);
        return convertToJson(max).toString();
    }

    public String get(int number) {
        Document max = getDocumentList(DATABASE_URL).stream().filter(d -> d.getInteger(PROPERTY_NUMBER).equals(number)).findFirst()
                .orElse(null);
        return convertToJson(max).toString();
    }

    public String delete(int number) {
        Document doc = getDocumentList(DATABASE_URL).stream().filter(d -> d.getInteger(PROPERTY_NUMBER).equals(number)).findFirst()
                .orElse(null);
        if (doc != null) {
            getDocumentMongoCollection(DATABASE_URL).deleteOne(doc);
            return MESSAGE_SUCCESS_DELETE;
        }
        return MESSAGE_NOT_FOUND;
    }

    public String insert(int number) {
        Document doc = getDocumentList(DATABASE_URL).stream().filter(d -> d.getInteger(PROPERTY_NUMBER).equals(number)).findFirst()
                .orElse(null);
        if (doc == null) {
            Document newDoc = new Document();
            newDoc.put(PROPERTY_NUMBER, number);
            newDoc.put(PROPERTY_INSERT_DATE, getFormattedDate());
            getDocumentMongoCollection(DATABASE_URL).insertOne(newDoc);
            return MESSAGE_SUCCESS_INSERT;
        }
        return MESSAGE_UNIQUE;
    }

    private String getFormattedDate() {
        String date = null;
        try {
            DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
            Calendar cal = Calendar.getInstance();
            date = dateFormat.format(cal.getTime());
        } catch (Exception e) {
            logger.error("Error while getting formatted date.", e);
        }
        return date;
    }

    public String list(String order) {
        JsonArray jsonArray = new JsonArray();
        List<Document> list = new ArrayList<>();
        if (order == null || order.equals(ORDER_ASCENDING)) {
            list = getDocumentList(DATABASE_URL).stream().sorted(Comparator.comparingInt(d -> d.getInteger(PROPERTY_NUMBER)))
                    .collect(Collectors.toList());
        } else if (order.equals(ORDER_DESCENDING)) {
            list = getDocumentList(DATABASE_URL).stream().sorted((d1, d2) ->
                    Integer.compare(d2.getInteger(PROPERTY_NUMBER), d1.getInteger(PROPERTY_NUMBER)))
                    .collect(Collectors.toList());
        }
        for (Document doc : list) {
            jsonArray.add(convertToJson(doc));
        }
        return jsonArray.toString();
    }

    private List<Document> getDocumentList(String connectionString) {
        List<Document> documents = new ArrayList<>();
        MongoCursor<Document> mongoCursor = getDocumentsIterator(connectionString);
        if (mongoCursor != null) {
            try {
                while (mongoCursor.hasNext()) {
                    documents.add(mongoCursor.next());
                }
            } catch (Exception e) {
                logger.error("Error while getting number list.", e);
            } finally {
                mongoCursor.close();
            }
        }
        return documents;
    }

    private JsonObject convertToJson(Document document) {
        JsonObject jsonObject = new JsonObject();
        if (document != null) {
            Object number = document.get(PROPERTY_NUMBER);
            Object date = document.get(PROPERTY_INSERT_DATE);
            if (number != null && date != null) {
                jsonObject.addProperty(PROPERTY_NUMBER, number.toString());
                jsonObject.addProperty(PROPERTY_INSERT_DATE, date.toString());
            }
        }
        return jsonObject;
    }

    private MongoCursor<Document> getDocumentsIterator(String connectionString) {
        MongoCursor<Document> iterator = null;
        try {
            MongoCollection<Document> numbers = getDocumentMongoCollection(connectionString);
            iterator = numbers.find().iterator();
        } catch (Exception e) {
            logger.error("Error while getting collection from database.", e);
        }
        return iterator;
    }

    private MongoCollection<Document> getDocumentMongoCollection(String connectionString) {
        MongoClientURI uri = new MongoClientURI(connectionString);
        MongoClient client = new MongoClient(uri);
        MongoDatabase db = client.getDatabase(uri.getDatabase());
        return db.getCollection(COLLECTION_NAME);
    }
}
