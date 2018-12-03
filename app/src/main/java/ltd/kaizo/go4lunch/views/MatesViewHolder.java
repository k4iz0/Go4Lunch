package ltd.kaizo.go4lunch.views;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import ltd.kaizo.go4lunch.R;
import ltd.kaizo.go4lunch.models.PlaceFormater;
import ltd.kaizo.go4lunch.models.User;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;
import static ltd.kaizo.go4lunch.models.utils.DataRecordHelper.RESTAURANT_LIST_KEY;
import static ltd.kaizo.go4lunch.models.utils.DataRecordHelper.getRestaurantListFromSharedPreferences;

public class MatesViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.mates_recycleViewItem_avatar)
    ImageView matesAvatar;
    @BindView(R.id.mates_recycleViewItem_textview)
    TextView matesTextView;

    public MatesViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void updateViewWithUserData(User user, RequestManager glide) {
        if (user.getChosenRestaurant().equalsIgnoreCase("")) {
            matesTextView.setText(user.getUsername());
            matesTextView.setText(user.getUsername() + " " + matesTextView.getContext().getString(R.string.decideYet));
            itemView.setClickable(false);
        } else {
            ArrayList<PlaceFormater> restaurantList = getRestaurantListFromSharedPreferences(RESTAURANT_LIST_KEY);
            String restaurantName = "";
            if (restaurantList != null) {
                for (PlaceFormater place : restaurantList) {
                    if (place.getId().equalsIgnoreCase(user.getChosenRestaurant())) {
                        restaurantName = place.getPlaceName();
                    }
                }
                matesTextView.setText(user.getUsername() + " " + restaurantName);

                matesTextView.setText(user.getUsername() + " " + matesTextView.getContext().getString(R.string.eatingAt) + restaurantName);
            }


        }

        glide.load(user.getUrlPicture())
                .apply(bitmapTransform(new RoundedCorners(10)) .fitCenter())
                .into(matesAvatar);
    }

    public void updateViewWithUserDataForJoiningList(ArrayList<User> userList, int position, RequestManager glide) {
        if (userList.size() > 0) {
            for (User user : userList) {
                Log.i("matesviewholder", "updateViewWithUserDataForJoiningList:  "+user.getUid());
            }
            matesTextView.setText(userList.get(position).getUsername() + " " + matesTextView.getContext().getString(R.string.isJoining));
            glide.load(userList.get(position).getUrlPicture()).into(matesAvatar);
        }

    }
}
