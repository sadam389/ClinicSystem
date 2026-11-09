/**
 * Clinic Appointment & Patient Records System
 * Client-Side JavaScript for dynamic forms, line item builders, and confirmations
 */

document.addEventListener('DOMContentLoaded', () => {
    // 1. Dynamic Prescription Item Builder
    const addMedBtn = document.getElementById('add-medicine-btn');
    const medContainer = document.getElementById('medicines-container');

    if (addMedBtn && medContainer) {
        addMedBtn.addEventListener('click', () => {
            const row = document.createElement('div');
            row.className = 'form-grid-2 medicine-row';
            row.style.marginBottom = '0.75rem';
            row.innerHTML = `
                <div>
                    <input type="text" name="medicineName" class="form-control" placeholder="Medicine Name (e.g. Amoxicillin 500mg)" required />
                </div>
                <div style="display: flex; gap: 0.5rem;">
                    <input type="text" name="dosage" class="form-control" placeholder="Dosage (e.g. 1 tab)" style="width: 30%;" />
                    <input type="text" name="frequency" class="form-control" placeholder="Freq (1-0-1)" style="width: 30%;" />
                    <input type="number" name="duration" class="form-control" placeholder="Days" style="width: 25%;" value="7" />
                    <button type="button" class="btn btn-danger btn-sm remove-med-btn" style="padding: 0 0.5rem;">&times;</button>
                </div>
                <div style="grid-column: span 2; margin-top: -0.25rem;">
                    <input type="text" name="instructions" class="form-control" placeholder="Instructions (e.g., After food, avoid dairy)" />
                </div>
            `;
            medContainer.appendChild(row);

            row.querySelector('.remove-med-btn').addEventListener('click', () => {
                row.remove();
            });
        });
    }

    // 2. Date Input Guard: Disallow past dates on appointment booking
    const apptDateInput = document.getElementById('appointmentDate');
    if (apptDateInput) {
        const today = new Date().toISOString().split('T')[0];
        apptDateInput.setAttribute('min', today);
    }
});

function confirmCancel(appointmentNumber) {
    return confirm("Are you sure you want to cancel appointment " + appointmentNumber + "? This action cannot be undone.");
}
