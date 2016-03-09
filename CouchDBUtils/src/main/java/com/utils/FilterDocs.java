package com.utils;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.lightcouch.CouchDbClient;
import org.lightcouch.View;

/**
 * Updates values of a given key/val pair inside all docs of a given couchdb view
 * @author Ankur Srivastava
 *
 */

public class FilterDocs {
  public static void main(String[] args) {
    // CouchDbClient conn = new CouchDbClient("ocp_runscripts", true, "http", "10.216.138.222", 5984, "admin", "admin");
    CouchDbClient conn = new CouchDbClient("ocp_runscripts", true, "http", "localhost", 5984, "admin", "admin");
    conn.context().compact();

    String fieldName = "Country";
    Set<String> values = new HashSet<>();
    values.add("CN");
    values.add("CP");
    values.add("AT");
    values.add("IT");

    View view = conn.view("reg/StreetAddressOrderByCOD");
    List<Map> res = view.query(Map.class);
    System.out.println(res.size());

    res.stream().filter(m -> ((Map) m.get("value")).containsKey(fieldName) && values.contains(((Map) m.get("value")).get(fieldName))).forEach(m -> {
      System.out.println("Found: " + ((Map) m.get("value")).get("_id"));
    });

  }
}
