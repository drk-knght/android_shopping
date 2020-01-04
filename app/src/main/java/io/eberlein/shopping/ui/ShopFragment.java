package io.eberlein.shopping.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.eberlein.shopping.R;
import io.eberlein.shopping.events.EventShopSaved;
import io.eberlein.shopping.objects.Shop;
import io.realm.Realm;

public class ShopFragment extends Fragment {
    @BindView(R.id.et_name)
    EditText name;

    @BindView(R.id.et_description)
    EditText description;

    private Shop shop;

    @OnClick(R.id.btn_save)
    void btnSaveClicked(){
        shop.setName(name.getText().toString());
        description.setText(description.getText().toString());
        Realm r = Realm.getDefaultInstance();
        r.beginTransaction();
        r.copyToRealmOrUpdate(shop);
        r.commitTransaction();
        Toast.makeText(getContext(), "saved", Toast.LENGTH_LONG).show();
        EventBus.getDefault().post(new EventShopSaved());
    }

    public ShopFragment(){
        shop = new Shop();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_shop, container, false);
        ButterKnife.bind(this, v);
        return v;
    }
}
