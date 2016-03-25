package com.utils;

import java.util.List;
import java.util.Map;

import org.lightcouch.CouchDbClient;
import org.lightcouch.View;

/**
 * Updates a keyname in all given documents within a couchdb database view
 * @author Ankur Srivastava
 *
 */
public class Language {

  public static void main(String[] args) {
    CouchDbClient conn = new CouchDbClient("ocp_runscripts", true, "http", "10.216.138.222", 5984, "asriv5", "asriv5");
    // conn.context().compact();
    View view = conn.view("reg/all_docs_1");
    List<Map> res = view.query(Map.class);

    res.stream().map(m -> (Map) m.get("value")).filter(map -> !map.containsKey("Language")).map(m -> m.get("Country")).forEach(System.out::println);

    res.stream().map(m -> (Map) m.get("value")).filter(map -> !map.containsKey("Language"))// .map(map ->
                                                                                           // map.get("Country"))
        .forEach(map -> {
          if (map.containsKey("Country")) {
            String country = map.get("Country").toString();
            if (country.equalsIgnoreCase("NL")) {
              map.put("Language", "nl_NL");
              map.put("lastModifiedBy", "asriv5");
              conn.update(map);
            }
          }
        });

  }
}
