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

public class UpdateValueContains {
  public static void main(String[] args) {
    CouchDbClient conn = new CouchDbClient("ocp_paymentdata", true, "http", "10.216.138.222", 5984, "admin", "admin");
    // CouchDbClient conn = new CouchDbClient("ocp_paymentdata", true, "http", "localhost", 5984, "admin", "admin");
    conn.context().compact();

    String fieldName = "PaymentType";
    String fromVal = "CARTEXP";
    String toVal = "PAYPAL";

    View view = conn.view("ocp/payment_region");
    List<Map> res = view.query(Map.class);
    System.out.println(res.size());

    res.stream().filter(m -> ((Map) m.get("value")).containsKey(fieldName) && ((Map) m.get("value")).get(fieldName).toString().contains(fromVal))
        .forEach(m -> {
          String updateVal = ((Map) m.get("value")).get(fieldName).toString().replaceAll(fromVal, toVal);
          ((Map) m.get("value")).put(fieldName, updateVal);
          conn.update(((Map) m.get("value")));
          System.out.println("Updated" + ((Map) m.get("value")).get("_id"));
        });

  }
}
