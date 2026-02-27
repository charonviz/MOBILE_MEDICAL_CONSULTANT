/* MedScope — Copyright (c) 2023-2025 Maksim Nikonov. MIT License. */
package com.charonviz.medscope.model;
public class DiagnosticNode {
    public int id;
    public boolean isQuestion;
    public String question;
    public int yesId, noId, unsureId;
    public String diagnosisTitle, diagnosisBody, severity, icd10;
}
