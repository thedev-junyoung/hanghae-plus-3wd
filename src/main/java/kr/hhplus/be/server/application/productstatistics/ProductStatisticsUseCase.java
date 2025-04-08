package kr.hhplus.be.server.application.productstatistics;

public interface ProductStatisticsUseCase {
    public void record(RecordSalesCommand command);
}
