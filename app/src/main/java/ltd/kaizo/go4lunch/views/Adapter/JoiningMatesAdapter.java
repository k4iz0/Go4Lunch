package ltd.kaizo.go4lunch.views.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.RequestManager;

import java.util.ArrayList;

import ltd.kaizo.go4lunch.R;
import ltd.kaizo.go4lunch.models.User;
import ltd.kaizo.go4lunch.views.MatesViewHolder;

public class JoiningMatesAdapter extends RecyclerView.Adapter<MatesViewHolder> {
    private RequestManager glide;
    private ArrayList<User> userList;

    public JoiningMatesAdapter(ArrayList<User> userList, RequestManager glide) {
        this.glide = glide;
        this.userList = userList;
    }


    @NonNull
    @Override
    public MatesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.mates_fragment_list_item, parent, false);
        return new MatesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MatesViewHolder holder, int position) {
        holder.updateViewWithUserDataForJoiningList(userList, position, glide);
    }

    @Override
    public int getItemCount() {
        if (this.userList != null) {
            return this.userList.size();
        } else {
            return 0;
        }
    }
}