package com.example.tonio.tp_android.deezer;

/**
 * Created by tonio on 02/05/2017.
 */

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GsonSearchSong {

    @SerializedName("data")
    @Expose
    private List<Song> data = null;
    @SerializedName("total")
    @Expose
    private Integer total;

    public List<Song> getData() {
        return data;
    }

    public void setData(List<Song> data) {
        this.data = data;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}
