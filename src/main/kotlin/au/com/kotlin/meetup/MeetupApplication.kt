package au.com.kotlin.meetup

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class MeetupApplication

fun main(args: Array<String>) {
    SpringApplication.run(MeetupApplication::class.java, *args)
}

