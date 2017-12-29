package org.wit.myrent.models;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;

import org.wit.myrent.app.MyRentApp;
import org.wit.myrent.db.DbHelper;

import static org.wit.android.helpers.LogHelpers.info;

public class Portfolio {

    public  ArrayList<Residence>    residences;
    public DbHelper dbHelper;

    public Portfolio(Context context) {
        try {
            dbHelper = new DbHelper(context);
            residences = (ArrayList<Residence>) dbHelper.selectResidences();
        } catch (Exception e) {
            info(this, "Error loading residences: " + e.getMessage());
            residences = new ArrayList<>();
        }
    }

    public ArrayList<Residence> selectResidences() {
        return (ArrayList<Residence>) dbHelper.selectResidences();
    }

    public void addResidence(Residence residence) {
        residences.add(residence);
        dbHelper.addResidence(residence);
    }

    public Residence getResidence(Long id) {
        Log.i(this.getClass().getSimpleName(), "Long parameter id: " + id);

        for (Residence res : residences) {
            if (id.equals(res.id)) {
                return res;
            }
        }
        info(this, "failed to find residence. returning first element array to avoid crash");
        return null;
    }

    public void deleteResidence(Residence residence) {
        dbHelper.updateResidence(residence);
        residences.remove(residence);
    }

    public void refreshResidences(List<Residence> residences) {
        dbHelper.deleteResidences();
        this.residences.clear();

        dbHelper.addResidences(residences);

        this.residences.addAll(residences);
    }

    public void updateResidence(Residence residence) {
        dbHelper.updateResidence(residence);
        updateLocalResidences(residence);

    }

    private void updateLocalResidences(Residence residence) {
        for (int i = 0; i < residences.size(); i += 1) {
            Residence r = residences.get(i);
            if (r.id.equals(residence.id)) {
                residences.remove(i);
                residences.add(residence);
                return;
            }
        }
    }

}