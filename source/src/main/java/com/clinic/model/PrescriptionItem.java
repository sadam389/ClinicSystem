package com.clinic.model;

import java.io.Serializable;

/**
 * Line item in a prescription detailing dosage, frequency, and duration.
 */
public class PrescriptionItem implements Serializable {
    private static final long serialVersionUID = 1L;

    private int itemId;
    private int prescriptionId;
    private String medicineName;
    private String dosage;
    private String frequency;
    private int durationDays;
    private String instructions;

    public PrescriptionItem() {}

    public PrescriptionItem(String medicineName, String dosage, String frequency, int durationDays, String instructions) {
        this.medicineName = medicineName;
        this.dosage = dosage;
        this.frequency = frequency;
        this.durationDays = durationDays;
        this.instructions = instructions;
    }

    public int getItemId() { return itemId; }
    public void setItemId(int itemId) { this.itemId = itemId; }

    public int getPrescriptionId() { return prescriptionId; }
    public void setPrescriptionId(int prescriptionId) { this.prescriptionId = prescriptionId; }

    public String getMedicineName() { return medicineName; }
    public void setMedicineName(String medicineName) { this.medicineName = medicineName; }

    public String getDosage() { return dosage; }
    public void setDosage(String dosage) { this.dosage = dosage; }

    public String getFrequency() { return frequency; }
    public void setFrequency(String frequency) { this.frequency = frequency; }

    public int getDurationDays() { return durationDays; }
    public void setDurationDays(int durationDays) { this.durationDays = durationDays; }

    public String getInstructions() { return instructions; }
    public void setInstructions(String instructions) { this.instructions = instructions; }
}
