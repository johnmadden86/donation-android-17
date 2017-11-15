package app.donation.main;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import app.donation.model.Donation;
import app.donation.model.User;

public class DonationApp extends Application {

    public final int target = 10000;
    public int totalDonated = 0;
    public String totalDonatedString;
    public List<Donation> donations = new ArrayList<>();
    public List<User> users = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
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
                return true;
            }
        }
        return false;
    }
}