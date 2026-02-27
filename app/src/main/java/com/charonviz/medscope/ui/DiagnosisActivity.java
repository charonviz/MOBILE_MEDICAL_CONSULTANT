/* MedScope — Copyright (c) 2023-2025 Maksim Nikonov. MIT License. */
package com.charonviz.medscope.ui;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.charonviz.medscope.R;
import com.charonviz.medscope.data.DiagnosticTree;
import com.charonviz.medscope.model.DiagnosticNode;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class DiagnosisActivity extends AppCompatActivity {
    private DiagnosticTree tree;
    private DiagnosticNode currentNode;
    private final List<String> history = new ArrayList<>();
    private TextView tvQuestion, tvProgress, tvStepLabel;
    private MaterialButton btnYes, btnNo, btnUnsure, btnRestart;
    private LinearProgressIndicator progressBar;
    private View questionPanel, categoryPanel, resultPanel;
    private LinearLayout categoryContainer;
    private TextView tvResultTitle, tvResultBody, tvDisclaimer, tvSeverity, tvIcd;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnosis);
        setSupportActionBar(findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        tvQuestion = findViewById(R.id.tv_question);
        tvProgress = findViewById(R.id.tv_progress);
        tvStepLabel = findViewById(R.id.tv_step_label);
        btnYes = findViewById(R.id.btn_yes);
        btnNo = findViewById(R.id.btn_no);
        btnUnsure = findViewById(R.id.btn_unsure);
        btnRestart = findViewById(R.id.btn_restart);
        progressBar = findViewById(R.id.progress_bar);
        questionPanel = findViewById(R.id.question_panel);
        categoryPanel = findViewById(R.id.category_panel);
        categoryContainer = findViewById(R.id.category_container);
        resultPanel = findViewById(R.id.result_panel);
        tvResultTitle = findViewById(R.id.tv_result_title);
        tvResultBody = findViewById(R.id.tv_result_body);
        tvDisclaimer = findViewById(R.id.tv_disclaimer);
        tvSeverity = findViewById(R.id.tv_severity);
        tvIcd = findViewById(R.id.tv_icd);
        tree = new DiagnosticTree(this);
        startDiagnosis();
        btnYes.setOnClickListener(v -> answer("yes"));
        btnNo.setOnClickListener(v -> answer("no"));
        btnUnsure.setOnClickListener(v -> answer("unsure"));
        btnRestart.setOnClickListener(v -> startDiagnosis());
    }
    private void startDiagnosis() {
        history.clear(); currentNode = tree.getRoot();
        resultPanel.setVisibility(View.GONE); showNode();
    }
    private void showNode() {
        if (currentNode == null || !currentNode.isQuestion) { showResult(currentNode); return; }
        updateProgress(); tvQuestion.setText(currentNode.question);
        JSONArray options = tree.getOptions(currentNode.id);
        if (options != null && options.length() > 0) {
            questionPanel.setVisibility(View.GONE); categoryPanel.setVisibility(View.VISIBLE);
            categoryContainer.removeAllViews();
            for (int i = 0; i < options.length(); i++) {
                try {
                    JSONObject opt = options.getJSONObject(i);
                    MaterialButton btn = new MaterialButton(this, null,
                        com.google.android.material.R.attr.materialButtonOutlinedStyle);
                    btn.setText(opt.getString("label")); btn.setCornerRadius(dp(14));
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, dp(48));
                    lp.setMargins(0, 0, 0, dp(6)); btn.setLayoutParams(lp);
                    int nextId = opt.getInt("nextId");
                    btn.setOnClickListener(v -> {
                        history.add(currentNode.question + " -> " + btn.getText());
                        currentNode = tree.getNode(nextId); showNode();
                    });
                    categoryContainer.addView(btn);
                } catch (Exception e) { e.printStackTrace(); }
            }
        } else { questionPanel.setVisibility(View.VISIBLE); categoryPanel.setVisibility(View.GONE); }
    }
    private void answer(String choice) {
        history.add(currentNode.question + " -> " + choice);
        DiagnosticNode next = null;
        switch (choice) {
            case "yes": next = tree.getNode(currentNode.yesId); break;
            case "no": next = tree.getNode(currentNode.noId); break;
            case "unsure": next = tree.getNode(currentNode.unsureId); break;
        }
        currentNode = next; showNode();
    }
    private void showResult(DiagnosticNode node) {
        questionPanel.setVisibility(View.GONE); categoryPanel.setVisibility(View.GONE);
        resultPanel.setVisibility(View.VISIBLE);
        if (node != null) {
            tvResultTitle.setText(node.diagnosisTitle); tvResultBody.setText(node.diagnosisBody);
            String sev = node.severity != null ? node.severity : "low";
            tvSeverity.setText(sev.substring(0,1).toUpperCase() + sev.substring(1));
            switch (sev) {
                case "emergency": tvSeverity.setTextColor(0xFFFF1744); break;
                case "high": tvSeverity.setTextColor(0xFFFF6D00); break;
                case "medium": tvSeverity.setTextColor(0xFF2979FF); break;
                default: tvSeverity.setTextColor(0xFF00C853);
            }
            String icd = node.icd10 != null ? node.icd10 : "";
            tvIcd.setText(icd.isEmpty() ? "" : "ICD-10: " + icd);
        } else {
            tvResultTitle.setText("Inconclusive");
            tvResultBody.setText("Please consult a healthcare professional.");
            tvSeverity.setText(""); tvIcd.setText("");
        }
        tvDisclaimer.setText("This is not a medical diagnosis. Always consult a qualified healthcare professional.");
    }
    private void updateProgress() {
        int step = history.size() + 1;
        tvProgress.setText(step + " / ~" + tree.estimatedDepth());
        tvStepLabel.setText("Step " + step);
        progressBar.setProgress((int)(100.0 * step / tree.estimatedDepth()));
    }
    private int dp(int dp) { return (int)(dp * getResources().getDisplayMetrics().density); }
    @Override public boolean onSupportNavigateUp() { onBackPressed(); return true; }
}
