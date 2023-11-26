package com.example.myapplication.dto;

import com.google.gson.annotations.SerializedName;

public class ChatCount {
    @SerializedName("identifier")
    private Integer identifier;
    @SerializedName("last_cnt")
    private Integer last_cnt;
    @SerializedName("room_cnt")
    private Integer room_cnt;
    @SerializedName("room_id_list")
    private String room_id;
    @SerializedName("room_user_list")
    private String room_user;

    public Integer getIdentifier() { return identifier; }
    public Integer get_room_cnt() {
        return room_cnt;
    }
    public Integer[] get_room_id() {
        Integer[] room_id_list;
        String[] room_id = this.room_id.split("/");
        room_id_list = new Integer[room_cnt];
        for(int i=0; i<room_id.length; i++) {
            room_id_list[i] = Integer.parseInt(room_id[i]);
        }
        return room_id_list;
    }
    public String[] get_user_list(){
        String[] room_user_list;
        room_user_list = room_user.split("/");
        return room_user_list;
    }
}
