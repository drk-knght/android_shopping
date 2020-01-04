package io.eberlein.shopping.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.OnLongClick;
import io.eberlein.shopping.R;
import io.eberlein.shopping.events.EventShopExtraMenuOpened;
import io.eberlein.shopping.events.EventShopFavourited;
import io.eberlein.shopping.events.EventShopSelected;
import io.eberlein.shopping.objects.Shop;
import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmRecyclerViewAdapter;


// todo fix shop favourites

public class ShopAdapter extends RealmRecyclerViewAdapter<Shop, ShopAdapter.ViewHolder> {
    public ShopAdapter(OrderedRealmCollection<Shop> data){
        super(data, true);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private Shop shop;
        private boolean extraMenuOpen = false;

        @BindView(R.id.tv_name) TextView name;
        @BindView(R.id.tv_description) TextView description;
        @BindView(R.id.btn_delete) Button delete;

        @OnClick(R.id.btn_delete)
        void btnDeleteClicked(){
            Realm r = shop.getRealm();
            r.beginTransaction();
            shop.deleteFromRealm();
            r.commitTransaction();
        }

        @OnClick
        void onClick(){
            if(!extraMenuOpen) EventBus.getDefault().post(new EventShopSelected(shop));
        }

        @OnLongClick
        void onLongClick(){
            if(!extraMenuOpen) openExtraMenu();
            else closeExtraMenu();
        }

        public void closeExtraMenu(){
            extraMenuOpen = false;
            delete.setVisibility(View.GONE);
        }

        void openExtraMenu(){
            EventBus.getDefault().post(new EventShopExtraMenuOpened());
            extraMenuOpen = true;
            delete.setVisibility(View.VISIBLE);
        }

        ViewHolder(View v){
            super(v);
            ButterKnife.bind(this, v);
        }

        void setShop(Shop shop){
            this.shop = shop;
            name.setText(shop.getName());
            description.setText(shop.getDescription());
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shop, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setShop(getItem(position));
    }
}
