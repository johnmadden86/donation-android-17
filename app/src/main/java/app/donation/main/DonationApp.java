package app.donation.main;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import app.donation.model.Candidate;
import app.donation.model.Donation;
import app.donation.model.Token;
import app.donation.model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public  class   DonationApp
        extends Application {

    public DonationService  donationService;
    public boolean          donationServiceAvailable = false;
    public String           serviceUrl = "https://dry-cliffs-14757.herokuapp.com";

    public final    int     target = 10000;
    public          int     totalDonated = 0;
    public          String  totalDonatedString;

    public User             currentUser;
    public List<Donation>   donations   = new ArrayList<>();
    public List<User>       users       = new ArrayList<>();
    public List<Candidate>  candidates  = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();

        for (Donation donation : donations) {
            totalDonated += donation.amount;
        }

        Gson gson = new GsonBuilder().create();
        Retrofit retrofit = new Retrofit
                                .Builder()
                                .baseUrl(serviceUrl)
                                .addConverterFactory(GsonConverterFactory.create(gson))
                                .build();

        donationService = retrofit.create(DonationService.class);

        Log.v("Donate", "Donation App Started");
    }

    public void newDonation(Donation donation) {
        boolean targetAchieved = totalDonated > target;
        if (!targetAchieved) {
            donations.add(donation);
            totalDonated += donation.amount;
            totalDonatedString = "Total: €" + totalDonated;
            Log.v("Donate", donation.amount + " donated by " + donation.method + "\nCurrent total " + totalDonated);
        } else {
            Toast toast = Toast.makeText(this, "Target Exceeded!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void newUser (User user) {
        users.add(user);
    }

    public boolean validUser (String email, String password) {
        for (User user : users) {
            if (user.email.equals(email) && user.password.equals(password)) {
                currentUser = user;
                return true;
            }
        }
        return false;
    }
}