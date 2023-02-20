package ru.nb.medalist.msmuser.mq
//
//import org.springframework.context.annotation.Bean
//import org.springframework.context.annotation.Configuration
//import org.springframework.messaging.Message
//import reactor.core.publisher.Flux
//import reactor.core.publisher.Sinks
//import reactor.util.concurrent.Queues
//import java.util.function.Supplier
//
//// описываются все каналы с помощью функциональных методов
//@Configuration // spring считывает бины и создает соотв. каналы
//class MessageFunc {
//    // для того чтобы считывать данные по требованию (а не постоянно) - создаем поток, откуда данные будут отправляться уже в канал SCS
//    // будем исп. внутреннюю шину, из которой будут отправляться сообщения в канал SCS (по требованию)
//    val innerBus: Sinks.Many<Message<Long>> = Sinks.many().multicast().onBackpressureBuffer(Queues.SMALL_BUFFER_SIZE, false)
//
//    // отправляет в канал id пользователя, для которого нужно создать тестовые данные
//    // название метода должно совпадать с настройками definition и bindings в файлах properties (или yml)
//    @Bean
//    fun newUserActionProduce(): Supplier<Flux<Message<Long>>> {
//        return Supplier { innerBus.asFlux() } // будет считывать данные из потока Flux (как только туда попадают новые сообщения)
//    }
//}