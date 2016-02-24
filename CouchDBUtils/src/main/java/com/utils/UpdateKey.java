package com.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lightcouch.CouchDbClient;
import org.lightcouch.View;

/**
 * Updates a keyname in all given documents within a couchdb database view
 * @author Ankur Srivastava
 *
 */
public class UpdateKey {

  public static void main(String[] args) {
    CouchDbClient conn = new CouchDbClient("ocp_runscripts", true, "http", "10.216.138.222", 5984, "admin", "admin");
    conn.context().compact();
    String src = "ProductNLomBuild";
    String tgt = "ProductFromBuild";
    View view = conn.view("reg/StreetAddressOrderByCC");
    List<Map> res = view.query(Map.class);
    System.out.println(res.size());

    Map<String, Integer> scnt = new HashMap<String, Integer>();
    for (Map m : res) {
      Map m2 = (Map) m.get("value");
      if (m2.containsKey(src)) {
        System.out.println(m2.get("_id"));
        String val = m2.get(src).toString();
        m2.put(tgt, val);
        m2.remove(src);
        conn.update(m2);
      } else {
        System.err.println(m2);
      }
    }
  }
}
