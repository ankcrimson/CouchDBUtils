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

public class UpdateValues2 {
  @SuppressWarnings("rawtypes")
  public static void main(String[] args) {
    CouchDbClient conn = new CouchDbClient("ocp_hardcodeddata", true, "http", "10.216.138.222", 5984, "admin", "admin");
    // CouchDbClient conn = new CouchDbClient("ocp_hardcodeddata", true, "http", "localhost", 5984, "admin", "admin");
    // conn.context().compact();

    String fieldName = "GiftCardPath";
    String searchVal = "'shop,gift_cards'";
    String toVal = "shop,gift_cards";

    View view = conn.view("ocp/exec_region");
    List<Map> res = view.query(Map.class);
    System.out.println(res.size());

    res.stream().filter(m -> ((Map) m.get("value")).containsKey(fieldName) && ((Map) m.get("value")).get(fieldName).toString().contains(searchVal))
        .forEach(m -> {
          String val = ((Map) m.get("value")).get(fieldName).toString();
          val = val.replaceAll(searchVal, toVal);
          ((Map) m.get("value")).put(fieldName, val);
          conn.update(((Map) m.get("value")));
          System.out.println("Updated" + ((Map) m.get("value")).get("_id"));
        });

  }
}
