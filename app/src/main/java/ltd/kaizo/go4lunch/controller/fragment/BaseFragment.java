package ltd.kaizo.go4lunch.controller.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * The type Base fragment.
 */
public abstract class BaseFragment extends Fragment {

    /**
     * Gets fragment layout.
     *
     * @return the fragment layout
     */
    protected abstract int getFragmentLayout();

    /**
     * Configure design.
     */
    protected abstract void configureDesign();

    /**
     * Update design.
     */
    protected abstract void updateDesign();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getFragmentLayout(), container, false);
        ButterKnife.bind(this, view);
        this.configureDesign();
        return view;
    }

    @Override

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.updateDesign();

    }


    @Override

    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }


}
