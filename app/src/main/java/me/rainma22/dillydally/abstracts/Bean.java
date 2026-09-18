package me.rainma22.dillydally.abstracts;

import java.util.Map;

import org.json.JSONObject;

public abstract class Bean {
    public JSONObject toJson(){
        return new JSONObject(this);
    }

    public Map<String, Object> toMap(){
        return toJson().toMap();
    }
}
