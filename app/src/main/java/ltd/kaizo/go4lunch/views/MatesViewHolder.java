package ltd.kaizo.go4lunch.views;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import ltd.kaizo.go4lunch.R;
import ltd.kaizo.go4lunch.models.PlaceFormater;
import ltd.kaizo.go4lunch.models.User;

import static com.bumptech.glide.request.RequestOptions.circleCropTransform;

/**
 * The type Mates view holder.
 */
public class MatesViewHolder extends RecyclerView.ViewHolder {
    /**
     * The Mates avatar.
     */
    @BindView(R.id.mates_recycleViewItem_avatar)
    ImageView matesAvatar;
    /**
     * The Mates text view.
     */
    @BindView(R.id.mates_recycleViewItem_textview)
    TextView matesTextView;

    /**
     * Instantiates a new Mates view holder.
     *
     * @param itemView the item view
     */
    public MatesViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    /**
     * Update view with user data.
     *
     * @param user           the user
     * @param glide          the glide
     * @param restaurantList the restaurant list
     */
    public void updateViewWithUserData(User user, RequestManager glide, ArrayList<PlaceFormater> restaurantList) {


        matesTextView.setText(String.format("%s %s", user.getUsername(), matesTextView.getContext().getString(R.string.decideYet)));
        matesTextView.setTextColor(itemView.getContext().getResources().getColor(R.color.grey));
        matesTextView.setTypeface(null, Typeface.ITALIC);
        itemView.setClickable(false);
        if (!user.getChosenRestaurant().equalsIgnoreCase("")) {
            String restaurantName = "";
            if (restaurantList != null) {
                for (PlaceFormater place : restaurantList) {
                    if (place.getId().equalsIgnoreCase(user.getChosenRestaurant())) {
                        restaurantName = place.getPlaceName();
                    }
                    if (restaurantName.isEmpty()) {
                        matesTextView.setText(String.format("%s %s.", user.getUsername(), matesTextView.getContext().getString(R.string.not_eating_nearby)));
                    } else {
                        matesTextView.setText(String.format("%s %s %s", user.getUsername(), matesTextView.getContext().getString(R.string.eatingAt), restaurantName));
                    }
                    matesTextView.setTextColor(itemView.getContext().getResources().getColor(R.color.black));
                    matesTextView.setTypeface(null, Typeface.NORMAL);
                }
            }


        }

        glide.load(user.getUrlPicture())
                .apply(circleCropTransform())
                .into(matesAvatar);
    }

    /**
     * Update view with user data for joining list.
     *
     * @param userList the user list
     * @param position the position
     * @param glide    the glide
     */
    public void updateViewWithUserDataForJoiningList(ArrayList<User> userList, int position, RequestManager glide) {
        if (userList.size() > 0) {

            matesTextView.setText(String.format("%s %s", userList.get(position).getUsername(), matesTextView.getContext().getString(R.string.isJoining)));
            glide.load(userList.get(position).getUrlPicture())
                    .apply(circleCropTransform())
                    .into(matesAvatar);
        }

    }
}
