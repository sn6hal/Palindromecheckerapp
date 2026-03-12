import java.util.*;
public class PlagiarismDetector {
    private final Map<String, Set<String>> ngramIndex = new HashMap<>();
    private final Map<String, Integer> docSizes = new HashMap<>();
    private final int N = 5;
    public void addDocument(String docId, String content) {
        List<String> ngrams = extractNGrams(content);
        docSizes.put(docId, ngrams.size());

        for (String gram : ngrams) {
            ngramIndex.computeIfAbsent(gram, k -> new HashSet<>()).add(docId);
        }
    }
    public void analyzeDocument(String content) {
        List<String> inputNgrams = extractNGrams(content);
        Map<String, Integer> matchCounts = new HashMap<>();

        for (String gram : inputNgrams) {
            if (ngramIndex.containsKey(gram)) {
                for (String docId : ngramIndex.get(gram)) {
                    matchCounts.put(docId, matchCounts.getOrDefault(docId, 0) + 1);
                }
            }
        }
        System.out.println("Extracted " + inputNgrams.size() + " n-grams");
        for (Map.Entry<String, Integer> entry : matchCounts.entrySet()) {
            double similarity = (entry.getValue() * 100.0) / inputNgrams.size();
            String status = similarity > 50 ? "PLAGIARISM DETECTED" : (similarity > 10 ? "suspicious" : "clean");

            System.out.printf("-> Found %d matching n-grams with %s\n", entry.getValue(), entry.getKey());
            System.out.printf("-> Similarity: %.1f%% (%s)\n", similarity, status);
        }
    }
    private List<String> extractNGrams(String text) {
        List<String> ngrams = new ArrayList<>();
        String[] words = text.toLowerCase().replaceAll("[^a-z ]", "").split("\\s+");

        for (int i = 0; i <= words.length - N; i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < N; j++) {
                sb.append(words[i + j]).append(j < N - 1 ? " " : "");
            }
            ngrams.add(sb.toString());
        }
        return ngrams;
    }
    public static void main(String[] args) {
        PlagiarismDetector detector = new PlagiarismDetector();

        detector.addDocument("essay_089.txt", "the quick brown fox jumps over the lazy dog");
        detector.addDocument("essay_092.txt", "data structures and algorithms are essential for coding interviews");

        System.out.println("Analyzing submission...");
        detector.analyzeDocument("data structures and algorithms are very essential for interviews");
    }
}