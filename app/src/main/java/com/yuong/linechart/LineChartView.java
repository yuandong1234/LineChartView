package com.yuong.linechart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by yuandong on 2018/5/10.
 */

public class LineChartView extends View {

    //view宽度
    private int width;

    //view高度
    private int height;

    //X轴长度、Y轴长度
    private float xLen, yLen;

    //Y坐标轴长度与view宽度之比
    private float RATIO = 10f / 19;

    //O点坐标
    private float oX, oY;

    //view边距
    private int padding;

    //文字画笔
    private Paint textPaint;

    //文字颜色
    private int textColor = Color.parseColor("#313131");

    //线条画笔
    private Paint linePaint;

    //内部线条颜色
    private int lineColor = Color.parseColor("#DADADA");

    //X轴颜色
    private int XColor = Color.parseColor("#9BE1BC");

    //线条数
    private int lines = 7;

    //折线画笔
    private Paint pathPaint;

    //内部填充画笔
    private Paint shaderPaint;

    //折线路径
    private Path path;

    //填充路径
    private Path shaderPath;

    //其他画笔
    private Paint otherPaint;

    //单位
    private String unit;

    //Y坐标titles
    private String[] yTitles = {"140", "90", "40"};

    //Y坐标最大值、最小值
    private int maxY, minY;

    //X轴titles
    private String[] xTitles = {"00:00", "06:00", "12:00", "18:00", "24:00"};

    //日统计标注
    private String[] dayLabels = {"正   常", "超   标", "未测量"};

    //月统计标注
    private String[] monthLabels = {"最大值", "最小值", "未测量"};

    //X坐标允许画的点的数量
    private int maxPoints = 5;

    //正常颜色
    private int COLOR_NORMAl = Color.parseColor("#50C647");

    //异常颜色
    private int COLOR_ABNORMAl = Color.parseColor("#FF654B");

    //为测量
    private int COLOR_NONE = Color.parseColor("#4C4C4C");

    //折线颜色
    private int color;

    //线条标注
    private String[] labels;

    //数据
    private float[] data;


    public LineChartView(Context context) {
        this(context, null);
    }

    public LineChartView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LineChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        //默认
        padding = dp2px(10);
        unit = "(ug/m³)";
        maxY = Integer.parseInt(yTitles[0]);
        minY = Integer.parseInt(yTitles[yTitles.length - 1]);

        color = COLOR_NORMAl;
        labels = dayLabels;

        // 初始化画笔
        textPaint = new Paint();
        // 设置抗锯齿
        textPaint.setAntiAlias(true);
        textPaint.setDither(true);

        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        // 设置画笔的宽度（线的粗细）
        linePaint.setStrokeWidth(1);
        linePaint.setDither(true);

        pathPaint = new Paint();
        pathPaint.setAntiAlias(true);
        pathPaint.setStrokeWidth(5);
        pathPaint.setDither(true);
        pathPaint.setStrokeCap(Paint.Cap.ROUND);
        pathPaint.setStyle(Paint.Style.STROKE);

        shaderPaint = new Paint();
        shaderPaint.setAntiAlias(true);
        shaderPaint.setStrokeWidth(2);
        shaderPaint.setDither(true);

        otherPaint = new Paint();
        otherPaint.setAntiAlias(true);
        otherPaint.setStrokeWidth(3);
        otherPaint.setDither(true);

        path = new Path();
        shaderPath = new Path();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
        if (height == 0) {
            setMeasuredDimension(width, MeasureSpec.getSize(heightMeasureSpec));
        } else {
            setMeasuredDimension(width, height);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.WHITE);
        //画单位
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(dp2px(14));
        textPaint.setColor(textColor);
        Rect bounds = new Rect();
        String refer = "(ug/m³)";
        textPaint.getTextBounds(refer, 0, refer.length(), bounds);
        float unitX = 2 * padding + bounds.width() / 2;
        float unitY = padding + bounds.height();
        canvas.drawText(unit, unitX, unitY, textPaint);

        Rect bounds2 = new Rect();
        String refer2 = "140";
        textPaint.getTextBounds(refer2, 0, refer2.length(), bounds2);

