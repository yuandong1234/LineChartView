package com.yuong.linechart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuandong on 2018/5/10.
 */

public class LineChartView extends View {

    private static final String TAG = LineChartView.class.getSimpleName();

    //日
    public static final int DAY = 0X11;
    //月
    public static final int MONTH = 0X12;

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

    //圆圈半径
    private float radius;

    //文字画笔
    private Paint textPaint;

    //文字矩形（用于确定文字的宽高）
    private Rect bounds,bounds2,bounds3;

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

    //虚线画笔
    private Paint dashLinePaint;

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

    //日统计坐标title
    private String[] dayTitles = {"00:00", "06:00", "12:00", "18:00", "24:00"};

    //月统计坐标title
    private String[] monthTitles = {"1", "7", "14", "21", "28"};
    //X轴titles
    private String[] xTitles = dayTitles;

    //日统计标注
    private String[] dayLabels = {"正   常", "超   标", "未测量"};

    //月统计标注
    private String[] monthLabels = {"最大值", "最小值", "未测量"};

    //X坐标允许画的点的数量
    private int maxPoints = 24 * 60 * 60;

    //正常颜色
    private int COLOR_NORMAl = Color.parseColor("#50C647");

    //异常颜色
    private int COLOR_ABNORMAl = Color.parseColor("#FF654B");

    //未测量
    private int COLOR_MAX = Color.parseColor("#486FC6");

    //最大值
    private int COLOR_MIN = Color.parseColor("#C2C647");

    //
    private int COLOR_NONE = Color.parseColor("#a5a5a5");

    //折线颜色
    private int color;

    //线条标注
    private String[] labels;

    //统计类型(日与月)
    private int style = DAY;

    //虚点集合
    private List<PointItem> dashPoints;

    //圆圈集合
    private List<PointItem> circlePoints;

    //数据
    private List<List<ViewItem>> mData;

    //月统计数据
    private List<List<Float>> mData2;


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
        radius = 15f;
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

        dashLinePaint = new Paint();
        dashLinePaint.setStyle(Paint.Style.STROKE);
        dashLinePaint.setStrokeWidth(5);
        dashLinePaint.setAntiAlias(true);
        dashLinePaint.setColor(COLOR_NONE);
        DashPathEffect pathEffect = new DashPathEffect(new float[]{10, 10}, 0);
        dashLinePaint.setPathEffect(pathEffect);

        path = new Path();
        shaderPath = new Path();

        bounds = new Rect();
        bounds2 = new Rect();
        bounds3 = new Rect();

        dashPoints = new ArrayList<>();
        circlePoints = new ArrayList<>();


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

        String refer = "(ug/m³)";
        textPaint.getTextBounds(refer, 0, refer.length(), bounds);
        float unitX = 2 * padding + bounds.width() / 2;
        float unitY = padding + bounds.height();
        canvas.drawText(unit, unitX, unitY, textPaint);

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
        String refer3 = "未测量";
        textPaint.setTextSize(dp2px(10));
        textPaint.getTextBounds(refer3, 0, refer3.length(), bounds3);
        float labelY = oY + bounds2.height() + bounds3.height() + 2 * padding;
        float labelWidth = 2 * bounds3.width() + padding / 2;
        float space = (width - 4 * padding - bounds.width() - 3 * labelWidth) / 4;

        for (int i = 0; i < labels.length; i++) {
            float x = 2 * padding + bounds.width() + space * (i + 1) + labelWidth * i;
            if (i == 0) {
                if (style == DAY) {
                    linePaint.setColor(COLOR_NORMAl);
                } else {
                    linePaint.setColor(COLOR_MAX);
                }
            } else if (i == 1) {
                if (style == DAY) {
                    linePaint.setColor(COLOR_ABNORMAl);
                } else {
                    linePaint.setColor(COLOR_MIN);
                }
            } else {
                linePaint.setColor(COLOR_NONE);
            }
            linePaint.setStrokeWidth(1);
            textPaint.setTextAlign(Paint.Align.LEFT);
            float stopX = x + bounds3.width();
            canvas.drawLine(x, labelY - bounds3.height() / 2, stopX, labelY - bounds3.height() / 2, linePaint);
            canvas.drawText(labels[i], stopX + padding / 2, labelY, textPaint);
        }

