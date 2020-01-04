package io.eberlein.shopping;


import android.os.Bundle;

import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.blankj.utilcode.util.FragmentUtils;
import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.eberlein.shopping.adapters.ShopAdapter;
import io.eberlein.shopping.events.EventShopExtraMenuOpened;
import io.eberlein.shopping.events.EventShopSaved;
import io.eberlein.shopping.events.EventShopSelected;
import io.eberlein.shopping.objects.Shop;
import io.eberlein.shopping.ui.GroceriesFragment;
import io.eberlein.shopping.ui.ShopFragment;
import io.realm.Realm;


public class MainActivity extends AppCompatActivity {
    @BindView(R.id.nav_view) NavigationView navigationView;
    @BindView(R.id.drawer_layout) DrawerLayout drawer;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.recyclerShop) RecyclerView recyclerShop;

    @OnClick(R.id.btn_add_shop)
    void btnAddShopClicked(){
        FragmentUtils.replace(getSupportFragmentManager(), new ShopFragment(), R.id.nav_host_fragment);
        drawer.closeDrawers();
    }

    private AppBarConfiguration mAppBarConfiguration;
    private Realm realm;
    private ShopAdapter shopAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Realm.init(this);
        realm = Realm.getDefaultInstance();
        setSupportActionBar(toolbar);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        setupRecyclerShop();

        if(shopAdapter.getItemCount() == 0) drawer.openDrawer(GravityCompat.START);
        else FragmentUtils.replace(getSupportFragmentManager(), new GroceriesFragment(shopAdapter.getItem(0)), R.id.nav_host_fragment);
    }

    void setupRecyclerShop(){
        shopAdapter = new ShopAdapter(realm.where(Shop.class).findAll());
        recyclerShop.setLayoutManager(new LinearLayoutManager(this));
        recyclerShop.setAdapter(shopAdapter);
        recyclerShop.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventShopSaved(EventShopSaved e){
        drawer.openDrawer(GravityCompat.START);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventShopSelected(EventShopSelected e){
        Shop shop = e.getShop();
        toolbar.setTitle(shop.getName());
        FragmentUtils.replace(getSupportFragmentManager(), new GroceriesFragment(shop), R.id.nav_host_fragment);
        drawer.closeDrawers();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventShopExtraMenuOpened(EventShopExtraMenuOpened e){
        for(int i=0; i<shopAdapter.getItemCount(); i++){
            ShopAdapter.ViewHolder vh = (ShopAdapter.ViewHolder) recyclerShop.getChildViewHolder(recyclerShop.getChildAt(i));
            vh.closeExtraMenu();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.removeAllChangeListeners();
        realm.close();
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }
}
