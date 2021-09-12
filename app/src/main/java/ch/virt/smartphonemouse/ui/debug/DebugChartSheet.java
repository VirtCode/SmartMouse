package ch.virt.smartphonemouse.ui.debug;

import static android.view.inputmethod.EditorInfo.IME_ACTION_DONE;
import static android.view.inputmethod.EditorInfo.IME_ACTION_NEXT;
import static android.view.inputmethod.EditorInfo.IME_ACTION_PREVIOUS;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ch.virt.smartphonemouse.R;
import ch.virt.smartphonemouse.ui.debug.handling.DebugChartHandler;
import ch.virt.smartphonemouse.ui.debug.handling.DebugDataHandler;

public class DebugChartSheet extends SideDialogFragment {

    Dialog dialog;
    RecyclerView seriesView;
    RadioGroup axis;

    DebugChartHandler chartHandler;
    DebugDataHandler dataHandler;
    EditText averageEdit;

    public DebugChartSheet(DebugChartHandler chartHandler, DebugDataHandler dataHandler) {
        this.chartHandler = chartHandler;
        this.dataHandler = dataHandler;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View view = requireActivity().getLayoutInflater().inflate(R.layout.sheet_debug_chart, null);
        builder.setView(view)
                .setNegativeButton(R.string.dialog_debug_sheet_done, null);

        dialog = builder.create();

        seriesView = view.findViewById(R.id.debug_chart_series);
        axis = view.findViewById(R.id.debug_chart_axis_group);
        averageEdit = view.findViewById(R.id.sheet_debug_chart_average_edit);

        seriesView.setAdapter(new ListAdapter(chartHandler));
        seriesView.setLayoutManager(new LinearLayoutManager(getContext()));

        axis.check(dataHandler.getAxis() == 0 ? R.id.debug_chart_axis_x : dataHandler.getAxis() == 1 ? R.id.debug_chart_axis_y : R.id.debug_chart_axis_z);
        axis.setOnCheckedChangeListener((group, checkedId) -> dataHandler.setAxis(checkedId == R.id.debug_chart_axis_x ? 0 : checkedId == R.id.debug_chart_axis_y ? 1 : 2));

        averageEdit.setText("" + chartHandler.getAverageAmount());
        averageEdit.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == IME_ACTION_DONE || actionId == IME_ACTION_NEXT || actionId == IME_ACTION_PREVIOUS) chartHandler.setAverageAmount(Integer.parseInt(v.getText().toString()));
            return false;
        });

        return dialog;
    }

    private static class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

        DebugChartHandler handler;

        public ListAdapter(DebugChartHandler series) {

            this.handler = series;
        }


        @NonNull
        @Override
        public ListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_debug_chart_axis, parent, false);

            return new ListAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ListAdapter.ViewHolder holder, int position) {
            holder.bind(handler, position);
        }

        @Override
        public int getItemCount() {
            return handler.getSeries().size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            private final ImageView color;
            private final CheckBox checkbox;


            public ViewHolder(View view) {
                super(view);

                checkbox = view.findViewById(R.id.debug_chart_series_item_checkbox);
                color = view.findViewById(R.id.debug_chart_series_item_color);
            }

            public void bind(DebugChartHandler handler, int index) {
                ((GradientDrawable) color.getBackground()).setColor(handler.getSeries().get(index).getColor() | 0xFF000000);
                checkbox.setText(handler.getSeries().get(index).getName());
                checkbox.setChecked(handler.getSeries().get(index).isVisible());
                checkbox.setOnCheckedChangeListener((buttonView, isChecked) -> handler.setSeriesVisibility(handler.getSeries().get(index), isChecked));
            }
        }


    }
}