        //画Y坐标
        float interval = 0;
        yLen = width * RATIO;
        if (yTitles != null && yTitles.length > 0) {
            interval = yLen / (yTitles.length - 1);
            for (int i = 0; i < yTitles.length; i++) {
                float startY = unitY + 2 * padding + bounds2.height() / 2 + interval * i;
                canvas.drawText(yTitles[i], unitX, startY, textPaint);
            }
        }

        //画内部线条
        interval = yLen / (lines - 1);
        for (int i = 0; i < lines; i++) {
            float startX = 0;
            float stopX = 0;
            float startY = unitY + 2 * padding + interval * i;
            linePaint.setStrokeCap(Paint.Cap.ROUND);
            if (i < lines - 1) {
                startX = 3 * padding + bounds.width();
                stopX = width - 3 * padding;
                linePaint.setColor(lineColor);
                linePaint.setStrokeWidth(1);
            } else {
                startX = 2 * padding + bounds.width();
                stopX = width - 2 * padding;
                linePaint.setColor(XColor);
                linePaint.setStrokeWidth(10);
            }
            canvas.drawLine(startX, startY, stopX, startY, linePaint);
        }
        //画X轴坐标
        //圆心坐标
        oX = 3 * padding + bounds.width();
        oY = unitY + 2 * padding + yLen;
        xLen = width - 6 * padding - bounds.width();
        interval = xLen / (xTitles.length - 1);
        for (int i = 0; i < xTitles.length; i++) {
            float x = oX + interval * i;
            float y = oY + bounds2.height() + padding;
            canvas.drawText(xTitles[i], x, y, textPaint);
        }

        //画标注
        float space = (width - 4 * padding - bounds.width()) / 4;
        float labelY = oY + bounds2.height() * 2 + 2 * padding;
        textPaint.setTextSize(dp2px(10));

        for (int i = 0; i < labels.length; i++) {
            float x = 2 * padding + bounds.width() + space * (i + 1);
            canvas.drawText(labels[i], x, labelY, textPaint);
        }

        if (height == 0) {
            height = (int) (labelY + padding);
            requestLayout();
        }
        //画折线和填充颜色
        drawPath(canvas);
    }

    //TODO
    private void drawPath(Canvas canvas) {
        if (data == null || data.length == 0) return;
        Point firstPoint = null;
        //计算X轴方向两个点之间的距离
        if (data.length > 1) {
            for (int i = 0; i < data.length; i++) {
                Point point = computePoint(i, data[i]);
                if (i == 0) {
                    firstPoint = point;
                    path.moveTo(point.x, point.y);
                    shaderPath.moveTo(point.x, point.y);
                } else {
                    path.lineTo(point.x, point.y);
                    shaderPath.lineTo(point.x, point.y);
                    if (i == data.length - 1) {
                        shaderPath.lineTo(point.x, oY);
                        shaderPath.lineTo(oX, oY);
                        shaderPath.close();
                    }
                }
            }
            Shader mShader = new LinearGradient(0, 0, 0, getHeight(), color, color & 0x00ffffff, Shader.TileMode.MIRROR);
            shaderPaint.setShader(mShader);
            //画填充颜色
            canvas.drawPath(shaderPath, shaderPaint);
            //画折线
            pathPaint.setColor(color);
            pathPaint.setStrokeWidth(5);
            canvas.drawPath(path, pathPaint);
        }

        //圆圈半径
        float radius = 15;
        //画第一个点
        if (firstPoint != null) {
            otherPaint.setColor(Color.WHITE);
            otherPaint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(firstPoint.x, firstPoint.y, radius, otherPaint);
            otherPaint.setStyle(Paint.Style.STROKE);
            otherPaint.setColor(color);
            canvas.drawCircle(firstPoint.x, firstPoint.y, radius, otherPaint);
            otherPaint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(firstPoint.x, firstPoint.y, radius / 2, otherPaint);
        }
    }

    //TODO
    private Point computePoint(int index, float value) {
        float distance = xLen / (maxPoints - 1);
        Point point = new Point();
        point.x = (int) (oX + distance * index);
        point.y = (int) (oY - (value - minY) * yLen / (maxY - minY));
        return point;
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    public void setData(float[] data) {
        this.data = data;
        invalidate();
    }
}