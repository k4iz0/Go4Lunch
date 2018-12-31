package ltd.kaizo.go4lunch.views.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.RequestManager;

import java.util.ArrayList;

import ltd.kaizo.go4lunch.R;
import ltd.kaizo.go4lunch.models.User;
import ltd.kaizo.go4lunch.views.MatesViewHolder;

/**
 * The type Joining mates adapter.
 */
public class JoiningMatesAdapter extends RecyclerView.Adapter<MatesViewHolder> {
    /**
     * The Glide.
     */
    private RequestManager glide;
    /**
     * The User list.
     */
    private ArrayList<User> userList;

    /**
     * Instantiates a new Joining mates adapter.
     *
     * @param userArrayList the user array list
     * @param glide         the glide
     */
    public JoiningMatesAdapter(ArrayList<User> userArrayList, RequestManager glide) {
        this.userList = userArrayList;
        this.glide = glide;

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

        holder.updateViewWithUserDataForJoiningList(this.userList, position, glide);
    }

    @Override
    public int getItemCount() {
        if (this.userList == null) {
            return 0;
        } else {
        return this.userList.size();
        }
    }


    /**
     * Sets user list.
     *
     * @param userList the user list
     */
    public void setUserList(ArrayList<User> userList) {
        this.userList = userList;
        Log.i("joiningMatesAdapter", "setUserList: "+userList.size());
        notifyDataSetChanged();
    }
}
