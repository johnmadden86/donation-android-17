package org.wit.myrent.app;

import org.wit.myrent.db.DbHelper;
import org.wit.myrent.models.Portfolio;
import org.wit.myrent.models.PortfolioSerializer;

import android.app.Application;
import android.util.Log;

import static org.wit.android.helpers.LogHelpers.info;

public  class   MyRentApp
        extends Application {

    protected static MyRentApp app;
    public Portfolio portfolio;
    // private static final String FILENAME = "portfolio.json";
    // public DbHelper dbHelper = null;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        // PortfolioSerializer serializer = new PortfolioSerializer(this, FILENAME);

        portfolio = new Portfolio(getApplicationContext());
        // dbHelper = new DbHelper(getApplicationContext());
        info(this, "MyRent app launched");
    }

    @org.jetbrains.annotations.Contract(pure = true)
    public static MyRentApp getApp() {
        return app;
    }
}