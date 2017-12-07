package com.example.tonio.tp_android.deezer;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GsonSearchArtist {

    @SerializedName("data")
    @Expose
    private List<Artist> data = null;
    @SerializedName("total")
    @Expose
    private Integer total;

    public List<Artist> getData() {
        return data;
    }

    public void setData(List<Artist> data) {
        this.data = data;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

}
