package models;

public class Charge {
    private String statute;
    private String name;
    private Float bailAmount;

    public Charge() {

    }

    public Charge(String statute, String name, Float bailAmount) {
        this.statute = statute;
        this.name = name;
        this.bailAmount = bailAmount;
    }

    public String getStatute() {
        return statute;
    }

    public void setStatute(String statute) {
        this.statute = statute;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getBailAmount() {
        return bailAmount;
    }

    public void setBailAmount(Float bailAmount) {
        this.bailAmount = bailAmount;
    }

    @Override
    public String toString() {
        return "Charge{" +
                "statute='" + statute + '\'' +
                ", name='" + name + '\'' +
                ", bailAmount=" + bailAmount +
                '}';
    }
}
