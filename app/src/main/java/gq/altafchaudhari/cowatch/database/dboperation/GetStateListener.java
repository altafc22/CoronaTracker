package gq.altafchaudhari.cowatch.database.dboperation;

import java.util.List;

import gq.altafchaudhari.cowatch.model.states.Statewise;

public interface GetStateListener {
    void onStatesFound(List<Statewise> stateList);
}
