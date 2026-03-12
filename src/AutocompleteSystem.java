import java.util.*;
import java.util.concurrent.*;
public class AutocompleteSystem {
    private final Map<String, Integer> queryFrequencies = new ConcurrentHashMap<>();
    private final TrieNode root = new TrieNode();
    class TrieNode {
        Map<Character, TrieNode> children = new HashMap<>();
        // Pre-computed top 10 results for this prefix
        List<String> topSuggestions = new ArrayList<>();
    }
    public void updateFrequency(String query) {
        int newFreq = queryFrequencies.getOrDefault(query, 0) + 1;
        queryFrequencies.put(query, newFreq);
        insertIntoTrie(query);
    }
    private void insertIntoTrie(String query) {
        TrieNode curr = root;
        for (char c : query.toCharArray()) {
            curr.children.putIfAbsent(c, new TrieNode());
            curr = curr.children.get(c);
            updateNodeSuggestions(curr, query);
        }
    }
    private void updateNodeSuggestions(TrieNode node, String query) {
        if (!node.topSuggestions.contains(query)) {
            node.topSuggestions.add(query);
        }
        node.topSuggestions.sort((a, b) -> {
            int freqA = queryFrequencies.getOrDefault(a, 0);
            int freqB = queryFrequencies.getOrDefault(b, 0);
            return freqB != freqA ? freqB - freqA : a.compareTo(b);
        });
        if (node.topSuggestions.size() > 10) {
            node.topSuggestions.remove(node.topSuggestions.size() - 1);
        }
    }
    public List<String> search(String prefix) {
        TrieNode curr = root;
        for (char c : prefix.toCharArray()) {
            if (!curr.children.containsKey(c)) return Collections.emptyList();
            curr = curr.children.get(c);
        }
        return curr.topSuggestions;
    }
    public static void main(String[] args) {
        AutocompleteSystem autocomplete = new AutocompleteSystem();
        autocomplete.updateFrequency("java tutorial");
        autocomplete.updateFrequency("java tutorial"); // Higher weight
        autocomplete.updateFrequency("javascript");
        autocomplete.updateFrequency("java download");
        System.out.println("Suggestions for 'jav': " + autocomplete.search("jav"));
    }
}