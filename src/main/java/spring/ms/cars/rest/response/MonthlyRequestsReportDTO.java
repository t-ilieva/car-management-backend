package spring.ms.cars.rest.response;

public class MonthlyRequestsReportDTO {

    private String yearMonth;
    private int requests;

    public MonthlyRequestsReportDTO(String yearMonth, int requests) {
        this.yearMonth = yearMonth;
        this.requests = requests;
    }

    public String getYearMonth() {
        return yearMonth;
    }

    public void setYearMonth(String yearMonth) {
        this.yearMonth = yearMonth;
    }

    public int getRequests() {
        return requests;
    }

    public void setRequests(int requests) {
        this.requests = requests;
    }
}
