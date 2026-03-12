import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
public class FlashSaleManager {
    private final ConcurrentHashMap<String, AtomicInteger> inventory = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, LinkedHashSet<Integer>> waitingLists = new ConcurrentHashMap<>();
    public void addProduct(String productId, int count) {
        inventory.put(productId, new AtomicInteger(count));
        waitingLists.put(productId, new LinkedHashSet<>());
    }
    public String purchaseItem(String productId, int userId) {
        AtomicInteger stock = inventory.get(productId);
        if (stock == null) {
            return "Product not found";
        }
        while (true) {
            int currentStock = stock.get();
            if (currentStock <= 0) break;

            if (stock.compareAndSet(currentStock, currentStock - 1)) {
                return "Success, " + (currentStock - 1) + " units remaining";
            }
        }
        synchronized (waitingLists.get(productId)) {
            LinkedHashSet<Integer> list = waitingLists.get(productId);
            list.add(userId);
            return "Added to waiting list, position #" + list.size();
        }
    }
    public static void main(String[] args) {
        FlashSaleManager manager = new FlashSaleManager();

        manager.addProduct("IPHONE15", 2);

        System.out.println(manager.purchaseItem("IPHONE15", 101));
        System.out.println(manager.purchaseItem("IPHONE15", 102));
        System.out.println(manager.purchaseItem("IPHONE15", 103));
    }
}