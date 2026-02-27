# MedScope Diagnostic Algorithm — Technical Documentation

## Overview

MedScope uses a triage-first decision tree with **108 nodes** (49 question nodes, 59 diagnosis nodes) stored in `assets/diagnostic_tree.json`. The tree spans 7 body systems, and every terminal node carries a severity level and ICD-10 code.

## Architecture

The algorithm is implemented across three classes:

- **DiagnosticNode** (`model/DiagnosticNode.java`) — a POJO representing one node in the tree. Question nodes have `isQuestion = true` and carry `yesId`, `noId`, `unsureId` pointers. Diagnosis nodes carry `diagnosisTitle`, `diagnosisBody`, `severity`, and `icd10`.
- **DiagnosticTree** (`data/DiagnosticTree.java`) — loads the JSON asset, indexes all nodes by id, and exposes `getRoot()`, `getNode(int id)`, `getOptions(int id)`, and `estimatedDepth()`.
- **DiagnosisActivity** (`ui/DiagnosisActivity.java`) — the UI controller. It supports two question modes: category selection (used at the root to pick a body system) and yes/no/unsure (used for all subsequent questions).

## Triage-first Flow

```
Root (id=0) → category selection
  ├── Head & Neurological (id=1)
  ├── Chest & Respiratory (id=2)
  ├── Abdomen & Digestive (id=3)
  ├── Musculoskeletal (id=4)
  ├── Skin (id=5)
  ├── General / Systemic (id=6)
  └── Urinary / Reproductive (id=7)
```

From each body system, the tree branches into yes/no/unsure questions, progressively narrowing until a diagnosis is reached.

## Severity Levels

| Level       | Color   | Meaning                          | Action                    |
|-------------|---------|----------------------------------|---------------------------|
| Emergency   | #FF1744 | Life-threatening                | Call 911 immediately       |
| High        | #FF6D00 | Needs same-day medical attention| Go to urgent care / ER     |
| Medium      | #2979FF | Should see a doctor soon        | Schedule appointment       |
| Low         | #00C853 | Self-care likely sufficient     | Monitor at home            |

## Red Flag Conditions (7 Emergency Nodes)

1. **Subarachnoid Hemorrhage (SAH)** — I60.9 — thunderclap headache
2. **Meningitis** — G03.9 — headache + stiff neck + fever
3. **Stroke / TIA** — I63.9 — sudden vision loss + weakness
4. **Retinal Artery Occlusion** — H34.1 — sudden painless vision loss
5. **Myocardial Infarction** — I21.9 — crushing chest pain + arm/jaw radiation
6. **Pulmonary Embolism** — I26.99 — pleuritic chest pain + dyspnea + calf swelling
7. **Appendicitis** — K35.80 — RLQ pain + fever + rebound tenderness

## ICD-10 Coding

All 59 diagnosis nodes carry an ICD-10-CM code. These are informational and intended for reference, not for billing or clinical documentation. Examples:

- G43.9 — Migraine, unspecified
- M54.5 — Low back pain
- L30.9 — Dermatitis, unspecified
- N30.0 — Acute cystitis
- J06.9 — Acute upper respiratory infection

## JSON Schema

Each node in `diagnostic_tree.json`:

```json
{
  "id": 10,
  "isQuestion": true,
  "question": "Is the headache sudden and severe (thunderclap)?",
  "yesId": 11,
  "noId": 12,
  "unsureId": 13,
  "options": null
}
```

Category nodes use `options` instead of yes/no/unsure:

```json
{
  "id": 0,
  "isQuestion": true,
  "question": "Which area concerns you the most?",
  "options": [
    {"label": "Head & Neurological", "nextId": 1},
    {"label": "Chest & Respiratory", "nextId": 2}
  ]
}
```

Diagnosis nodes:

```json
{
  "id": 11,
  "isQuestion": false,
  "diagnosisTitle": "Possible Subarachnoid Hemorrhage",
  "diagnosisBody": "A thunderclap headache...",
  "severity": "emergency",
  "icd10": "I60.9"
}
```
