package com.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lightcouch.CouchDbClient;
import org.lightcouch.View;

public class UpdateExecIDs {



  public static void main(String[] args) {
    String dbName = "ocp_smoke_exec";
    String viewName = "reg/all_docs_1";
    String pre = "st_";
    CouchDbClient conn = new CouchDbClient(dbName, true, "http", "10.216.138.222", 5984, "admin", "admin");
    // conn.context().compact();
    View view = conn.view(viewName);
    List<Map> res = view.query(Map.class);
    System.out.println(res.size());

    Map<String, Integer> scnt = new HashMap<String, Integer>();
    for (Map m : res) {
      Map m2 = (Map) m.get("value");
      if (m2.containsKey("ScriptName")) {
        int ctr;
        String key = pre + m2.get("Country") + "_" + m2.get("ScriptName");
        if (scnt.containsKey(key)) {
          ctr = scnt.get(key);
        } else
          ctr = 0;
        ctr++;
        String newId = key + ctr;
        scnt.put(key, ctr);
        System.out.println(newId);
        conn.remove(m2.get("_id").toString(), m2.get("_rev").toString());
        m2.put("_id", newId);
        m2.remove("_rev");
        conn.save(m2);

      } else {
        System.err.println(m2);
      }
    }
  }
}
