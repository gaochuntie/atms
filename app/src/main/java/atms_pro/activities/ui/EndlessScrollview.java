package atms_pro.activities.ui;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * 无限滚动的ScrollView(无缝滚动)
 * Created by jp04 on 2017/3/10.
 */

public class EndlessScrollview extends ScrollView {

    private static final int MESSAGE_SCROLL = 10010;
    private static int offset = 1000; //滚动的时间间隔
    private int pageSize, itemHeight, maxScrollHeight;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_SCROLL:
                    int scrollY = getScrollY();
                    int delay = 0;

                    if (scrollY >= maxScrollHeight) {
                        scrollTo(0, 0);
                    } else {
                        smoothScrollBy(0, itemHeight);
                        delay = offset;
                    }
                    postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            sendEmptyMessage(MESSAGE_SCROLL);
                        }
                    }, delay);

                    break;

            }
        }
    };

    public EndlessScrollview(Context context) {
        this(context, null);
    }

    public EndlessScrollview(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EndlessScrollview(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        handler.sendEmptyMessage(MESSAGE_SCROLL);
    }

}