package desktop.model.primitives;

import org.fluttercode.datafactory.impl.DataFactory;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SpectatorTest {

    private static DataFactory df = new DataFactory();

    private static Spectator spectator;

    public static final String NICK = df.getName();

    @Before
    public void setUp() {
        spectator = new Spectator(NICK);
    }


    @Test
    public void testNick() {
        assertEquals(spectator.getNick(),NICK);
    }

}