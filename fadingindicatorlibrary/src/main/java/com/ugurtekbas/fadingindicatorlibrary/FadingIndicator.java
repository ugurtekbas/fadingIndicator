package com.ugurtekbas.fadingindicatorlibrary;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import static com.ugurtekbas.fadingindicatorlibrary.FadingIndicator.Shapes.Rectangle;
import static com.ugurtekbas.fadingindicatorlibrary.FadingIndicator.Shapes.Triangle;

/**
 * Classic viewpager indicators with fading effects.
 * @author Ugur Tekbas
 */
public class FadingIndicator extends View implements ViewPager.OnPageChangeListener{

    public enum Shapes {
        Circle(0),
        Rectangle(1),
        Triangle(2);

        private int shapeValue;
        private Shapes(int value) {
            this.shapeValue = value;
        }

        private int getShape(){
            return this.shapeValue;
        }
    }
    private Paint fillPaint = new Paint();
    private Paint strokePaint = new Paint();
    private ViewPager viewPager;
    private int fillColor;
    private int strokeColor;
    private int activeItem = 0;
    private int numberOfItems = 0;
    private int previouslyActiveItem = 99;
    private static final int SELECTED_FACTOR = 2;
    private static final int SPACING_FACTOR = 1;
    private float radius = 30f;
    private float calculatedRadius,constantRadius,previousRadius;
    private Shapes shape;
    private PageChangedListener pageListener;

    public FadingIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        loadAttributes(attrs);

        fillPaint.setAntiAlias(true);
        fillPaint.setStyle(Paint.Style.FILL);
        fillPaint.setStrokeJoin(Paint.Join.ROUND);

