package atms_pro.activities.ui;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewDebug.ExportedProperty;
import android.widget.TextView;

@SuppressLint("AppCompatCustomView")
public class ScrollTextview extends TextView{

    public ScrollTextview(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public ScrollTextview(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
    }

    public ScrollTextview(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }
    @Override
    @ExportedProperty(category = "focus")
    public boolean isFocused() {
        // TODO Auto-generated method stub
        return true;
    }
}