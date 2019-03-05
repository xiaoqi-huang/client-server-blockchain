import java.util.ArrayList;

public class Blockchain {
    private Block head;
    private ArrayList<Transaction> pool;
    private int length;

    private final int poolLimit = 3;

    public Blockchain() {
        pool = new ArrayList<>();
        length = 0;
    }

    // getters and setters
    public Block getHead() { return head; }
    public ArrayList<Transaction> getPool() { return pool; }
    public int getLength() { return length; }
    public void setHead(Block head) { this.head = head; }
    public void setPool(ArrayList<Transaction> pool) { this.pool = pool; }
    public void setLength(int length) { this.length = length; }

    /**
     * This function adds a new transaction to the blockchain.
     * If the pool limit is reached after adding the new transaction, a new block is added.
     * @param txString is a string contains the sender and content.
     * @return 0, if txString is invalid;
     *         1, if a transaction is added successfully and the pool limit is not reached;
     *         2, if a transaction is added successfully and the pool limit is reached.
     */
    public int addTransaction(String txString) {
        Transaction tx = createTransaction(txString);
        if (tx == null) { return 0; }

        pool.add(tx);

        if (pool.size() < poolLimit) {
            return 1;
        } else {
            // Builds a new block
            Block block = new Block();
            if (length == 0) {
                block.setPreviousBlock(null);
                block.setPreviousHash(new byte[32]);
            } else {
                block.setPreviousBlock(head);
                block.setPreviousHash(head.calculateHash());
            }
            block.setTransactions(pool);

            // Updates head, pool and length
            head = block;
            pool = new ArrayList<>();
            length++;

            return 2;
        }
    }

    public String toString() {
        String cutOffRule = new String(new char[81]).replace("\0", "-") + "\n";
        String poolString = "";
        for (Transaction tx : pool) {
            poolString += tx.toString();
        }

        String blockString = "";
        Block bl = head;
        while (bl != null) {
            blockString += bl.toString();
            bl = bl.getPreviousBlock();
        }

        return "Pool:\n"
                + cutOffRule
                + poolString
                + cutOffRule
                + blockString;
    }

    // implement helper functions here if you need any.
    public Transaction createTransaction(String txString) {
        String[] tokens = txString.split("\\|");

        if (tokens.length == 3
                && tokens[0].equals("tx")
                && tokens[1].matches("[a-z]{4}[0-9]{4}")
                && tokens[2].length() <= 70) {
            Transaction tx = new Transaction();
            tx.setSender(tokens[1]);
            tx.setContent(tokens[2]);
            return tx;
        } else {
            return null;
        }
    }
}