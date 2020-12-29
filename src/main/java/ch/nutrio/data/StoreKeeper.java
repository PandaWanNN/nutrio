package ch.nutrio.data;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinSession;

public class StoreKeeper {

  private static final Map<String, Store> storeMap;

  static {
    try {
      final URL resource = StoreKeeper.class.getClassLoader().getResource("data.json");
      final File json = new File(Objects.requireNonNull(resource).getFile());
      if (json.exists() && json.length() > 0) {
        final ObjectMapper objectMapper = new ObjectMapper();
        TypeFactory typeFactory = objectMapper.getTypeFactory();
        final MapType mapType = typeFactory.constructMapType(HashMap.class, String.class, Store.class);
        storeMap = objectMapper.readValue(json, mapType);
      } else {
        storeMap = new HashMap<>();
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static Store getStore(final String id) {
    return storeMap.get(id);
  }

  public static boolean storeExists(final String id) {
    return storeMap.containsKey(id);
  }

  public static void addStore(final Store store) {
    storeMap.put(store.getId(), store);
  }

  public static String generateStoreId() {
    Random rnd = new Random();
    int number = rnd.nextInt(999999);
    return String.format("%06d", number);
  }

  public static void setStoreIdOnSession(final String id) {
    VaadinSession.getCurrent().setAttribute("store", id);
  }

  public static Optional<Store> getStoreIdForSession() {
    final String id = (String) VaadinSession.getCurrent().getAttribute("store");
    if (id == null) {
      return Optional.empty();
    }
    return Optional.of(getStore(id));
  }

  public static Store getStoreForSession() {
    final Store store;
    final Optional<Store> storeIdForSession = StoreKeeper.getStoreIdForSession();
    if (storeIdForSession.isPresent()) {
      store = storeIdForSession.get();
    } else {
      UI.getCurrent().navigate("");
      return new Store("");
    }
    return store;
  }

  public static void clearSession() {
    VaadinSession.getCurrent().setAttribute("store", null);
  }

  public static Map<String, Store> getAllStores() {
    return storeMap;
  }
}
