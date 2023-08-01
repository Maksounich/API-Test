package api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class FailedReg {
    private String error;
    @JsonCreator
    public FailedReg(@JsonProperty ("error") String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }
}
