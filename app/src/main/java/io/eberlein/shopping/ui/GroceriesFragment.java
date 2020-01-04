package io.eberlein.shopping.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.eberlein.shopping.R;
import io.eberlein.shopping.adapters.GroceryAdapter;
import io.eberlein.shopping.events.EventGrocerySelected;
import io.eberlein.shopping.objects.Grocery;
import io.eberlein.shopping.objects.Shop;


public class GroceriesFragment extends Fragment {
    private Shop shop;

    @BindView(R.id.recyclerGrocery)
    RecyclerView recyclerGrocery;

    public GroceriesFragment(Shop shop){
        this.shop = shop;
    }

    class DialogGrocery {
        @BindView(R.id.et_name) EditText name;
        @BindView(R.id.et_count) EditText count;
        @BindView(R.id.et_price) EditText price;

        void build(@Nullable Grocery grocery){
            View v = LayoutInflater.from(getContext()).inflate(R.layout.dialog_grocery, null, false);
            ButterKnife.bind(this, v);
            if(grocery != null) {
                name.setText(grocery.getName());
                count.setText(String.valueOf(grocery.getCount()));
                price.setText(String.valueOf(grocery.getPrice()));
            }
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext())
                    .setView(v)
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            if(grocery == null) {
                builder.setPositiveButton("add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        shop.getRealm().beginTransaction();
                        shop.getGroceries().add(new Grocery(
                                name.getText().toString(),
                                Integer.valueOf(count.getText().toString()),
                                Double.valueOf(price.getText().toString())
                        ));
                        shop.getRealm().commitTransaction();
                        dialog.dismiss();
                    }
                });
            } else {
                builder.setPositiveButton("save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        shop.getRealm().beginTransaction();
                        grocery.setCount(Integer.valueOf(count.getText().toString()));
                        grocery.setName(name.getText().toString());
                        grocery.setPrice(Double.valueOf(price.getText().toString()));
                        shop.getRealm().commitTransaction();
                    }
                });
            }
            builder.show();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventGrocerySelected(EventGrocerySelected e){
        new DialogGrocery().build(e.getGrocery());
    }

    @OnClick(R.id.btn_add_grocery)
    void btnAddGroceryClicked(){
        new DialogGrocery().build(null);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_groceries, container, false);
        ButterKnife.bind(this, v);
        recyclerGrocery.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerGrocery.setAdapter(new GroceryAdapter(shop.getGroceries()));
        recyclerGrocery.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        return v;
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }
}
