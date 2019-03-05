import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

public class Block {
    private Block previousBlock;
    private byte[] previousHash;
    private ArrayList<Transaction> transactions;

    public Block() { transactions = new ArrayList<>(); }

    // getters and setters
    public Block getPreviousBlock() { return previousBlock; }
    public byte[] getPreviousHash() { return previousHash; }
    public ArrayList<Transaction> getTransactions() { return transactions; }
    public void setPreviousBlock(Block previousBlock) { this.previousBlock = previousBlock; }
    public void setPreviousHash(byte[] previousHash) { this.previousHash = previousHash; }
    public void setTransactions(ArrayList<Transaction> transactions) {
        this.transactions = transactions;
    }

    public String toString() {
        String cutOffRule = new String(new char[81]).replace("\0", "-") + "\n";
        String prevHashString = String.format("|PreviousHash:|%65s|\n",
                Base64.getEncoder().encodeToString(previousHash));
        String hashString = String.format("|CurrentHash:|%66s|\n",
                Base64.getEncoder().encodeToString(calculateHash()));
        String transactionsString = "";
        for (Transaction tx : transactions) {
            transactionsString += tx.toString();
        }
        return "Block:\n"
                + cutOffRule
                + hashString
                + cutOffRule
                + transactionsString
                + cutOffRule
                + prevHashString
                + cutOffRule;
    }

    // to calculate the hash of current block.
    public byte[] calculateHash() {
        byte[] hash = null;

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);

            // Writes previous block's hash value
            dos.writeUTF(encodeHash(previousHash));

            // Writes transactions
            for (Transaction tx : transactions) {
                dos.writeUTF(String.format("tx|%s", tx.toString()).replace("\n", ""));
            }

            byte[] bytes = baos.toByteArray();
            hash = digest.digest(bytes);
        } catch (NoSuchAlgorithmException | IOException e) {
            System.out.println(e.getStackTrace());
        }

        return hash;
    }

    // implement helper functions here if you need any.
    private String encodeHash(byte[] hash) {
        return Base64.getEncoder().encodeToString(hash);
    }
}