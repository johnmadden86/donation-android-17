package app.donation;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class DonationApp extends Application {

    public final int target = 10000;
    public int totalDonated = 0;
    private String totalDonatedString = "Total: " + totalDonated;
    public List<Donation> donations = new ArrayList<>();
    public List<User> users = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.v("Donate", "Donation App Started");
    }

    public boolean newDonation(Donation donation) {
        boolean targetAchieved = totalDonated > target;
        if (!targetAchieved) {
            donations.add(donation);
            totalDonated += donation.amount;
        } else {
            Toast toast = Toast.makeText(this, "Target Exceeded!", Toast.LENGTH_SHORT);
            toast.show();
        }
        return targetAchieved;
    }

    public void newUser (User user) {
        users.add(user);
    }
}