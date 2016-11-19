package com.favn.firstaid.database;

import com.favn.firstaid.models.Injury;

/**
 * Created by Hung Gia on 11/19/2016.
 */

public class InjuryList {
    private Injury[] injuries;

    public InjuryList(Injury[] injuries) {
        this.injuries = injuries;
    }

    public Injury[] getInjuries() {
        return injuries;
    }
}
