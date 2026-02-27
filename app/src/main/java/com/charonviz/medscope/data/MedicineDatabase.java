/* Copyright (c) 2023-2025 Maksim Nikonov. MIT License. */
package com.charonviz.medscope.data;

import android.content.Context;
import com.charonviz.medscope.model.Medicine;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Offline medicine reference database loaded from a JSON asset.
 * Supports search by name and ATC code.
 */
public class MedicineDatabase {

    private final List<Medicine> medicines = new ArrayList<>();

    public MedicineDatabase(Context ctx) {
        try {
            InputStream is = ctx.getAssets().open("medicines.json");
            byte[] buf = new byte[is.available()];
            is.read(buf);
            is.close();
            JSONArray arr = new JSONArray(new String(buf, StandardCharsets.UTF_8));
            for (int i = 0; i < arr.length(); i++) {
                JSONObject o = arr.getJSONObject(i);
                Medicine m = new Medicine();
                m.name = o.optString("name");
                m.atcCode = o.optString("atcCode");
                m.category = o.optString("category");
                m.description = o.optString("description");
                m.dosage = o.optString("dosage");
                m.sideEffects = o.optString("sideEffects");
                m.contraindications = o.optString("contraindications");
                medicines.add(m);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Medicine> getAll() { return medicines; }

    public List<Medicine> search(String query) {
        String q = query.toLowerCase();
        List<Medicine> result = new ArrayList<>();
        for (Medicine m : medicines)
            if (m.name.toLowerCase().contains(q) || m.atcCode.toLowerCase().contains(q))
                result.add(m);
        return result;
    }
}
