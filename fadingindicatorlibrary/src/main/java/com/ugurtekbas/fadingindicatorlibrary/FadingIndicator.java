package com.ugurtekbas.fadingindicatorlibrary;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Classic viewpager indicators with fading effects.
 * Created by Ugur Tekbas.
 */
public class FadingIndicator extends View implements ViewPager.OnPageChangeListener{

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
    private String shape="",circle,rectangle;

    public FadingIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        circle = getContext().getString(R.string.shape_circle);
        rectangle = getContext().getString(R.string.shape_rect);

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
            if(attributes.getString(R.styleable.FadingIndicator_shape) != null){
                setShape(attributes.getString(R.styleable.FadingIndicator_shape));
            }
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
        if (shape.equals(rectangle)) {
            canvas.drawRect(
                    coordinateX - (calculatedSize),
                    coordinateY - (calculatedSize),
                    coordinateX + (calculatedSize),
                    coordinateY + (calculatedSize),
                    fillPaint);
            canvas.drawRect(
                    coordinateX - (calculatedSize),
                    coordinateY - (calculatedSize),
                    coordinateX + (calculatedSize),
                    coordinateY + (calculatedSize),
                    strokePaint);
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

    public String getShape() {
        return this.shape;
    }

    public void setShape(String shape) {
        this.shape = shape;
        this.invalidate();
    }
}
