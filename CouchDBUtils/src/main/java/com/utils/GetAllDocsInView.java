package com.utils;

import java.util.List;
import java.util.Map;

import org.lightcouch.CouchDbClient;
import org.lightcouch.View;

public class GetAllDocsInView {

  public static void main(String[] args) {
    String user = "asriv5";
    CouchDbClient conn = new CouchDbClient("ocp_runscripts", true, "http", "10.216.138.222", 5984, user, "asriv5");
    View view = conn.view("reg/GSOrderByPaypalMember");
    List<Map> rows = view.query(Map.class);
    System.out.println(rows.size());
    rows.stream().map(row -> (Map) row.get("value")).forEach(map -> {
      map.put("ScriptName", "GSOrderByPaypalMemberDevice");
      map.remove("_id");
      map.remove("_rev");
      map.put("lastModifiedBy", user);
      conn.save(map);
    });
  }

}
