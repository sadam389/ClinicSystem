# End-to-End System Test Report
**Course:** Software Construction and Development (SCD)  
**System:** Clinic Appointment and Patient Records System  

---

## 1. Scope of End-to-End Walkthrough
System testing validates the entire integrated application from the end-user perspective across all 3 key clinical personas:
1. **Patient Workflow:** Self-registration &rarr; login &rarr; appointment search & reservation &rarr; viewing confirmed bookings &rarr; reviewing EHR and prescriptions.
2. **Physician Workflow:** Login &rarr; roster inspection &rarr; conducting clinical consult &rarr; dynamic prescription builder &rarr; consultation finalization.
3. **Administrator Workflow:** Login &rarr; executive dashboard inspection &rarr; physician credentialing &rarr; clinical report generation &rarr; legacy data salvage run.

---

## 2. Walkthrough Results Matrix

| Scenario | Steps Executed | Pass Criteria | Outcome | Status |
|:---|:---|:---|:---|:---|
| **E2E-01: Full Patient Booking Journey** | 1. Register `pat.test@gmail.com`<br>2. Login with password<br>3. Select Dr. Smith (Cardiology) for 11:30<br>4. Submit appointment request | Appointment number generated (`APT-XXXX`), dashboard shows status CONFIRMED, doctor's roster updates | Roster updated in real time, slot locked | **PASS** |
| **E2E-02: Double-Booking Prevention** | 1. Open second private browser session<br>2. Login as Sarah Jenkins<br>3. Attempt to book Dr. Smith at identical 11:30 slot | System blocks booking, alerts user that slot is taken | Error message cleanly displayed, no duplicate record in DB | **PASS** |
| **E2E-03: Doctor Clinical Documentation** | 1. Login as `dr.smith@medicare.org`<br>2. Select patient consultation<br>3. Record vitals (BP 125/82, Pulse 76)<br>4. Prescribe 2 medicines using dynamic JS row builder<br>5. Submit prescription | Appointment marked COMPLETED, prescription available immediately in Patient portal | Real-time state synchronized across portals | **PASS** |
| **E2E-04: Legacy Data Salvage & Account Provisioning** | 1. Login as Administrator<br>2. Navigate to Clinical Analytics<br>3. Paste 3-line legacy patient file<br>4. Run legacy importer | Importer parses records, stages invalid lines, creates active accounts for valid patients | 2 valid accounts created; 1 malformed line safely staged | **PASS** |

## 3. System Test Conclusion
The system successfully satisfied all operational clinical workflows with zero UI distortions, consistent responsive rendering across desktop and mobile viewports, and solid session safety.