        if (height == 0) {
            height = (int) (labelY + padding);
            requestLayout();
        }

        if (style == DAY) {
            //日统计
            drawPaths(canvas);
        } else {
            //月统计
            drawPaths2(canvas);
        }

    }

    private void drawPaths(Canvas canvas) {
        if (mData == null || mData.size() == 0) return;
        circlePoints.clear();
        dashPoints.clear();
        for (int i = 0; i < mData.size(); i++) {
            List<ViewItem> element = mData.get(i);
            drawLine(canvas, i, element);
        }
        //画虚线
        drawDashPaths(canvas);
        //画圆圈
        drawCircles(canvas);
    }

    private void drawPaths2(Canvas canvas) {
        if (mData2 == null || mData2.size() == 0) return;
        for (int i = 0; i < mData2.size(); i++) {
            drawLine2(canvas, i, mData2.get(i));
        }
    }


    private void drawLine(Canvas canvas, int index, List<ViewItem> data) {
        if (data == null || data.size() == 0) return;
        Point firstPoint = null;
        Point lastPoint = null;
        double startTen = 0;
        double endTen = 0;
        double ten = 0;

        path.reset();
        shaderPath.reset();
        //计算X轴方向两个点之间的距离
        if (data.size() > 1) {
            for (int i = 0; i < data.size(); i++) {
                Point point = computePoint(data.get(i));
                if (i == 0) {
                    firstPoint = point;
                } else if (i == data.size() - 1) {
                    lastPoint = point;
                }
                if (i + 1 <= data.size() - 1) {
                    ViewItem item = data.get(i + 1);
                    Point point2 = computePoint(item);
                    if (item.level == 0) {
                        color = COLOR_NORMAl;
                    } else {
                        color = COLOR_ABNORMAl;
                    }

                    path.reset();
                    shaderPath.reset();
                    path.moveTo(point.x, point.y);
                    path.lineTo(point2.x, point2.y);

                    shaderPath.moveTo(point.x, point.y);
                    shaderPath.lineTo(point2.x, point2.y);
                    shaderPath.lineTo(point2.x, oY);
                    shaderPath.lineTo(point.x, oY);
                    shaderPath.close();

                    Shader mShader = new LinearGradient(0, 0, 0, getHeight(), color, color & 0x00ffffff, Shader.TileMode.MIRROR);
                    shaderPaint.setShader(mShader);
                    //画填充颜色
                    canvas.drawPath(shaderPath, shaderPaint);
                    //画折线
                    pathPaint.setColor(color);
                    pathPaint.setStrokeWidth(5);
                    canvas.drawPath(path, pathPaint);
                }
                if (i + 1 <= data.size() - 1) {
                    Point nextPoint = computePoint(data.get(i + 1));
                    double y1 = nextPoint.y - point.y;
                    double x1 = nextPoint.x - point.x;
                   // Log.e(TAG, "y1 :　" + y1 + " x1 : " + x1);
                    ten = y1 * 1d / x1;
                    if (i == 0) {
                        startTen = -ten;
                    }
                }
                endTen = -ten;
            }
        } else {
            //就一个点
            firstPoint = computePoint(data.get(0));
        }

        //添加虚点和圆圈点
        if (data.size() > 1) {
            if (index == 0) {
                if (firstPoint != null) {
                    PointItem item = new PointItem();
                    item.point = firstPoint;
                    item.isFirst = true;
                    circlePoints.add(item);
                }
                if (lastPoint != null) {
                    Point point = computePoint(lastPoint, endTen, true, false);
                    PointItem item = new PointItem();
                    item.point = point;
                    item.isFirst = false;
                    dashPoints.add(item);
                    circlePoints.add(item);
                }
            } else {
                if (firstPoint != null) {
                    Point point = computePoint(firstPoint, startTen, true, true);
                    PointItem item = new PointItem();
                    item.point = point;
                    item.isFirst = true;
                    dashPoints.add(item);
                    circlePoints.add(item);
                }
                if (lastPoint != null) {
                    Point point = computePoint(lastPoint, endTen, true, false);
                    PointItem item = new PointItem();
                    item.point = point;
                    item.isFirst = false;
                    if (index != mData.size() - 1) {
                        dashPoints.add(item);
                        circlePoints.add(item);
                    }
                }
            }
        } else {
            PointItem item = new PointItem();
            item.point = firstPoint;
            item.isFirst = true;
            dashPoints.add(item);
            circlePoints.add(item);
        }
    }

    private void drawDashPaths(Canvas canvas) {
        if (dashPoints == null || dashPoints.size() == 0 || dashPoints.size() == 1) return;
        for (int i = 0; i < dashPoints.size(); i++) {
            if (i + 1 <= dashPoints.size() - 1) {
                PointItem point1 = dashPoints.get(i);
                PointItem point2 = dashPoints.get(i + 1);
                if (point2.isFirst) {
                    path.reset();
                    path.moveTo(point1.point.x, point1.point.y);
                    path.lineTo(point2.point.x, point2.point.y);
                    dashLinePaint.setColor(COLOR_NONE);
                    canvas.drawPath(path, dashLinePaint);
                }
            }
        }
    }

    private void drawCircles(Canvas canvas) {
        if (circlePoints == null || circlePoints.size() == 0) return;
        for (int i = 0; i < circlePoints.size(); i++) {
            PointItem item = circlePoints.get(i);
            if (i == 0) {
                ViewItem viewItem = mData.get(0).get(0);
                if (viewItem.level == 0) {
                    color = COLOR_NORMAl;
                } else {
                    color = COLOR_ABNORMAl;
                }
                drawSolidCircle(canvas, item.point);
            } else {
                drawHollowCircle(canvas, item.point);
            }
        }

    }

    private void drawLine2(Canvas canvas, int index, List<Float> data) {

        if (data == null || data.size() == 0) return;
        if (index == 0) {
            color = COLOR_MAX;
        } else {
            color = COLOR_MIN;
        }
        pathPaint.setColor(color);
        dashLinePaint.setColor(color);

        Point firstPoint = null;
        boolean isReal = false;

        List<Point> dashPoints = new ArrayList<>();

        if (data.size() > 1) {
            for (int i = 0; i < data.size(); i++) {
                Point point = computePoint(i, data.get(i));
                //获得第一个点
                if (point.y != -1 && firstPoint == null) {
                    firstPoint = point;
                    isReal = true;
                }

                //添加画虚线的点
                if (firstPoint != null) {
                    if (point.y != -1) {
                        if (!isReal) {
                            dashPoints.add(point);
                        }
                        isReal = true;
                    } else {
                        if (isReal) {
                            Point lastPoint = computePoint(i - 1, data.get(i - 1));
                            dashPoints.add(lastPoint);
                        }
                        isReal = false;
                    }
                }

                //画折线
                if (i + 1 <= data.size() - 1) {
                    Point point2 = computePoint(i + 1, data.get(i + 1));
                    if (point.y != -1 && point2.y != -1) {
                        path.reset();
                        path.moveTo(point.x, point.y);
                        path.lineTo(point2.x, point2.y);
                        pathPaint.setStrokeWidth(5);
                        canvas.drawPath(path, pathPaint);
                    }
                }
            }

            //画虚线和圆圈
            if (dashPoints.size() > 1) {
                for (int i = 0; i < dashPoints.size(); i++) {
                    Point point1 = dashPoints.get(i);
                    if (i + 1 <= dashPoints.size() - 1) {
                        Point point2 = dashPoints.get(i + 1);
                        path.reset();
                        path.moveTo(point1.x, point1.y);
                        path.lineTo(point2.x, point2.y);
                        canvas.drawPath(path, dashLinePaint);
                    }
                    drawHollowCircle(canvas, point1);
                }
            }
        } else {
            //就一个点
            Point point = computePoint(0, data.get(0));
            if (point.y != -1) {
                firstPoint = point;
            }
        }
        if (firstPoint != null) {
            drawSolidCircle(canvas, firstPoint);
        }
    }

    //画实心圆
    private void drawSolidCircle(Canvas canvas, Point point) {
        otherPaint.setColor(Color.WHITE);
        otherPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(point.x, point.y, radius, otherPaint);
        otherPaint.setStyle(Paint.Style.STROKE);
        otherPaint.setColor(color);
        canvas.drawCircle(point.x, point.y, radius, otherPaint);
        otherPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(point.x, point.y, radius / 2, otherPaint);
    }

    //画空心圆
    private void drawHollowCircle(Canvas canvas, Point point) {
        otherPaint.setColor(Color.WHITE);
        otherPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(point.x, point.y, radius, otherPaint);
        otherPaint.setStyle(Paint.Style.STROKE);
        otherPaint.setColor(COLOR_NONE);
        canvas.drawCircle(point.x, point.y, radius, otherPaint);
    }

    //计算坐标
    private Point computePoint(ViewItem item) {
        float distance = xLen / (maxPoints - 1);
        Point point = new Point();
        point.x = (int) (oX + distance * item.time);
        //TODO 数据值有可能等于0
        point.y = (int) (oY - (item.value - minY) * yLen / (maxY - minY));
        return point;
    }


    private Point computePoint(int index, Float value) {
        float distance = xLen / (maxPoints - 1);
        Point point = new Point();
        point.x = (int) (oX + distance * index);
        if (value != null) {
            point.y = (int) (oY - (value - minY) * yLen / (maxY - minY));
        } else {
            point.y = -1;
        }

        return point;
    }

    /**
     * 计算出偏移的坐标
     *
     * @param point
     * @param ten
     * @param offset
     * @param isFirst
     * @return
     */
    private Point computePoint(Point point, double ten, boolean offset, boolean isFirst) {
        Point newPoint = new Point();
        double angle = Math.atan(Math.abs(ten));
        float x = 0;
        float y = 0;
        if (ten > 0) {
            if (!offset) {
                x = point.x;
                y = point.y;
            } else {
                if (isFirst) {
                    x = (float) (point.x - radius * Math.cos(angle));
                    y = (float) (point.y + radius * Math.sin(angle));
                } else {
                    x = (float) (point.x + radius * Math.cos(angle));
                    y = (float) (point.y - radius * Math.sin(angle));
                }
            }
        } else if (ten == 0) {
            if (!offset) {
                x = point.x;
                y = point.y;
            } else {
                if (isFirst) {
                    x = point.x - radius;
                    y = point.y;
                } else {
                    x = point.x + radius;
                    y = point.y;
                }
            }

        } else {
            if (!offset) {
                x = point.x;
                y = point.y;
            } else {
                if (isFirst) {
                    x = (float) (point.x - radius * Math.cos(angle));
                    y = (float) (point.y - radius * Math.sin(angle));
                } else {
                    x = (float) (point.x + radius * Math.cos(angle));
                    y = (float) (point.y + radius * Math.sin(angle));
                }
            }
        }
        newPoint.x = (int) x;
        newPoint.y = (int) y;
        return newPoint;
    }


    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }


    public void setUnit(String unit, String[] yTitles) {
        this.unit = unit;
        this.yTitles = yTitles;
        maxY = Integer.parseInt(yTitles[0]);
        minY = Integer.parseInt(yTitles[yTitles.length - 1]);
        invalidate();
    }

    public void setStyle(int style) {
        this.style = style;
        switch (style) {
            case DAY:
                labels = dayLabels;
                xTitles = dayTitles;
                maxPoints = 24 * 60 * 60;
                break;
            case MONTH:
                labels = monthLabels;
                xTitles = monthTitles;
                maxPoints = 31;
                break;
            default:
                labels = dayLabels;
                xTitles = dayTitles;
                maxPoints = 31;
                break;
        }
        invalidate();
    }

    //设置数据
    public void setData(List<List<ViewItem>> data) {
        this.mData = data;
        this.mData2 = null;
        invalidate();
    }

    public void setData2(List<List<Float>> mData2) {
        this.mData2 = mData2;
        this.mData = null;
        invalidate();
    }

    public class PointItem {
        boolean isFirst;
        Point point;
    }
}
