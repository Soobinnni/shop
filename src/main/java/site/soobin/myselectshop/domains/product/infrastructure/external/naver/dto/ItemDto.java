package site.soobin.myselectshop.domains.product.infrastructure.external.naver.dto;

import org.json.JSONObject;

public record ItemDto(String title, String link, String image, int lprice) {

  public static ItemDto from(JSONObject itemJson) {
    return create(itemJson);
  }

  private static ItemDto create(JSONObject itemJson) {
    String title = itemJson.getString("title");
    String link = itemJson.getString("link");
    String image = itemJson.getString("image");
    int lprice = itemJson.getInt("lprice");

    return new ItemDto(title, link, image, lprice);
  }
}
