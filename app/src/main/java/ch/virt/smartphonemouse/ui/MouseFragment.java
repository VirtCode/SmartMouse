package ch.virt.smartphonemouse.ui;

import android.annotation.SuppressLint;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowInsets;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import ch.virt.smartphonemouse.R;
import ch.virt.smartphonemouse.helper.MainContext;
import ch.virt.smartphonemouse.mouse.MouseInputs;

import static android.content.Context.VIBRATOR_SERVICE;

public class MouseFragment extends CustomFragment {

    RelativeLayout root;
    private int width, height;

    MouseInputs mouse;

    // Feedback
    private View leftView, rightView, middleView;
    private Vibrator vibrator;

    // Buttons
    private int leftX, leftY, leftWidth, leftHeight;
    private int rightX, rightY, rightWidth, rightHeight;
    private int middleX, middleY, middleWidth, middleHeight;

    boolean left, right, middle;

    // Middle Specific
    private int middleStart;
    private long middleStartTime;

    private boolean middleDecided;
    private boolean middleScrolling;

    // Configurable
    private float buttonsHeight = 0.333f;
    private float buttonsMiddleWidth = 0.2f;

    private boolean visuals = true;
    private int buttonsStrokeWeight = 4;

    private int scrollThreshold = 50;
    private int middleClickWait = 300;

    private boolean vibrations = true;
    private int buttonIntensity = 100;
    private int buttonLength = 30;
    private int scrollIntensity = 50;
    private int scrollLength = 20;
    private int specialIntensity = 100;
    private int specialLength = 50;

    public MouseFragment(MainContext context, MouseInputs mouse) {
        super(R.layout.fragment_mouse, context);

        this.mouse = mouse;
    }

