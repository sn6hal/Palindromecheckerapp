import java.util.*;

public class FraudDetector {
    static class Transaction {
        int id;
        int amount;
        String merchant;
        long timestamp; // epoch milliseconds
        String accountId;

        Transaction(int id, int amount, String merchant, long timestamp, String accountId) {
            this.id = id;
            this.amount = amount;
            this.merchant = merchant;
            this.timestamp = timestamp;
            this.accountId = accountId;
        }
    }

    private final List<Transaction> transactions = new ArrayList<>();

    public void addTransaction(Transaction t) {
        transactions.add(t);
    }

    // O(N) lookup for pairs summing to target
    public List<String> findTwoSum(int target) {
        List<String> results = new ArrayList<>();
        Map<Integer, Transaction> seen = new HashMap<>();

        for (Transaction t : transactions) {
            int complement = target - t.amount;
            if (seen.containsKey(complement)) {
                results.add("Pair: (ID " + seen.get(complement).id + ", ID " + t.id + ")");
            }
            seen.put(t.amount, t);
        }
        return results;
    }

    // Duplicate detection: Same amount, same merchant
    public void detectDuplicates() {
        // Key: amount + merchant
        Map<String, List<Transaction>> groups = new HashMap<>();

        for (Transaction t : transactions) {
            String key = t.amount + "|" + t.merchant;
            groups.computeIfAbsent(key, k -> new ArrayList<>()).add(t);
        }

        for (Map.Entry<String, List<Transaction>> entry : groups.entrySet()) {
            if (entry.getValue().size() > 1) {
                System.out.println("Potential Duplicate: " + entry.getKey() + " across " + entry.getValue().size() + " accounts.");
            }
        }
    }
    public static void main(String[] args) {
        FraudDetector detector = new FraudDetector();
        detector.addTransaction(new Transaction(1, 500, "Store A", 1000, "Acc1"));
        detector.addTransaction(new Transaction(2, 300, "Store B", 1015, "Acc2"));
        detector.addTransaction(new Transaction(3, 200, "Store C", 1030, "Acc3"));

        System.out.println("Pairs for 500: " + detector.findTwoSum(500));
        detector.detectDuplicates();
    }
}