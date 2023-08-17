package api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Resouce {
    private Integer id;
    private String name;
    private Integer year;
    private String color;
    private String pantone_value;

    @JsonCreator
    public Resouce(@JsonProperty ("id") Integer id,
                   @JsonProperty ("name") String name,
                   @JsonProperty ("year") Integer year,
                   @JsonProperty ("color") String color,
                   @JsonProperty ("pantone_value") String pantone_value) {
        this.id = id;
        this.name = name;
        this.year = year;
        this.color = color;
        this.pantone_value = pantone_value;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getYear() {
        return year;
    }

    public String getColor() {
        return color;
    }

    public String getPantone_value() {
        return pantone_value;
    }
}
