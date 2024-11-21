package ojt.aada.data.datasource.remote.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import ojt.aada.domain.models.CastnCrew;

public class CastNCrewResponse {
    private int id;
    @SerializedName("cast")
    private ArrayList<CastnCrew> castList;

    public CastNCrewResponse(int id, ArrayList<CastnCrew> castList) {
        this.id = id;
        this.castList = castList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<CastnCrew> getCastList() {
        return castList;
    }

    public void setCastList(ArrayList<CastnCrew> castList) {
        this.castList = castList;
    }
}
