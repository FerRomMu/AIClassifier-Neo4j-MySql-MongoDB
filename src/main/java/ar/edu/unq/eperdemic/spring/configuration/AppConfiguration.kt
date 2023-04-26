package ar.edu.unq.eperdemic.spring.configuration

import ar.edu.unq.eperdemic.persistencia.dao.PatogenoDAO
import ar.edu.unq.eperdemic.services.*
import ar.edu.unq.eperdemic.services.impl.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AppConfiguration {

    @Bean
    fun groupName() : String {
        return "Juanes y los de camisa negra"
    }

}
