package ch.virt.smartphonemouse.ui;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.PreferenceManager;

import com.github.mikephil.charting.charts.LineChart;

import ch.virt.smartphonemouse.R;
import ch.virt.smartphonemouse.helper.MainContext;
import ch.virt.smartphonemouse.ui.debug.DebugChartSheet;
import ch.virt.smartphonemouse.ui.debug.handling.DebugChartHandler;
import ch.virt.smartphonemouse.ui.debug.handling.DebugDataHandler;

public class DebugFragment extends CustomFragment{

    LineChart chart;

    ImageView buttonPlay, buttonClear, buttonRenew, buttonSeries, buttonExport;

    DebugChartHandler chartHandler;
    DebugDataHandler dataHandler;

    public DebugFragment(MainContext context) {
        super(R.layout.fragment_debug, context);
    }

    public DebugFragment(){
        this(null);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void render() {

    }

    @Override
    protected void initComponents() {

        chartHandler = new DebugChartHandler(chart, PreferenceManager.getDefaultSharedPreferences(getContext()));
        dataHandler = new DebugDataHandler((SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE), chartHandler, PreferenceManager.getDefaultSharedPreferences(getContext()));

        chartHandler.addSeries("Unfiltered Acceleration", 0);
        //chartHandler.addSeries("Filtered Gravitation", 1);
        //chartHandler.addSeries("Frozen Gravitation", 2);
        //chartHandler.addSeries("Subtracted Acceleration", 3);
        //chartHandler.addSeries("Noise Cancelled Acceleration", 4);
        //chartHandler.addSeries("Unfiltered Velocity", 5);
        //chartHandler.addSeries("Cached Velocity", 6);
        //chartHandler.addSeries("Noise Cancelled Velocity", 7);
        //chartHandler.addSeries("Scaled Velocity", 8);
        //chartHandler.addSeries("Unfiltered Distance", 9);
        //chartHandler.addSeries("Cached Distance", 10);
        //chartHandler.addSeries("Final Distance", 11);

        buttonSeries.setOnClickListener(v -> {
            DebugChartSheet chartSheet = new DebugChartSheet();
            chartSheet.show(getParentFragmentManager(), "debugChartSheet");
        });

        buttonPlay.setOnClickListener(v -> {

            if(dataHandler.isRegistered()) dataHandler.unregister();
            else dataHandler.register();

        });

    }

    @Override
    protected void loadComponents(View view) {
        chart = view.findViewById(R.id.debug_chart);

        buttonPlay = view.findViewById(R.id.debug_button_play);
        buttonClear = view.findViewById(R.id.debug_button_clear);
        buttonRenew = view.findViewById(R.id.debug_button_renew);
        buttonSeries = view.findViewById(R.id.debug_button_series);
        buttonExport = view.findViewById(R.id.debug_button_export);
    }
}
