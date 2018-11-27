package ltd.kaizo.go4lunch.views.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.RequestManager;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import ltd.kaizo.go4lunch.R;
import ltd.kaizo.go4lunch.models.User;
import ltd.kaizo.go4lunch.views.MatesViewHolder;

public class MatesRecycleAdapter extends FirestoreRecyclerAdapter<User, MatesViewHolder> {
    private final RequestManager glide;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public MatesRecycleAdapter(@NonNull FirestoreRecyclerOptions<User> options, RequestManager glide) {
        super(options);
        this.glide = glide;
    }

    @Override
    protected void onBindViewHolder(@NonNull MatesViewHolder holder, int position, @NonNull User user) {
        holder.updateViewWithUserData(user, glide);
    }

    @NonNull
    @Override
    public MatesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.mates_fragment_list_item, parent, false);
        return new MatesViewHolder(view);
    }
}
