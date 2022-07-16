package com.parkingsystem.parkingapi.repositories

import com.parkingsystem.parkingapi.configuration.DatasourceProvider
import com.parkingsystem.parkingapi.infrastructure.exceptions.InternalServerException
import com.parkingsystem.parkingapi.infrastructure.logging.Logger
import com.parkingsystem.parkingapi.infrastructure.logging.LoggerFactory
import com.parkingsystem.parkingapi.utils.DateUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import reactor.core.scheduler.Scheduler

import java.util.concurrent.Callable

import static com.parkingsystem.parkingapi.infrastructure.ErrorEnum.ERROR_WHILE_UPDATE_UTILIZATION

@Component
class UtilizationEventRepository {

    private static final Logger logger = LoggerFactory.createLogger(UtilizationEventRepository)

    @Autowired
    DatasourceProvider datasourceProvider

    @Autowired
    @Qualifier("jdbcScheduler")
    Scheduler scheduler

    static final String UPDATE = '''
        UPDATE Utilizations
        SET FinishParkingDate   = :FinishParkingDate,
            Cost                = :Cost,
            UtilizationStatus   = :UtilizationStatus,
            Duration            = :Duration,
            UpdatedAt           = :UpdatedAt
        WHERE
            Utilization_ID      = :Utilization_ID
    '''

    void updateUtilization(Long utilizationId, String finishParkingDate, BigDecimal cost, String utilizationStatus, Integer duration) {
        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(datasourceProvider.parkingDataSource)
        def params = [
            'Utilization_ID': utilizationId,
            'FinishParkingDate': finishParkingDate,
            'Cost': cost,
            'UtilizationStatus': utilizationStatus,
            'Duration': duration,
            'UpdatedAt': DateUtils.toBrazilGMT()
        ]

        try {
            jdbcTemplate.update(UPDATE, params)
        } catch (Exception ex) {
            logger.createMessage("${this.class.simpleName}.updateUtilization", ERROR_WHILE_UPDATE_UTILIZATION.message)
                .error(ex)

            throw new InternalServerException(ERROR_WHILE_UPDATE_UTILIZATION)
        }
    }



    private <T> Mono<T> async(Callable<T> callable) {
        return Mono.fromCallable(callable).publishOn(scheduler)
    }

}