        if(strokeColor == 0) {
            strokeColor = fillColor;
        }
        strokePaint.setAntiAlias(true);
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setStrokeWidth((radius / 10) * 2);
        calculatedRadius = (radius / 10) * 6;
        //size of inactive indicator
        constantRadius = calculatedRadius;
    }

    /**
     * Sets indicators' attributes from xml file
     * @param attrs
     */
    private void loadAttributes(AttributeSet attrs) {
        if (attrs == null) return;

        TypedArray attributes = getContext().getTheme().obtainStyledAttributes(
            attrs,
            R.styleable.FadingIndicator,
            0, 0
        );

        if(attributes == null) return;
        try {
            setRadius(attributes.getDimension(R.styleable.FadingIndicator_radius, radius));
            setFillColor(attributes.getColor(R.styleable.FadingIndicator_fillColor, Color.DKGRAY));
            setStrokeColor(attributes.getColor(R.styleable.FadingIndicator_strokeColor, Color.BLACK));
            setShape(getShapeValue(attributes.getInteger(R.styleable.FadingIndicator_shape, Shapes.Circle.getShape())));

        } finally {
            attributes.recycle();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        fillPaint.setColor(fillColor);
        strokePaint.setColor(strokeColor);

        float x, y;
        for (int i = 0;i < numberOfItems; i++) {
            //coordinates of circles
            x = (i * SELECTED_FACTOR * radius) + (i * radius) + (radius * SELECTED_FACTOR);
            y = radius * SELECTED_FACTOR;

            if (i == activeItem) {
                //if its on initial state
                if (previouslyActiveItem == 99){
                    drawIndicators(canvas, x, y, (calculatedRadius * SELECTED_FACTOR));
                }else{
                    drawIndicators(canvas, x, y, calculatedRadius);
                }
            }else if( i == previouslyActiveItem){
                drawIndicators(canvas, x, y, previousRadius);
            }else{
                drawIndicators(canvas, x, y, constantRadius);
            }
        }
    }

    public void drawIndicators(Canvas canvas, float coordinateX, float coordinateY, float calculatedSize) {
        float left      = coordinateX - calculatedSize;
        float top       = coordinateY - calculatedSize;
        float right     = coordinateX + calculatedSize;
        float bottom    = coordinateY + calculatedSize;

        if(shape.equals(Shapes.Rectangle)){
            canvas.drawRect(left, top, right, bottom, fillPaint);
            canvas.drawRect(left, top, right, bottom, strokePaint);
        }else if(shape.equals(Shapes.Triangle)){
            //Creates points of the triangle
            Point pBegining = new Point((int)coordinateX, (int)bottom);
            Point pRightBottom = new Point((int)right, (int)bottom);
            Point pTop = new Point((int)coordinateX, (int)top);
            Point pLeftBottom = new Point((int)left, (int)bottom);

            Path path = new Path();
            //Move to middle of the triangle at the bottom
            path.moveTo(pBegining.x, pBegining.y);
            //Draw the bottom line
            path.lineTo(pRightBottom.x, pRightBottom.y);
            //Draw through top
            path.lineTo(pTop.x, pTop.y);
            //Complete by drawing through bottom
            path.lineTo(pLeftBottom.x, pLeftBottom.y);
            path.close();

            canvas.drawPath(path, fillPaint);
            canvas.drawPath(path, strokePaint);
        }else{
            canvas.drawCircle(coordinateX, coordinateY, calculatedSize, fillPaint);
            canvas.drawCircle(coordinateX, coordinateY, calculatedSize, strokePaint);
        }
    }

    //to set new indicator active when viewpager flipped
    public void setActiveItem(int activeItem, float desiredRadius) {
        this.previouslyActiveItem = this.activeItem;
        this.activeItem = activeItem;

        //setting up animation
        ValueAnimator animation = ValueAnimator.ofFloat(calculatedRadius,desiredRadius);
        animation.setDuration(350);
        animation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                //on every animation update effects 2 indicator:
                //active and previously active one
                calculatedRadius = (float) valueAnimator.getAnimatedValue();
                previousRadius = (constantRadius * SELECTED_FACTOR) - (calculatedRadius - constantRadius);
                FadingIndicator.this.invalidate();
            }
        });
        animation.start();

        this.invalidate();
        this.requestLayout();
    }

    public ViewPager getViewPager() {
        return viewPager;
    }

    public void setViewPager(ViewPager viewPager) {
        this.viewPager = viewPager;
        this.numberOfItems = this.viewPager.getAdapter().getCount();
        this.activeItem = viewPager.getCurrentItem();
        this.invalidate();
        this.requestLayout();
        this.viewPager.addOnPageChangeListener(this);
    }


    @Override
    public void onPageSelected(int position) {
        if(previouslyActiveItem != 99){
            calculatedRadius /= SELECTED_FACTOR;
        }
        //desiredRadius is always size of active indicator
        setActiveItem(position, constantRadius * SELECTED_FACTOR);
        if(pageListener != null){
            pageListener.onPageFlipped(position);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // x(n) = a + d(n-1)
        int n = numberOfItems;
        int measuredWidth = (int) (radius * SELECTED_FACTOR + (radius * (SELECTED_FACTOR + SPACING_FACTOR)) * (n - 1));
        measuredWidth += radius * SELECTED_FACTOR;
        int measuredHeight = (int) (radius * SELECTED_FACTOR) * 2;
        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    public void setRadius(float newRadius){
        this.radius = newRadius;
        calculatedRadius = (radius / 10) * 6;
        constantRadius = calculatedRadius;
        this.invalidate();
    }

    public float getRadius(){
        return this.radius;
    }

    public void setFillColor(int color){
        this.fillColor = color;
        this.invalidate();
    }

    public int getFillColor(){
        return this.fillColor;
    }

    public void setStrokeColor(int color){
        this.strokeColor = color;
        this.invalidate();
    }

    public int getStrokeColor(){
        return this.strokeColor;
    }

    public Shapes getShape() {
        return this.shape;
    }

    public void setShape(Shapes shape) {
        this.shape = shape;
        this.invalidate();
    }

    public PageChangedListener getPageListener() {
        return pageListener;
    }

    public void setPageListener(PageChangedListener pageListener) {
        this.pageListener = pageListener;
    }

    public Shapes getShapeValue(int inValue){
        Shapes mShape;
        switch (inValue){
            case 0:
                mShape = Shapes.Circle;
                break;
            case 1:
                mShape = Rectangle;
                break;
            case 2:
                mShape = Triangle;
                break;
            default:
                mShape = Shapes.Circle;
                break;
        }

        return mShape;
    }
}
