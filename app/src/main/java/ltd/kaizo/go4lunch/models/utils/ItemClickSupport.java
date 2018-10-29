package ltd.kaizo.go4lunch.models.utils;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * The type Item click support.
 */
public class ItemClickSupport {
    /**
     * The M recycler view.
     */
    private final RecyclerView mRecyclerView;
    /**
     * The M on item click listener.
     */
    private OnItemClickListener mOnItemClickListener;
    /**
     * The M on item long click listener.
     */
    private OnItemLongClickListener mOnItemLongClickListener;
    /**
     * The M item id.
     */
    private int mItemID;
    /**
     * The M on click listener.
     */
    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                RecyclerView.ViewHolder holder = mRecyclerView.getChildViewHolder(v);
                mOnItemClickListener.onItemClicked(mRecyclerView, holder.getAdapterPosition(), v);
            }
        }
    };
    /**
     * The M on long click listener.
     */
    private View.OnLongClickListener mOnLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            if (mOnItemLongClickListener != null) {
                RecyclerView.ViewHolder holder = mRecyclerView.getChildViewHolder(v);
                return mOnItemLongClickListener.onItemLongClicked(mRecyclerView, holder.getAdapterPosition(), v);
            }
            return false;
        }
    };
    /**
     * The M attach listener.
     */
    private RecyclerView.OnChildAttachStateChangeListener mAttachListener
            = new RecyclerView.OnChildAttachStateChangeListener() {
        @Override
        public void onChildViewAttachedToWindow(View view) {
            if (mOnItemClickListener != null) {
                view.setOnClickListener(mOnClickListener);
            }
            if (mOnItemLongClickListener != null) {
                view.setOnLongClickListener(mOnLongClickListener);
            }
        }

        @Override
        public void onChildViewDetachedFromWindow(View view) {

        }
    };

    /**
     * Instantiates a new Item click support.
     *
     * @param recyclerView the recycler view
     * @param itemID       the item id
     */
    private ItemClickSupport(RecyclerView recyclerView, int itemID) {
        mRecyclerView = recyclerView;
        mItemID = itemID;
        mRecyclerView.setTag(itemID, this);
        mRecyclerView.addOnChildAttachStateChangeListener(mAttachListener);
    }

    /**
     * Add to item click support.
     *
     * @param view   the view
     * @param itemID the item id
     * @return the item click support
     */
    public static ItemClickSupport addTo(RecyclerView view, int itemID) {
        ItemClickSupport support = (ItemClickSupport) view.getTag(itemID);
        if (support == null) {
            support = new ItemClickSupport(view, itemID);
        }
        return support;
    }

    /**
     * Remove from item click support.
     *
     * @param view   the view
     * @param itemID the item id
     * @return the item click support
     */
    public static ItemClickSupport removeFrom(RecyclerView view, int itemID) {
        ItemClickSupport support = (ItemClickSupport) view.getTag(itemID);
        if (support != null) {
            support.detach(view);
        }
        return support;
    }

    /**
     * Sets on item click listener.
     *
     * @param listener the listener
     * @return the on item click listener
     */
    public ItemClickSupport setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
        return this;
    }

    /**
     * Sets on item long click listener.
     *
     * @param listener the listener
     * @return the on item long click listener
     */
    public ItemClickSupport setOnItemLongClickListener(OnItemLongClickListener listener) {
        mOnItemLongClickListener = listener;
        return this;
    }

    /**
     * Detach.
     *
     * @param view the view
     */
    private void detach(RecyclerView view) {
        view.removeOnChildAttachStateChangeListener(mAttachListener);
        view.setTag(mItemID, null);
    }

    /**
     * The interface On item click listener.
     */
    public interface OnItemClickListener {

        /**
         * On item clicked.
         *
         * @param recyclerView the recycler view
         * @param position     the position
         * @param v            the v
         */
        void onItemClicked(RecyclerView recyclerView, int position, View v);
    }

    /**
     * The interface On item long click listener.
     */
    public interface OnItemLongClickListener {

        /**
         * On item long clicked boolean.
         *
         * @param recyclerView the recycler view
         * @param position     the position
         * @param v            the v
         * @return the boolean
         */
        boolean onItemLongClicked(RecyclerView recyclerView, int position, View v);
    }
}