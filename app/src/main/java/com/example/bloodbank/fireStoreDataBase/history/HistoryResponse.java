package com.example.bloodbank.fireStoreDataBase.history;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created By Mohamed El Banna On 6/27/2020
 **/
public class HistoryResponse implements Serializable {
    private String id;
    private List<Map<String, Object>> history;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Map<String, Object>> getHistory() {
        return history;
    }

    public void setHistory(List<Map<String, Object>> history) {
        this.history = history;
    }
}
