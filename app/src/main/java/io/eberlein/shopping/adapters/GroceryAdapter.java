package io.eberlein.shopping.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import io.eberlein.shopping.R;
import io.eberlein.shopping.events.EventGrocerySelected;
import io.eberlein.shopping.objects.Grocery;
import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmRecyclerViewAdapter;


public class GroceryAdapter extends RealmRecyclerViewAdapter<Grocery, GroceryAdapter.ViewHolder> {
    public GroceryAdapter(OrderedRealmCollection<Grocery> data){
        super(data, true);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private Grocery grocery;
        private boolean extraMenuOpen = false;

        @BindView(R.id.tv_name) TextView name;
        @BindView(R.id.btn_delete) Button delete;

        @OnClick
        void onClick(){
            if(!extraMenuOpen) EventBus.getDefault().post(new EventGrocerySelected(grocery));
            else closeExtraMenu();
        }

        @OnLongClick
        void onLongClick(){
            if(!extraMenuOpen) openExtraMenu();
            else closeExtraMenu();
        }

        @OnClick(R.id.btn_delete)
        void btnDeleteClicked(){
            Realm r = grocery.getRealm();
            r.beginTransaction();
            grocery.deleteFromRealm();
            r.commitTransaction();
        }

        void closeExtraMenu(){
            extraMenuOpen = false;
            delete.setVisibility(View.GONE);
        }

        void openExtraMenu(){
            extraMenuOpen = true;
            delete.setVisibility(View.VISIBLE);
        }

        public ViewHolder(View v){
            super(v);
            ButterKnife.bind(this, v);
        }

        void setGrocery(Grocery grocery){
            this.grocery = grocery;
            name.setText(grocery.getName());
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grocery, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setGrocery(getItem(position));
    }
}
