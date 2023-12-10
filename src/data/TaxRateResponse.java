package data;
/**
 * This class contains the response from the tax rate API.
 */
public class TaxRateResponse {
    private String zip_code;
    private double total_rate;
    private double state_rate;
    private double city_rate;
    private double county_rate;
    private double additional_rate;

    public TaxRateResponse() {
    }

    public String getZipCode() {
        return zip_code;
    }

    public double getTotalRate() {
        return total_rate;
    }

    public double getStateRate() {
        return state_rate;
    }

    public double getCityRate() {
        return city_rate;
    }

    public double getCountyRate() {
        return county_rate;
    }

    public double getAdditionalRate() {
        return additional_rate;
    }

    public void setZipCode(String zip_code) {
        this.zip_code = zip_code;
    }

    public void setTotalRate(double total_rate) {
        this.total_rate = total_rate;
    }

    public void setStateRate(double state_rate) {
        this.state_rate = state_rate;
    }

    public void setCityRate(double city_rate) {
        this.city_rate = city_rate;
    }

    public void setCountyRate(double county_rate) {
        this.county_rate = county_rate;
    }

    public void setAdditionalRate(double additional_rate) {
        this.additional_rate = additional_rate;
    }

    @Override
    public String toString() {
        return "TaxRateResponse{" +
                "zip_code='" + zip_code + '\'' +
                ", total_rate=" + total_rate +
                ", state_rate=" + state_rate +
                ", city_rate=" + city_rate +
                ", county_rate=" + county_rate +
                ", additional_rate=" + additional_rate +
                '}';
    }

}
