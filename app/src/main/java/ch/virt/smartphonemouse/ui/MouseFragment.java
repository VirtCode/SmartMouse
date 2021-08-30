package ch.virt.smartphonemouse.ui;

import static android.content.Context.VIBRATOR_SERVICE;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import androidx.preference.PreferenceManager;

import ch.virt.smartphonemouse.R;
import ch.virt.smartphonemouse.helper.MainContext;
import ch.virt.smartphonemouse.mouse.MouseInputs;
import ch.virt.smartphonemouse.mouse.MovementHandler;
import ch.virt.smartphonemouse.ui.mouse.MouseUsageDialog;

/**
 * This fragment represents the mouse interface the user uses to input button strokes 
 */
public class MouseFragment extends CustomFragment {

    private RelativeLayout root;
    private int width, height;
    private boolean theme; // false = light, true = dark

    private MouseInputs mouse;
    private MovementHandler movement;

    // Feedback
    private boolean visuals;
    private int buttonsStrokeWeight;
    private float viewIntensity;

    private boolean vibrations;
    private int buttonIntensity;
    private int buttonLength;
    private int scrollIntensity;
    private int scrollLength;
    private int specialIntensity;
    private int specialLength;

    private View leftView, rightView, middleView;
    private Vibrator vibrator;

    // Buttons
    private float buttonsHeight;
    private float buttonsMiddleWidth;

    private int leftX, leftY, leftWidth, leftHeight;
    private int rightX, rightY, rightWidth, rightHeight;
    private int middleX, middleY, middleWidth, middleHeight;

    boolean left, right, middle;

    // Middle Specific
    private int middleClickWait;
    private int scrollThreshold;

    private int middleStart;
    private long middleStartTime;

    private boolean middleDecided;
    private boolean middleScrolling;

    /**
     * Creates a Mouse Fragment
     * @param context main context
     * @param mouse mouse to send the inputs to
     */
    public MouseFragment(MainContext context, MouseInputs mouse, MovementHandler movement) {
        super(R.layout.fragment_mouse, context);

        this.mouse = mouse;
        this.movement = movement;
    }

    /**
     * Reads the settings for the fragment from the preferences
     */
    private void readSettings(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());

        theme = prefs.getString("interfaceTheme", "dark").equals("dark");

        scrollThreshold = prefs.getInt("interfaceBehaviourScrollStep", 50);
        middleClickWait = prefs.getInt("interfaceBehaviourSpecialWait", 300);

        visuals = prefs.getBoolean("interfaceVisualsEnable", true);
        buttonsStrokeWeight = prefs.getInt("interfaceVisualsStrokeWeight", 4);
        viewIntensity = prefs.getFloat("interfaceVisualsIntensity", 0.5f);

        vibrations = prefs.getBoolean("interfaceVibrationsEnable", true);
        buttonIntensity = prefs.getInt("interfaceVibrationsButtonIntensity", 100);
        buttonLength = prefs.getInt("interfaceVibrationsButtonLength", 30);
        scrollIntensity = prefs.getInt("interfaceVibrationsScrollIntensity", 50);
        scrollLength = prefs.getInt("interfaceVibrationsScrollLength", 20);
        specialIntensity = prefs.getInt("interfaceVibrationsSpecialIntensity", 100);
        specialLength = prefs.getInt("interfaceVibrationsSpecialLength", 50);

