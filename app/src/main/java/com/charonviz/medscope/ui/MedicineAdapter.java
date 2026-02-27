/* Copyright (c) 2023-2025 Maksim Nikonov. MIT License. */
package com.charonviz.medscope.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.charonviz.medscope.R;
import com.charonviz.medscope.model.Medicine;
import java.util.ArrayList;
import java.util.List;

public class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.VH> {
    private final List<Medicine> all;
    private List<Medicine> filtered;

    public MedicineAdapter(List<Medicine> m) { all = m; filtered = new ArrayList<>(m); }

    public void filter(String q) {
        String lo = q.toLowerCase();
        filtered.clear();
        for (Medicine m : all)
            if (m.name.toLowerCase().contains(lo) || m.atcCode.toLowerCase().contains(lo))
                filtered.add(m);
        notifyDataSetChanged();
    }

    @NonNull @Override public VH onCreateViewHolder(@NonNull ViewGroup p, int t) {
        return new VH(LayoutInflater.from(p.getContext()).inflate(R.layout.item_medicine, p, false));
    }
    @Override public void onBindViewHolder(@NonNull VH h, int pos) {
        Medicine m = filtered.get(pos);
        h.name.setText(m.name); h.cat.setText(m.category);
        h.atc.setText(m.atcCode); h.desc.setText(m.description);
    }
    @Override public int getItemCount() { return filtered.size(); }

    static class VH extends RecyclerView.ViewHolder {
        TextView name, cat, atc, desc;
        VH(View v) { super(v);
            name = v.findViewById(R.id.tv_med_name); cat = v.findViewById(R.id.tv_med_category);
            atc = v.findViewById(R.id.tv_med_atc); desc = v.findViewById(R.id.tv_med_desc);
        }
    }
}
