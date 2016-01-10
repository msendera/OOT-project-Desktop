package desktop.model.primitives;


import org.junit.Test;

import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;

public class GameTypeTest  {

    private final static Logger logger = Logger.getLogger(GameTypeTest.class.getName());

    @Test
    public void testGetName() throws Exception {
        logger.info("Checking if gameType.values() has correct corresponding String values");
        assertEquals(GameType.POKER.getName(), "Poker");
        assertEquals(GameType.N_PLUS.getName(), "N+");
        assertEquals(GameType.N_PRODUCT.getName(), "N*");
        logger.info("Passed");
    }

    @Test
    public void testGet() throws Exception {
        logger.info("Checking if gameType.get() returns proper gameType gor given String value");
        assertEquals(GameType.get("Poker"),GameType.POKER);
        assertEquals(GameType.get("N+"),GameType.N_PLUS);
        assertEquals(GameType.get("N*"),GameType.N_PRODUCT);
        logger.info("Passed");
    }
}