package com.parkingsystem.parkingapi.repositories

import com.parkingsystem.parkingapi.configuration.DatasourceProvider
import com.parkingsystem.parkingapi.domain.utilizations.UtilizationStatus
import com.parkingsystem.parkingapi.infrastructure.logging.Logger
import com.parkingsystem.parkingapi.infrastructure.logging.LoggerFactory
import com.parkingsystem.parkingapi.repositories.rowmapper.UtilizationRowMapper
import com.parkingsystem.parkingapi.utils.DateUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.jdbc.support.KeyHolder
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import reactor.core.scheduler.Scheduler

import java.util.concurrent.Callable

import static com.parkingsystem.parkingapi.infrastructure.ErrorEnum.ERROR_WHILE_REGISTER_UTILIZATION

@Component
class UtilizationRepository {

    private static final Logger logger = LoggerFactory.createLogger(UtilizationRepository)

    @Autowired
    DatasourceProvider datasourceProvider

    @Autowired
    UtilizationRowMapper utilizationRowMapper

    @Autowired
    @Qualifier("jdbcScheduler")
    Scheduler scheduler

    static final String INSERT = '''
        INSERT INTO Utilizations (
            Organization_ID,
            Plate,
            Brand,
            Model,
            InitialParkingDate,
            UtilizationStatus,
            CreatedAt
        ) VALUES (
            :Organization_ID,
            :Plate,
            :Brand,
            :Model,
            :InitialParkingDate,
            :UtilizationStatus,
            :CreatedAt
        )
    '''

    Mono<Long> registerUtilization(
        Long organization_ID,
        String plate,
        String brand,
        String model,
        String initialParkingDate,
        String utilizationStatus
    ) {
        async {
            NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(datasourceProvider.parkingDataSource)

            def params = new MapSqlParameterSource('Organization_ID', organization_ID)
                .addValue('Plate', plate)
                .addValue('Brand', brand)
                .addValue('Model', model)
                .addValue('InitialParkingDate', initialParkingDate)
                .addValue('UtilizationStatus', utilizationStatus)
                .addValue('CreatedAt', DateUtils.toBrazilGMT())

            logger.createMessage("${this.class.simpleName}.registerUtilization", "Executing query")
                .with("params", params.values)
                .info()

            String[] arr = ["Utilization_ID"]
            KeyHolder keyHolder = new GeneratedKeyHolder()

            try {
                jdbcTemplate.update(INSERT, params, keyHolder, arr)
            } catch(Exception ex) {
                logger.createMessage("${this.class.simpleName}.registerUtilization", ERROR_WHILE_REGISTER_UTILIZATION.message)
                    .error(ex)

                throw new Error(ERROR_WHILE_REGISTER_UTILIZATION.message, ex)
            }

            keyHolder.key.longValue()
        }
    }

    private <T> Mono<T> async(Callable<T> callable) {
        return Mono.fromCallable(callable).publishOn(scheduler)
    }

}
