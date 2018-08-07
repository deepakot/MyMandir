package com.example.deepak.mymandir.Presenter;

import com.example.deepak.mymandir.Models.AttachData;
import com.example.deepak.mymandir.Models.StoryData;
import com.example.deepak.mymandir.Models.UserData;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Sharmaji on 6/2/2018.
 */

public class StoryPresenter {

    private static StoryPresenter single_instance = null;
    // static method to create instance of Singleton class
    public static StoryPresenter getInstance()
    {
        if (single_instance == null)
            single_instance = new StoryPresenter();
        return single_instance;
    }

    public ArrayList<StoryData> showJSON(JSONArray json) {
        ArrayList<StoryData> postList=new ArrayList<>();
        if (json != null) {
            try {
                //Get JSON response by converting JSONArray into String
                for (int i = 0; i < json.length(); i++) {
                    JSONObject obj = json.getJSONObject(i);

                    StoryData storyData = new StoryData();

                    if(obj.getString("text")!=null) {
                        storyData.setText(obj.getString("text"));
                    }
                    if(obj.getString("title")!=null) {
                        storyData.setTitle(obj.getString("title"));
                    }
                    storyData.setLikeCount(obj.getInt("likeCount"));
                    storyData.setCommentCount(obj.getInt("commentCount"));
                    storyData.setShareCount(obj.getInt("shareCount"));
                    if(obj.has("sender"))
                    {
                        JSONObject userObj = obj.getJSONObject("sender");
                        UserData userData = new UserData();
                        if(userObj.getString("name")!=null) {
                            userData.setUserName(userObj.getString("name"));
                        }
                        if(userObj.getString("imageUrl")!=null) {
                            userData.setUserImage(userObj.getString("imageUrl"));
                        }
                        storyData.setUserData(userData);
                    }

                    if(obj.has("attachments"))
                    {
                        JSONArray jsonArray = obj.getJSONArray("attachments");
                        if(jsonArray.length()>0&&jsonArray.getJSONObject(0)!=null)
                        {
                            JSONObject attachObj = jsonArray.getJSONObject(0);
                            AttachData attachData = new AttachData();
                            if(attachObj.getString("url")!=null) {
                                attachData.setUrl(attachObj.getString("url"));
                            }
                            if(attachObj.getString("thumbnail_url")!=null) {
                                attachData.setThumbnailUrl(attachObj.getString("thumbnail_url"));
                            }
                            if(attachObj.getString("type")!=null) {
                                attachData.setType(attachObj.getString("type"));
                            }
                            if(attachObj.getString("title")!=null) {
                                attachData.setTitle(attachObj.getString("title"));
                            }
                            storyData.setAttachData(attachData);

                        }
                    }
                    postList.add(storyData);
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //When JSON is null
        return postList;
    }

}
