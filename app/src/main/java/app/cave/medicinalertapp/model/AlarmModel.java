package app.cave.medicinalertapp.model;

public class AlarmModel {

    int id;
    String ndt;
    int numberOfSlot;
    String firstSlotTime;
    String secondSlotTime;
    String thirdSlotTime;
    int firstSlotRequestCode;
    int secondSlotRequestCode;
    int thirdSlotRequestCode;

    public AlarmModel(int id, String ndt, int numberOfSlot, String firstSlotTime, String secondSlotTime,
                      String thirdSlotTime, int firstSlotRequestCode, int secondSlotRequestCode, int thirdSlotRequestCode) {
        this.id = id;
        this.ndt = ndt;
        this.numberOfSlot = numberOfSlot;
        this.firstSlotTime = firstSlotTime;
        this.secondSlotTime = secondSlotTime;
        this.thirdSlotTime = thirdSlotTime;
        this.firstSlotRequestCode = firstSlotRequestCode;
        this.secondSlotRequestCode = secondSlotRequestCode;
        this.thirdSlotRequestCode = thirdSlotRequestCode;
    }

    public AlarmModel() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNdt() {
        return ndt;
    }

    public void setNdt(String ndt) {
        this.ndt = ndt;
    }

    public int getNumberOfSlot() {
        return numberOfSlot;
    }

    public void setNumberOfSlot(int numberOfSlot) {
        this.numberOfSlot = numberOfSlot;
    }

    public String getFirstSlotTime() {
        return firstSlotTime;
    }

    public void setFirstSlotTime(String firstSlotTime) {
        this.firstSlotTime = firstSlotTime;
    }

    public String getSecondSlotTime() {
        return secondSlotTime;
    }

    public void setSecondSlotTime(String secondSlotTime) {
        this.secondSlotTime = secondSlotTime;
    }

    public String getThirdSlotTime() {
        return thirdSlotTime;
    }

    public void setThirdSlotTime(String thirdSlotTime) {
        this.thirdSlotTime = thirdSlotTime;
    }

    public int getFirstSlotRequestCode() {
        return firstSlotRequestCode;
    }

    public void setFirstSlotRequestCode(int firstSlotRequestCode) {
        this.firstSlotRequestCode = firstSlotRequestCode;
    }

    public int getSecondSlotRequestCode() {
        return secondSlotRequestCode;
    }

    public void setSecondSlotRequestCode(int secondSlotRequestCode) {
        this.secondSlotRequestCode = secondSlotRequestCode;
    }

    public int getThirdSlotRequestCode() {
        return thirdSlotRequestCode;
    }

    public void setThirdSlotRequestCode(int thirdSlotRequestCode) {
        this.thirdSlotRequestCode = thirdSlotRequestCode;
    }
}
