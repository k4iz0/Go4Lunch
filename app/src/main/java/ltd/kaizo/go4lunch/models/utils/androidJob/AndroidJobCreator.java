package ltd.kaizo.go4lunch.models.utils.androidJob;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.evernote.android.job.DailyJob;
import com.evernote.android.job.Job;
import com.evernote.android.job.JobCreator;

/**
 * The type Android job creator.
 */
public class AndroidJobCreator implements JobCreator {
    @Nullable
    @Override
    public DailyJob create(@NonNull String tag) {
        return new ResetUserChoiceJob();
    }
}
