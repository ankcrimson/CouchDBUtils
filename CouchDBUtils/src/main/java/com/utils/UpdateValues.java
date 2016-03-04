package com.utils;

import java.util.List;
import java.util.Map;

import org.lightcouch.CouchDbClient;
import org.lightcouch.View;

/**
 * Updates values of a given key/val pair inside all docs of a given couchdb view
 * @author Ankur Srivastava
 *
 */

public class UpdateValues {
  public static void main(String[] args) {
    CouchDbClient conn = new CouchDbClient("ocp_runscripts", true, "http", "10.216.138.222", 5984, "admin", "admin");
    // CouchDbClient conn = new CouchDbClient("ocp_runscripts", true, "http", "localhost", 5984, "admin", "admin");
    conn.context().compact();

    String fieldName = "ProductFromBuild";
    String fromVal = "No";
    String toVal = "Yes";

    View view = conn.view("reg/StreetAddressOrderByExpPayPal");
    List<Map> res = view.query(Map.class);
    System.out.println(res.size());

    res.stream().filter(m -> ((Map) m.get("value")).containsKey(fieldName) && ((Map) m.get("value")).get(fieldName).equals(fromVal)).forEach(m -> {
      ((Map) m.get("value")).put(fieldName, toVal);
      conn.update(((Map) m.get("value")));
      System.out.println("Updated" + ((Map) m.get("value")).get("_id"));
    });

  }
}
