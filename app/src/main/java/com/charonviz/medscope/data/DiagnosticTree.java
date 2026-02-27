/* MedScope — Copyright (c) 2023-2025 Maksim Nikonov. MIT License. */
package com.charonviz.medscope.data;

import android.content.Context;
import com.charonviz.medscope.model.DiagnosticNode;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class DiagnosticTree {
    private final Map<Integer, DiagnosticNode> nodes = new HashMap<>();
    private final Map<Integer, JSONArray> optionsMap = new HashMap<>();
    private int rootId = 0;
    private int maxDepth = 8;

    public DiagnosticTree(Context ctx) {
        try {
            InputStream is = ctx.getAssets().open("diagnostic_tree.json");
            byte[] buf = new byte[is.available()]; is.read(buf); is.close();
            JSONObject root = new JSONObject(new String(buf, StandardCharsets.UTF_8));
            rootId = root.optInt("rootId", 0);
            maxDepth = root.optInt("estimatedDepth", 8);
            JSONArray arr = root.getJSONArray("nodes");
            for (int i = 0; i < arr.length(); i++) {
                JSONObject o = arr.getJSONObject(i);
                DiagnosticNode n = new DiagnosticNode();
                n.id = o.getInt("id");
                n.isQuestion = o.optBoolean("isQuestion", true);
                n.question = o.optString("question", "");
                n.yesId = o.optInt("yesId", -1);
                n.noId = o.optInt("noId", -1);
                n.unsureId = o.optInt("unsureId", -1);
                n.diagnosisTitle = o.optString("diagnosisTitle", "");
                n.diagnosisBody = o.optString("diagnosisBody", "");
                n.severity = o.optString("severity", "low");
                n.icd10 = o.optString("icd10", "");
                nodes.put(n.id, n);
                if (o.has("options")) optionsMap.put(n.id, o.getJSONArray("options"));
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    public DiagnosticNode getRoot()            { return nodes.get(rootId); }
    public DiagnosticNode getNode(int id)      { return nodes.get(id); }
    public JSONArray getOptions(int id)        { return optionsMap.get(id); }
    public int estimatedDepth()                { return maxDepth; }
    public int size()                          { return nodes.size(); }
}
