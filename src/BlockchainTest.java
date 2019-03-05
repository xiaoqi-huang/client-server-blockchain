import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BlockchainTest {

    @Test
    void testCreateTransactionInvalidString() {
        Blockchain bc = new Blockchain();
        assertEquals(null, bc.createTransaction("tx|xhua731|This is a message"));
    }

    @Test
    void createTransactionTestValidString() {
        Blockchain bc = new Blockchain();
        assertNotEquals(null, bc.createTransaction("tx|xhua7314|This is a message"));
    }
}