package ltd.kaizo.go4lunch.views.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;

import ltd.kaizo.go4lunch.R;
import ltd.kaizo.go4lunch.models.PlaceFormater;
import timber.log.Timber;

/**
 * The type Place auto complete array adapter.
 */
public class PlaceAutoCompleteArrayAdapter extends ArrayAdapter<PlaceFormater> {
    /**
     * The Place formater list full.
     */
    private ArrayList<PlaceFormater> placeFormaterListFull;
    /**
     * The Place filter.
     */
    private Filter placeFilter = new Filter() {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            ArrayList<PlaceFormater> suggestions = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                //if nothing's type add all list's element to the suggestion list
                suggestions.addAll(placeFormaterListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (PlaceFormater place : placeFormaterListFull) {
                    if (place.getPlaceName().toLowerCase().contains(filterPattern)) {
                        suggestions.add(place);
                        Timber.i("place filter = " + place.getPlaceName());
                    }
                }
            }
            filterResults.values = suggestions;
            filterResults.count = suggestions.size();
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            clear();
            addAll((ArrayList) results.values);
            notifyDataSetChanged();
        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((PlaceFormater) resultValue).getPlaceName();
        }
    };


    /**
     * Instantiates a new Place auto complete array adapter.
     *
     * @param context           the context
     * @param placeFormaterList the place formater list
     */
    public PlaceAutoCompleteArrayAdapter(@NonNull Context context, @NonNull ArrayList<PlaceFormater> placeFormaterList) {
        super(context, 0, placeFormaterList);
        placeFormaterListFull = new ArrayList<>();
        placeFormaterListFull.addAll(placeFormaterList);
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return placeFilter;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.place_autocomplete_row, parent, false
            );
        }
        TextView placeTextView = convertView.findViewById(R.id.autocomplete_textview);
        PlaceFormater placeItem = getItem(position);
        if (placeItem != null) {
            placeTextView.setText(placeItem.getPlaceName() + ", " + placeItem.getPlaceAddress());
        }
        return convertView;
    }
}