        buttonsHeight = prefs.getFloat("interfaceLayoutHeight", 0.3f);
        buttonsMiddleWidth = prefs.getFloat("interfaceLayoutMiddleWidth", 0.2f);
    }

    @Override
    public void render() {
        root.setBackgroundResource(theme ? R.color.mouse_background_dark : R.color.mouse_background_light);
        getActivity().getWindow().setStatusBarColor(getResources().getColor(theme ? R.color.mouse_background_dark : R.color.mouse_background_light));
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!visuals) getActivity().getWindow().getInsetsController().hide(WindowInsets.Type.statusBars());
            getActivity().getWindow().getInsetsController().hide(WindowInsets.Type.mandatorySystemGestures());
            getActivity().getWindow().getInsetsController().hide(WindowInsets.Type.systemGestures());
            getActivity().getWindow().getInsetsController().hide(WindowInsets.Type.navigationBars());
        } else {
            if (!visuals) getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        }

    }

    @Override
    public void restore() {
        getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.design_default_color_primary_dark));
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!visuals) getActivity().getWindow().getInsetsController().show(WindowInsets.Type.statusBars());

            getActivity().getWindow().getInsetsController().show(WindowInsets.Type.mandatorySystemGestures());
            getActivity().getWindow().getInsetsController().show(WindowInsets.Type.systemGestures());
            getActivity().getWindow().getInsetsController().show(WindowInsets.Type.navigationBars());
        } else {
            getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        }


    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void initComponents() {
        readSettings();

        root.post(() -> {

            calculate();
            if (visuals) createVisuals();

        });

        root.setOnTouchListener((v, event) -> viewTouched(event));

        if (vibrations) vibrator = (Vibrator) getContext().getSystemService(VIBRATOR_SERVICE);

        if (PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean("showUsage", true)){
            movement.unregister();
            mouse.stop();

            MouseUsageDialog dialog = new MouseUsageDialog(this.main, () -> {

                mouse.start();
                movement.register();

            });
            dialog.show(getParentFragmentManager(), null);
        }

    }

    @Override
    protected void loadComponents(View view) {
        root = view.findViewById(R.id.mouse_root);
    }

    /**
     * Calculates the buttons size
     */
    private void calculate(){
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

    /**
     * Creates the visuals
     */
    private void createVisuals(){

        View horizontal = new View(getContext());
        horizontal.setBackgroundResource(theme ? R.color.mouse_stroke_dark : R.color.mouse_stroke_light);
        horizontal.setAlpha(viewIntensity);
        horizontal.setLayoutParams(new FrameLayout.LayoutParams(width, buttonsStrokeWeight));
        horizontal.setY(middleHeight);
        horizontal.setX(0);
        root.addView(horizontal);

        View verticalLeft = new View(getContext());
        verticalLeft.setBackgroundResource(theme ? R.color.mouse_stroke_dark : R.color.mouse_stroke_light);
        verticalLeft.setAlpha(viewIntensity);
        verticalLeft.setLayoutParams(new FrameLayout.LayoutParams(buttonsStrokeWeight, leftHeight));
        verticalLeft.setX(leftWidth - buttonsStrokeWeight / 2);
        verticalLeft.setY(leftY);
        root.addView(verticalLeft);

        View verticalRight = new View(getContext());
        verticalRight.setBackgroundResource(theme ? R.color.mouse_stroke_dark : R.color.mouse_stroke_light);
        verticalRight.setAlpha(viewIntensity);
        verticalRight.setLayoutParams(new FrameLayout.LayoutParams(buttonsStrokeWeight, rightHeight));
        verticalRight.setX(rightX - buttonsStrokeWeight / 2);
        verticalRight.setY(rightY);
        root.addView(verticalRight);

        leftView = new View(getContext());
        leftView.setBackgroundResource(theme ? R.color.mouse_pressed_dark : R.color.mouse_pressed_light);
        leftView.setAlpha(viewIntensity);
        leftView.setLayoutParams(new FrameLayout.LayoutParams(leftWidth - buttonsStrokeWeight / 2, leftHeight));
        leftView.setX(leftX);
        leftView.setY(leftY);
        root.addView(leftView);

        rightView = new View(getContext());
        rightView.setBackgroundResource(theme ? R.color.mouse_pressed_dark : R.color.mouse_pressed_light);
        rightView.setAlpha(viewIntensity);
        rightView.setLayoutParams(new FrameLayout.LayoutParams(rightWidth - buttonsStrokeWeight / 2, rightHeight));
        rightView.setX(rightX + buttonsStrokeWeight / 2);
        rightView.setY(rightY);
        root.addView(rightView);

        middleView = new View(getContext());
        middleView.setBackgroundResource(theme ? R.color.mouse_pressed_dark : R.color.mouse_pressed_light);
        middleView.setAlpha(viewIntensity);
        middleView.setLayoutParams(new FrameLayout.LayoutParams(middleWidth - buttonsStrokeWeight, middleHeight));
        middleView.setX(middleX + buttonsStrokeWeight / 2);
        middleView.setY(middleY);
        root.addView(middleView);

        leftView.setVisibility(View.INVISIBLE);
        rightView.setVisibility(View.INVISIBLE);
        middleView.setVisibility(View.INVISIBLE);

    }

    /**
     * Processes all touch events
     * @param event touch event
     * @return whether used
     */
    private boolean viewTouched(MotionEvent event) {
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

    /**
     * Vibrates the device if the vibrations are enabled
     * @param length length of the vibration
     * @param intensity intensity of the vibration
     */
    private void vibrate(int length, int intensity){
        if (vibrations) vibrator.vibrate(VibrationEffect.createOneShot(length, intensity));
    }

    /**
     * Sets the visibility of a view if the visuals are enabled
     * @param view view to set visibility for
     * @param visible whether the view is visible
     */
    private void setVisibility(View view, boolean visible){
        if (!visuals) return;

        if (visible) view.setVisibility(View.VISIBLE);
        else view.setVisibility(View.INVISIBLE);
    }

    /**
     * Checks whether certain coordinates are within a boundary
     * @param touchX x coordinate
     * @param touchY y coordinate
     * @param x x coordinate of the boundary
     * @param y y coordinate of the boundary
     * @param width width of the boundary
     * @param height height of the boundary
     * @return whether it is inside
     */
    private static boolean within(float touchX, float touchY, int x, int y, int width, int height){
        return touchX > x && touchX < x + width && touchY > y && touchY < y + height;
    }
}
