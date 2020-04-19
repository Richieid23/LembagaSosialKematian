package com.example.lembagasosialkematian.ui.berhenti;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.lembagasosialkematian.R;
import com.google.android.material.tabs.TabLayout;


public class BerhentiFragment extends Fragment {
    ViewPager viewPager;
    TabLayout tabs;
    SectionPagerAdapter sectionsPagerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_berhenti, container, false);
        viewPager = root.findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        tabs = root.findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        TabAdapter adapter;
        adapter = new TabAdapter(getFragmentManager());
        adapter.addFragment(new TabMeninggal(), "Meninggal");
        adapter.addFragment(new TabPindah(), "Pindah");
        adapter.addFragment(new TabUndurdiri(), "Undur Diri");
        viewPager.setAdapter(adapter);
        tabs.setupWithViewPager(viewPager);
    }
}
