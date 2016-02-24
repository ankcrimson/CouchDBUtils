package com.utils;

import java.util.HashMap;
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
    conn.context().compact();

    String fieldName = "ValidateEDDEDR";
    String fromVal = "Yes";
    String toVal = "No";

    View view = conn.view("reg/StreetAddressIDProductsOrders");
    List<Map> res = view.query(Map.class);
    System.out.println(res.size());

    Map<String, Integer> scnt = new HashMap<String, Integer>();
    for (Map m : res) {
      Map m2 = (Map) m.get("value");
      if (m2.containsKey(fieldName) && m2.get(fieldName).toString().equalsIgnoreCase(fromVal)) {

        System.out.println(m2.get("_id"));
        m2.put(fieldName, toVal);

        conn.update(m2);
      } else {
        System.err.println(m2);
      }
    }
  }
}
