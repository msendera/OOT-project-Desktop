package desktop.model.GsonAdapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import desktop.exception.BadDieValueException;
import desktop.exception.BadPointsValueException;
import desktop.model.primitives.Dice;
import desktop.model.primitives.Participant;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leszek Placzkiewicz on 2015-12-18.
 */
public class ParticipantAdapter extends TypeAdapter<Participant> {
    @Override
    public Participant read(JsonReader jsonReader) throws IOException {

        int rollsInRound = 0;
        int points = 0;
        String nick = null;
        Dice dice = null;

        jsonReader.beginObject();

        while(jsonReader.hasNext()) {
            String name = jsonReader.nextName();

            if(name.equals("nick")) {
                nick = jsonReader.nextString();
            } else if(name.equals("rollsInRound")) {
                rollsInRound = jsonReader.nextInt();
            } else if(name.equals("hand")) {
                try {
                    dice = readDice(jsonReader);
                } catch (BadDieValueException e) {
                    throw new RuntimeException(e);
                }
            } else if(name.equals("points")) {
                points = jsonReader.nextInt();
            } else {
                jsonReader.skipValue();
            }
        }

        jsonReader.endObject();

        Participant participant = new Participant(nick);
        try {
            participant.setPoints(points);
        } catch (BadPointsValueException e) {
            throw new RuntimeException(e);
        }
        participant.setDice(dice);
        participant.setRollsInRound(rollsInRound);

        return participant;
    }

    private Dice readDice(JsonReader jsonReader) throws IOException, BadDieValueException {
        List<Integer> values = new ArrayList<>();
        String description = null;
        int n = 0;

        jsonReader.beginObject();

        while (jsonReader.hasNext()) {
            String name = jsonReader.nextName();

            if (name.equals("dices")) {
                jsonReader.beginArray();

                while (jsonReader.hasNext()) {
                    values.add(jsonReader.nextInt());
                }

                jsonReader.endArray();
            } else if (name.equals("description")) {
                description = jsonReader.nextString();
            } else if (name.equals("n")) {
                n = jsonReader.nextInt();
            } else {
                jsonReader.skipValue();
            }
        }

        jsonReader.endObject();

        int[] intValues = values.stream().mapToInt(x -> x).toArray();
        Dice dice = new Dice(intValues);
        dice.setConfigurationDescription(description);
        dice.setN(n);

        return dice;
    }

    @Override
    public void write(JsonWriter jsonWriter, Participant participant) throws IOException {
        throw new UnsupportedOperationException();
    }
}
