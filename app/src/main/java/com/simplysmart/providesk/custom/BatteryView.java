package com.simplysmart.providesk.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by chandrashekhar on 26/8/15.
 */
public class BatteryView extends View {

    private Rect rectangle;
    private Paint paint;

    public BatteryView(Context context) {
        super(context);
    }

    public BatteryView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.TRANSPARENT);

        drawContainerRect(canvas);
        drawHeadRect(canvas);

    }

    private void drawHeadRect(Canvas canvas) {

        int x = 50;
        int y = 50;
        int widthLength = 200;
        int heightLength=100;

        rectangle = new Rect(x, y, widthLength, heightLength);

        // create the Paint and set its color head rectangle
        paint = new Paint();
        paint.setColor(Color.RED);

        canvas.drawRect(rectangle, paint);
    }

    private void drawContainerRect(Canvas canvas) {

        int x = 50;
        int y = 100;
        int widthLength = 450;
        int heightLength=800;

        rectangle = new Rect(x, y, widthLength, heightLength);

        // create the Paint and set its color for container rectangle
        paint = new Paint();
        paint.setColor(Color.GRAY);

        canvas.drawRect(rectangle, paint);
    }

}
