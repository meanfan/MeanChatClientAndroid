package com.mean.meanchat.android;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.mean.meanchat.android.view.MeFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements MeFragment.OnFragmentInteractionListener {
    private MeFragment meFragment;
    private List<Fragment> fragments;
    private int currentFragment;

    private BottomNavigationView navigation;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            if(fragments.size()<3){
                return false;
            }
            switch (item.getItemId()) {
                case R.id.navigation_chat: {
                    return switchFragment(currentFragment, 0);
                }
                case R.id.navigation_contacts: {
                    return switchFragment(currentFragment, 1);
                }
                case R.id.navigation_me: {
                    return switchFragment(currentFragment, 2);
                }
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragments = new ArrayList<>();
        meFragment = new MeFragment();
        fragments.add(meFragment);
        navigation = findViewById(R.id.navigation);
        getSupportFragmentManager().beginTransaction().replace(R.id.ll_main,meFragment).show(meFragment).commit();
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_me);

    }

    @Override
    public void onMeFragmentInteraction(Uri uri) {
        if(uri.compareTo(MeFragment.URI_VIEW_BUTTON_LOGOUT)==0){
            if(logout()){
                showToast("注销成功");
            }else {
                showToast("注销失败");
            }
        }
    }

    private boolean switchFragment(int currentFragment,int index)
    {
        FragmentTransaction transaction =getSupportFragmentManager().beginTransaction();
        if(currentFragment!=index && fragments.get(index)!=null)
        {
            transaction.hide(fragments.get(currentFragment));  //隐藏当前Fragment
            transaction.add(R.id.ll_main,fragments.get(index));
            transaction.show(fragments.get(index)).commitAllowingStateLoss();
            return true;
        }
        return false;
    }

    boolean logout(){
        //TODO logout
        return false;
    }

    private void showToast(String msg){
        Toast.makeText(MainActivity.this,msg,Toast.LENGTH_SHORT).show();
    }
}
