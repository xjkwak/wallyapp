package siliconwally.net.wallyapp;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import siliconwally.net.wallyapp.model.MatchNode;
import siliconwally.net.wallyapp.service.EndPointApi;
import siliconwally.net.wallyapp.service.RestApiAdapter;

/**
 * Created by jhamil on 27-02-18.
 */

public class MatchUtil {

    public static final String NOT_INITIALIZE  = "41";
    public static final String INPROGRESS = "42";
    public static final String FINALIZED  = "43";

    public MatchUtil() {}

    public void updateNodeTeamStatus(Match match, String status, Context context) {
        // Call to service
        RestApiAdapter restApiAdapter = new RestApiAdapter();
        EndPointApi service = restApiAdapter.connexionToApi(context);
        JsonObject data = new JsonObject();
        int nid = match.getNid();
        String nodeId = String.valueOf(nid);

        //Create Json Payload
        JsonObject nidObject = new JsonObject();
        nidObject.addProperty("value", nodeId);
        JsonArray arrayNid = new JsonArray();
        arrayNid.add(nidObject);

        JsonObject type = new JsonObject();
        type.addProperty("target_id", "partido");
        JsonArray arrayType = new JsonArray();
        arrayType.add(type);


        JsonObject state = new JsonObject();
        state.addProperty("target_id", status);
        JsonArray arrayState = new JsonArray();
        arrayState.add(state);

        data.add("nid", arrayNid);
        data.add("type", arrayType);
        data.add("field_partido_estado", arrayState);

        service.updateStateMatch(nid, data).enqueue(new Callback<MatchNode>() {

            @Override
            public void onResponse(@NonNull Call<MatchNode> call, @NonNull Response<MatchNode> response) {
                if (response.isSuccessful()) {
                    MatchNode node = response.body();
                }
            }

            @Override
            public void onFailure(@NonNull Call<MatchNode> call, @NonNull Throwable t) {
                System.out.println("Error update node!!!!!!");
            }
        });
    }
}