    @Override
    public void render() {

        root.setBackgroundResource(R.color.mouse_background);
        getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.mouse_background));

        if (!visuals) getActivity().getWindow().getInsetsController().hide(WindowInsets.Type.statusBars());

        getActivity().getWindow().getInsetsController().hide(WindowInsets.Type.mandatorySystemGestures());
        getActivity().getWindow().getInsetsController().hide(WindowInsets.Type.systemGestures());
        getActivity().getWindow().getInsetsController().hide(WindowInsets.Type.navigationBars());
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void initComponents() {
        root.post(() -> {

            calculate();
            if (visuals) createVisuals();

        });

        root.setOnTouchListener((v, event) -> viewTouched(event));

        if (vibrations) vibrator = (Vibrator) getContext().getSystemService(VIBRATOR_SERVICE);

    }

    @Override
    protected void loadComponents(View view) {
        root = view.findViewById(R.id.mouse_root);
    }

    public void calculate(){
        width = root.getWidth();
        height = root.getHeight();

        int buttonWidth = (int) (width * ((1 - buttonsMiddleWidth) / 2));
        int buttonHeight = (int) (height * buttonsHeight);

        leftX = 0;
        leftY = 0;
        leftHeight = buttonHeight;
        leftWidth = buttonWidth;

        rightX = width - buttonWidth;
        rightY = 0;
        rightHeight = buttonHeight;
        rightWidth = buttonWidth;

        middleX = buttonWidth;
        middleY = 0;
        middleHeight = buttonHeight;
        middleWidth = width - buttonWidth * 2;
    }

    public void createVisuals(){

        View horizontal = new View(getContext());
        horizontal.setBackgroundResource(R.color.mouse_stroke);
        horizontal.setLayoutParams(new FrameLayout.LayoutParams(width, buttonsStrokeWeight));
        horizontal.setY(middleHeight);
        horizontal.setX(0);
        root.addView(horizontal);

        View verticalLeft = new View(getContext());
        verticalLeft.setBackgroundResource(R.color.mouse_stroke);
        verticalLeft.setLayoutParams(new FrameLayout.LayoutParams(buttonsStrokeWeight, leftHeight));
        verticalLeft.setX(leftWidth - buttonsStrokeWeight / 2);
        verticalLeft.setY(leftY);
        root.addView(verticalLeft);

        View verticalRight = new View(getContext());
        verticalRight.setBackgroundResource(R.color.mouse_stroke);
        verticalRight.setLayoutParams(new FrameLayout.LayoutParams(buttonsStrokeWeight, rightHeight));
        verticalRight.setX(rightX - buttonsStrokeWeight / 2);
        verticalRight.setY(rightY);
        root.addView(verticalRight);

        leftView = new View(getContext());
        leftView.setBackgroundResource(R.color.mouse_pressed);
        leftView.setLayoutParams(new FrameLayout.LayoutParams(leftWidth - buttonsStrokeWeight / 2, leftHeight));
        leftView.setX(leftX);
        leftView.setY(leftY);
        root.addView(leftView);

        rightView = new View(getContext());
        rightView.setBackgroundResource(R.color.mouse_pressed);
        rightView.setLayoutParams(new FrameLayout.LayoutParams(rightWidth - buttonsStrokeWeight / 2, rightHeight));
        rightView.setX(rightX + buttonsStrokeWeight / 2);
        rightView.setY(rightY);
        root.addView(rightView);

        middleView = new View(getContext());
        middleView.setBackgroundResource(R.color.mouse_pressed);
        middleView.setLayoutParams(new FrameLayout.LayoutParams(middleWidth - buttonsStrokeWeight, middleHeight));
        middleView.setX(middleX + buttonsStrokeWeight / 2);
        middleView.setY(middleY);
        root.addView(middleView);

        leftView.setVisibility(View.INVISIBLE);
        rightView.setVisibility(View.INVISIBLE);
        middleView.setVisibility(View.INVISIBLE);

    }

    public void vibrate(int length, int intensity){
        if (vibrations) vibrator.vibrate(VibrationEffect.createOneShot(length, intensity));
    }

    public void setVisibility(View view, boolean visible){
        if (visible) view.setVisibility(View.VISIBLE);
        else view.setVisibility(View.INVISIBLE);
    }

    public static boolean within(float touchX, float touchY, int x, int y, int width, int height){
        return touchX > x && touchX < x + width && touchY > y && touchY < y + height;
    }

    public boolean viewTouched(MotionEvent event) {

        // Temporary Variables
        boolean left = false, right = false, middle = false;

        // Check whether a pointer is on a button, and if, check whether it is currently releasing or not
        for (int i = 0; i < event.getPointerCount(); i++) {
            if (within(event.getX(i), event.getY(i), leftX, leftY, leftWidth, leftHeight)){ // Left Mouse Button
                if ((event.getActionIndex() == i && event.getActionMasked() != MotionEvent.ACTION_POINTER_UP && event.getActionMasked() != MotionEvent.ACTION_UP) || event.getActionIndex() != i) left = true;
            }
            if (within(event.getX(i), event.getY(i), rightX, rightY, rightWidth, rightHeight)){ // Right Mouse Button
                if ((event.getActionIndex() == i && event.getActionMasked() != MotionEvent.ACTION_POINTER_UP && event.getActionMasked() != MotionEvent.ACTION_UP) || event.getActionIndex() != i) right = true;
            }
            if (within(event.getX(i), event.getY(i), middleX, middleY, middleWidth, middleHeight)){ // Middle Mouse Button
                if ((event.getActionIndex() == i && event.getActionMasked() != MotionEvent.ACTION_POINTER_UP && event.getActionMasked() != MotionEvent.ACTION_UP) || event.getActionIndex() != i) middle = true;

                if (!this.middle && middle){
                    middleStart = (int) event.getY(i);
                    middleStartTime = System.currentTimeMillis();
                    middleDecided = false;
                }
                else if (middle){
                    if (middleStart - event.getY(i) > scrollThreshold && (!middleDecided || middleScrolling)){ // Scroll up
                        mouse.changeWheelPosition(1);
                        middleStart -= scrollThreshold;

                        middleDecided = true;
                        middleScrolling = true;

                        setVisibility(middleView, true);
                        vibrate(scrollLength, scrollIntensity);
                    }else if (middleStart - event.getY(i) < -scrollThreshold && (!middleDecided || middleScrolling)){ // Scroll down
                        mouse.changeWheelPosition(-1);
                        middleStart += scrollThreshold;

                        middleDecided = true;
                        middleScrolling = true;

                        setVisibility(middleView, true);
                        vibrate(scrollLength, scrollIntensity);
                    }else { // Click
                        if (System.currentTimeMillis() - middleStartTime > middleClickWait && !middleDecided) {
                            mouse.setMiddleButton(true);

                            middleDecided = true;
                            middleScrolling = false;

                            setVisibility(middleView, true);
                            vibrate(specialLength, specialIntensity);
                        }
                    }
                }
            }
        }

        // Update Feedback
        if (this.left != left) {
            vibrate(buttonLength, buttonIntensity);
            setVisibility(leftView, left);
        }
        if (this.right != right) {
            vibrate(buttonLength, buttonIntensity);
            setVisibility(rightView, right);
        }
        if (this.middle != middle){
            if (!middle) setVisibility(middleView, false);
            if (!middle && middleDecided && !middleScrolling) vibrate(buttonLength, buttonIntensity);
        }

        // Send Data
        if (this.middle != middle && !middle && middleDecided && !middleScrolling) mouse.setMiddleButton(false);
        if (this.left != left) mouse.setLeftButton(left);
        if (this.right != right) mouse.setRightButton(right);

        // Update self
        this.left = left;
        this.right = right;
        this.middle = middle;

        return true;
    }
}
