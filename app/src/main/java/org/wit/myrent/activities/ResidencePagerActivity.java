package org.wit.myrent.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import org.wit.myrent.app.MyRentApp;
import org.wit.myrent.models.Portfolio;
import org.wit.myrent.models.Residence;

import java.util.ArrayList;

import app.donation.R;

import static org.wit.android.helpers.LogHelpers.info;

public  class       ResidencePagerActivity
        extends     AppCompatActivity
        implements  ViewPager.OnPageChangeListener {

    private ViewPager viewPager;
    private ArrayList<Residence> residences;
    private Portfolio portfolio;
    private PagerAdapter pagerAdapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewPager = new ViewPager(this);
        viewPager.setId(R.id.viewPager);
        setContentView(viewPager);
        setResidenceList();
        pagerAdapter = new PagerAdapter(getSupportFragmentManager(), residences);
        viewPager.setAdapter(pagerAdapter);
        setCurrentItem();
        viewPager.addOnPageChangeListener(this);
    }

    private void setResidenceList() {
        MyRentApp app = (MyRentApp) getApplication();
        portfolio = app.portfolio;
        residences = portfolio.residences;
    }

    private void setCurrentItem() {
        Long resId = (Long) getIntent().getSerializableExtra(ResidenceFragment.EXTRA_RESIDENCE_ID);
        for (int i = 0; i < residences.size(); i++) {
            if (residences.get(i).id.equals(resId)) {
                viewPager.setCurrentItem(i);
                break;
            }
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        info(this,
                "onPageScrolled: position " + position +
                        " arg1 "+ positionOffset +
                        " positionOffsetPixels " + positionOffsetPixels);
        Residence residence = residences.get(position);
        if (residence.geolocation != null) {
            setTitle(residence.geolocation);
        }
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    class PagerAdapter extends FragmentStatePagerAdapter {

        private ArrayList<Residence> residences;

        public PagerAdapter(FragmentManager fragmentManager, ArrayList<Residence> residences) {
            super(fragmentManager);
            this.residences = residences;
        }

        @Override
        public Fragment getItem(int position) {
            Residence residence = residences.get(position);
            Bundle args = new Bundle();
            args.putSerializable(ResidenceFragment.EXTRA_RESIDENCE_ID, residence.id);
            ResidenceFragment fragment = new ResidenceFragment();
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            return residences.size();
        }
    }
}