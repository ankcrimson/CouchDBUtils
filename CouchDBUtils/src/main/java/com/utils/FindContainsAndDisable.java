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

public class FindContainsAndDisable {
  @SuppressWarnings("unchecked")
  public static void main(String[] args) {
    String user = "asriv5";
    String password = "asriv5";
    CouchDbClient conn = new CouchDbClient("ocp_runscripts", true, "http", "10.216.138.222", 5984, user, password);
    // CouchDbClient conn = new CouchDbClient("ocp_paymentdata", true, "http", "localhost", 5984, "admin", "admin");
    // conn.context().compact();

    // String searchfieldName = "ProductType";
    String searchVal = "PHYSICALGC";
    String fieldName = "ExecuteScript";
    String fromVal = "Yes";
    String toVal = "No";

    View view = conn.view("reg/all_docs_1");
    List<Map> res = view.query(Map.class);
    System.out.println(res.size());

    res.stream().map(m -> (Map) m.get("value"))
        .filter(m -> m.containsKey(fieldName) && m.get(fieldName).toString().equals(fromVal) && m.toString().contains(searchVal)).forEach(m -> {
          m.put(fieldName, toVal);
          m.put("lastModifiedBy", user);
          conn.update(m);
          System.out.println("Updated - " + m.get("_id") + " - " + m);
        });

  }
}
