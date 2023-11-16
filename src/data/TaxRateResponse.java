package data;

public class TaxRateResponse {
    private String Zip;  // Field names are case-sensitive and must match the JSON response
    private String City;
    private String State;
    private double TaxRate;

    public TaxRateResponse() {
    }

    public String getZip() {
        return Zip;
    }

    public void setZip(String zip) {
        this.Zip = zip;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        this.City = city;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        this.State = state;
    }

    public double getTaxRate() {
        return TaxRate;
    }

    public void setTaxRate(double taxRate) {
        this.TaxRate = taxRate;
    }
}
