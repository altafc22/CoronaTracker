package gq.altafchaudhari.cowatch.database.dboperation;

import java.util.List;

import gq.altafchaudhari.cowatch.model.states.CasesTimeSeries;

public interface GetTimeSeriesListener {
    void onSeriesFound(List<CasesTimeSeries> timeSeries);
}
