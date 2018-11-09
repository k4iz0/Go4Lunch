package ltd.kaizo.go4lunch.views;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import ltd.kaizo.go4lunch.R;
import ltd.kaizo.go4lunch.models.User;

public class MatesViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.mates_recycleViewItem_avatar)
    ImageView matesAvatar;
    @BindView(R.id.mates_recycleViewItem_textview)
    TextView matesTextView;
    public MatesViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }

    public void updateViewWithUserData(User user, RequestManager glide) {
        matesTextView.setText(user.getUsername());
        glide.load(user.getUrlPicture()).into(matesAvatar);

    }
}
