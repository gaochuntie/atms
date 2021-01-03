package atms_pro.activities.ui.objs;

import android.content.Context;

public class ScrollTextview extends androidx.appcompat.widget.AppCompatTextView {
    public ScrollTextview(Context context) {
        super(context);
    }

    @Override
    public boolean isFocused() {
        return true;
    }
}
