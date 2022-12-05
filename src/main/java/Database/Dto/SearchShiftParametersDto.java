package Database.Dto;

public class SearchShiftParametersDto {

    public String date;
    public String workerName;

    public SearchShiftParametersDto(String date, String workerName) {
        this.date = date;
        this.workerName = workerName;
    }

    public SearchShiftParametersDto() {
    }
}
