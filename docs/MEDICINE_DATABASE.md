# Medicine Reference — Database Documentation

## Overview

MedScope includes an offline drug reference stored in `assets/medicines.json`. It contains 15+ common medications organized by pharmacological category with ATC codes.

## Architecture

- **Medicine** (`model/Medicine.java`) — POJO: `name`, `atcCode`, `category`, `dosage`, `sideEffects`, `contraindications`, `description`.
- **MedicineDatabase** (`data/MedicineDatabase.java`) — loads JSON, provides `getAll()` and `search(query)`.
- **MedicineActivity** (`ui/MedicineActivity.java`) — SearchView with RecyclerView.
- **MedicineAdapter** (`ui/MedicineAdapter.java`) — displays drug cards with expandable detail.

## Drug Categories

The database covers 12 pharmacological categories:

| Category        | Example Drug   | ATC Code |
|-----------------|---------------|----------|
| NSAID           | Ibuprofen     | M01AE01  |
| Analgesic       | Paracetamol   | N02BE01  |
| Antibiotic      | Amoxicillin   | J01CA04  |
| PPI             | Omeprazole    | A02BC01  |
| Antihistamine   | Loratadine    | R06AX13  |
| Antihypertensive| Amlodipine    | C08CA01  |
| Statin          | Atorvastatin  | C10AA05  |
| Antidiabetic    | Metformin     | A10BA02  |
| Bronchodilator  | Salbutamol    | R03AC02  |
| Corticosteroid  | Prednisolone  | H02AB06  |
| Antidepressant  | Sertraline    | N06AB06  |
| Anticoagulant   | Aspirin (low) | B01AC06  |

## ATC Classification

All drugs use WHO ATC (Anatomical Therapeutic Chemical) codes. The first letter indicates the anatomical group:

- A = Alimentary tract
- B = Blood
- C = Cardiovascular
- H = Systemic hormonal
- J = Anti-infectives
- M = Musculoskeletal
- N = Nervous system
- R = Respiratory

## JSON Schema

```json
{
  "name": "Ibuprofen",
  "atcCode": "M01AE01",
  "category": "NSAID",
  "dosage": "200-400mg every 4-6h, max 1200mg/day (OTC)",
  "sideEffects": "GI upset, ulcers, renal impairment, cardiovascular risk",
  "contraindications": "Active GI bleeding, severe renal impairment, 3rd trimester pregnancy",
  "description": "Non-steroidal anti-inflammatory drug. Inhibits COX-1 and COX-2."
}
```
