package com.insta.processing.test1;

import org.json.*;

import java.util.ArrayList;

/**
 * @author Rodolphe
 *         <p/>
 *         Created at : 22/09/2014
 *         Project : com.insta.processing.1
 */
public class TwitterTrend {

    private String name;
    private String query;
    private String url;
    private String promotedContent;

    public static ArrayList<TwitterTrend> parseJSON(String jsonString) throws JSONException {
        ArrayList<TwitterTrend> trendsArrayList = new ArrayList<TwitterTrend>();

        JSONObject object = (new JSONArray(jsonString)).getJSONObject(0);
        JSONArray trends = object.getJSONArray("trends");
        int len = trends.length();

        for (int i = 0 ; i < len ; i++) {
            TwitterTrend aTrend = new TwitterTrend();
            JSONObject jsonTrend = trends.getJSONObject(i);
            aTrend.setName(jsonTrend.getString("name"));
            aTrend.setPromotedContent(jsonTrend.optString("promoted_content"));
            aTrend.setQuery(jsonTrend.getString("query"));
            aTrend.setUrl(jsonTrend.getString("url"));

            trendsArrayList.add(aTrend);
        }


        return trendsArrayList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPromotedContent() {
        return promotedContent;
    }

    public void setPromotedContent(String promotedContent) {
        this.promotedContent = promotedContent;
    }
}
